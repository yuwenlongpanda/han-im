package com.ywl.framework.mysql.shard;

import java.io.Serializable;
import java.util.List;

/**
 * @author liao
 */
public interface TableShardStrategy {

    /**
     * 通过主键id 和 原始表命进行分表
     * @param tableName 表名
     * @param id 主键id
     * @return 目标表名
     */
    String getTableShardName(Class<?> tableClass, String tableName, Object id);


    List<String> listTableShardName(Class<?> tableClass, String tableName);
}
