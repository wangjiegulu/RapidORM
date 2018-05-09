# RapidORM
### Quick solution for Android ORM

[![](https://img.shields.io/badge/license-Apache%202-orange.svg)](http://www.apache.org/licenses/LICENSE-2.0) ![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)

[![](https://img.shields.io/badge/blog-Wang%20Jie-228377.svg)](https://blog.wangjiegulu.com) [![](https://img.shields.io/badge/twitter-@wangjiegulu-blue.svg)](https://twitter.com/wangjiegulu)

Android lightweight, high performance ORM framework.

## About RapidORM

- Primary key is supported with any type.
- Non-reflective to execute SQLs.
- Compatible with both `android.database.sqlite.SQLiteDatabase` and `net.sqlcipher.database.SQLiteDatabase`.
- Join query NOT supported.

`v2.0` blog: <http://www.cnblogs.com/tiantianbyconan/p/5626716.html>

`v1.0` blog: <http://www.cnblogs.com/tiantianbyconan/p/4748077.html>

## How to use

### 1. Compile it in `build.gradle`

### Gadle([Check newest version](http://search.maven.org/#search%7Cga%7C1%7CRapidORM))

- **rapidorm:** [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.wangjiegulu/rapidorm/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.wangjiegulu/rapidorm)

- **rapidorm-api:** [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.wangjiegulu/rapidorm-api/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.wangjiegulu/rapidorm-api)

- **rapidorm-compiler:** [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.wangjiegulu/rapidorm-compiler/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.wangjiegulu/rapidorm-compiler)

```groovy
compile "com.github.wangjiegulu:rapidorm:x.x.x"

compile "com.github.wangjiegulu:rapidorm-api:x.x.x"

apt "com.github.wangjiegulu:rapidorm-compiler:x.x.x"
```

### Maven

```xml
<dependency>
        <groupId>com.github.wangjiegulu</groupId>
        <artifactId>rapidorm</artifactId>
        <version>x.x.x</version>
</dependency>
```

### 2. Create persistent class mapping to table

```java
/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
@Table(indices = {
        @Index(value = "birth, student", unique = true),
        @Index(value = "is_succeed", name = "INDEX_CUSTOM_NAME_IS_SUCCEED", unique = false)
})
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
```


### 3. Generate persistent helper class in compile time

```java
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
  public int bindInsertArgs(Person model, RapidORMSQLiteStatementDelegate statement, int indexOffset) {
    indexOffset++;
    Integer id = model.id;
    if (null == id) {
      statement.bindNull(indexOffset);
    } else {
      statement.bindLong(indexOffset, id);
    }
    indexOffset++;
    Integer typeId = model.typeId;
    if (null == typeId) {
      statement.bindNull(indexOffset);
    } else {
      statement.bindLong(indexOffset, typeId);
    }
    indexOffset++;
    String name = model.name;
    if (null == name) {
      statement.bindNull(indexOffset);
    } else {
      statement.bindString(indexOffset, name);
    }
    indexOffset++;
    int age = model.age;
    statement.bindLong(indexOffset, age);
    indexOffset++;
    String address = model.address;
    if (null == address) {
      statement.bindNull(indexOffset);
    } else {
      statement.bindString(indexOffset, address);
    }
    indexOffset++;
    Long birth = model.birth;
    if (null == birth) {
      statement.bindNull(indexOffset);
    } else {
      statement.bindLong(indexOffset, birth);
    }
    indexOffset++;
    Boolean student = model.student;
    if (null == student) {
      statement.bindNull(indexOffset);
    } else {
      statement.bindLong(indexOffset, student ? 1 : 0);
    }
    indexOffset++;
    boolean isSucceed = model.isSucceed;
    statement.bindLong(indexOffset, isSucceed ? 1 : 0);
    return indexOffset;
  }

  @Override
  public int bindUpdateArgs(Person model, RapidORMSQLiteStatementDelegate statement, int indexOffset) {
    indexOffset++;
    String name = model.name;
    if (null == name) {
      statement.bindNull(indexOffset);
    } else {
      statement.bindString(indexOffset, name);
    }
    indexOffset++;
    int age = model.age;
    statement.bindLong(indexOffset, age);
    indexOffset++;
    String address = model.address;
    if (null == address) {
      statement.bindNull(indexOffset);
    } else {
      statement.bindString(indexOffset, address);
    }
    indexOffset++;
    Long birth = model.birth;
    if (null == birth) {
      statement.bindNull(indexOffset);
    } else {
      statement.bindLong(indexOffset, birth);
    }
    indexOffset++;
    Boolean student = model.student;
    if (null == student) {
      statement.bindNull(indexOffset);
    } else {
      statement.bindLong(indexOffset, student ? 1 : 0);
    }
    indexOffset++;
    boolean isSucceed = model.isSucceed;
    statement.bindLong(indexOffset, isSucceed ? 1 : 0);
    return indexOffset;
  }

  @Override
  public int bindPkArgs(Person model, RapidORMSQLiteStatementDelegate statement, int indexOffset) {
    indexOffset++;
    Integer id = model.id;
    if (null == id) {
      statement.bindNull(indexOffset);
    } else {
      statement.bindLong(indexOffset, id);
    }
    indexOffset++;
    Integer typeId = model.typeId;
    if (null == typeId) {
      statement.bindNull(indexOffset);
    } else {
      statement.bindLong(indexOffset, typeId);
    }
    return indexOffset;
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

  @Override
  public void createTable(RapidORMSQLiteDatabaseDelegate db, boolean ifNotExists) throws Exception {
    String ifNotExistsConstraint = ifNotExists? "IF NOT EXISTS " : "";
    db.execSQL("CREATE TABLE " + ifNotExistsConstraint + "`Person` ( \n"
            + "`id` INTEGER,\n"
            + "`type_id` INTEGER,\n"
            + "`name` TEXT,\n"
            + "`age` INTEGER,\n"
            + "`address` TEXT,\n"
            + "`birth` LONG,\n"
            + "`student` INTEGER,\n"
            + "`is_succeed` INTEGER,\n"
            + " PRIMARY KEY (id, type_id));");
    db.execSQL("CREATE UNIQUE INDEX " + ifNotExistsConstraint + "INDEX_BIRTH_STUDENT ON `Person`(\"birth, student\");");
    db.execSQL("CREATE INDEX " + ifNotExistsConstraint + "INDEX_CUSTOM_NAME_IS_SUCCEED ON `Person`(\"is_succeed\");");
  }
}
```

### 4. Register persistent class

Extends `RapidORMConnection` and override `registerTableConfigMapper(HashMap<Class, TableConfig> tableConfigMapper)` method:

```java
@Override
protected void registerTableConfigMapper(HashMap<Class, TableConfig> tableConfigMapper) {
    tableConfigMapper.put(Person.class, new Person_RORM());
    // register all table config here...
}
```

### 5. Execute SQL use Builder

> `QueryBuilder`, `UpdateBuilder`, `DeleteBuilder`

#### 5.1 QueryBuilder

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

#### 5.2 UpdateBuilder
```java
public void updatePerson() throws Exception {
    updateBuilder()
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
        .addUpdateColumn(Person_RORM.BIRTH, System.currentTimeMillis())
        .addUpdateColumn(Person_RORM.ADDRESS, "New address")
        .update(this);
}
```

#### 5.3 DeleteBuilder

```java
public void deletePerson() throws Exception {
    deleteBuilder()
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
        .delete(this);
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
