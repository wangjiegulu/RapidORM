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
        String tableName = tableConfig.getTableName();
        final String alias = tableName;
        StringBuilder builder = new StringBuilder("UPDATE ");
        SqlUtil.formatName(builder, tableName)
                .append(" SET ");

        CollectionJoiner.join(tableConfig.getNoPkColumnConfigs(), ",", builder, new CollectionJoiner.OnCollectionJoiner<ColumnConfig>() {

            @Override
            public void joinContent(StringBuilder builder, ColumnConfig obj) {
                SqlUtil.formatName(builder, obj.getColumnName()).append("=?");
            }
        });

        builder.append(" WHERE ");

        CollectionJoiner.join(tableConfig.getPkColumnConfigs(), " AND ", builder, new CollectionJoiner.OnCollectionJoiner<ColumnConfig>() {

            @Override
            public void joinContent(StringBuilder builder, ColumnConfig obj) {
                builder.append(alias).append(".");
                SqlUtil.formatName(builder, obj.getColumnName()).append("=?");
            }
        });

        return builder.toString();
    }


}
