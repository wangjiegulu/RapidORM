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
    private StringBuilder where;
    private List<Object> values = new ArrayList<>();

    public Where() {
    }

    public Where(StringBuilder where) {
        this.where = where;
    }

    /**
     * 相等条件判断
     *
     * @param column
     * @param value
     * @return
     */
    public static Where eq(String column, Object value) {
        return single(column, value, " = ");
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
        Where where = new Where(
                new StringBuilder("(")
                        .append(formatColumn(column))
                        .append(symbol)
                        .append("?)")
        );
        where.setValue(value);
        return where;
    }

    private static Where inOrNot(String column, List<Object> values, String symbol) {
        StringBuilder builder = new StringBuilder()
                .append("(")
                .append(formatColumn(column))
                .append(symbol)
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
        return new Where(
                new StringBuilder()
                        .append("(")
                        .append(column)
                        .append(isNull ? " IS NULL " : " IS NOT NULL ")
                        .append(")")
        );
    }

    /**
     * 原生sql条件判断
     *
     * @param rawWhere
     * @param values
     * @return
     */
    public static Where raw(String rawWhere, Object... values) {
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
        return andOr(" AND ", wheres);
    }

    /**
     * Or
     *
     * @param wheres
     * @return
     */
    public static Where or(Where... wheres) {
        return andOr(" OR ", wheres);
    }

    private static Where andOr(String andOr, Where... wheres) {
        int len = wheres.length;
        if (1 > len) {
            throw new RapidORMRuntimeException(andOr + " operation of Where must has more than zero arg!");
        }
        if (1 == len) {
            return wheres[0];
        }

        Where where = new Where();
        final List<Object> values = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        where.setWhere(CollectionJoiner.join(Arrays.asList(wheres), andOr, builder, new CollectionJoiner.OnCollectionJoiner<Where>() {
            @Override
            public String getJoinContent(Where obj) {
                values.addAll(obj.getValues());
                return obj.getWhere().toString();
            }
        }));
        builder.append(")");
        where.setValues(values);
        where.setWhere(builder);
        return where;
    }


    private static String formatColumn(String column) {
        return "`" + column + "`";
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
        if (null == values) {
            this.values.clear();
        } else {
            this.values = Arrays.asList(values);
        }
    }

    public void setValue(Object value) {
        values.add(value);
    }


}
