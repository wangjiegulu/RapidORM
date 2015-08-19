package com.wangjie.rapidorm.core;

import android.support.annotation.NonNull;
import android.util.Log;
import com.wangjie.rapidorm.core.dao.BaseDao;
import com.wangjie.rapidorm.core.dao.DatabaseProcessor;
import com.wangjie.rapidorm.core.delegate.openhelper.RapidORMDatabaseOpenHelperDelegate;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
public abstract class RapidORMConnection<T extends RapidORMDatabaseOpenHelperDelegate> {
    private static final String TAG = RapidORMConnection.class.getSimpleName();
    protected List<Class<?>> allTableClass;

    private HashMap<Class<?>, BaseDao<?>> daoMapper = new HashMap<>();

//    @SuppressWarnings("unchecked")
//    public synchronized <D extends BaseDao> D getDao(Class<D> clazz) {
//        BaseDao dao = daoMapper.get(clazz);
//        if(null == dao){
//            try {
//                dao = clazz.newInstance();
//                daoMapper.put(clazz, dao);
//            } catch (Exception e) {
//                Log.e(TAG, "", e);
//            }
//        }
//        return (D)dao;
//    }

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    @SuppressWarnings("unchecked")
    public <D extends BaseDao> D getDao(Class<D> clazz) {
        lock.readLock().lock();
        try {
            BaseDao dao = daoMapper.get(clazz);
            if (null == dao) {
                lock.readLock().unlock();
                lock.writeLock().lock();
                try {
//                    if (null == dao) {
                    dao = clazz.newInstance();
                    daoMapper.put(clazz, dao);
//                    }
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                } finally {
                    lock.writeLock().unlock();
                }
                lock.readLock().lock();
            }
            return (D) dao;
        } finally {
            lock.readLock().unlock();
        }

    }


    public RapidORMConnection() {
        initial();
    }

    private void initial() {
        allTableClass = registerAllTableClass();
        DatabaseProcessor.getInstance().initializeAllTableClass(allTableClass);
    }

    private String databaseName;
    private T rapidORMDatabaseOpenHelper;

    /**
     * 数据库reset，则返回true；没有reset，则返回false
     * @param databaseName
     * @return
     */
    public boolean resetDatabase(@NonNull String databaseName) {
        if (databaseName.equals(this.databaseName)) {
            return false;
        }
        daoMapper = new HashMap<>();
        this.databaseName = databaseName;
        rapidORMDatabaseOpenHelper = getRapidORMDatabaseOpenHelper(databaseName);
        DatabaseProcessor.getInstance().resetRapidORMDatabaseOpenHelper(rapidORMDatabaseOpenHelper);
        return true;
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
