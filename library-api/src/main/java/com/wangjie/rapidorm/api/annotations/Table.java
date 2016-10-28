package com.wangjie.rapidorm.api.annotations;


import com.wangjie.rapidorm.api.constant.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Table {
    /**
     * Table name
     */
    String name() default Constants.AnnotationNotSetValue.TABLE_NAME;

    Index[] indices() default {};
}
