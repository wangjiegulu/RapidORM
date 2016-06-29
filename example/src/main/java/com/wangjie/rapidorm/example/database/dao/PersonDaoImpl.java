package com.wangjie.rapidorm.example.database.dao;

import com.wangjie.rapidorm.core.generate.builder.Where;
import com.wangjie.rapidorm.example.database.model.Person;
import com.wangjie.rapidorm.example.database.model.Person_RORM;

import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/26/15.
 */
public class PersonDaoImpl extends XBaseDaoImpl<Person> {
    public PersonDaoImpl() {
        super(Person.class);
    }

    public List<Person> findPersonsByWhere() throws Exception {
        return queryBuilder()
                .addSelectColumn(Person_RORM.ID, Person_RORM.TYPE_ID, Person_RORM.NAME,
                        Person_RORM.AGE, Person_RORM.BIRTH, Person_RORM.ADDRESS)
                .setWhere(getTestWhere())
                .addOrder(Person_RORM.ID, false)
                .addOrder(Person_RORM.NAME, true)
                .setLimit(10).query(this);
    }

    public void deletePerson() throws Exception {
        deleteBuilder()
                .setWhere(getTestWhere()).delete(this);
    }

    public void updatePerson() throws Exception {
        long now = System.currentTimeMillis();
        updateBuilder()
                .setWhere(getTestWhere())
                .addUpdateColumn(Person_RORM.BIRTH, now)
                .addUpdateColumn(Person_RORM.ADDRESS, "address_" + now).update(this);
    }

    private Where getTestWhere() {
        return Where.and(
//                Where.eq(Person_RORM.NAME, "wangjie"),
                Where.like(Person_RORM.NAME, "%wangjie%"),
                Where.lt(Person_RORM.ID, 200),
//                Where.raw(Person_RORM.AGE + " > ? and " + Person_RORM.ADDRESS + " is not null", 8),
                Where.or(
//                        Where.in(Person_RORM.AGE, 19, 29, 39),
                        Where.between(Person_RORM.AGE, 19, 39),
                        Where.isNull(Person_RORM.ADDRESS)
                ),
                Where.eq(Person_RORM.TYPE_ID, 1)
        );
    }

    public List<Person> findPersons() throws Exception {
        return queryBuilder().addOrder(Person_RORM.ID, false).setLimit(10).query(this);
    }

}
