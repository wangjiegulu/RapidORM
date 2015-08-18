package com.wangjie.rapidorm.core.dao;

//import android.database.sqlite.RapidORMSupportSQLiteDatabase;

import com.wangjie.rapidorm.core.config.TableConfig;
import com.wangjie.rapidorm.core.delegate.database.RapidORMSQLiteDatabaseDelegate;
import com.wangjie.rapidorm.core.delegate.openhelper.RapidORMDatabaseOpenHelperDelegate;
import com.wangjie.rapidorm.exception.RapidORMRuntimeException;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
public class DatabaseProcessor {
    private static DatabaseProcessor instance;

    public static DatabaseProcessor getInstance() {
        if (null == instance) {
            instance = new DatabaseProcessor();
        }
        return instance;
    }

    private HashMap<Class<?>, TableConfig> tableConfigMapper = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> void createTable(RapidORMSQLiteDatabaseDelegate db, Class<T> clazz, boolean ifNotExists) {
        TableConfig<T> tableConfig = tableConfigMapper.get(clazz);
        if (null == tableConfig) {
            throw new RapidORMRuntimeException("tableConfigMapper not initialized, had you invoke super() method in the sub class of RapidORMConnection ?");
        }
        try {
            db.execSQL(tableConfig.getTableCreateStatement().buildStatement(ifNotExists).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> void dropTable(RapidORMSQLiteDatabaseDelegate db, Class<T> clazz) {
        TableConfig<T> tableConfig = tableConfigMapper.get(clazz);
        if (null == tableConfig) {
            return;
        }
        try {
            db.execSQL("drop table " + tableConfig.getTableName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dropAllTable(RapidORMSQLiteDatabaseDelegate db) {
        Set<Class<?>> entrySet = tableConfigMapper.keySet();
        for (Class<?> anEntrySet : entrySet) {
            dropTable(db, anEntrySet);
        }
    }


    private RapidORMDatabaseOpenHelperDelegate rapidORMDatabaseOpenHelperDelegate;
    private RapidORMSQLiteDatabaseDelegate db;
    protected List<Class<?>> allTableClass;

    private DatabaseProcessor() {
    }

    /**
     * RapidORMConnection构造的时候就初始化好所有的tableClass
     *
     * @param allTableClass
     */
    @SuppressWarnings("unchecked")
    public void initializeAllTableClass(List<Class<?>> allTableClass) {
        tableConfigMapper.clear();
        this.allTableClass = allTableClass;
        for (Class<?> clazz : allTableClass) {
            tableConfigMapper.put(clazz, new TableConfig(clazz));
        }
    }

    public void resetRapidORMDatabaseOpenHelper(RapidORMDatabaseOpenHelperDelegate rapidORMDatabaseOpenHelper) {
        this.rapidORMDatabaseOpenHelperDelegate = rapidORMDatabaseOpenHelper;
        this.db = null;
    }

    public void initializeDatabase(RapidORMSQLiteDatabaseDelegate db) {
        this.db = db;
    }

    public DatabaseProcessor(List<Class<?>> allTableClass) {
        this.allTableClass = allTableClass;
    }

    public synchronized RapidORMSQLiteDatabaseDelegate getDb() {
        if (null == db) {
            db = (RapidORMSQLiteDatabaseDelegate) this.rapidORMDatabaseOpenHelperDelegate.getWritableDatabase();
        }
        return db;
    }

    @SuppressWarnings("unchecked")
    public <T> TableConfig<T> getTableConfig(Class<T> clazz) {
        return (TableConfig<T>) tableConfigMapper.get(clazz);
    }

    public List<Class<?>> getAllTableClass() {
        return allTableClass;
    }
}
