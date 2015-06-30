package com.wangjie.rapidorm.core;

import android.support.annotation.NonNull;
import android.util.Log;
import com.wangjie.rapidorm.core.dao.BaseDao;
import com.wangjie.rapidorm.core.dao.DatabaseProcessor;

import java.util.HashMap;
import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
public abstract class RapidORMConnection<T extends RapidORMDatabaseOpenHelper> {
    private static final String TAG = RapidORMConnection.class.getSimpleName();
    private List<Class<?>> allTableClass;

    private HashMap<Class<?>, BaseDao<?>> daoMapper = new HashMap<>();

    @SuppressWarnings("unchecked")
    public synchronized <D extends BaseDao> D getDao(Class<D> clazz) {
        BaseDao dao = daoMapper.get(clazz);
        if(null == dao){
            try {
                dao = clazz.newInstance();
                daoMapper.put(clazz, dao);
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
        return (D)dao;
    }

    public RapidORMConnection() {
        initial();
    }

    private void initial() {
        daoMapper = new HashMap<>();
        allTableClass = registerAllTableClass();
        DatabaseProcessor.getInstance().initializeAllTableClass(allTableClass);
    }

    private String databaseName;
    private T rapidORMDatabaseOpenHelper;

    public void resetDatabase(@NonNull String databaseName) {
        if (databaseName.equals(this.databaseName)) {
            return;
        }
        this.databaseName = databaseName;
        rapidORMDatabaseOpenHelper = getRapidORMDatabaseOpenHelper(databaseName);
        DatabaseProcessor.getInstance().resetRapidORMDatabaseOpenHelper(rapidORMDatabaseOpenHelper);

    }

    protected abstract T getRapidORMDatabaseOpenHelper(@NonNull String databaseName);

    public T getRapidORMDatabaseOpenHelper() {
        return rapidORMDatabaseOpenHelper;
    }

    protected abstract List<Class<?>> registerAllTableClass();

    public List<Class<?>> getAllTableClass() {
        return allTableClass;
    }

}
