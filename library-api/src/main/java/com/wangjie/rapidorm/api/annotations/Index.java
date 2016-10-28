package com.wangjie.rapidorm.api.annotations;

import com.wangjie.rapidorm.api.constant.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * org.greenrobot.greendao.annotation.Index
 *
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Index {
    /**
     * Comma-separated list of properties that should be indexed, e.g. "propertyA, propertyB, propertyC"
     * To specify order, add ASC or DESC after column name, e.g.: "propertyA DESC, propertyB ASC"
     * This should be only set if this annotation is used in {@link Table#indices()}
     */
    String value() default Constants.AnnotationNotSetValue.INDEX_VALUE;

    /**
     * Optional name of the index.
     * If omitted, then generated automatically by greenDAO with base on property/properties column name(s)
     */
    String name() default Constants.AnnotationNotSetValue.INDEX_NAME;

    /**
     * Whether the unique constraint should be created with base on this index
     */
    boolean unique() default false;

}
