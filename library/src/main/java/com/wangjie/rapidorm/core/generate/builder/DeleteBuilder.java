package com.wangjie.rapidorm.core.generate.builder;

import com.wangjie.rapidorm.core.config.TableConfig;
import com.wangjie.rapidorm.exception.RapidORMRuntimeException;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/30/15.
 */
public class DeleteBuilder<T> extends RapidBuilder {
    private Where where;
    private List<Object> values;
    private TableConfig<T> tableConfig;

    public DeleteBuilder() {
        values = new ArrayList<>();
    }

    public Where getWhere() {
        return where;
    }

    public DeleteBuilder<T> setWhere(Where where) {
        this.where = where;
        return this;
    }

    public List<Object> getValues() {
        return values;
    }

    public TableConfig<T> getTableConfig() {
        return tableConfig;
    }

    public void setTableConfig(TableConfig<T> tableConfig) {
        this.tableConfig = tableConfig;
    }

    @Override
    public String generateSql() {
        if (null == tableConfig) {
            throw new RapidORMRuntimeException("[generateSql() method of QueryBuilder] TableConfig is null!");
        }
        StringBuilder sql = new StringBuilder(" DELETE FROM ").append(formatTableName(tableConfig.getTableName()));
        if (null != where) {
            sql.append(" WHERE ")
                    .append(where.getWhere());
            values = where.getValues();
        }
        return sql.toString();
    }
}
