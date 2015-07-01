package com.wangjie.rapidorm.core.dao;

import android.database.Cursor;
import android.database.SQLException;
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
import com.wangjie.rapidorm.core.model.ModelWithoutReflection;
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

    public BaseDaoImpl(Class<T> clazz) {
        this.clazz = clazz;
        this.tableConfig = DatabaseProcessor.getInstance().getTableConfig(clazz);
    }

    @Override
    public int insert(@NonNull T model) throws SQLException {
        SQLiteDatabase db = getDatabase();
        int count = 0;
        if (db.isDbLockedByCurrentThread()) {
            synchronized (LOCK) {
                count = executeInsert(model, db, SqlUtil.getInsertColumnConfigs(tableConfig));
            }
        } else {
            // Do TX to acquire a connection before locking the stmt to avoid deadlocks
            db.beginTransaction();
            try {
                synchronized (LOCK) {
                    count = executeInsert(model, db, SqlUtil.getInsertColumnConfigs(tableConfig));
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
        return count;
    }

    private int executeInsert(@NonNull T model, SQLiteDatabase db, List<ColumnConfig> insertColumnConfigs) throws SQLException {
        String sql = tableConfig.getInsertStatement().getStatement();
        if (0 == insertColumnConfigs.size()) {
            Log.e(TAG, "insertColumns is null");
            return 0;
        }

        Object[] args;
        if (model instanceof ModelWithoutReflection) {
            List<Object> argList = new ArrayList<>();
            ((ModelWithoutReflection) model).bindInsertArgs(argList);
            args = argList.toArray();
        } else {
            args = generateArgs(model, insertColumnConfigs).toArray();
        }

        if (RapidORMConfig.DEBUG)
            Log.i(TAG, "executeInsert ==> sql: " + sql + " >> args: " + Arrays.toString(args));

        db.execSQL(sql, args);
        return 1;
    }

    @Override
    public int update(@NonNull T model) throws SQLException {
        SQLiteDatabase db = getDatabase();
        if (db.isDbLockedByCurrentThread()) {
            synchronized (LOCK) {
                executeUpdate(model, db);
            }
        } else {
            // Do TX to acquire a connection before locking the stmt to avoid deadlocks
            db.beginTransaction();
            try {
                synchronized (LOCK) {
                    executeUpdate(model, db);
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
        return 0;
    }

    private int executeUpdate(T model, SQLiteDatabase db) throws SQLException {
        String sql = tableConfig.getUpdateStatement().getStatement();
        Object[] args;
        if (model instanceof ModelWithoutReflection) {
            List<Object> argList = new ArrayList<>();
            ModelWithoutReflection mwr = ((ModelWithoutReflection) model);
            mwr.bindUpdateArgs(argList);
            mwr.bindPkArgs(argList);
            args = argList.toArray();
        } else {
            List<Object> argList = generateArgs(model, tableConfig.getNoPkColumnConfigs());
            argList.addAll(generateArgs(model, tableConfig.getPkColumnConfigs()));
            args = argList.toArray();
        }

        if (RapidORMConfig.DEBUG)
            Log.i(TAG, "executeUpdate ==> sql: " + sql + " >> args: " + Arrays.toString(args));

        db.execSQL(sql, args);
        return 1;

    }

    @Override
    public int delete(@NonNull T model) throws SQLException {
        SQLiteDatabase db = getDatabase();
        int count = 0;
        if (db.isDbLockedByCurrentThread()) {
            synchronized (LOCK) {
                count = executeDelete(model, db);
            }
        } else {
            // Do TX to acquire a connection before locking the stmt to avoid deadlocks
            db.beginTransaction();
            try {
                synchronized (LOCK) {
                    count = executeDelete(model, db);
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
        return count;
    }

    private int executeDelete(@NonNull T model, SQLiteDatabase db) throws SQLException {
        String sql = tableConfig.getDeleteStatement().getStatement();
        List<ColumnConfig> pkColumnConfigs = tableConfig.getPkColumnConfigs();
        if (null == pkColumnConfigs || 0 == pkColumnConfigs.size()) {
            Log.e(TAG, "The table [" + tableConfig.getTableName() + "] has no primary key column!");
            return 0;
        }

        Object[] args = generateArgs(model, pkColumnConfigs).toArray();

        if (RapidORMConfig.DEBUG)
            Log.i(TAG, "executeDelete ==> sql: " + sql + " >> args: " + Arrays.toString(args));

        db.execSQL(sql, args);
        // todo: query change count
        return 1;
    }

    private List<Object> generateArgs(@NonNull T model, List<ColumnConfig> columnConfigs) {
        List<Object> args = new ArrayList<>();
        for (ColumnConfig columnConfig : columnConfigs) {
            Field field = columnConfig.getField();
            field.setAccessible(true);
            try {
                args.add(field.get(model));
            } catch (IllegalAccessException e) {
                Log.e(TAG, "", e);
                args.add(null);
            }
        }
        return args;
    }

    @Override
    public int deleteAll() throws SQLException {
        SQLiteDatabase db = getDatabase();
        int count = 0;
        if (db.isDbLockedByCurrentThread()) {
            synchronized (LOCK) {
                executeDeleteAll(db);
            }
        } else {
            // Do TX to acquire a connection before locking the stmt to avoid deadlocks
            db.beginTransaction();
            try {
                synchronized (LOCK) {
                    executeDeleteAll(db);
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
        // todo: query change count
        return count;
    }

    private void executeDeleteAll(SQLiteDatabase db) {
        String sql = SqlUtil.generateSqlDeleteAll(tableConfig);
        if (RapidORMConfig.DEBUG)
            Log.i(TAG, "executeDeleteAll ==> sql: " + sql);
        db.execSQL(sql);
    }

    @Override
    public int insertOrUpdate(@NonNull T model) throws SQLException {
        return 0;
    }

    @Override
    public List<T> queryAll() throws SQLException {
        String sql = "SELECT * FROM " + tableConfig.getTableName();
        return rawQuery(sql, null);
    }

    @Override
    public List<T> rawQuery(String sql, String[] selectionArgs) throws SQLException {
        if (RapidORMConfig.DEBUG)
            Log.i(TAG, "rawQuery ==> sql: " + sql + " >> args: " + Arrays.toString(selectionArgs));

        List<T> resultList = new ArrayList<>();

        SQLiteDatabase db = getDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, selectionArgs);
            while (cursor.moveToNext()) {
                T obj = clazz.newInstance();
                List<ColumnConfig> allColumnConfigs = tableConfig.getAllColumnConfigs();
                for (ColumnConfig columnConfig : allColumnConfigs) {
                    Field field = columnConfig.getField();
                    field.setAccessible(true);
                    int index = cursor.getColumnIndex(columnConfig.getColumnName());
                    if (-1 != index) {
                        field.set(obj, getObject(cursor, field.getType(), index));
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
    public int rawExecute(String sql, Object[] bindArgs) throws SQLException {
        SQLiteDatabase db = getDatabase();
        if (db.isDbLockedByCurrentThread()) {
            synchronized (LOCK) {
                rawExecute(db, sql, bindArgs);
            }
        } else {
            // Do TX to acquire a connection before locking the stmt to avoid deadlocks
            db.beginTransaction();
            try {
                synchronized (LOCK) {
                    rawExecute(db, sql, bindArgs);
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
        return 0;
    }

    private void rawExecute(SQLiteDatabase db, String sql, Object[] bindArgs) throws SQLException {
        if (RapidORMConfig.DEBUG)
            Log.i(TAG, "rawExecute ==> sql: " + sql + " >> args: " + Arrays.toString(bindArgs));

        if (null == bindArgs || 0 == bindArgs.length) {
            db.execSQL(sql);
        } else {
            db.execSQL(sql, bindArgs);
        }
    }

    @SafeVarargs
    @Override
    public final void insertInTx(T... models) throws SQLException {
        insertInTx(Arrays.asList(models));
    }

    @Override
    public void insertInTx(final Iterable<T> models) throws SQLException {
        final SQLiteDatabase db = getDatabase();
        executeInTx(new RapidOrmFunc1() {
            @Override
            public void call() {
                List<ColumnConfig> insertColumnConfigs = SqlUtil.getInsertColumnConfigs(tableConfig);
                for (T model : models) {
                    executeInsert(model, db, insertColumnConfigs);
                }
            }
        });
    }

    @SafeVarargs
    @Override
    public final void updateInTx(T... models) throws SQLException {
        updateInTx(Arrays.asList(models));
    }

    @Override
    public void updateInTx(final Iterable<T> models) throws SQLException {
        final SQLiteDatabase db = getDatabase();
        executeInTx(new RapidOrmFunc1() {
            @Override
            public void call() {
                for (T model : models) {
                    executeUpdate(model, db);
                }
            }
        });
    }

    @SafeVarargs
    @Override
    public final void deleteInTx(T... models) throws SQLException {
        deleteInTx(Arrays.asList(models));
    }

    @Override
    public void deleteInTx(final Iterable<T> models) throws SQLException {
        final SQLiteDatabase db = getDatabase();
        executeInTx(new RapidOrmFunc1() {
            @Override
            public void call() {
                for (T model : models) {
                    executeDelete(model, db);
                }
            }
        });
    }

    @Override
    public void executeInTx(RapidOrmFunc1 func1) throws SQLException {
        if (null == func1) {
            return;
        }
        SQLiteDatabase db = getDatabase();
        db.beginTransaction();
        try {
            synchronized (LOCK) {
                func1.call();
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e(TAG, "", e);
        } finally {
            db.endTransaction();
        }
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
