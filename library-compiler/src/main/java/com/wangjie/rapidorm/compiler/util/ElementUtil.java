package com.wangjie.rapidorm.compiler.util;

import com.google.auto.common.MoreElements;

import java.util.List;

import javax.lang.model.element.VariableElement;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 3/16/16.
 */
public class ElementUtil {
    /**
     * 两组参数类型相同
     */
    public static boolean deepSame(List<? extends VariableElement> _this, List<? extends VariableElement> _that) {
        if (null == _this && null == _that) {
            return true;
        }

        if (null == _this || null == _that) {
            return false;
        }

        if (_this.size() != _that.size()) {
            return false;
        }

        for (int i = 0, len = _this.size(); i < len; i++) {
            VariableElement _thisEle = _this.get(i);
            VariableElement _thatEle = _that.get(i);

            if (!MoreElements.asType(_thisEle).getQualifiedName().toString()
                    .equals(MoreElements.asType(_thatEle).getQualifiedName().toString())) {
                return false;
            }
        }

        return true;
    }
}
