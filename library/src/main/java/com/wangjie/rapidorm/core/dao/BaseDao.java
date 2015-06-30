package com.wangjie.rapidorm.core.dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
public interface BaseDao<T> {

    int insert(T model) throws SQLException;

    int update(T model) throws SQLException;

    int delete(T model) throws SQLException;

    int deleteAll() throws SQLException;

    int insertOrUpdate(T model) throws SQLException;

    List<T> queryAll() throws SQLException;

    List<T> rawQuery(String sql, String[] selectionArgs) throws SQLException;

    int rawExecute(String sql, Object[] bindArgs) throws SQLException;



}
