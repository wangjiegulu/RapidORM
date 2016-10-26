package com.wangjie.rapidorm.core.connection;

//import android.database.sqlite.RapidORMSupportSQLiteDatabase;

import android.os.Process;

import com.wangjie.rapidorm.core.config.TableConfig;
import com.wangjie.rapidorm.core.delegate.database.RapidORMSQLiteDatabaseDelegate;
import com.wangjie.rapidorm.core.delegate.openhelper.RapidORMDatabaseOpenHelperDelegate;
import com.wangjie.rapidorm.exception.RapidORMRuntimeException;

import java.util.Map;
import java.util.Set;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
public class DatabaseProcessor {
    private static class Holder {
        private static DatabaseProcessor instance = new DatabaseProcessor();
    }

    public static DatabaseProcessor getInstance() {
        return Holder.instance;
    }

    private Map<Class, TableConfig> tableConfigMapper;

    private boolean isInitialized = false;

    @SuppressWarnings("unchecked")
    public <T> void createTable(RapidORMSQLiteDatabaseDelegate db, Class<T> clazz, boolean ifNotExists) {
        checkInitialized();
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
        checkInitialized();
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
        checkInitialized();
        Set<Class> entrySet = tableConfigMapper.keySet();
        for (Class anEntrySet : entrySet) {
            dropTable(db, anEntrySet);
        }
    }

    private RapidORMConnection rapidORMConnection;
    private RapidORMDatabaseOpenHelperDelegate rapidORMDatabaseOpenHelperDelegate;
    private RapidORMSQLiteDatabaseDelegate db;
//    protected List<Class> allTableClass;

    private DatabaseProcessor() {
    }

    /**
     * RapidORMConnection构造的时候就初始化好所有的tableClass
     */
    @SuppressWarnings("unchecked")
    public void initializeConnection(RapidORMConnection rapidORMConnection, Map<Class, TableConfig> tableConfigMapper) {
        if (isInitialized) {
            return;
        }
        isInitialized = true;

        this.rapidORMConnection = rapidORMConnection;
        this.tableConfigMapper = tableConfigMapper;
    }

    public void resetRapidORMDatabaseOpenHelper(RapidORMDatabaseOpenHelperDelegate rapidORMDatabaseOpenHelper) {
        this.rapidORMDatabaseOpenHelperDelegate = rapidORMDatabaseOpenHelper;
        if (null != db) {
            db.close();
        }
        this.db = null;
    }

    public void initializeDatabase(RapidORMSQLiteDatabaseDelegate db) {
        this.db = db;
    }

    public RapidORMSQLiteDatabaseDelegate getDb() {
        checkInitialized();
        if (null == db) {
            synchronized (this) {
                if (null == db) {
                    if (null == this.rapidORMDatabaseOpenHelperDelegate) {
                        if (!rapidORMConnection.resetDatabaseIfCrash()) {
                            android.os.Process.killProcess(Process.myPid());
                        }
                    }
                    db = (RapidORMSQLiteDatabaseDelegate) this.rapidORMDatabaseOpenHelperDelegate.getWritableDatabase();
                }
            }
        }
        return db;
    }

    @SuppressWarnings("unchecked")
    public <T> TableConfig<T> getTableConfig(Class<T> clazz) {
        checkInitialized();
        return (TableConfig<T>) tableConfigMapper.get(clazz);
    }

    private void checkInitialized() {
        if (!isInitialized) {
            throw new RapidORMRuntimeException("DatabaseProcessor is not initialized, had you invoke super() method in the sub class of RapidORMConnection ?");
        }
    }

}
