package com.ywl.framework.mysql.interceptor;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ywl.framework.common.context.UserContext;
import com.ywl.framework.common.model.UserLoginInfo;
import com.ywl.framework.mysql.shard.ShardTableContext;
import com.ywl.framework.mysql.shard.TableShard;
import com.ywl.framework.mysql.shard.TableShardField;
import lombok.SneakyThrows;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author liao
 */
public class DataOperationInterceptor implements MetaObjectHandler {

    private static final String CREATE_TIME = "createTime";
    private static final String CREATE_USER = "createUser";
    private static final String UPDATE_TIME = "updateTime";
    private static final String UPDATE_USER = "updateUser";
    private static final String IS_DELETED = "isDeleted";

    private Long getCurrentUserId() {
        return Optional.ofNullable(UserContext.get()).map(UserLoginInfo::getId).orElse(0L);
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, CREATE_TIME, LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, CREATE_USER, this::getCurrentUserId, Long.class);
        this.strictInsertFill(metaObject, IS_DELETED, () -> Boolean.FALSE, Boolean.class);
        checkTableField(metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, UPDATE_TIME, LocalDateTime::now, LocalDateTime.class);
        this.strictUpdateFill(metaObject, UPDATE_USER, this::getCurrentUserId, Long.class);
        checkTableField(metaObject);
    }


    @SneakyThrows
    private void checkTableField(MetaObject metaObject) {
        TableShard annotation = metaObject.getOriginalObject().getClass().getAnnotation(TableShard.class);
        if (annotation != null) {
            Object originalObject = metaObject.getOriginalObject();
            Field[] declaredFields = originalObject.getClass().getDeclaredFields();
            Optional<Field> fieldOptional = Arrays.stream(declaredFields).filter(field -> field.isAnnotationPresent(TableShardField.class)).findFirst();
            if (!fieldOptional.isPresent()) {
                return;
            }
            ReflectionUtils.makeAccessible(fieldOptional.get());
            Object fieldValue = ReflectionUtils.getField(fieldOptional.get(), originalObject);
            if (fieldValue == null) {
                throw new RuntimeException("该表已经分表，需要必传，分表取模字段");
            }
            ShardTableContext.set(originalObject.getClass(), fieldValue);
        }
    }
}
