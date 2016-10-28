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
@Target(ElementType.FIELD)
public @interface Column {
    /**
     * 表示要映射到的表字段名称，不填写则默认以属性名作为表字段名
     *
     * @return
     */
    String name() default Constants.AnnotationNotSetValue.COLUMN_NAME;

    /**
     * 是否为主键
     *
     * @return
     */
    boolean primaryKey() default false;

    /**
     * 是否自增长（只作用于属性为Long类型）
     *
     * @return
     */
    boolean autoincrement() default false;

    /**
     * 默认值
     *
     * @return
     */
    String defaultValue() default Constants.AnnotationNotSetValue.DEFAULT_VALUE;

    /**
     * 不能为空
     *
     * @return
     */
    boolean notNull() default false;

    boolean unique() default false;

    boolean uniqueCombo() default false;

    @Deprecated
    boolean index() default false;

}
