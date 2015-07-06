package com.wangjie.rapidorm.core.generate.templates;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 7/1/15.
 */
public class ModelFieldMapper {
    public int order;
    public String field;
    public String column;

    public ModelFieldMapper(int order, String fieldName, String columnName) {
        this.order = order;
        this.field = fieldName;
        this.column = columnName;
    }
}
