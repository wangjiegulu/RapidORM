package com.wangjie.rapidorm.core.generate.builder;

import com.wangjie.rapidorm.core.config.TableConfig;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/30/15.
 */
public class UpdateBuilder<T> extends RapidBuilder {

    private Where where;
    private TableConfig<T> tableConfig;

    public TableConfig<T> getTableConfig() {
        return tableConfig;
    }

    public void setTableConfig(TableConfig<T> tableConfig) {
        this.tableConfig = tableConfig;
    }

    public Where getWhere() {
        return where;
    }

    public UpdateBuilder<T> setWhere(Where where) {
        this.where = where;
        return this;
    }

    @Override
    public String generateSql() {
        return null;
    }
}
