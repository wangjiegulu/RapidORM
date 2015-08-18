package com.wangjie.rapidorm.example.database.dao;

import com.wangjie.rapidorm.core.generate.builder.DeleteBuilder;
import com.wangjie.rapidorm.core.generate.builder.QueryBuilder;
import com.wangjie.rapidorm.core.generate.builder.UpdateBuilder;
import com.wangjie.rapidorm.core.generate.builder.Where;
import com.wangjie.rapidorm.example.database.model.Person;
import com.wangjie.rapidorm.example.database.model.config.PersonProperty;

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
        Where where = getTestWhere();

        QueryBuilder<Person> queryBuilder = queryBuilder()
                .addSelectColumn(PersonProperty.id.column, PersonProperty.typeId.column, PersonProperty.name.column,
                        PersonProperty.age.column, PersonProperty.birth.column, PersonProperty.address.column)
                .setWhere(where)
                .addOrder(PersonProperty.id.column, false)
                .addOrder(PersonProperty.name.column, true)
                .setLimit(10);
        return rawQuery(queryBuilder.generateSql(), queryBuilder.getValuesAsStringArray());
    }

    public void deletePerson() throws Exception {
        Where where = getTestWhere();

        DeleteBuilder<Person> deleteBuilder = deleteBuilder()
                .setWhere(where);
        rawExecute(deleteBuilder.generateSql(), deleteBuilder.getValues().toArray());
    }

    public void updatePerson() throws Exception {
        Where where = getTestWhere();

        long now = System.currentTimeMillis();
        UpdateBuilder<Person> updateBuilder = updateBuilder()
                .setWhere(where)
                .addUpdateColumn(PersonProperty.birth.column, now)
                .addUpdateColumn(PersonProperty.address.column, "address_" + now);
        rawExecute(updateBuilder.generateSql(), updateBuilder.getValues().toArray());
    }

    private Where getTestWhere() {
        return Where.and(
//                Where.eq(PersonProperty.name.column, "wangjie"),
                Where.like(PersonProperty.name.column, "%wangjie%"),
                Where.lt(PersonProperty.id.column, 200),
//                Where.raw(PersonProperty.age.column + " > ? and " + PersonProperty.address.column + " is not null", 8),
                Where.or(
//                        Where.in(PersonProperty.age.column, 19, 29, 39),
                        Where.between(PersonProperty.age.column, 19, 39),
                        Where.isNull(PersonProperty.address.column)
                ),
                Where.eq(PersonProperty.typeId.column, 1)
        );
    }

    public List<Person> findPersons() throws Exception {
        QueryBuilder queryBuilder = queryBuilder().addOrder(PersonProperty.id.column, false).setLimit(10);
        return rawQuery(queryBuilder.generateSql(), queryBuilder.getValuesAsStringArray());
    }

}
