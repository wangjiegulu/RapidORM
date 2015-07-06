package com.wangjie.rapidorm.core.generate.withoutreflection;

import android.database.Cursor;

import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 7/2/15.
 */
public interface IModelProperty<T> {
    void bindInsertArgs(T model, List<Object> insertArgs);

    void bindUpdateArgs(T model, List<Object> updateArgs);

    void bindPkArgs(T model, List<Object> pkArgs);

    T parseFromCursor(Cursor cursor);
}
