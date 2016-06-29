# RapidORM
### Quick solution for Android ORM

## About RapidORM
- Composite primary key supported with any type.
- supports the use of reflective and non-reflective (template generation) two ways to perform SQL.
- Compatible with both `android.database.sqlite.SQLiteDatabase` and `net.sqlcipher.database.SQLiteDatabase`.
- Join query NOT supported.

`v2.0` blog: <http://www.cnblogs.com/tiantianbyconan/p/5626716.html>

`v1.0` blog: <http://www.cnblogs.com/tiantianbyconan/p/4748077.html>

## How to use

### 1. Creating persistent class:

```java
/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
@Table
public class Person implements Serializable {
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
    
    // getter/setter...
}
```

### 2. Rebuild Project

> Android Studio -> Build -> Rebuild Project

After Build success, under the directory of main project `build/generated/source/apt/` will generate `Person_RORM` class, as follows:

```java
// GENERATED CODE BY RapidORM. DO NOT MODIFY! "2016-06-29 14:08:504", Source table: "com.wangjie.rapidorm.example.database.model.Person"
package com.wangjie.rapidorm.example.database.model;

import android.database.Cursor;
import com.wangjie.rapidorm.core.config.ColumnConfig;
import com.wangjie.rapidorm.core.config.TableConfig;
import java.util.List;

public class Person_RORM extends TableConfig<Person> {
  /**
   * Column name: "id", field name: {@link Person#id}
   */
  public static final String ID = "id";

  /**
   * Column name: "type_id", field name: {@link Person#typeId}
   */
  public static final String TYPE_ID = "type_id";

  /**
   * Column name: "name", field name: {@link Person#name}
   */
  public static final String NAME = "name";

  /**
   * Column name: "age", field name: {@link Person#age}
   */
  public static final String AGE = "age";

  /**
   * Column name: "address", field name: {@link Person#address}
   */
  public static final String ADDRESS = "address";

  /**
   * Column name: "birth", field name: {@link Person#birth}
   */
  public static final String BIRTH = "birth";

  /**
   * Column name: "student", field name: {@link Person#student}
   */
  public static final String STUDENT = "student";

  /**
   * Column name: "is_succeed", field name: {@link Person#isSucceed}
   */
  public static final String IS_SUCCEED = "is_succeed";

  public Person_RORM() {
    super(Person.class);
  }

  @Override
  protected void parseAllConfigs() {
    tableName = "Person";
    ColumnConfig idColumnConfig = buildColumnConfig("id"/*column name*/, false/*autoincrement*/, false/*notNull*/, ""/*defaultValue*/, false/*index*/, false/*unique*/, false/*uniqueCombo*/, true/*primaryKey*/, "INTEGER"/*dbType*/);
    allColumnConfigs.add(idColumnConfig);
    allFieldColumnConfigMapper.put("id"/*field name*/, idColumnConfig);
    pkColumnConfigs.add(idColumnConfig);
    ColumnConfig typeIdColumnConfig = buildColumnConfig("type_id"/*column name*/, false/*autoincrement*/, false/*notNull*/, ""/*defaultValue*/, false/*index*/, false/*unique*/, false/*uniqueCombo*/, true/*primaryKey*/, "INTEGER"/*dbType*/);
    allColumnConfigs.add(typeIdColumnConfig);
    allFieldColumnConfigMapper.put("typeId"/*field name*/, typeIdColumnConfig);
    pkColumnConfigs.add(typeIdColumnConfig);
    ColumnConfig nameColumnConfig = buildColumnConfig("name"/*column name*/, false/*autoincrement*/, false/*notNull*/, ""/*defaultValue*/, false/*index*/, false/*unique*/, false/*uniqueCombo*/, false/*primaryKey*/, "TEXT"/*dbType*/);
    allColumnConfigs.add(nameColumnConfig);
    allFieldColumnConfigMapper.put("name"/*field name*/, nameColumnConfig);
    noPkColumnConfigs.add(nameColumnConfig);
    ColumnConfig ageColumnConfig = buildColumnConfig("age"/*column name*/, false/*autoincrement*/, false/*notNull*/, ""/*defaultValue*/, false/*index*/, false/*unique*/, false/*uniqueCombo*/, false/*primaryKey*/, "INTEGER"/*dbType*/);
    allColumnConfigs.add(ageColumnConfig);
    allFieldColumnConfigMapper.put("age"/*field name*/, ageColumnConfig);
    noPkColumnConfigs.add(ageColumnConfig);
    ColumnConfig addressColumnConfig = buildColumnConfig("address"/*column name*/, false/*autoincrement*/, false/*notNull*/, ""/*defaultValue*/, false/*index*/, false/*unique*/, false/*uniqueCombo*/, false/*primaryKey*/, "TEXT"/*dbType*/);
    allColumnConfigs.add(addressColumnConfig);
    allFieldColumnConfigMapper.put("address"/*field name*/, addressColumnConfig);
    noPkColumnConfigs.add(addressColumnConfig);
    ColumnConfig birthColumnConfig = buildColumnConfig("birth"/*column name*/, false/*autoincrement*/, false/*notNull*/, ""/*defaultValue*/, false/*index*/, false/*unique*/, false/*uniqueCombo*/, false/*primaryKey*/, "LONG"/*dbType*/);
    allColumnConfigs.add(birthColumnConfig);
    allFieldColumnConfigMapper.put("birth"/*field name*/, birthColumnConfig);
    noPkColumnConfigs.add(birthColumnConfig);
    ColumnConfig studentColumnConfig = buildColumnConfig("student"/*column name*/, false/*autoincrement*/, false/*notNull*/, ""/*defaultValue*/, false/*index*/, false/*unique*/, false/*uniqueCombo*/, false/*primaryKey*/, "INTEGER"/*dbType*/);
    allColumnConfigs.add(studentColumnConfig);
    allFieldColumnConfigMapper.put("student"/*field name*/, studentColumnConfig);
    noPkColumnConfigs.add(studentColumnConfig);
    ColumnConfig isSucceedColumnConfig = buildColumnConfig("is_succeed"/*column name*/, false/*autoincrement*/, false/*notNull*/, ""/*defaultValue*/, false/*index*/, false/*unique*/, false/*uniqueCombo*/, false/*primaryKey*/, "INTEGER"/*dbType*/);
    allColumnConfigs.add(isSucceedColumnConfig);
    allFieldColumnConfigMapper.put("isSucceed"/*field name*/, isSucceedColumnConfig);
    noPkColumnConfigs.add(isSucceedColumnConfig);
  }

  @Override
  public void bindInsertArgs(Person model, List<Object> insertArgs) {
    Integer id = model.id;
    insertArgs.add(null == id ? null : id );
    Integer typeId = model.typeId;
    insertArgs.add(null == typeId ? null : typeId );
    String name = model.name;
    insertArgs.add(null == name ? null : name );
    int age = model.age;
    insertArgs.add(age);
    String address = model.address;
    insertArgs.add(null == address ? null : address );
    Long birth = model.birth;
    insertArgs.add(null == birth ? null : birth );
    Boolean student = model.student;
    insertArgs.add(null == student ? null : student  ? 1 : 0);
    boolean isSucceed = model.isSucceed;
    insertArgs.add(isSucceed ? 1 : 0);
  }

  @Override
  public void bindUpdateArgs(Person model, List<Object> updateArgs) {
    String name = model.name;
    updateArgs.add(null == name ? null : name);
    int age = model.age;
    updateArgs.add(age);
    String address = model.address;
    updateArgs.add(null == address ? null : address);
    Long birth = model.birth;
    updateArgs.add(null == birth ? null : birth);
    Boolean student = model.student;
    updateArgs.add(null == student ? null : student ? 1 : 0);
    boolean isSucceed = model.isSucceed;
    updateArgs.add(isSucceed ? 1 : 0);
  }

  @Override
  public void bindPkArgs(Person model, List<Object> pkArgs) {
    Integer id = model.id;
    pkArgs.add(null == id ? null : id);
    Integer typeId = model.typeId;
    pkArgs.add(null == typeId ? null : typeId);
  }

  @Override
  public Person parseFromCursor(Cursor cursor) {
    Person model = new Person();
    int index;
    index = cursor.getColumnIndex("id");
    if(-1 != index) {
      model.id = cursor.isNull(index) ? null : (cursor.getInt(index));
    }
    index = cursor.getColumnIndex("type_id");
    if(-1 != index) {
      model.typeId = cursor.isNull(index) ? null : (cursor.getInt(index));
    }
    index = cursor.getColumnIndex("name");
    if(-1 != index) {
      model.name = cursor.isNull(index) ? null : (cursor.getString(index));
    }
    index = cursor.getColumnIndex("age");
    if(-1 != index) {
      model.age = cursor.isNull(index) ? null : (cursor.getInt(index));
    }
    index = cursor.getColumnIndex("address");
    if(-1 != index) {
      model.address = cursor.isNull(index) ? null : (cursor.getString(index));
    }
    index = cursor.getColumnIndex("birth");
    if(-1 != index) {
      model.birth = cursor.isNull(index) ? null : (cursor.getLong(index));
    }
    index = cursor.getColumnIndex("student");
    if(-1 != index) {
      model.student = cursor.isNull(index) ? null : (cursor.getInt(index) == 1);
    }
    index = cursor.getColumnIndex("is_succeed");
    if(-1 != index) {
      model.isSucceed = cursor.isNull(index) ? null : (cursor.getInt(index) == 1);
    }
    return model;
  }
}
```

### 3. Register persistent class

Extends `RapidORMConnection` and override `registerTableConfigMapper(HashMap<Class, TableConfig> tableConfigMapper)` method:

```java
@Override
protected void registerTableConfigMapper(HashMap<Class, TableConfig> tableConfigMapper) {
    tableConfigMapper.put(Person.class, new Person_RORM());
    // register all table config here...
}
```

### 4. Execute SQL use Builder

> `QueryBuilder`, `UpdateBuilder`, `DeleteBuilder`

**PersonDaoImpl**:

```java
public List<Person> findPersonsByWhere() throws Exception {
    return queryBuilder()
            .addSelectColumn(Person_RORM.ID, Person_RORM.TYPE_ID, Person_RORM.NAME,
                    Person_RORM.AGE, Person_RORM.BIRTH, Person_RORM.ADDRESS)
            .setWhere(
            	Where.and(
	                Where.like(Person_RORM.NAME, "%wangjie%"),
	                Where.lt(Person_RORM.ID, 200),
	                Where.or(
	                        Where.between(Person_RORM.AGE, 19, 39),
	                        Where.isNull(Person_RORM.ADDRESS)
	                ),
	                Where.eq(Person_RORM.TYPE_ID, 1)
        		)
            )
            .addOrder(Person_RORM.ID, false)
            .addOrder(Person_RORM.NAME, true)
            .setLimit(10)
            .query(this);
}
```


License
=======

    Copyright 2015 Wang Jie

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing blacklist and
    limitations under the License.
