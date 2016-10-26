package com.wangjie.rapidorm.util;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * Author: wangjie  email:tiantian.china.2@gmail.com
 * Date: 14-3-24
 * Time: 下午4:41
 */
public abstract class ReflectionUtils {
    private static String TAG = ReflectionUtils.class.getSimpleName();

    public interface FieldCallback {
        void doWith(Field field) throws Exception;
    }

    /**
     * Iterate over all fields with reflection
     * @param clazz
     * @param fieldCallback
     */
    public static void doWithFields(Class<?> clazz, FieldCallback fieldCallback) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            try {
                fieldCallback.doWith(f);
            } catch (Exception ex) {
                Log.e(TAG, "ReflectionUtils.doWithFields error", ex);
            }
        }
    }

    /**
     * Iterate over all fields with reflection，include super class
     * @param clazz
     * @param fieldCallback
     */
    public static void doWithFieldsWithSuper(Class<?> clazz, FieldCallback fieldCallback) {
        while (!Object.class.equals(clazz)) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field f : fields) {
                try {
                    fieldCallback.doWith(f);
                } catch (Exception ex) {
                    Log.e(TAG, "ReflectionUtils.doWithFieldsWithSuper error", ex);
                }
            }
            clazz = clazz.getSuperclass();
        }

    }

    /**
     * ************************************** modifier ****************************************
     */
    public static boolean isModifier(Field field, int modifier) {
        return (field.getModifiers() & modifier) == modifier;
    }

    public static boolean isModifier(Method method, int modifier) {
        return (method.getModifiers() & modifier) == modifier;
    }

    public static boolean isModifier(Class clazz, int modifier) {
        return (clazz.getModifiers() & modifier) == modifier;
    }


}
