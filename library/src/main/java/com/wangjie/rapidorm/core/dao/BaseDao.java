package com.wangjie.rapidorm.core.dao;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.wangjie.rapidorm.util.func.RapidOrmFunc1;

import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
public interface BaseDao<T> {

    void insert(T model) throws SQLException;

    void update(T model) throws SQLException;

    void delete(T model) throws SQLException;

    void deleteAll() throws SQLException;

    void insertOrReplace(T model) throws SQLException;

    List<T> queryAll() throws SQLException;

    List<T> rawQuery(String sql, String[] selectionArgs) throws SQLException;

    void rawExecute(String sql, Object[] bindArgs) throws SQLException;

    /**
     * ********************* execute in tx *************************
     */
    void insertInTx(T... models) throws SQLException;

    void insertInTx(Iterable<T> models) throws SQLException;

    void updateInTx(T... models) throws SQLException;

    void updateInTx(Iterable<T> models) throws SQLException;

    void deleteInTx(T... models) throws SQLException;

    void deleteInTx(Iterable<T> models) throws SQLException;

    void executeInTx(SQLiteDatabase db, RapidOrmFunc1 func1) throws SQLException;

}
