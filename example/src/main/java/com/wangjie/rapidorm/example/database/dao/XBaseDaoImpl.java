package com.wangjie.rapidorm.example.database.dao;

import com.wangjie.rapidorm.core.dao.BaseDaoImpl;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 7/2/15.
 */
public class XBaseDaoImpl<T> extends BaseDaoImpl<T> {
    public XBaseDaoImpl(Class<T> clazz) {
        super(clazz);
    }


    public void insertOrUpdate(T model) {


    }

}
