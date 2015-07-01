package com.wangjie.rapidorm.util.collection;

import android.support.annotation.Nullable;

import java.util.Collection;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/30/15.
 */
public class CollectionJoiner {
    public interface OnCollectionJoiner<T> {
        void joinContent(StringBuilder builder, T obj);
    }

    public static <T> StringBuilder join(Collection<T> collection, String stuff) {
        return join(collection, stuff, null, null);
    }

    public static <T> StringBuilder join(Collection<T> collection, String stuff, @Nullable StringBuilder builder, @Nullable OnCollectionJoiner<T> onCollectionJoiner) {
        if (null == collection) {
            return null;
        }
        if (null == builder) {
            builder = new StringBuilder();
        }
        int i = 0;
        int size = collection.size();
        for (T t : collection) {
            if (null != onCollectionJoiner) {
                onCollectionJoiner.joinContent(builder, t);
            } else {
                builder.append(t);
            }
            if (i != size - 1) {
                builder.append(stuff);
            }
            i++;
        }
        return builder;
    }


}
