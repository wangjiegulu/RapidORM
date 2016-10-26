package com.wangjie.rapidorm.core.generate.builder;

import com.wangjie.rapidorm.core.config.TableConfig;
import com.wangjie.rapidorm.core.dao.BaseDao;
import com.wangjie.rapidorm.core.generate.statement.util.SqlUtil;
import com.wangjie.rapidorm.exception.RapidORMRuntimeException;
import com.wangjie.rapidorm.util.collection.CollectionJoiner;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/30/15.
 */
public class UpdateBuilder<T> extends RapidBuilder {
    class UpdateCase {
        public String column;
        public Object value;

        public UpdateCase(String column, Object value) {
            this.column = column;
            this.value = value;
        }
    }

    private Where where;
    private List<UpdateCase> updateCases;
    private List<Object> values;
    private TableConfig<T> tableConfig;

    private BaseDao<T> dao;

    public UpdateBuilder(BaseDao<T> dao) {
        updateCases = new ArrayList<>();
        values = new ArrayList<>();
        this.dao = dao;
    }

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

    public UpdateBuilder<T> addUpdateColumn(String column, Object value) {
        updateCases.add(new UpdateCase(column, SqlUtil.convertValue(value)));
        return this;
    }

    public List<Object> getValues() {
        return values;
    }

    public String[] getValuesAsStringArray() {
        return objectListToStringArray(values);
    }

    @Override
    public String generateSql() {
        if (null == updateCases || 0 == updateCases.size()) {
            throw new RapidORMRuntimeException("UPDATE statements must have at least one SET column.");
        }
        values.clear();

        StringBuilder sql = new StringBuilder(" UPDATE ");
        SqlUtil.formatName(sql, tableConfig.getTableName());
        sql.append(" SET ");

        CollectionJoiner.join(updateCases, ",", sql, new CollectionJoiner.OnCollectionJoiner<UpdateCase>() {

            @Override
            public void joinContent(StringBuilder builder, UpdateCase obj) {
                values.add(obj.value);
                SqlUtil.formatName(builder, obj.column).append("=?");
            }
        });

        if (null != where) {
            sql.append(" WHERE ")
                    .append(where.getWhere());
            values.addAll(where.getValues());
        }
        return sql.toString();
    }

    public void update() throws Exception{
        dao.rawExecute(generateSql(), getValuesAsStringArray());
    }

    @Deprecated
    public void update(BaseDao<T> baseDao) throws Exception{
        baseDao.rawExecute(generateSql(), getValuesAsStringArray());
    }
}
