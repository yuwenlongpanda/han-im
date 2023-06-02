package com.ywl.framework.mysql.shard;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liao
 */
public class TableHashShardStrategy implements TableShardStrategy {

    @Override
    public String getTableShardName(Class<?> tableClass, String tableName, Object id) {
        long idLong = Long.parseLong(String.valueOf(id));
        TableShard annotation = tableClass.getAnnotation(TableShard.class);
        int shard = annotation.hashShard();
        long l = idLong % shard;
        return tableName.concat("_").concat(String.valueOf(l));
    }

    @Override
    public List<String> listTableShardName(Class<?> tableClass, String tableName) {
        TableShard annotation = tableClass.getAnnotation(TableShard.class);
        int shard = annotation.hashShard();
        List<String> tableList = new ArrayList<>();
        for (int i = 0; i < shard; i++) {
            tableList.add(tableName.concat("_").concat(String.valueOf(i)));
        }
        return tableList;
    }
}
