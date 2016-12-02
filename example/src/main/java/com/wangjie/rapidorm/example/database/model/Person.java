package com.wangjie.rapidorm.example.database.model;

import com.wangjie.rapidorm.api.annotations.Column;
import com.wangjie.rapidorm.api.annotations.Index;
import com.wangjie.rapidorm.api.annotations.Table;

import java.io.Serializable;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
@Table(indices = {
//        @Index(value = "`birth`, `student`", unique = true),
        @Index(value = "`is_succeed`", name = "INDEX_CUSTOM_NAME_IS_SUCCEED", unique = false)
})
public class Person extends People implements Serializable {

    @Column(primaryKey = true)
    Integer id;

    @Column(primaryKey = true, name = "type_id")
    Integer typeId;

    @Column
    String name;

    @Column
    int age;

    @Column
    String address;

    @Column
    Long birth;

    @Column
    Boolean student;

    @Column(name = "is_succeed")
    boolean isSucceed;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
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

    public Boolean getStudent() {
        return student;
    }

    public void setStudent(Boolean student) {
        this.student = student;
    }

    public boolean isSucceed() {
        return isSucceed;
    }

    public void setSucceed(boolean succeed) {
        isSucceed = succeed;
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
                ", isSucceed=" + isSucceed +
                '}' + super.toString();
    }
}
