package com.wangjie.rapidorm.util;

import java.sql.Blob;

/**
 * Created by wangjie on 10/21/16.
 */

public class TypeUtil {
    public static boolean isLongIncludePrimitive(Class valueType) {
        return long.class == valueType || Long.class == valueType;
    }

    public static boolean isIntegerIncludePrimitive(Class valueType) {
        return int.class == valueType || Integer.class == valueType;
    }

    public static boolean isShortIncludePrimitive(Class valueType) {
        return short.class == valueType || Short.class == valueType;
    }

    public static boolean isDoubleIncludePrimitive(Class valueType) {
        return double.class == valueType || Double.class == valueType;
    }

    public static boolean isFloatIncludePrimitive(Class valueType) {
        return float.class == valueType || Float.class == valueType;
    }

    public static boolean isBooleanIncludePrimitive(Class<?> fieldType) {
        return boolean.class == fieldType || Boolean.class == fieldType;
    }

    public static boolean isBlobIncludePrimitive(Class<?> fieldType) {
        return byte[].class == fieldType || Byte[].class == fieldType || Blob.class == fieldType;
    }
}
