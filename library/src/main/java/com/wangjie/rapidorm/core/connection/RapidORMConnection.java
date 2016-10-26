package com.wangjie.rapidorm.core.connection;

import com.wangjie.rapidorm.core.config.TableConfig;
import com.wangjie.rapidorm.core.delegate.openhelper.RapidORMDatabaseOpenHelperDelegate;

import android.support.annotation.NonNull;

import java.util.HashMap;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
public abstract class RapidORMConnection<T extends RapidORMDatabaseOpenHelperDelegate> {
    private static final String TAG = RapidORMConnection.class.getSimpleName();

    public RapidORMConnection() {
        initial();
    }

    private void initial() {
        HashMap<Class, TableConfig> mapper = new HashMap<>();
        registerTableConfigMapper(mapper);
        DatabaseProcessor.getInstance().initializeConnection(this, mapper);
    }

    /**
     * Database name
     */
    private String databaseName;

    /**
     * if database reset，return true；else return false.
     *
     * @param databaseName
     * @return
     */
    public boolean resetDatabase(@NonNull String databaseName) {
        if (databaseName.equals(this.databaseName)) {
            return false;
        }
        this.databaseName = databaseName;
        DatabaseProcessor.getInstance().resetRapidORMDatabaseOpenHelper(getRapidORMDatabaseOpenHelper(databaseName));
        return true;
    }

    /**
     * RapidORM will be invoke this method to reconnect database if the application crashed.
     *
     * @return
     */
    public abstract boolean resetDatabaseIfCrash();

    protected abstract T getRapidORMDatabaseOpenHelper(@NonNull String databaseName);

    protected abstract void registerTableConfigMapper(HashMap<Class, TableConfig> tableConfigMapper);

}
