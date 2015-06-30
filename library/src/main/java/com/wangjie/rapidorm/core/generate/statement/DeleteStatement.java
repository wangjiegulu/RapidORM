package com.wangjie.rapidorm.core.generate.statement;

import com.wangjie.rapidorm.core.config.ColumnConfig;
import com.wangjie.rapidorm.core.config.TableConfig;
import com.wangjie.rapidorm.core.generate.statement.util.SqlUtil;

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
    protected String generateStatement() {
        String tableName = tableConfig.getTableName();
        StringBuilder builder = new StringBuilder("DELETE FROM ");
        List<ColumnConfig> pkColumns = tableConfig.getPkColumnConfigs();
        builder.append(tableName);
        if (pkColumns != null && pkColumns.size() > 0) {
            builder.append(" WHERE ");
            SqlUtil.appendColumnsEqValue(builder, tableName, " AND ", pkColumns);
        }
        return builder.toString();
    }
}
