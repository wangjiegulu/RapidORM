package com.wangjie.rapidorm.core.config;

import java.lang.reflect.Field;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
public class ColumnConfig {
    /**
     * Field NOT NULL only when `{@link com.wangjie.rapidorm.constants.RapidORMConfig#BIND_FIELD_COLUMN_WITH_REFLECTION}`
     * is TRUE, default is null.
     */
    private Field field;
    /**
     * Column name of database
     */
    private String columnName;
    /**
     * Whether it is a primary key
     */
    private boolean primaryKey;
    /**
     * Whether it is autoincrement
     * It is only works when this column is the one and only one primary key
     */
    private boolean autoincrement;
    /**
     * Default value, Not recommended
     */
    private String defaultValue;
    /**
     * Whether it is not null
     */
    private boolean notNull;
    /**
     * Whether it is unique constraint
     */
    private boolean unique;
    private boolean uniqueCombo;
    @Deprecated
    private boolean index;

    //    private int ordinals;
    private String dbType;

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

//    public void setOrdinals(int ordinals) {
//        this.ordinals = ordinals;
//    }
//
//    public int getOrdinals() {
//        return ordinals;
//    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }
}
