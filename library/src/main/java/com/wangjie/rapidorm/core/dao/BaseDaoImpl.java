package com.wangjie.rapidorm.core.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;
import com.wangjie.rapidorm.constants.RapidORMConfig;
import com.wangjie.rapidorm.core.config.ColumnConfig;
import com.wangjie.rapidorm.core.config.TableConfig;
import com.wangjie.rapidorm.core.generate.builder.DeleteBuilder;
import com.wangjie.rapidorm.core.generate.builder.QueryBuilder;
import com.wangjie.rapidorm.core.generate.builder.UpdateBuilder;
import com.wangjie.rapidorm.core.generate.statement.util.SqlUtil;
import com.wangjie.rapidorm.core.generate.withoutreflection.IModelProperty;
import com.wangjie.rapidorm.core.generate.withoutreflection.ModelPropertyFactory;
import com.wangjie.rapidorm.exception.RapidORMRuntimeException;
import com.wangjie.rapidorm.util.func.RapidOrmFunc1;

import java.lang.reflect.Field;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
public class BaseDaoImpl<T> implements BaseDao<T> {
    private static final String TAG = BaseDaoImpl.class.getSimpleName();
    private final byte[] LOCK = new byte[0];
//    private final ReadWriteLock LOCK = new ReentrantReadWriteLock();

    protected Class<T> clazz;
    protected TableConfig<T> tableConfig;
    private String insertStatement;
    private String updateStatement;
    private String deleteStatement;
    private IModelProperty<T> iModelProperty;

    public BaseDaoImpl(Class<T> clazz) {
        this.clazz = clazz;
        this.tableConfig = DatabaseProcessor.getInstance().getTableConfig(clazz);
        insertStatement = tableConfig.getInsertStatement().getStatement();
        updateStatement = tableConfig.getUpdateStatement().getStatement();
        deleteStatement = tableConfig.getDeleteStatement().getStatement();
        if (tableConfig.isWithoutReflection()) {
            iModelProperty = ModelPropertyFactory.getMapper(tableConfig.getPropertyClazz());
        }
    }

    @Override
    public void insert(@NonNull final T model) throws Exception {
        final SQLiteDatabase db = getDatabase();
        if (db.isDbLockedByCurrentThread()) {
            synchronized (LOCK) {
                executeInsert(model, db, SqlUtil.getInsertColumnConfigs(tableConfig));
            }
        } else {
            // Do TX to acquire a connection before locking the stmt to avoid deadlocks
            executeInTx(db, new RapidOrmFunc1() {
                @Override
                public void call() throws Exception{
                    executeInsert(model, db, SqlUtil.getInsertColumnConfigs(tableConfig));
                }
            });
        }
    }

    private void executeInsert(@NonNull T model, SQLiteDatabase db, List<ColumnConfig> insertColumnConfigs) throws Exception {
        Object[] args;
        if (null != iModelProperty) {
            List<Object> argList = new ArrayList<>();
//            args = new Object[insertColumnConfigs.size()];
            iModelProperty.bindInsertArgs(model, argList);
            args = argList.toArray();
        } else {
            args = generateArgs(model, insertColumnConfigs).toArray();
        }

        if (RapidORMConfig.DEBUG)
            Log.i(TAG, "executeInsert ==> sql: " + insertStatement + " >> args: " + Arrays.toString(args));

        db.execSQL(insertStatement, args);
    }

    @Override
    public void update(@NonNull final T model) throws Exception {
        final SQLiteDatabase db = getDatabase();
        if (db.isDbLockedByCurrentThread()) {
            synchronized (LOCK) {
                executeUpdate(model, db);
            }
        } else {
            // Do TX to acquire a connection before locking the stmt to avoid deadlocks
            executeInTx(db, new RapidOrmFunc1() {
                @Override
                public void call() throws Exception{
                    executeUpdate(model, db);
                }
            });
        }
    }

    private void executeUpdate(T model, SQLiteDatabase db) throws Exception {
        Object[] args;
        if (null != iModelProperty) {
            List<Object> argList = new ArrayList<>();
            iModelProperty.bindUpdateArgs(model, argList);
            iModelProperty.bindPkArgs(model, argList);
            args = argList.toArray();
        } else {
            List<Object> argList = generateArgs(model, tableConfig.getNoPkColumnConfigs());
            argList.addAll(generateArgs(model, tableConfig.getPkColumnConfigs()));
            args = argList.toArray();
        }

        if (RapidORMConfig.DEBUG)
            Log.i(TAG, "executeUpdate ==> sql: " + updateStatement + " >> args: " + Arrays.toString(args));

        db.execSQL(updateStatement, args);
    }

    @Override
    public void delete(@NonNull final T model) throws Exception {
        final SQLiteDatabase db = getDatabase();
        if (db.isDbLockedByCurrentThread()) {
            synchronized (LOCK) {
                executeDelete(model, db);
            }
        } else {
            // Do TX to acquire a connection before locking the stmt to avoid deadlocks
            executeInTx(db, new RapidOrmFunc1() {
                @Override
                public void call() throws Exception{
                    executeDelete(model, db);
                }
            });
        }
    }

    private void executeDelete(@NonNull T model, SQLiteDatabase db) throws Exception {
        List<ColumnConfig> pkColumnConfigs = tableConfig.getPkColumnConfigs();
        if (null == pkColumnConfigs || 0 == pkColumnConfigs.size()) {
            Log.e(TAG, "The table [" + tableConfig.getTableName() + "] has no primary key column!");
            return;
        }

        Object[] args;
        if (null != iModelProperty) {
            List<Object> argList = new ArrayList<>();
            iModelProperty.bindPkArgs(model, argList);
            args = argList.toArray();
        } else {
            args = generateArgs(model, pkColumnConfigs).toArray();
        }

        if (RapidORMConfig.DEBUG)
            Log.i(TAG, "executeDelete ==> sql: " + deleteStatement + " >> args: " + Arrays.toString(args));

        db.execSQL(deleteStatement, args);
    }

    private List<Object> generateArgs(@NonNull T model, List<ColumnConfig> columnConfigs) {
        List<Object> args = new ArrayList<>();
        for (ColumnConfig columnConfig : columnConfigs) {
            Field field = columnConfig.getField();
            field.setAccessible(true);
            try {
                Object value;
                if (isBoolean(field.getType())) {
                    value = ((Boolean) field.get(model)) ? 1 : 0;
                } else {
                    value = field.get(model);
                }
                args.add(value);
            } catch (IllegalAccessException e) {
                Log.e(TAG, "", e);
                args.add(null);
            }
        }
        return args;
    }

    private boolean isBoolean(Class<?> type) {
        return boolean.class == type || Boolean.class == type;
    }

    @Override
    public void deleteAll() throws Exception {
        final SQLiteDatabase db = getDatabase();
        if (db.isDbLockedByCurrentThread()) {
            synchronized (LOCK) {
                executeDeleteAll(db);
            }
        } else {
            // Do TX to acquire a connection before locking the stmt to avoid deadlocks
            executeInTx(db, new RapidOrmFunc1() {
                @Override
                public void call() {
                    executeDeleteAll(db);
                }
            });
        }
    }

    private void executeDeleteAll(SQLiteDatabase db) {
        String sql = SqlUtil.generateSqlDeleteAll(tableConfig);
        if (RapidORMConfig.DEBUG)
            Log.i(TAG, "executeDeleteAll ==> sql: " + sql);
        db.execSQL(sql);
    }

    @Override
    public void insertOrReplace(@NonNull T model) throws Exception {
        throw new RapidORMRuntimeException("InsertOrReplace Not Support Yet!");
    }

    @Override
    public List<T> queryAll() throws Exception {
        String sql = "SELECT * FROM " + tableConfig.getTableName();
        return rawQuery(sql, null);
    }

    @Override
    public List<T> rawQuery(String sql, String[] selectionArgs) throws Exception {
        if (RapidORMConfig.DEBUG)
            Log.i(TAG, "rawQuery ==> sql: " + sql + " >> args: " + Arrays.toString(selectionArgs));

        List<T> resultList = new ArrayList<>();

        SQLiteDatabase db = getDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, selectionArgs);
            while (cursor.moveToNext()) {
                T obj;
                if (null != iModelProperty) {
                    obj = iModelProperty.parseFromCursor(cursor);
                } else {
                    obj = clazz.newInstance();
                    List<ColumnConfig> allColumnConfigs = tableConfig.getAllColumnConfigs();
                    for (ColumnConfig columnConfig : allColumnConfigs) {
                        Field field = columnConfig.getField();
                        field.setAccessible(true);
                        int index = cursor.getColumnIndex(columnConfig.getColumnName());
                        if (-1 != index) {
                            field.set(obj, getObject(cursor, field.getType(), index));
                        }
                    }
                }
                resultList.add(obj);
            }
        } catch (Exception ex) {
            Log.e(TAG, "", ex);
        } finally {
            closeCursor(cursor);
        }
        return resultList;
    }

    @Override
    public void rawExecute(final String sql, final Object[] bindArgs) throws Exception {
        final SQLiteDatabase db = getDatabase();
        if (db.isDbLockedByCurrentThread()) {
            synchronized (LOCK) {
                rawExecute(db, sql, bindArgs);
            }
        } else {
            // Do TX to acquire a connection before locking the stmt to avoid deadlocks
            executeInTx(db, new RapidOrmFunc1() {
                @Override
                public void call() throws Exception{
                    rawExecute(db, sql, bindArgs);
                }
            });
        }
    }

    private void rawExecute(SQLiteDatabase db, String sql, Object[] bindArgs) throws Exception {
        if (RapidORMConfig.DEBUG)
            Log.i(TAG, "rawExecute ==> sql: " + sql + " >> args: " + Arrays.toString(bindArgs));

        if (null == bindArgs || 0 == bindArgs.length) {
            db.execSQL(sql);
        } else {
            db.execSQL(sql, bindArgs);
        }
    }

    /**
     * ********************* execute in tx *************************
     */

    @SafeVarargs
    @Override
    public final void insertInTx(T... models) throws Exception {
        insertInTx(Arrays.asList(models));
    }

    @Override
    public void insertInTx(final Iterable<T> models) throws Exception {
        final SQLiteDatabase db = getDatabase();
        executeInTx(db, new RapidOrmFunc1() {
            @Override
            public void call() throws Exception{
                List<ColumnConfig> insertColumnConfigs = SqlUtil.getInsertColumnConfigs(tableConfig);
                for (T model : models) {
                    executeInsert(model, db, insertColumnConfigs);
                }
            }
        });
    }

    @SafeVarargs
    @Override
    public final void updateInTx(T... models) throws Exception {
        updateInTx(Arrays.asList(models));
    }

    @Override
    public void updateInTx(final Iterable<T> models) throws Exception {
        final SQLiteDatabase db = getDatabase();
        executeInTx(db, new RapidOrmFunc1() {
            @Override
            public void call() throws Exception{
                for (T model : models){
                    executeUpdate(model, db);
                }
            }
        });
    }

    @SafeVarargs
    @Override
    public final void deleteInTx(T... models) throws Exception {
        deleteInTx(Arrays.asList(models));
    }

    @Override
    public void deleteInTx(final Iterable<T> models) throws Exception {
        final SQLiteDatabase db = getDatabase();
        executeInTx(db, new RapidOrmFunc1() {
            @Override
            public void call() throws Exception{
                for (T model : models) {
                    executeDelete(model, db);
                }
            }
        });
    }

    @Override
    public void executeInTx(SQLiteDatabase db, RapidOrmFunc1 func1) throws Exception {
        if (null == func1) {
            return;
        }
        db.beginTransaction();
        try {
            synchronized (LOCK) {
                func1.call();
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void executeInTx(RapidOrmFunc1 func1) throws Exception {
        executeInTx(getDatabase(), func1);
    }


    public SQLiteDatabase getDatabase() {
        return DatabaseProcessor.getInstance().getDb();
    }

    /**
     * 关闭cursor
     *
     * @param cursor
     */
    protected void closeCursor(Cursor cursor) {
        if (null != cursor) {
            cursor.close();
        }
    }

    /**
     * 反射获取cursor中对应field的值
     *
     * @param cursor
     * @param fieldType
     * @param index
     * @return
     */
    protected Object getObject(Cursor cursor, Class fieldType, int index) {
        if (null == cursor) {
            return null;
        }
        if (String.class == fieldType) {
            return cursor.getString(index);
        } else if (Long.class == fieldType || long.class == fieldType) {
            return cursor.isNull(index) ? null : cursor.getLong(index);
        } else if (Integer.class == fieldType || int.class == fieldType) {
            return cursor.isNull(index) ? null : cursor.getInt(index);
        } else if (Short.class == fieldType || short.class == fieldType) {
            return cursor.isNull(index) ? null : cursor.getShort(index);
        } else if (Double.class == fieldType || double.class == fieldType) {
            return cursor.isNull(index) ? null : cursor.getDouble(index);
        } else if (Float.class == fieldType || float.class == fieldType) {
            return cursor.isNull(index) ? null : cursor.getFloat(index);
        } else if (Blob.class == fieldType) {
            return cursor.isNull(index) ? null : cursor.getBlob(index);
        } else if (isBoolean(fieldType)) {
            return cursor.isNull(index) ? null : (cursor.getInt(index) == 1);
        }
        return null;
    }

    /**
     * 构建一个QueryBuilder
     *
     * @return
     */
    public QueryBuilder<T> queryBuilder() {
        QueryBuilder<T> queryBuilder = new QueryBuilder<>();
        queryBuilder.setTableConfig(tableConfig);
        return queryBuilder;
    }

    /**
     * 构建一个UpdateBuilder
     *
     * @return
     */
    public UpdateBuilder<T> updateBuilder() {
        UpdateBuilder<T> updateBuilder = new UpdateBuilder<>();
        updateBuilder.setTableConfig(tableConfig);
        return updateBuilder;
    }

    /**
     * 构建一个DeleteBuilder
     *
     * @return
     */
    public DeleteBuilder<T> deleteBuilder() {
        DeleteBuilder<T> deleteBuilder = new DeleteBuilder<>();
        deleteBuilder.setTableConfig(tableConfig);
        return deleteBuilder;
    }

}
