package com.wangjie.rapidorm.core.generate.statement;

import com.wangjie.rapidorm.api.constant.Constants;
import com.wangjie.rapidorm.constants.RapidORMConfig;
import com.wangjie.rapidorm.core.config.ColumnConfig;
import com.wangjie.rapidorm.core.config.TableConfig;

import android.util.Log;

import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
public class TableCreateStatement<T> {

    public static class StatementTemplate {
        public static final String CREATE_TABLE_HEAD = " CREATE TABLE #{ifNotExists} `#{tableName}` ( ";
        public static final String CREATE_TABLE_COLUMN = " '#{columnName}' #{type} #{primaryKey} #{autoincrement} #{notNull} #{unique} #{default}";
        public static final String CREATE_TABLE_PRIMARY_KEY = " PRIMARY KEY (#{primaryKeyJoin}) ";
        public static final String CREATE_TABLE_END = " ); ";
    }

    private static final String TAG = TableCreateStatement.class.getSimpleName();

    private TableConfig<T> tableConfig;

    public TableCreateStatement(TableConfig<T> tableConfig) {
        this.tableConfig = tableConfig;
    }

    public StringBuilder buildStatement(boolean ifNotExists) {
        String existsConstraint = ifNotExists ? "IF NOT EXISTS " : "";
        StringBuilder sql = new StringBuilder();
        sql.append(
                StatementTemplate.CREATE_TABLE_HEAD
                        .replace("#{ifNotExists}", existsConstraint)
                        .replace("#{tableName}", tableConfig.getTableName())
        );

        List<ColumnConfig> pkColumnConfigs = tableConfig.getPkColumnConfigs();
        List<ColumnConfig> columnConfigs = tableConfig.getAllColumnConfigs();
        boolean isPkMultiple = pkColumnConfigs.size() > 1;
        for (int i = 0, len = columnConfigs.size(); i < len; i++) {
            ColumnConfig columnConfig = columnConfigs.get(i);
            String defaultValue;
            sql.append("\n")
                    .append(
                            StatementTemplate.CREATE_TABLE_COLUMN
                                    .replace("#{columnName}", columnConfig.getColumnName())
                                    .replace("#{type}", columnConfig.getDbType())
                                    .replace("#{primaryKey}", !isPkMultiple && columnConfig.isPrimaryKey() ? "PRIMARY KEY" : "")
                                    .replace("#{autoincrement}", !isPkMultiple && columnConfig.isAutoincrement() ? "AUTOINCREMENT" : "")
                                    .replace("#{notNull}", columnConfig.isNotNull() ? "NOT NULL" : "")
                                    .replace("#{unique}", columnConfig.isUnique() ? "UNIQUE" : "")
                                    .replace("#{default}", Constants.AnnotationNotSetValue.DEFAULT_VALUE.equals(defaultValue = columnConfig.getDefaultValue()) ? "" : "default " + defaultValue)
                    );
            if (i != len - 1) { // not last
                sql.append(",");
            } else {
                if(isPkMultiple){
                    addPrimaryKeyConstraints(pkColumnConfigs, sql);
                }
            }
        }


        sql.append(StatementTemplate.CREATE_TABLE_END);
        if (RapidORMConfig.DEBUG)
            Log.i(TAG, "buildCreateTable[" + tableConfig.getTableName() + "] sql: " + sql.toString());

        return sql;
    }

    private void addPrimaryKeyConstraints(List<ColumnConfig> pkColumnConfigs, StringBuilder sql) {
        // Primary key Constraint
        int pkCount = pkColumnConfigs.size();
        switch (pkCount) {
            case 0: // No primary key
                break;
            case 1: // One primary key
                ColumnConfig columnConfig = pkColumnConfigs.get(0);
                sql.append(",")
                        .append("\n")
                        .append(StatementTemplate.CREATE_TABLE_PRIMARY_KEY
                                        .replace("#{primaryKeyJoin}", columnConfig.getColumnName())
                        );
                break;
            default: // Multiple primary keys
                sql.append(",")
                        .append("\n")
                        .append(StatementTemplate.CREATE_TABLE_PRIMARY_KEY
                                        .replace("#{primaryKeyJoin}", joinPks(pkColumnConfigs))
                        );
                break;
        }

    }


    private String joinPks(List<ColumnConfig> pkColumnConfigs) {
        StringBuilder pks = new StringBuilder();
        for (int i = 0, size = pkColumnConfigs.size(); i < size; i++) {
            pks.append(pkColumnConfigs.get(i).getColumnName());
            if (i != size - 1) {
                pks.append(", ");
            }
        }
        return pks.toString();
    }


}
