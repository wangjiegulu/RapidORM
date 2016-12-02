package com.wangjie.rapidorm.example.database.model;

import com.wangjie.rapidorm.api.annotations.Column;

import java.io.Serializable;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 12/2/16.
 */
public class People extends Earth implements Serializable {

    @Column
    String species;

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    @Override
    public String toString() {
        return "People{" +
                "species='" + species + '\'' +
                '}' + super.toString();
    }
}
