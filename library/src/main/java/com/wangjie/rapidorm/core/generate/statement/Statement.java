package com.wangjie.rapidorm.core.generate.statement;

import com.wangjie.rapidorm.core.config.TableConfig;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/30/15.
 */
public abstract class Statement<T> {
    protected String statement;
    protected TableConfig<T> tableConfig;

    public Statement(TableConfig<T> tableConfig) {
        this.tableConfig = tableConfig;
    }

    public synchronized String getStatement() {
        if (null == statement) {
            statement = generateStatement();
        }
        return statement;
    }

    protected abstract String generateStatement();

}
