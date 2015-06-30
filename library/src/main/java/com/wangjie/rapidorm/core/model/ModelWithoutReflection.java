package com.wangjie.rapidorm.core.model;

import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/29/15.
 */
public interface ModelWithoutReflection {
    void bindInsertArgs(List<Object> insertArgs);

    void bindUpdateArgs(List<Object> updateArgs);

    void bindPkArgs(List<Object> pkArgs);
}
