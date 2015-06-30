package com.wangjie.rapidorm.core.generate.statement.util;

import android.support.annotation.NonNull;
import com.wangjie.rapidorm.core.config.ColumnConfig;
import com.wangjie.rapidorm.core.config.TableConfig;

import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
public class SqlUtil {

    public static <T> String generateInsertSql(TableConfig<T> tableConfig) {
        return generateInsertOrReplaceSql(" INSERT INTO ", tableConfig);
    }

    public static <T> String generateInsertOrReplaceSql(TableConfig<T> tableConfig) {
        return generateInsertOrReplaceSql(" INSERT OR REPLACE INTO ", tableConfig);
    }

    private static <T> String generateInsertOrReplaceSql(String insertInto, TableConfig<T> tableConfig) {
        List<ColumnConfig> insertColumns = getInsertColumnConfigs(tableConfig);

        StringBuilder builder = new StringBuilder(insertInto);
        builder.append(tableConfig.getTableName()).append(" (");
        appendColumns(builder, insertColumns);
        builder.append(") VALUES (");
        appendPlaceholders(builder, insertColumns.size());
        builder.append(')');
        return builder.toString();
    }

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


    public static <T> String generateUpdateSql(String tablename, TableConfig<T> tableConfig) {
        StringBuilder builder = new StringBuilder("UPDATE ");
        builder.append(tablename).append(" SET ");
        appendColumnsEqualPlaceholders(builder, tableConfig.getNoPkColumnConfigs());
        builder.append(" WHERE ");
        appendColumnsEqValue(builder, tablename, tableConfig.getPkColumnConfigs());
        return builder.toString();
    }

    /**
     * Remember: SQLite does not support joins nor table alias for DELETE.
     */
    public static <T> String generateSqlDelete(@NonNull TableConfig<T> tableConfig) {
        String tableName = tableConfig.getTableName();
        StringBuilder builder = new StringBuilder("DELETE FROM ");
        List<ColumnConfig> pkColumns = tableConfig.getPkColumnConfigs();
        builder.append(tableName);
        if (pkColumns != null && pkColumns.size() > 0) {
            builder.append(" WHERE ");
            appendColumnsEqValue(builder, tableName, pkColumns);
        }
        return builder.toString();
    }

    public static <T> String generateSqlDeleteAll(@NonNull TableConfig<T> tableConfig) {
        return "DELETE FROM " + tableConfig.getTableName();
    }


    public static <T> StringBuilder appendColumnsEqualPlaceholders(StringBuilder builder, List<ColumnConfig> columns) {
        for (int i = 0; i < columns.size(); i++) {
            appendColumn(builder, columns.get(i).getColumnName()).append("=?");
            if (i < columns.size() - 1) {
                builder.append(',');
            }
        }
        return builder;
    }

    public static StringBuilder appendColumnsEqValue(StringBuilder builder, String tableAlias, List<ColumnConfig> columns) {
//        for (int i = 0, size = columns.size(); i < size; i++) {
//            appendColumn(builder, tableAlias, columns.get(i).getColumnName()).append("=?");
//            if (i != size - 1) {
//                builder.append(',');
//            }
//        }
//        return builder;
        return appendColumnsEqValue(builder, tableAlias, ",", columns);
    }

    public static StringBuilder appendColumnsEqValue(StringBuilder builder, String tableAlias, String stuff, List<ColumnConfig> columns) {
        for (int i = 0, size = columns.size(); i < size; i++) {
            appendColumn(builder, tableAlias, columns.get(i).getColumnName()).append("=?");
            if (i != size - 1) {
                builder.append(stuff);
            }
        }
        return builder;
    }

    public static StringBuilder appendColumn(StringBuilder builder, String column) {
        builder.append('\'').append(column).append('\'');
        return builder;
    }

    public static StringBuilder appendColumn(StringBuilder builder, String tableAlias, String column) {
        builder.append(tableAlias).append(".'").append(column).append('\'');
        return builder;
    }

    public static StringBuilder appendColumns(StringBuilder builder, String tableAlias, String[] columns) {
        int length = columns.length;
        for (int i = 0; i < length; i++) {
            appendColumn(builder, tableAlias, columns[i]);
            if (i < length - 1) {
                builder.append(',');
            }
        }
        return builder;
    }


    public static StringBuilder appendColumns(StringBuilder builder, List<ColumnConfig> columns) {
        int length = columns.size();
        for (int i = 0; i < length; i++) {
            builder.append('\'').append(columns.get(i).getColumnName()).append('\'');
            if (i < length - 1) {
                builder.append(',');
            }
        }
        return builder;
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


    public static String formatName(String name) {
        return "`" + name + "`";
    }

}
