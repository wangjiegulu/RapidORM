package com.wangjie.rapidorm.core.generate.withoutreflection;

import java.util.HashMap;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 7/2/15.
 */
public class ModelPropertyFactory {
    private static HashMap<Class<?>, IModelProperty<?>> mapper = new HashMap<>();

    public static <T extends IModelProperty<?>> IModelProperty<T> getMapper(Class<T> modelPropertyClazz) {
        IModelProperty<?> iModelProperty = mapper.get(modelPropertyClazz);
        if (null == iModelProperty) {
            try {
                iModelProperty = modelPropertyClazz.newInstance();
                mapper.put(modelPropertyClazz, iModelProperty);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (IModelProperty<T>) iModelProperty;
    }
}
