package com.wangjie.rapidorm.core.config;

import com.wangjie.rapidorm.exception.RapidORMRuntimeException;

import java.sql.Blob;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
public class ColumnType {

    public static String getTypeName(Class typeClazz) {
        if (String.class == typeClazz) {
            return "TEXT";
        } else if (Long.class == typeClazz || long.class == typeClazz) {
            return "INTEGER";
        } else if (Integer.class == typeClazz || int.class == typeClazz) {
            return "INTEGER";
        } else if (Short.class == typeClazz || short.class == typeClazz) {
            return "SHORT";
        } else if (Double.class == typeClazz || double.class == typeClazz) {
            return "DOUBLE";
        } else if (Float.class == typeClazz || float.class == typeClazz) {
            return "FLOAT";
        } else if (Blob.class == typeClazz) {
            return "BLOB";
        }

        throw new RapidORMRuntimeException("columnType[" + typeClazz.getName() + "] not supported");
    }
}
