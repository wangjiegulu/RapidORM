package com.wangjie.rapidorm.core.generate.statement;

import com.wangjie.rapidorm.core.config.ColumnConfig;
import com.wangjie.rapidorm.core.config.TableConfig;
import com.wangjie.rapidorm.core.generate.statement.util.SqlUtil;
import com.wangjie.rapidorm.util.collection.CollectionJoiner;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/30/15.
 */
public class UpdateStatement<T> extends Statement<T> {

    public UpdateStatement(TableConfig<T> tableConfig) {
        super(tableConfig);
    }

    @Override
    protected String initializeStatement() {
        final String tableName = tableConfig.getTableName();
        StringBuilder builder = new StringBuilder("UPDATE ");
        builder.append(tableName).append(" SET ");
//        appendColumnsEqualPlaceholders(builder, tableConfig.getNoPkColumnConfigs());

        CollectionJoiner.join(tableConfig.getNoPkColumnConfigs(), ",", builder, new CollectionJoiner.OnCollectionJoiner<ColumnConfig>() {

            @Override
            public void joinContent(StringBuilder builder, ColumnConfig obj) {
                SqlUtil.formatName(builder, obj.getColumnName()).append("=?");
            }
        });

        builder.append(" WHERE ");
//        SqlUtil.appendColumnsEqValue(builder, tableName, " AND ", tableConfig.getPkColumnConfigs());

        CollectionJoiner.join(tableConfig.getPkColumnConfigs(), " AND ", builder, new CollectionJoiner.OnCollectionJoiner<ColumnConfig>() {

            @Override
            public void joinContent(StringBuilder builder, ColumnConfig obj) {
                builder.append(tableName).append(".");
                SqlUtil.formatName(builder, obj.getColumnName()).append("=?");
            }
        });

        return builder.toString();
    }


}
