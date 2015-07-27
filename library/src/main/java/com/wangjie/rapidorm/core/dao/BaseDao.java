package com.wangjie.rapidorm.core.dao;

import android.database.sqlite.SQLiteDatabase;
import com.wangjie.rapidorm.util.func.RapidOrmFunc1;

import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
public interface BaseDao<T> {

    void insert(T model) throws Exception;

    void update(T model) throws Exception;

    void delete(T model) throws Exception;

    void deleteAll() throws Exception;

    void insertOrReplace(T model) throws Exception;

    List<T> queryAll() throws Exception;

    List<T> rawQuery(String sql, String[] selectionArgs) throws Exception;

    void rawExecute(String sql, Object[] bindArgs) throws Exception;

    /**
     * ********************* execute in tx *************************
     */
    void insertInTx(T... models) throws Exception;

    void insertInTx(Iterable<T> models) throws Exception;

    void updateInTx(T... models) throws Exception;

    void updateInTx(Iterable<T> models) throws Exception;

    void deleteInTx(T... models) throws Exception;

    void deleteInTx(Iterable<T> models) throws Exception;

    void executeInTx(SQLiteDatabase db, RapidOrmFunc1 func1) throws Exception;

    void executeInTx(RapidOrmFunc1 func1) throws Exception;

}
