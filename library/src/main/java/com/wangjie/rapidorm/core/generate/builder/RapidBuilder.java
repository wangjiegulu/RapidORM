package com.wangjie.rapidorm.core.generate.builder;

import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/30/15.
 */
public abstract class RapidBuilder {
    public abstract String generateSql();

    public String[] objectListToStringArray(List<Object> args) {
        if (null == args) {
            return null;
        }
        int size;
        String[] array = new String[size = args.size()];
        for (int i = 0; i < size; i++) {
            Object arg = args.get(i);
            array[i] = null == arg ? null : arg.toString();
        }
        return array;
    }



}
