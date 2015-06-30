package com.wangjie.rapidorm.core.generate.statement;

import com.wangjie.rapidorm.core.config.TableConfig;
import com.wangjie.rapidorm.core.generate.statement.util.SqlUtil;

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
    protected String generateStatement() {
        String tableName = tableConfig.getTableName();
        StringBuilder builder = new StringBuilder("UPDATE ");
        builder.append(tableName).append(" SET ");
        SqlUtil.appendColumnsEqualPlaceholders(builder, tableConfig.getNoPkColumnConfigs());
        builder.append(" WHERE ");
        SqlUtil.appendColumnsEqValue(builder, tableName, " AND ", tableConfig.getPkColumnConfigs());
        return builder.toString();
    }


}
