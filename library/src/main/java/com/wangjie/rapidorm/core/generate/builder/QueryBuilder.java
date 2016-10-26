package com.wangjie.rapidorm.core.generate.builder;

import com.wangjie.rapidorm.core.config.TableConfig;
import com.wangjie.rapidorm.core.dao.BaseDao;
import com.wangjie.rapidorm.core.generate.statement.util.SqlUtil;
import com.wangjie.rapidorm.exception.RapidORMRuntimeException;
import com.wangjie.rapidorm.util.collection.CollectionJoiner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/30/15.
 */
public class QueryBuilder<T> extends RapidBuilder {

    class OrderCase {
        public boolean isAsc;
        public String column;

        public OrderCase(String column, boolean isAsc) {
            this.column = column;
            this.isAsc = isAsc;
        }
    }

    private List<String> selectColumns;
    private Where where;
    private Integer limit;
    private TableConfig<T> tableConfig;
    private List<Object> values;
    private List<OrderCase> orderCase;
    private boolean distinct;

    private BaseDao<T> dao;

    public QueryBuilder(BaseDao<T> dao) {
        selectColumns = new ArrayList<>();
        values = new ArrayList<>();
        orderCase = new ArrayList<>();
        this.dao = dao;
    }

    public QueryBuilder<T> setWhere(Where where) {
        this.where = where;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public QueryBuilder<T> setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public QueryBuilder<T> setDistinct(boolean distinct) {
        this.distinct = distinct;
        return this;
    }

    public QueryBuilder<T> addSelectColumns(List<String> selectColumns) {
        if (null != selectColumns) {
            this.selectColumns.addAll(selectColumns);
        }
        return this;
    }

    public QueryBuilder<T> addSelectColumn(String... selectColumn) {
        this.selectColumns.addAll(Arrays.asList(selectColumn));
        return this;
    }

    public QueryBuilder<T> addOrder(String column, boolean isAsc) {
        orderCase.add(new OrderCase(column, isAsc));
        return this;
    }

    public QueryBuilder<T> setTableConfig(TableConfig<T> tableConfig) {
        this.tableConfig = tableConfig;
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
        if (null == tableConfig) {
            throw new RapidORMRuntimeException("[generateSql() method of QueryBuilder] TableConfig is null!");
        }
        values.clear();

        StringBuilder sql = new StringBuilder(" SELECT ");
        if (distinct) {
            sql.append(" DISTINCT ");
        }
        sql.append(null == selectColumns || 0 == selectColumns.size() ? "*" : CollectionJoiner.join(selectColumns, ","));
        sql.append(" FROM ");
        SqlUtil.formatName(sql, tableConfig.getTableName());
        sql.append(" ");

        if (null != where) {
            sql.append(" WHERE ")
                    .append(where.getWhere());
            values = where.getValues();
        }

        if (0 < orderCase.size()) {
            sql.append(" ORDER BY ");
            CollectionJoiner.join(orderCase, ",", sql, new CollectionJoiner.OnCollectionJoiner<OrderCase>() {

                @Override
                public void joinContent(StringBuilder builder, OrderCase obj) {
                    SqlUtil.formatName(builder, obj.column).append(" ").append(obj.isAsc ? " ASC " : " DESC ");
                }
            });
        }

        if (null != limit) {
            sql.append(" LIMIT ? ");
            values.add(limit);
        }
        return sql.toString();
    }

    public List<T> query() throws Exception {
        return dao.rawQuery(generateSql(), getValuesAsStringArray());
    }

    public T queryFirst() throws Exception {
        List<T> list = dao.rawQuery(generateSql(), getValuesAsStringArray());
        return null == list || 0 == list.size() ? null : list.get(0);
    }

    @Deprecated
    public List<T> query(BaseDao<T> baseDao) throws Exception {
        return baseDao.rawQuery(generateSql(), getValuesAsStringArray());
    }

    @Deprecated
    public T queryFirst(BaseDao<T> baseDao) throws Exception {
        List<T> list = baseDao.rawQuery(generateSql(), getValuesAsStringArray());
        return null == list || 0 == list.size() ? null : list.get(0);
    }

}
