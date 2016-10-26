package com.wangjie.rapidorm.compiler.objs;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import com.wangjie.rapidorm.api.annotations.Column;
import com.wangjie.rapidorm.api.constant.Constants;

import java.sql.Blob;

import javax.lang.model.element.Element;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/28/16.
 */
public class ColumnEntry {
    private Element fieldColumnElement;
    private TypeName fieldColumnTypeName;
    private String fieldSimpleName;
    private String columnName;
    private String dbType;
    private String dataType;

    public ColumnEntry(Element fieldColumnElement) {
        this.fieldColumnElement = fieldColumnElement;
        fieldSimpleName = fieldColumnElement.getSimpleName().toString();
        columnName = getColumnName(fieldSimpleName, fieldColumnElement.getAnnotation(Column.class));
        fieldColumnTypeName = ClassName.get(fieldColumnElement.asType());
        dbType = parseDbType(fieldColumnTypeName);
        dataType = parseDataType(fieldColumnTypeName);
    }

    public String getFieldSimpleName() {
        return fieldSimpleName;
    }

    public Element getFieldColumnElement() {
        return fieldColumnElement;
    }

    public TypeName getFieldColumnTypeName() {
        return fieldColumnTypeName;
    }

    @Override
    public String toString() {
        return "ColumnEntry{" +
                "fieldColumnElement=" + fieldColumnElement +
                '}';
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    /**
     * 获得Column对应的value值（表的列名）
     *
     * @return 如果该属性的column注解value为空，则使用属性名作为列名；否则使用value值
     */
    private String getColumnName(String defaultName, Column column) {
        String name = column.name();
        if (Constants.AnnotationNotSetValue.COLUMN_NAME.equals(name)) { // 如果该属性的column注解value为空，则使用属性名
            return defaultName;
        }
        return name;
    }

    public String parseDbType(TypeName typeName) {
        String typeNameStr = typeName.toString();
        if (String.class.getCanonicalName().equals(typeNameStr)) {
            return "TEXT";
        } else if (Long.class.getCanonicalName().equals(typeNameStr) || long.class.getCanonicalName().equals(typeNameStr)) {
            return "LONG";
        } else if (Integer.class.getCanonicalName().equals(typeNameStr) || int.class.getCanonicalName().equals(typeNameStr) || Boolean.class.getCanonicalName().equals(typeNameStr) || boolean.class.getCanonicalName().equals(typeNameStr)) {
            return "INTEGER";
        } else if (Short.class.getCanonicalName().equals(typeNameStr) || short.class.getCanonicalName().equals(typeNameStr)) {
            return "SHORT";
        } else if (Double.class.getCanonicalName().equals(typeNameStr) || double.class.getCanonicalName().equals(typeNameStr)) {
            return "DOUBLE";
        } else if (Float.class.getCanonicalName().equals(typeNameStr) || float.class.getCanonicalName().equals(typeNameStr)) {
            return "FLOAT";
        } else if (Blob.class.getCanonicalName().equals(typeNameStr)) {
            return "BLOB";
        }

        throw new RuntimeException("columnType[" + typeNameStr + "] not supported");
    }

    protected String parseDataType(TypeName typeName) {
        String typeNameStr = typeName.toString();
        if (String.class.getCanonicalName().equals(typeNameStr)) {
            return "String";
        } else if (Long.class.getCanonicalName().equals(typeNameStr) || long.class.getCanonicalName().equals(typeNameStr)) {
            return "Long";
        } else if (Integer.class.getCanonicalName().equals(typeNameStr) || int.class.getCanonicalName().equals(typeNameStr) || Boolean.class.getCanonicalName().equals(typeNameStr) || boolean.class.getCanonicalName().equals(typeNameStr)) {
            return "Int";
        } else if (Short.class.getCanonicalName().equals(typeNameStr) || short.class.getCanonicalName().equals(typeNameStr)) {
            return "Short";
        } else if (Double.class.getCanonicalName().equals(typeNameStr) || double.class.getCanonicalName().equals(typeNameStr)) {
            return "Double";
        } else if (Float.class.getCanonicalName().equals(typeNameStr) || float.class.getCanonicalName().equals(typeNameStr)) {
            return "Float";
        } else if (Blob.class.getCanonicalName().equals(typeNameStr)) {
            return "Blob";
        }
        return null;
    }
}
