package com.wangjie.rapidorm.example.database.model;

import com.wangjie.rapidorm.annotations.Column;
import com.wangjie.rapidorm.annotations.Table;
import com.wangjie.rapidorm.example.database.model.config.PersonProperty;

import java.io.Serializable;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
@Table(propertyClazz = PersonProperty.class)
public class Person implements Serializable{

    @Column(primaryKey = true)
    private Integer id;

    @Column(primaryKey = true)
    private Integer typeId;

    @Column
    private String name;

    @Column
    private Integer age;

    @Column
    private String address;

    @Column
    private Long birth;

    @Column
    private Boolean student;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getBirth() {
        return birth;
    }

    public void setBirth(Long birth) {
        this.birth = birth;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Boolean isStudent() {
        return student;
    }

    public void setStudent(Boolean student) {
        this.student = student;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", typeId=" + typeId +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                ", birth=" + birth +
                ", student=" + student +
                '}';
    }

}
