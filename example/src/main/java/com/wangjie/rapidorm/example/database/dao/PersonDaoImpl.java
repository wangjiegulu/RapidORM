package com.wangjie.rapidorm.example.database.dao;

import com.wangjie.rapidorm.core.dao.BaseDaoImpl;
import com.wangjie.rapidorm.core.generate.builder.QueryBuilder;
import com.wangjie.rapidorm.core.generate.builder.Where;
import com.wangjie.rapidorm.example.database.model.Person;

import java.sql.SQLException;
import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/26/15.
 */
public class PersonDaoImpl extends BaseDaoImpl<Person> {
    public PersonDaoImpl() {
        super(Person.class);
    }

    public List<Person> findPersons() throws SQLException {
        Where where = Where.and(
                Where.eq(Person.COLUMN_NAME, "wangjie"),
                Where.lt(Person.COLUMN_ID, 200),
//                Where.raw(Person.COLUMN_AGE + " > ? and " + Person.COLUMN_ADDRESS + " is not null", 8),
                Where.or(
                        Where.in(Person.COLUMN_AGE, 19, 29, 39),
                        Where.isNull(Person.COLUMN_ADDRESS)
                ),
                Where.eq(Person.COLUMN_TYPE_ID, 1)
        );

        QueryBuilder<Person> queryBuilder = queryBuilder()
                .addSelectColumn(Person.COLUMN_ID, Person.COLUMN_NAME)
                .setWhere(where)
                .addOrder(Person.COLUMN_ID, false)
                .addOrder(Person.COLUMN_NAME, true)
                .setLimit(10);
        return rawQuery(queryBuilder.generateSql(), queryBuilder.getValuesAsStringArray());
    }

}
