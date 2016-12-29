package com.wangjie.rapidorm.core.dao;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;
import com.wangjie.rapidorm.constants.RapidORMConfig;
import com.wangjie.rapidorm.core.config.ColumnConfig;
import com.wangjie.rapidorm.core.config.TableConfig;
import com.wangjie.rapidorm.core.connection.DatabaseProcessor;
import com.wangjie.rapidorm.core.delegate.database.RapidORMSQLiteDatabaseDelegate;
import com.wangjie.rapidorm.core.delegate.sqlitestatement.RapidORMSQLiteStatementDelegate;
import com.wangjie.rapidorm.core.generate.builder.DeleteBuilder;
import com.wangjie.rapidorm.core.generate.builder.QueryBuilder;
import com.wangjie.rapidorm.core.generate.builder.UpdateBuilder;
import com.wangjie.rapidorm.core.generate.statement.util.SqlUtil;
import com.wangjie.rapidorm.exception.RapidORMException;
import com.wangjie.rapidorm.exception.RapidORMRuntimeException;
import com.wangjie.rapidorm.util.TypeUtil;
import com.wangjie.rapidorm.util.func.RapidOrmFunc1;

import java.lang.reflect.Field;
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
//    protected final byte[] LOCK = new byte[0];

    protected final Class<T> clazz;
    protected final TableConfig<T> tableConfig;
    protected final String insertStatement;
    protected final String updateStatement;
    protected final String deleteStatement;

    private RapidORMSQLiteStatementDelegate insertStmt;
    private RapidORMSQLiteStatementDelegate updateStmt;
    private RapidORMSQLiteStatementDelegate deleteStmt;

    public BaseDaoImpl(Class<T> clazz) {
        this.clazz = clazz;
        this.tableConfig = DatabaseProcessor.getInstance().getTableConfig(clazz);
        insertStatement = tableConfig.getInsertStatement().getStatement();
        updateStatement = tableConfig.getUpdateStatement().getStatement();
        deleteStatement = tableConfig.getDeleteStatement().getStatement();

        try {
            RapidORMSQLiteDatabaseDelegate db = getDatabase();
            insertStmt = db.compileStatement(insertStatement);
            updateStmt = tableConfig.getPkColumnConfigs().isEmpty() ? null : db.compileStatement(updateStatement);
            deleteStmt = db.compileStatement(deleteStatement);
        } catch (Exception e) {
            throw new RapidORMRuntimeException(e);
        }
    }

    @Override
    public void insert(@NonNull final T model) throws Exception {
        final RapidORMSQLiteDatabaseDelegate db = getDatabase();
        if (db.isDbLockedByCurrentThread()) {
            synchronized (tableConfig) {
                insertInternal(model);
            }
        } else {
            // Do TX to acquire a connection before locking the stmt to avoid deadlocks
            executeInTx(db, new RapidOrmFunc1() {
                @Override
                public void call() throws Exception {
                    synchronized (tableConfig) {
                        insertInternal(model);
                    }
                }
            });
        }
    }

    protected void insertInternal(@NonNull T model) throws Exception {
        insertStmt.clearBindings();
        tableConfig.bindInsertArgs(model, insertStmt, 0);

        if (RapidORMConfig.DEBUG)
            Log.i(TAG, "insertInternal ==> sql: " + insertStatement + " >> model: " + model);

        insertStmt.executeInsert();
    }

    @Override
    public void update(@NonNull final T model) throws Exception {
        final RapidORMSQLiteDatabaseDelegate db = getDatabase();
        if (db.isDbLockedByCurrentThread()) {
            synchronized (tableConfig) {
                updateInternal(model);
            }
        } else {
            // Do TX to acquire a connection before locking the stmt to avoid deadlocks
            executeInTx(db, new RapidOrmFunc1() {
                @Override
                public void call() throws Exception {
                    synchronized (tableConfig) {
                        updateInternal(model);
                    }
                }
            });
        }
    }

    protected void updateInternal(T model) throws Exception {
        if (null == updateStmt) {
            throw new RapidORMException("Table " + tableConfig.getTableName() + " have no primary key column. Please use `UpdateBuilder` to update");
        }
        updateStmt.clearBindings();
        int indexOff = tableConfig.bindUpdateArgs(model, updateStmt, 0);
        tableConfig.bindPkArgs(model, updateStmt, indexOff);

        if (RapidORMConfig.DEBUG)
            Log.i(TAG, "updateInternal ==> sql: " + updateStatement + " >> model: " + model);

        updateStmt.executeUpdateDelete();
    }

    @Override
    public void delete(@NonNull final T model) throws Exception {
        final RapidORMSQLiteDatabaseDelegate db = getDatabase();
        if (db.isDbLockedByCurrentThread()) {
            synchronized (tableConfig) {
                deleteInternal(model);
            }
        } else {
            // Do TX to acquire a connection before locking the stmt to avoid deadlocks
            executeInTx(db, new RapidOrmFunc1() {
                @Override
                public void call() throws Exception {
                    synchronized (tableConfig) {
                        deleteInternal(model);
                    }
                }
            });
        }
    }

    protected void deleteInternal(@NonNull T model) throws Exception {
        List<ColumnConfig> pkColumnConfigs = tableConfig.getPkColumnConfigs();
        if (null == pkColumnConfigs || 0 == pkColumnConfigs.size()) {
            Log.e(TAG, "The table [" + tableConfig.getTableName() + "] has no primary key column!");
            return;
        }

        deleteStmt.clearBindings();
        tableConfig.bindPkArgs(model, deleteStmt, 0);

        if (RapidORMConfig.DEBUG)
            Log.i(TAG, "deleteInternal ==> sql: " + deleteStatement + " >> model: " + model);

        deleteStmt.executeUpdateDelete();

    }

    @Deprecated
    private List<Object> generateArgs(@NonNull T model, List<ColumnConfig> columnConfigs) {
        List<Object> args = new ArrayList<>();
        for (ColumnConfig columnConfig : columnConfigs) {
            Field field = columnConfig.getField();
            field.setAccessible(true);
            try {
                args.add(getFieldValue(model, field));
            } catch (IllegalAccessException e) {
                Log.e(TAG, "", e);
                args.add(null);
            }
        }
        return args;
    }

    @Override
    public void deleteAll() throws Exception {
        final RapidORMSQLiteDatabaseDelegate db = getDatabase();
        if (db.isDbLockedByCurrentThread()) {
            synchronized (tableConfig) {
                deleteAllInternal(db);
            }
        } else {
            // Do TX to acquire a connection before locking the stmt to avoid deadlocks
            executeInTx(db, new RapidOrmFunc1() {
                @Override
                public void call() throws Exception {
                    synchronized (tableConfig) {
                        deleteAllInternal(db);
                    }
                }
            });
        }
    }

    private void deleteAllInternal(RapidORMSQLiteDatabaseDelegate db) throws Exception {
        String sql = SqlUtil.generateSqlDeleteAll(tableConfig);
        if (RapidORMConfig.DEBUG)
            Log.i(TAG, "deleteAllInternal ==> sql: " + sql);
        db.execSQL(sql);
    }

    @Override
    public void insertOrReplace(@NonNull T model) throws Exception {
        throw new RapidORMRuntimeException("InsertOrReplace Not Support Yet!");
    }

    @Override
    public List<T> queryAll() throws Exception {
        String sql = SqlUtil.generateSqlQueryAll(tableConfig);
        if (RapidORMConfig.DEBUG)
            Log.i(TAG, "queryAll ==> sql: " + sql);
        return rawQuery(sql, null);
    }

    @Override
    public List<T> rawQuery(String sql, String[] selectionArgs) throws Exception {
        if (RapidORMConfig.DEBUG)
            Log.i(TAG, "rawQuery ==> sql: " + sql + " >> args: " + Arrays.toString(selectionArgs));

        List<T> resultList = new ArrayList<>();

        RapidORMSQLiteDatabaseDelegate db = getDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, selectionArgs);
            while (cursor.moveToNext()) {
                resultList.add(tableConfig.parseFromCursor(cursor));
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
        final RapidORMSQLiteDatabaseDelegate db = getDatabase();
        if (db.isDbLockedByCurrentThread()) {
            synchronized (tableConfig) {
                rawExecute(db, sql, bindArgs);
            }
        } else {
            // Do TX to acquire a connection before locking the stmt to avoid deadlocks
            executeInTx(db, new RapidOrmFunc1() {
                @Override
                public void call() throws Exception {
                    synchronized (tableConfig) {
                        rawExecute(db, sql, bindArgs);
                    }
                }
            });
        }
    }

    private void rawExecute(RapidORMSQLiteDatabaseDelegate db, String sql, Object[] bindArgs) throws Exception {
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
        final RapidORMSQLiteDatabaseDelegate db = getDatabase();
        executeInTx(db, new RapidOrmFunc1() {
            @Override
            public void call() throws Exception {
                for (T model : models) {
                    insertInternal(model);
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
        final RapidORMSQLiteDatabaseDelegate db = getDatabase();
        executeInTx(db, new RapidOrmFunc1() {
            @Override
            public void call() throws Exception {
                for (T model : models) {
                    updateInternal(model);
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
        final RapidORMSQLiteDatabaseDelegate db = getDatabase();
        executeInTx(db, new RapidOrmFunc1() {
            @Override
            public void call() throws Exception {
                for (T model : models) {
                    deleteInternal(model);
                }
            }
        });
    }

    @Override
    public void executeInTx(RapidORMSQLiteDatabaseDelegate db, RapidOrmFunc1 func1) throws Exception {
        if (null == db) {
            db = getDatabase();
        }
        if (null == func1) {
            return;
        }

        db.beginTransaction();
        try {
//            synchronized (tableConfig) {
            func1.call();
//            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void executeInTx(RapidOrmFunc1 func1) throws Exception {
        executeInTx(null, func1);
    }

    @Override
    public void executeInTxSync(RapidOrmFunc1 func1) throws Exception {
        RapidORMSQLiteDatabaseDelegate db = getDatabase();
        if (null == func1) {
            return;
        }

        db.beginTransaction();
        try {
            synchronized (tableConfig) {
                func1.call();
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public RapidORMSQLiteDatabaseDelegate getDatabase() {
        return DatabaseProcessor.getInstance().getDb();
    }


    /**
     * Close cursor safely
     */
    protected void closeCursor(Cursor cursor) {
        if (null != cursor) {
            cursor.close();
        }
    }

    protected Object getFieldValue(T model, Field field) throws IllegalAccessException {
        return SqlUtil.convertValue(field.get(model));
    }

    @Deprecated
    protected Object getObject(Cursor cursor, Class fieldType, int index) {
        if (null == cursor) {
            return null;
        }
        if (String.class == fieldType) {
            return cursor.getString(index);
        } else if (TypeUtil.isLongIncludePrimitive(fieldType)) {
            return cursor.isNull(index) ? null : cursor.getLong(index);
        } else if (TypeUtil.isIntegerIncludePrimitive(fieldType)) {
            return cursor.isNull(index) ? null : cursor.getInt(index);
        } else if (TypeUtil.isShortIncludePrimitive(fieldType)) {
            return cursor.isNull(index) ? null : cursor.getShort(index);
        } else if (TypeUtil.isDoubleIncludePrimitive(fieldType)) {
            return cursor.isNull(index) ? null : cursor.getDouble(index);
        } else if (TypeUtil.isFloatIncludePrimitive(fieldType)) {
            return cursor.isNull(index) ? null : cursor.getFloat(index);
        } else if (TypeUtil.isBlobIncludePrimitive(fieldType)) {
            return cursor.isNull(index) ? null : cursor.getBlob(index);
        } else if (TypeUtil.isBooleanIncludePrimitive(fieldType)) {
            return cursor.isNull(index) ? null : (cursor.getInt(index) == 1);
        }
        return null;
    }

    /**
     * Build a QueryBuilder
     */
    public QueryBuilder<T> queryBuilder() {
        QueryBuilder<T> queryBuilder = new QueryBuilder<>(this);
        queryBuilder.setTableConfig(tableConfig);
        return queryBuilder;
    }

    /**
     * Build an UpdateBuilder
     */
    public UpdateBuilder<T> updateBuilder() {
        UpdateBuilder<T> updateBuilder = new UpdateBuilder<>(this);
        updateBuilder.setTableConfig(tableConfig);
        return updateBuilder;
    }

    /**
     * Build a DeleteBuilder
     */
    public DeleteBuilder<T> deleteBuilder() {
        DeleteBuilder<T> deleteBuilder = new DeleteBuilder<>(this);
        deleteBuilder.setTableConfig(tableConfig);
        return deleteBuilder;
    }

}
