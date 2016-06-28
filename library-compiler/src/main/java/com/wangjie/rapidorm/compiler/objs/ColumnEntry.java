package com.wangjie.rapidorm.compiler.objs;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/28/16.
 */
public class ColumnEntry {
    private Element fieldColumnElement;
    private String fieldSimpleName;

    public ColumnEntry(Element fieldColumnElement) {
        this.fieldColumnElement = fieldColumnElement;
        fieldSimpleName = fieldColumnElement.getSimpleName().toString();
    }

    public String getFieldSimpleName() {
        return fieldSimpleName;
    }

    public Element getFieldColumnElement() {
        return fieldColumnElement;
    }

    public TypeName getFieldColumnTypeName() {
        return ClassName.get(fieldColumnElement.asType());
    }

    @Override
    public String toString() {
        return "ColumnEntry{" +
                "fieldColumnElement=" + fieldColumnElement +
                '}';
    }
}
