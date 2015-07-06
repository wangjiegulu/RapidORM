package com.wangjie.rapidorm.core.config;

import android.support.annotation.NonNull;
import android.util.Log;
import com.wangjie.rapidorm.annotations.Column;
import com.wangjie.rapidorm.annotations.Table;
import com.wangjie.rapidorm.constants.Constants;
import com.wangjie.rapidorm.constants.RapidORMConfig;
import com.wangjie.rapidorm.core.generate.statement.DeleteStatement;
import com.wangjie.rapidorm.core.generate.statement.InsertStatement;
import com.wangjie.rapidorm.core.generate.statement.TableCreateStatement;
import com.wangjie.rapidorm.core.generate.statement.UpdateStatement;
import com.wangjie.rapidorm.core.generate.templates.ModelFieldMapper;
import com.wangjie.rapidorm.core.generate.templates.ModelPropertyGenerator;
import com.wangjie.rapidorm.core.generate.withoutreflection.IModelProperty;
import com.wangjie.rapidorm.exception.ConfigInitialFailedException;
import com.wangjie.rapidorm.exception.RapidORMRuntimeException;
import com.wangjie.rapidorm.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

    private Class propertyClazz;

    private List<ColumnConfig> allColumnConfigs;
    private List<ColumnConfig> pkColumnConfigs;
    private List<ColumnConfig> noPkColumnConfigs;

    private List<ColumnConfig> indexColumnConfigsCache;
    private List<ColumnConfig> uniqueComboColumnConfigsCache;

    private TableCreateStatement<T> tableCreateStatement;
    private InsertStatement<T> insertStatement;
    private UpdateStatement<T> updateStatement;
    private DeleteStatement<T> deleteStatement;

    private boolean withoutReflection;

    public TableConfig(@NonNull Class<T> tableClazz) {
        this.tableClazz = tableClazz;
        parserAllConfigs();
        tableCreateStatement = new TableCreateStatement<>(this);
    }

    private void parserAllConfigs() {
        Table table = tableClazz.getAnnotation(Table.class);
        if (null == table) {
            throw new ConfigInitialFailedException(String.format("%s is not a table, have you add @Table to this class?.", tableClazz.getName()));
        }

        // 如果实现了ModelWithoutReflection，则不使用反射
        propertyClazz = table.propertyClazz();
        withoutReflection = null != propertyClazz && IModelProperty.class.isAssignableFrom(propertyClazz);

        tableName = getTableName(table);

        allColumnConfigs = new ArrayList<>();
        pkColumnConfigs = new ArrayList<>();
        noPkColumnConfigs = new ArrayList<>();

        if (!withoutReflection) {
            initialize();
            if (RapidORMConfig.DEBUG) Log.i(TAG, "SQL will execute with reflection...");
        } else {
            initializeWithoutReflection();
            if (RapidORMConfig.DEBUG) Log.i(TAG, "SQL will execute without reflection...");
        }


//        Comparator<ColumnConfig> configComparator = new Comparator<ColumnConfig>() {
//            @Override
//            public int compare(ColumnConfig lhs, ColumnConfig rhs) {
//                return lhs.getOrdinals() - rhs.getOrdinals();
//            }
//        };


//        if(isWithoutReflection()){
//            // todo: refactor it
//            Collections.sort(allColumnConfigs, configComparator);
//            Collections.sort(pkColumnConfigs, configComparator);
//            Collections.sort(noPkColumnConfigs, configComparator);
//        }

    }

    private void initialize() {
        ReflectionUtils.doWithFieldsWithSuper(tableClazz, new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws Exception {
                Column column = field.getAnnotation(Column.class);
                if (null == column) {
                    return;
                }

                ColumnConfig columnConfig = buildColumnConfig(field, column);

                allColumnConfigs.add(columnConfig);
                if (columnConfig.isPrimaryKey()) {
                    pkColumnConfigs.add(columnConfig);
                } else {
                    noPkColumnConfigs.add(columnConfig);
                }
            }
        });
    }

    private void initializeWithoutReflection() {
        final int modifierMask = Modifier.STATIC | Modifier.PUBLIC | Modifier.FINAL;
        final HashMap<String, ModelFieldMapper> modelFieldMapperCache = new HashMap<>();
        ReflectionUtils.doWithFields(propertyClazz, new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws Exception {
                if ((field.getModifiers() & modifierMask) == modifierMask) {
                    Object fieldValue = field.get(null);
                    if (null != fieldValue && (fieldValue instanceof ModelFieldMapper)) {
                        ModelFieldMapper mfm = (ModelFieldMapper) fieldValue;
                        modelFieldMapperCache.put(mfm.field, mfm);
                    }
                }
            }
        });

        final int size = modelFieldMapperCache.size();
        final ColumnConfig[] allColumnConfigArray = new ColumnConfig[size];
        ReflectionUtils.doWithFieldsWithSuper(tableClazz, new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws Exception {
                Column column = field.getAnnotation(Column.class);
                if (null == column) {
                    return;
                }

                ColumnConfig columnConfig = buildColumnConfig(field, column);
                ModelFieldMapper mfm = modelFieldMapperCache.get(field.getName());
                if (null == mfm) {
                    throw new RapidORMRuntimeException(String.format("initializeWithoutReflection error, have you use %s to generate ModelProperty?", ModelPropertyGenerator.class.getSimpleName()));
                }
                int order = mfm.order;
                if (order < 0 || order >= size) {
                    throw new RapidORMRuntimeException(String.format("Invalid order! Had you edited the class of %s which generated by %s?", tableClazz.getSimpleName(), ModelPropertyGenerator.class.getSimpleName()));
                }
                if (null != allColumnConfigArray[order]) {
                    throw new RapidORMRuntimeException("Duplicate property ordinals!");
                }
                allColumnConfigArray[order] = columnConfig;
            }
        });

        allColumnConfigs = Arrays.asList(allColumnConfigArray);
        for (ColumnConfig columnConfig : allColumnConfigs) {
            if (columnConfig.isPrimaryKey()) {
                pkColumnConfigs.add(columnConfig);
            } else {
                noPkColumnConfigs.add(columnConfig);
            }
        }

    }

    private ColumnConfig buildColumnConfig(Field field, Column column) {
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
        columnConfig.setPrimaryKey(column.primaryKey());
        return columnConfig;
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

    public Class getTableClazz() {
        return tableClazz;
    }

    public Class getPropertyClazz() {
        return propertyClazz;
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

    public InsertStatement<T> getInsertStatement() {
        if (null == insertStatement) {
            insertStatement = new InsertStatement<>(this);
        }
        return insertStatement;
    }

    public UpdateStatement<T> getUpdateStatement() {
        if (null == updateStatement) {
            updateStatement = new UpdateStatement<>(this);
        }
        return updateStatement;
    }

    public DeleteStatement<T> getDeleteStatement() {
        if (null == deleteStatement) {
            deleteStatement = new DeleteStatement<>(this);
        }
        return deleteStatement;
    }

    public boolean isWithoutReflection() {
        return withoutReflection;
    }
}
