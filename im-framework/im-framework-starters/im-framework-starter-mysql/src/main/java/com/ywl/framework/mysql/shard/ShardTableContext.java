package com.ywl.framework.mysql.shard;

import com.ywl.framework.common.model.UserLoginInfo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Optional;

/**
 * @author liao
 */
public class ShardTableContext {

    private ShardTableContext(){}

    private static final ThreadLocal<ShardTable> SHARD_TABLE_THREAD_LOCAL = new ThreadLocal<>();

    public static void set(Class<?> tableClass, Object id) {
        ShardTable shardTable = new ShardTable();
        shardTable.setTableClass(tableClass);
        shardTable.setId(id);
        SHARD_TABLE_THREAD_LOCAL.set(shardTable);
    }

    public static ShardTable get() {
        ShardTable shardTable = SHARD_TABLE_THREAD_LOCAL.get();
        SHARD_TABLE_THREAD_LOCAL.remove();
        return shardTable;
    }

    public static Optional<ShardTable> getOptional() {
        ShardTable shardTable = SHARD_TABLE_THREAD_LOCAL.get();
        SHARD_TABLE_THREAD_LOCAL.remove();
        return Optional.ofNullable(shardTable);
    }


    @Getter
    @Setter
    public static class ShardTable {
        private Class<?> tableClass;
        private Object id;
    }
}
