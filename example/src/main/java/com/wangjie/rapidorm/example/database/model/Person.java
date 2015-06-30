package com.wangjie.rapidorm.example.database.model;

import com.wangjie.rapidorm.annotations.Column;
import com.wangjie.rapidorm.annotations.Table;

import java.io.Serializable;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
@Table
public class Person implements Serializable/*, ModelWithoutReflection*/ {
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TYPE_ID = "typeId";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_BIRTH = "birth";

    @Column(name = COLUMN_ID, primaryKey = true, autoincrement = true)
    private Integer id;

    @Column(name = COLUMN_TYPE_ID, primaryKey = true, autoincrement = true)
    private Integer typeId;

    @Column(name = COLUMN_NAME)
    private String name;

    @Column(name = COLUMN_AGE)
    private Integer age;

    @Column(name = COLUMN_ADDRESS)
    private String address;

    @Column(name = COLUMN_BIRTH)
    private Long birth;

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

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", typeId=" + typeId +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                ", birth=" + birth +
                '}';
    }

//    @Override
//    public void bindInsertArgs(List<Object> insertArgs) {
//        insertArgs.add(id);
//        insertArgs.add(typeId);
//        insertArgs.add(name);
//        insertArgs.add(age);
//        insertArgs.add(address);
//        insertArgs.add(birth);
//    }
//
//    @Override
//    public void bindUpdateArgs(List<Object> updateArgs) {
//        updateArgs.add(name);
//        updateArgs.add(age);
//        updateArgs.add(address);
//        updateArgs.add(birth);
//    }
//
//    @Override
//    public void bindPkArgs(List<Object> pkArgs) {
//        pkArgs.add(id);
//        pkArgs.add(typeId);
//    }
}
