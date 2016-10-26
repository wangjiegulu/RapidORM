package com.wangjie.rapidorm.core.generate.statement;

import com.wangjie.rapidorm.core.config.ColumnConfig;
import com.wangjie.rapidorm.core.config.TableConfig;
import com.wangjie.rapidorm.core.generate.statement.util.SqlUtil;
import com.wangjie.rapidorm.util.collection.CollectionJoiner;

import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/30/15.
 */
public class DeleteStatement<T> extends Statement<T> {

    public DeleteStatement(TableConfig<T> tableConfig) {
        super(tableConfig);
    }

    @Override
    protected String initializeStatement() {
        String tableName = tableConfig.getTableName();
        final String alias = tableName;
        StringBuilder builder = new StringBuilder("DELETE FROM ");
        List<ColumnConfig> pkColumns = tableConfig.getPkColumnConfigs();
        SqlUtil.formatName(builder, tableName);
        if (pkColumns != null && pkColumns.size() > 0) {
            builder.append(" WHERE ");

            CollectionJoiner.join(pkColumns, " AND ", builder, new CollectionJoiner.OnCollectionJoiner<ColumnConfig>() {
                @Override
                public void joinContent(StringBuilder builder, ColumnConfig obj) {
                    builder.append(alias).append(".");
                    SqlUtil.formatName(builder, obj.getColumnName()).append("=?");
                }
            });

        }
        return builder.toString();
    }
}
