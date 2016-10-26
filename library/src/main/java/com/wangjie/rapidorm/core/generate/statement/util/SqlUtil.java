package com.wangjie.rapidorm.core.generate.statement.util;

import android.support.annotation.NonNull;
import com.wangjie.rapidorm.core.config.ColumnConfig;
import com.wangjie.rapidorm.core.config.TableConfig;
import com.wangjie.rapidorm.util.TypeUtil;

import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
public class SqlUtil {

    public static <T> List<ColumnConfig> getInsertColumnConfigs(TableConfig<T> tableConfig) {
        List<ColumnConfig> insertColumns;
        List<ColumnConfig> pkColumnConfigs = tableConfig.getPkColumnConfigs();
        ColumnConfig firstPkColumnConfig;
        if (
                1 == pkColumnConfigs.size()
                        && (firstPkColumnConfig = pkColumnConfigs.get(0)).isPrimaryKey()
                        && firstPkColumnConfig.isAutoincrement()
                ) {
            insertColumns = tableConfig.getNoPkColumnConfigs();
        } else {
            insertColumns = tableConfig.getAllColumnConfigs();
        }
        return insertColumns;
    }


    public static <T> String generateSqlDeleteAll(@NonNull TableConfig<T> tableConfig) {
        return "DELETE FROM " + tableConfig.getTableName();
    }

    public static <T> String generateSqlQueryAll(@NonNull TableConfig<T> tableConfig) {
        return "SELECT * FROM " + tableConfig.getTableName();
    }

    public static StringBuilder appendPlaceholders(StringBuilder builder, int count) {
        for (int i = 0; i < count; i++) {
            if (i < count - 1) {
                builder.append("?,");
            } else {
                builder.append('?');
            }
        }
        return builder;
    }

    public static StringBuilder formatName(StringBuilder builder, String name) {
        return builder.append("`").append(name).append("`");
    }



    public static Object convertValue(Object fieldValue){
        if(null == fieldValue){
            return null;
        }
        if(TypeUtil.isBooleanIncludePrimitive(fieldValue.getClass())){
            return ((Boolean)fieldValue) ? 1 : 0;
        }
        return fieldValue;
    }

}
