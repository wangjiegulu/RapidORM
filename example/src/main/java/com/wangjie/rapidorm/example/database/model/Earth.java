package com.wangjie.rapidorm.example.database.model;

import com.wangjie.rapidorm.api.annotations.Column;

import java.io.Serializable;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 12/2/16.
 */
public class Earth implements Serializable {
    @Column
    String earth;

    public String getEarth() {
        return earth;
    }

    public void setEarth(String earth) {
        this.earth = earth;
    }

    @Override
    public String toString() {
        return "Earth{" +
                "earth='" + earth + '\'' +
                '}';
    }
}
