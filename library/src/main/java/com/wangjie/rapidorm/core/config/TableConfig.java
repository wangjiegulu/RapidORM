package com.wangjie.rapidorm.core.config;

import android.support.annotation.NonNull;
import com.wangjie.rapidorm.annotations.Column;
import com.wangjie.rapidorm.annotations.Table;
import com.wangjie.rapidorm.constants.Constants;
import com.wangjie.rapidorm.core.generate.statement.DeleteStatement;
import com.wangjie.rapidorm.core.generate.statement.InsertStatement;
import com.wangjie.rapidorm.core.generate.statement.TableCreateStatement;
import com.wangjie.rapidorm.core.generate.statement.UpdateStatement;
import com.wangjie.rapidorm.exception.ConfigInitialFailedException;
import com.wangjie.rapidorm.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
public class TableConfig<T> {
    private static final String TAG = TableConfig.class.getSimpleName();
    private String tableName;
    private Class<T> tableClazz;

    private List<ColumnConfig> allColumnConfigs;
    private List<ColumnConfig> pkColumnConfigs;
    private List<ColumnConfig> noPkColumnConfigs;

    private List<ColumnConfig> indexColumnConfigsCache;
    private List<ColumnConfig> uniqueComboColumnConfigsCache;

    private TableCreateStatement<T> tableCreateStatement;
    private InsertStatement<T> insertStatement;
    private UpdateStatement<T> updateStatement;
    private DeleteStatement<T> deleteStatement;


    public TableConfig(@NonNull Class<T> tableClazz) {
        this.tableClazz = tableClazz;
        parserAllConfigs();
        tableCreateStatement = new TableCreateStatement<>(this);

    }

    private void parserAllConfigs() {
        Table table = tableClazz.getAnnotation(Table.class);
        if (null == table) {
            throw new ConfigInitialFailedException(tableClazz.getName() + " is not a table, have you add @Table to this class?.");
        }

        tableName = getTableName(table);

        allColumnConfigs = new ArrayList<>();
        pkColumnConfigs = new ArrayList<>();
        noPkColumnConfigs = new ArrayList<>();

        ReflectionUtils.doWithFieldsWithSuper(tableClazz, new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws Exception {
                Column column = field.getAnnotation(Column.class);
                if (null == column) {
                    return;
                }

                ColumnConfig columnConfig = new ColumnConfig();
                columnConfig.setField(field);
                // column name
                columnConfig.setColumnName(getColumnName(field, column));
                // autoincrement
                columnConfig.setAutoincrement(column.autoincrement());
                // notNull
                columnConfig.setNotNull(column.notNull());
                // defaultValue
                columnConfig.setDefaultValue(column.defaultValue());
                // index
                columnConfig.setIndex(column.index());
                // unique
                columnConfig.setUnique(column.unique());
                // uniqueCombo
                columnConfig.setUniqueCombo(column.uniqueCombo());
                // primaryKey
                boolean primaryKey = column.primaryKey();
                columnConfig.setPrimaryKey(primaryKey);

                allColumnConfigs.add(columnConfig);
                if (primaryKey) {
                    pkColumnConfigs.add(columnConfig);
                } else {
                    noPkColumnConfigs.add(columnConfig);
                }


            }
        });
    }


    /**
     * 获得Table对应的value值（表名）
     *
     * @return 如果Table注解为空，那么直接使用类名作为表名；否则使用value值
     */
    @NonNull
    private String getTableName(Table table) {
        String name = table.name();
        if (Constants.AnnotationNotSetValue.TABLE_NAME.equals(name)) { // 如果类中没有加Table注解，或者Table注解为空，那么直接使用类名作为表名
            return tableClazz.getSimpleName();
        }
        return name;
    }


    /**
     * 获得Column对应的value值（表的列名）
     *
     * @param field
     * @return 如果该属性的column注解value为空，则使用属性名作为列名；否则使用value值
     */
    private String getColumnName(Field field, Column column) {
        if (null == column) { // 如果该属性没有加column注解，则返回null
            // todo throw runtime exception
            return null;
        }
        String name = column.name();
        if (Constants.AnnotationNotSetValue.COLUMN_NAME.equals(name)) { // 如果该属性的column注解value为空，则使用属性名
            return field.getName();
        }
        return name;
    }


    public List<ColumnConfig> getUniqueComboColumnConfigs() {
        List<ColumnConfig> uniqueComboColumnConfigs = new ArrayList<>();
        for (ColumnConfig columnConfig : allColumnConfigs) {
            if (columnConfig.isUniqueCombo()) {
                uniqueComboColumnConfigs.add(columnConfig);
            }
        }
        return uniqueComboColumnConfigs;
    }

    public List<ColumnConfig> getUniqueComboColumnConfigsFromCache() {
        if (null == uniqueComboColumnConfigsCache) {
            uniqueComboColumnConfigsCache = getUniqueComboColumnConfigs();
        }
        return uniqueComboColumnConfigsCache;
    }

    public List<ColumnConfig> getIndexColumnConfigs() {
        List<ColumnConfig> indexColumnConfigs = new ArrayList<>();
        for (ColumnConfig columnConfig : allColumnConfigs) {
            if (columnConfig.isIndex()) {
                indexColumnConfigs.add(columnConfig);
            }
        }
        return indexColumnConfigs;
    }

    public List<ColumnConfig> getIndexColumnConfigsFromCache() {
        if (null == indexColumnConfigsCache) {
            indexColumnConfigsCache = getIndexColumnConfigs();
        }
        return indexColumnConfigsCache;
    }

    public String getTableName() {
        return tableName;
    }

    public Class<T> getTableClazz() {
        return tableClazz;
    }

    public List<ColumnConfig> getAllColumnConfigs() {
        return allColumnConfigs;
    }

    public List<ColumnConfig> getPkColumnConfigs() {
        return pkColumnConfigs;
    }

    public List<ColumnConfig> getNoPkColumnConfigs() {
        return noPkColumnConfigs;
    }


    public TableCreateStatement<T> getTableCreateStatement() {
        return tableCreateStatement;
    }

    public synchronized InsertStatement<T> getInsertStatement() {
        if(null == insertStatement){
            insertStatement = new InsertStatement<>(this);
        }
        return insertStatement;
    }

    public synchronized UpdateStatement<T> getUpdateStatement() {
        if(null == updateStatement){
            updateStatement = new UpdateStatement<>(this);
        }
        return updateStatement;
    }

    public synchronized DeleteStatement<T> getDeleteStatement() {
        if(null == deleteStatement){
            deleteStatement = new DeleteStatement<>(this);
        }
        return deleteStatement;
    }

}
