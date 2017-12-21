package com.wangjie.rapidorm.core.generate.statement;

import com.wangjie.rapidorm.core.config.ColumnConfig;
import com.wangjie.rapidorm.core.config.TableConfig;
import com.wangjie.rapidorm.core.generate.builder.Where;
import com.wangjie.rapidorm.core.generate.statement.util.SqlUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 20/12/2017.
 */
public class IsExistStatement<T> extends Statement<T> {
    public IsExistStatement(TableConfig<T> tableConfig) {
        super(tableConfig);
    }

    @Override
    protected String initializeStatement() {
        List<ColumnConfig> pkColumnConfigs = tableConfig.getPkColumnConfigs();
        int len = 0;
        if (null != pkColumnConfigs && 0 < (len = pkColumnConfigs.size())) {
            List<Where> wheres = new ArrayList<>();
            StringBuilder builder = new StringBuilder("SELECT COUNT(");
            SqlUtil.formatName(builder, pkColumnConfigs.get(0).getColumnName());
            builder.append(") FROM ");
            SqlUtil.formatName(builder, tableConfig.getTableName());
            builder.append(" WHERE ");
            for (int i = 0; i < len; i++) {
                wheres.add(Where.eq(pkColumnConfigs.get(i).getColumnName(), null));
            }
            Where finalWhere = Where.and(wheres);
            return builder.append(finalWhere.getWhere()).toString();
        }
        return "";
    }
}
