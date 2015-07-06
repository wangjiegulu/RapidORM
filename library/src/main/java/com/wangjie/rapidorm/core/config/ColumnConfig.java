package com.wangjie.rapidorm.core.config;

import java.lang.reflect.Field;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
public class ColumnConfig {
    private Field field;
    private String columnName;
    private boolean primaryKey;
    private boolean autoincrement;
    private String defaultValue;
    private boolean notNull;

    private boolean unique;
    private boolean uniqueCombo;
    private boolean index;

    private int ordinals;

//    private int kpSequence;
//    private int noKpSequence;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isAutoincrement() {
        return autoincrement;
    }

    public void setAutoincrement(boolean autoincrement) {
        this.autoincrement = autoincrement;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public boolean isUniqueCombo() {
        return uniqueCombo;
    }

    public void setUniqueCombo(boolean uniqueCombo) {
        this.uniqueCombo = uniqueCombo;
    }

    public boolean isIndex() {
        return index;
    }

    public void setIndex(boolean index) {
        this.index = index;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public void setOrdinals(int ordinals) {
        this.ordinals = ordinals;
    }

    public int getOrdinals() {
        return ordinals;
    }
}
