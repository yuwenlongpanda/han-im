package com.ywl.framework.mysql.shard;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

@Intercepts({
        @Signature(
                type = StatementHandler.class,
                method = "prepare",
                args = {Connection.class, Integer.class}
        )
})
@Slf4j
@Component
public class MybatisShardStatementInterceptor implements Interceptor {

    private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();
    public static final String DELEGATE_BOUND_SQL_SQL = "delegate.boundSql.sql";
    public static final String DELEGATE_MAPPED_STATEMENT = "delegate.mappedStatement";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(statementHandler,
                SystemMetaObject.DEFAULT_OBJECT_FACTORY,
                SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY,
                DEFAULT_REFLECTOR_FACTORY
        );


        Optional<ShardTableContext.ShardTable> optional = ShardTableContext.getOptional();

        String sql = (String) metaObject.getValue(DELEGATE_BOUND_SQL_SQL);

        if (optional.isPresent()) {

            ShardTableContext.ShardTable shardTable = optional.get();
            Object id = shardTable.getId();

            Class<?> tableClass = shardTable.getTableClass();

            TableName tableName = tableClass.getAnnotation(TableName.class);
            TableShard tableShard = tableClass.getAnnotation(TableShard.class);

            if (tableName != null && tableShard != null) {
                TableShardStrategy tableShardStrategy = tableShard.shardStrategy().newInstance();
                String tableShardName = tableShardStrategy.getTableShardName(tableClass, tableName.value(), id);
                metaObject.setValue(DELEGATE_BOUND_SQL_SQL, sql.replaceAll(tableName.value(), tableShardName));
            }
        } else {
            MappedStatement mappedStatement = (MappedStatement)
                    metaObject.getValue(DELEGATE_MAPPED_STATEMENT);
            Class<?> tableClass = getTableClass(mappedStatement);
            if (tableClass != null) {
                TableName tableName = tableClass.getAnnotation(TableName.class);
                TableShard tableShard = tableClass.getAnnotation(TableShard.class);
                if (tableShard != null && tableName != null) {
                    TableShardStrategy tableShardStrategy = tableShard.shardStrategy().newInstance();
                    List<String> list = tableShardStrategy.listTableShardName(tableClass, tableName.value());
                    String unionSql = buildAllTableSelect(tableName.value(), list);
                    metaObject.setValue(DELEGATE_BOUND_SQL_SQL, sql.replaceAll(tableName.value(), unionSql));
                }
            }
        }
        return invocation.proceed();
    }

    private String buildAllTableSelect(String originalTableName, List<String> tableList) {
        StringBuilder sqlBuilder = new StringBuilder("(");
        for (int i = 0; i < tableList.size(); i++) {
            String tableName = tableList.get(i);
            sqlBuilder.append(" select * from ").append(tableName);
            if (i < tableList.size() - 1) {
                sqlBuilder.append(" union all");
            }
        }
        return sqlBuilder.append(") ").append(originalTableName).toString();
    }


    private Class<?> getTableClass(MappedStatement mappedStatement) throws ClassNotFoundException {
        String className = mappedStatement.getId();
        //获取到BaseMapper的实现类
        className = className.substring(0, className.lastIndexOf('.'));
        Class<?> clazz = Class.forName(className);
        if (BaseMapper.class.isAssignableFrom(clazz)) {
            //public interface XXXMapper extends BaseMapper<XXX> 其实就是获取到泛型中的具体表实体类
            //获取表实体类
            return (Class<?>) ((ParameterizedType) (clazz.getGenericInterfaces()[0])).getActualTypeArguments()[0];
        }
        return null;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

}

