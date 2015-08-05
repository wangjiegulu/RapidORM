package com.wangjie.rapidorm.core.generate.builder;

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
public class Where {
    /**
     * 相等条件判断
     *
     * @param column
     * @param value
     * @return
     */
    public static Where eq(String column, Object value) {
        return single(column, SqlUtil.convertValue(value), " = ");
    }

    /**
     * 不相等条件判断
     *
     * @param column
     * @param value
     * @return
     */
    public static Where ne(String column, Object value) {
        return single(column, value, " <> ");
    }

    /**
     * 大于条件判断
     *
     * @param column
     * @param value
     * @return
     */
    public static Where gt(String column, Object value) {
        return single(column, value, " > ");
    }

    /**
     * 小于条件判断
     *
     * @param column
     * @param value
     * @return
     */
    public static Where lt(String column, Object value) {
        return single(column, value, " < ");
    }

    /**
     * 大于等于条件
     *
     * @param column
     * @param value
     * @return
     */
    public static Where ge(String column, Object value) {
        return single(column, value, " >= ");
    }

    /**
     * 小于等于条件
     *
     * @param column
     * @param value
     * @return
     */
    public static Where le(String column, Object value) {
        return single(column, value, " <= ");
    }

    /**
     * 包含条件判断
     *
     * @param column
     * @param values
     * @return
     */
    public static Where in(String column, Object... values) {
        return in(column, Arrays.asList(values));
    }

    public static Where in(String column, List<Object> values) {
        return inOrNot(column, values, " IN ");
    }

    /**
     * 不包含条件判断
     *
     * @param column
     * @param values
     * @return
     */
    public static Where ni(String column, Object... values) {
        return ni(column, Arrays.asList(values));
    }

    public static Where ni(String column, List<Object> values) {
        return inOrNot(column, values, " NOT IN ");
    }

    private static Where single(String column, Object value, String symbol) {
        StringBuilder builder = new StringBuilder("(");
        SqlUtil.formatName(builder, column);
        builder.append(symbol)
                .append("?)");
        Where where = new Where(builder);
        where.setValue(value);
        return where;
    }

    private static Where inOrNot(String column, List<Object> values, String symbol) {
        StringBuilder builder = new StringBuilder()
                .append("(");
        SqlUtil.formatName(builder, column);
        builder.append(symbol)
                .append("(");
        SqlUtil.appendPlaceholders(builder, values.size());
        builder.append(")")
                .append(")");

        Where where = new Where(builder);
        where.setValues(values);
        return where;
    }

    /**
     * 为null条件判断
     *
     * @param column
     * @return
     */
    public static Where isNull(String column) {
        return isNull(column, true);
    }

    /**
     * 不为null条件判断
     *
     * @param column
     * @return
     */
    public static Where notNull(String column) {
        return isNull(column, false);
    }

    private static Where isNull(String column, boolean isNull) {
        StringBuilder builder = new StringBuilder()
                .append("(");
        SqlUtil.formatName(builder, column);
        builder.append(isNull ? " IS NULL " : " IS NOT NULL ")
                .append(")");
        return new Where(builder);
    }

    /**
     * "BETWEEN ... AND ..." condition
     *
     * @param column
     * @param value1
     * @param value2
     * @return
     */
    public static Where between(String column, Object value1, Object value2) {
        StringBuilder builder = new StringBuilder("(");
        SqlUtil.formatName(builder, column);
        builder.append(" BETWEEN ? AND ? ")
                .append(")");
        Where where = new Where(builder);
        where.setValues(value1, value2);
        return where;
    }

    public static Where like(String column, Object value) {
        StringBuilder builder = new StringBuilder("(");
        SqlUtil.formatName(builder, column);
        builder.append(" LIKE ? ")
                .append(")");
        Where where = new Where(builder);
        where.setValue(value);
        return where;
    }

    /**
     * 原生sql条件判断
     *
     * @param rawWhere
     * @param values
     * @return
     */
    public static Where raw(String rawWhere, Object... values) {
        return raw(rawWhere, Arrays.asList(values));
    }

    public static Where raw(String rawWhere, List<Object> values) {
        Where where = new Where();
        where.setWhere(new StringBuilder(rawWhere));
        where.setValues(values);
        return where;
    }

    /**
     * And
     *
     * @param wheres
     * @return
     */
    public static Where and(Where... wheres) {
        return and(Arrays.asList(wheres));
    }

    public static Where and(List<Where> wheres) {
        return andOr(" AND ", wheres);
    }

    /**
     * Or
     *
     * @param wheres
     * @return
     */
    public static Where or(Where... wheres) {
        return or(Arrays.asList(wheres));
    }

    public static Where or(List<Where> wheres) {
        return andOr(" OR ", wheres);
    }

    private static Where andOr(String andOr, List<Where> wheres) {
        int len = wheres.size();
        if (1 > len) {
            throw new RapidORMRuntimeException(andOr + " operation of Where must has more than zero arg!");
        }
        if (1 == len) {
            return wheres.get(0);
        }

        Where where = new Where();
        final List<Object> values = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        where.setWhere(CollectionJoiner.join(wheres, andOr, builder, new CollectionJoiner.OnCollectionJoiner<Where>() {

            @Override
            public void joinContent(StringBuilder builder, Where obj) {
                values.addAll(obj.getValues());
                builder.append(obj.getWhere().toString());
            }
        }));
        builder.append(")");
        where.setValues(values);
        where.setWhere(builder);
        return where;
    }


    private StringBuilder where;
    private List<Object> values = new ArrayList<>();

    public Where() {
    }

    public Where(StringBuilder where) {
        this.where = where;
    }

    public StringBuilder getWhere() {
        return where;
    }

    public void setWhere(StringBuilder where) {
        this.where = where;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        if (null == values) {
            this.values.clear();
        } else {
            this.values = values;
        }
    }

    public void setValues(Object... values) {
        setValues(Arrays.asList(values));
    }

    public void setValue(Object value) {
        values.clear();
        if (null != value) {
            values.add(value);
        }
    }

    public void addValue(Object value) {
        if (null != value) {
            values.add(value);
        }
    }

    public void addValues(Object... values) {
        addValues(Arrays.asList(values));
    }

    public void addValues(List<Object> values) {
        if (null == values) {
            this.values.clear();
        } else {
            this.values.addAll(values);
        }
    }


}
