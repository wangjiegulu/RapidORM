package com.wangjie.rapidorm.compiler.objs;

import com.squareup.javapoet.*;
import com.wangjie.rapidorm.api.annotations.Column;
import com.wangjie.rapidorm.api.annotations.Index;
import com.wangjie.rapidorm.api.annotations.Table;
import com.wangjie.rapidorm.api.constant.Constants;
import com.wangjie.rapidorm.compiler.constants.GuessClass;
import com.wangjie.rapidorm.compiler.util.LogUtil;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/28/16.
 */
public class TableEntry {
    private static final String STUFF_RAPID_ORM_TABLE = "_RORM";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:SSS");

    private Element mSourceClassEle;
    private TypeName mSourceClassEleTypeName;

    private List<ColumnEntry> mColumnList = new ArrayList<>();

    private List<ColumnEntry> mPkColumnList = new ArrayList<>();
    private List<ColumnEntry> mNoPkColumnList = new ArrayList<>();

    private String tableName;
    private Table tableAnnotation;

    public TableEntry() {
    }

    public void setSourceClassEle(Element sourceClassEle) {
        mSourceClassEle = sourceClassEle;
        tableAnnotation = mSourceClassEle.getAnnotation(Table.class);
        mSourceClassEleTypeName = ClassName.get(mSourceClassEle.asType());
    }

    private void parse() throws Throwable {
        List<? extends Element> eles = mSourceClassEle.getEnclosedElements();
        for (Element e : eles) {
            if (ElementKind.FIELD == e.getKind()) {
                if (e.getModifiers().contains(Modifier.PRIVATE)) {
                    throw new RuntimeException(e.getSimpleName().toString() + " in " + mSourceClassEle.asType().toString() + " can not be private!");
                }
                Column column = e.getAnnotation(Column.class);
                if (null != column) {
                    mColumnList.add(new ColumnEntry(e));
                }
            }
        }
    }

    public JavaFile brewJava() throws Throwable {
        parse();

        LogUtil.logger("mSourceClassEle: " + mSourceClassEle + ", mColumnList: " + mColumnList);

        String sourceClassSimpleName = mSourceClassEle.getSimpleName().toString();
        String targetClassSimpleName = sourceClassSimpleName + STUFF_RAPID_ORM_TABLE;
        String targetPackage = mSourceClassEle.getEnclosingElement().toString();

        TypeSpec.Builder result = TypeSpec.classBuilder(targetClassSimpleName)
                .addModifiers(Modifier.PUBLIC)
                .superclass( // extends TableConfig
                        ParameterizedTypeName.get(
                                ClassName.bestGuess(GuessClass.BASE_TABLE_CONFIG),
                                mSourceClassEleTypeName
                        )
                );

        // Constructor method
        MethodSpec.Builder constructorMethod = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("super($T.class)", mSourceClassEleTypeName);

        overrideParseAllConfigsMethod(result);

        implementModelPropertyMethods(result);

        result.addMethod(constructorMethod.build());

        return JavaFile.builder(targetPackage, result.build())
                .addFileComment("GENERATED CODE BY RapidORM. DO NOT MODIFY! $S, Source table: $S",
                        DATE_FORMAT.format(new Date(System.currentTimeMillis())),
                        mSourceClassEle.asType().toString())
                .skipJavaLangImports(true)
                .build();
    }

    /**
     * Override parseAllConfigsMethod of TabConfig
     */
    private void overrideParseAllConfigsMethod(TypeSpec.Builder result) {
        MethodSpec.Builder parseAllConfigsMethod = MethodSpec.methodBuilder("parseAllConfigs")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED);

        parseAllConfigsMethod.addStatement("tableName = $S", getTableName());

        for (ColumnEntry columnEntry : mColumnList) {
            Element element = columnEntry.getFieldColumnElement();
            Column column = element.getAnnotation(Column.class);
            String columnConfigFieldName = element.getSimpleName().toString() + "ColumnConfig";
            parseAllConfigsMethod.addStatement("$T $L = buildColumnConfig($S/*column name*/, $L/*autoincrement*/, $L/*notNull*/, $S/*defaultValue*/, $L/*index*/, $L/*unique*/, $L/*uniqueCombo*/, $L/*primaryKey*/, $S/*dbType*/)",
                    ClassName.bestGuess(GuessClass.COLUMN_CONFIG),
                    columnConfigFieldName,
                    columnEntry.getColumnName(),
                    column.autoincrement(),
                    column.notNull(),
                    column.defaultValue(),
                    column.index(),
                    column.unique(),
                    column.uniqueCombo(),
                    column.primaryKey(),
                    columnEntry.getDbType()
            );

            parseAllConfigsMethod.addStatement("allColumnConfigs.add($L)", columnConfigFieldName);
            parseAllConfigsMethod.addStatement("allFieldColumnConfigMapper.put($S/*field name*/, $L)", element.getSimpleName(), columnConfigFieldName);
            int pcCounts = 0; // 统计主键个数
            if (column.primaryKey()) {
                pcCounts++;
                if (column.autoincrement()) {
                    if (pcCounts > 1) {
                        throw new RuntimeException("Autoincrement must be only one primaryKey");
                    }
                }
                parseAllConfigsMethod.addStatement("pkColumnConfigs.add($L)", columnConfigFieldName);
                mPkColumnList.add(columnEntry);
            } else {
                parseAllConfigsMethod.addStatement("noPkColumnConfigs.add($L)", columnConfigFieldName);
                mNoPkColumnList.add(columnEntry);
            }
        }
        result.addMethod(parseAllConfigsMethod.build());
    }

    /**
     * Implementation methods of PropertyMethod interface.
     */
    private void implementModelPropertyMethods(TypeSpec.Builder result) {
        ClassName sqliteStatementDelegateTypeName = ClassName.bestGuess(GuessClass.SQLITE_STATEMENT_DELEGATE);


        // bindInsertArgs
        MethodSpec.Builder bindInsertArgsMethod = MethodSpec.methodBuilder(GuessClass.ModelProperty.METHOD_NAME_BIND_INSERT_ARGS)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .addParameter(mSourceClassEleTypeName, "model")
//                .addParameter(ParameterizedTypeName.get(
//                        ClassName.bestGuess(List.class.getCanonicalName()),
//                        ClassName.bestGuess(Object.class.getCanonicalName())
//                ), "insertArgs");
                .addParameter(sqliteStatementDelegateTypeName, "statement")
                .addParameter(int.class, "indexOffset");

        // bindUpdateArgs
        MethodSpec.Builder bindUpdateArgsMethod = MethodSpec.methodBuilder(GuessClass.ModelProperty.METHOD_NAME_BIND_UPDATE_ARGS)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .addParameter(mSourceClassEleTypeName, "model")
//                .addParameter(ParameterizedTypeName.get(
//                        ClassName.bestGuess(List.class.getCanonicalName()),
//                        ClassName.bestGuess(Object.class.getCanonicalName())
//                ), "updateArgs");
                .addParameter(sqliteStatementDelegateTypeName, "statement")
                .addParameter(int.class, "indexOffset");

        // bindPkArgs
        MethodSpec.Builder bindPkArgsMethod = MethodSpec.methodBuilder(GuessClass.ModelProperty.METHOD_NAME_BIND_PK_ARGS)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .addParameter(mSourceClassEleTypeName, "model")
//                .addParameter(ParameterizedTypeName.get(
//                        ClassName.bestGuess(List.class.getCanonicalName()),
//                        ClassName.bestGuess(Object.class.getCanonicalName())
//                ), "pkArgs");
                .addParameter(sqliteStatementDelegateTypeName, "statement")
                .addParameter(int.class, "indexOffset");

        // parseFromCursor
        MethodSpec.Builder parseFromCursorMethod = MethodSpec.methodBuilder(GuessClass.ModelProperty.METHOD_NAME_PARSE_FROM_CURSOR)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(mSourceClassEleTypeName)
                .addParameter(ClassName.bestGuess(GuessClass.CURSOR), "cursor")
                .addStatement("$T model = new $T()", mSourceClassEleTypeName, mSourceClassEleTypeName)
                .addStatement("int index");

        ClassName stringClassName = ClassName.get(String.class);

        for (ColumnEntry columnEntry : mColumnList) {
            String fieldSimpleName = columnEntry.getFieldSimpleName();

            // parseFromCursor
            parseFromCursorMethodStatement(parseFromCursorMethod, columnEntry, fieldSimpleName);

            // Add `public static final String COLUMN_NAME...`
            String columnName = columnEntry.getColumnName();
            result.addField(
                    FieldSpec.builder(stringClassName, columnName.toUpperCase(), Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                            .addJavadoc("Column name: $S, field name: {@link $T#$L}\n",
                                    columnName,
                                    mSourceClassEleTypeName,
                                    columnEntry.getFieldSimpleName()
                            )
                            .initializer("$S", columnName)
                            .build()
            );

            Column column = columnEntry.getFieldColumnElement().getAnnotation(Column.class);
            if (column.primaryKey()) {
                if (!column.autoincrement()) {
                    // bindInsertArgs
                    bindInsertArgsStatement(bindInsertArgsMethod, columnEntry, fieldSimpleName);
                }
                // bindPkArgs
                bindPkArgsStatement(bindPkArgsMethod, columnEntry, fieldSimpleName);

            } else {
                // bindInsertArgs
                bindInsertArgsStatement(bindInsertArgsMethod, columnEntry, fieldSimpleName);
                // bindUpdateArgs
                bindUpdateArgsStatement(bindUpdateArgsMethod, columnEntry, fieldSimpleName);
            }
        }

        bindInsertArgsMethod.addStatement("return indexOffset");
        bindUpdateArgsMethod.addStatement("return indexOffset");
        bindPkArgsMethod.addStatement("return indexOffset");
        parseFromCursorMethod.addStatement("return model");

        result.addMethod(bindInsertArgsMethod.build());
        result.addMethod(bindUpdateArgsMethod.build());
        result.addMethod(bindPkArgsMethod.build());
        result.addMethod(parseFromCursorMethod.build());


        implementCreateTableModelPropertyMethods(result);


    }

    private void parseFromCursorMethodStatement(MethodSpec.Builder parseFromCursorMethod, ColumnEntry columnEntry, String fieldSimpleName) {
        parseFromCursorMethod.addStatement("index = cursor.getColumnIndex($S)", columnEntry.getColumnName());

        parseFromCursorMethod.beginControlFlow("if(-1 != index)");

        if (isBoolean(columnEntry.getFieldColumnTypeName())) {
            parseFromCursorMethod.addStatement("model.$L = cursor.isNull(index) ? null : (cursor.getInt(index) == 1)", fieldSimpleName);
        } else {
            parseFromCursorMethod.addStatement("model.$L = cursor.isNull(index) ? null : (cursor.get$L(index))", fieldSimpleName, columnEntry.getDataType());
        }

        parseFromCursorMethod.endControlFlow();

    }

    private void bindInsertArgsStatement(MethodSpec.Builder bindInsertArgsMethod, ColumnEntry columnEntry, String fieldSimpleName) {
        bindInsertArgsMethod.addStatement("indexOffset++");
        bindInsertArgsMethod.addStatement("$T $L = model.$L", columnEntry.getFieldColumnTypeName(), fieldSimpleName, fieldSimpleName);
        TypeName typeName = columnEntry.getFieldColumnTypeName();
        bindToStatement(bindInsertArgsMethod, columnEntry, typeName, fieldSimpleName);
    }

    private void bindPkArgsStatement(MethodSpec.Builder bindPkArgsMethod, ColumnEntry columnEntry, String fieldSimpleName) {
        bindPkArgsMethod.addStatement("indexOffset++");
        bindPkArgsMethod.addStatement("$T $L = model.$L", columnEntry.getFieldColumnTypeName(), fieldSimpleName, fieldSimpleName);
        TypeName typeName = columnEntry.getFieldColumnTypeName();
        bindToStatement(bindPkArgsMethod, columnEntry, typeName, fieldSimpleName);
    }

    private void bindUpdateArgsStatement(MethodSpec.Builder bindUpdateArgsMethod, ColumnEntry columnEntry, String fieldSimpleName) {
        bindUpdateArgsMethod.addStatement("indexOffset++");
        bindUpdateArgsMethod.addStatement("$T $L = model.$L", columnEntry.getFieldColumnTypeName(), fieldSimpleName, fieldSimpleName);
        TypeName typeName = columnEntry.getFieldColumnTypeName();
        bindToStatement(bindUpdateArgsMethod, columnEntry, typeName, fieldSimpleName);
    }

    private String bindToStatement(MethodSpec.Builder bindArgsMethod, ColumnEntry columnEntry, TypeName typeName, String fieldSimpleName) {
        switch (columnEntry.getDataType()) {
            case "Long":
            case "Int":
            case "Short":
                if (typeName.isPrimitive()) {
                    bindArgsMethod.addStatement("statement.bindLong(indexOffset, $L" + (isBoolean(typeName) ? " ? 1 : 0" : "") + ")", fieldSimpleName);
                } else {
                    bindArgsMethod.beginControlFlow("if (null == $L)", fieldSimpleName);
                    bindArgsMethod.addStatement("statement.bindNull(indexOffset)");
                    bindArgsMethod.nextControlFlow("else ");
                    bindArgsMethod.addStatement("statement.bindLong(indexOffset, $L" + (isBoolean(typeName) ? " ? 1 : 0" : "") + ")", fieldSimpleName);
                    bindArgsMethod.endControlFlow();
                }
                break;
            case "Float":
//                break;
            case "Double":
                if (typeName.isPrimitive()) {
                    bindArgsMethod.addStatement("statement.bindDouble(indexOffset, $L)", fieldSimpleName);
                } else {
                    bindArgsMethod.beginControlFlow("if (null == $L)", fieldSimpleName);
                    bindArgsMethod.addStatement("statement.bindNull(indexOffset)");
                    bindArgsMethod.nextControlFlow("else ");
                    bindArgsMethod.addStatement("statement.bindDouble(indexOffset, $L)", fieldSimpleName);
                    bindArgsMethod.endControlFlow();
                }
                break;
            case "Blob":
                if (typeName.isPrimitive()) {
                    bindArgsMethod.addStatement("statement.bindBlob(indexOffset, $L)", fieldSimpleName);
                } else {
                    bindArgsMethod.beginControlFlow("if (null == $L)", fieldSimpleName);
                    bindArgsMethod.addStatement("statement.bindNull(indexOffset)");
                    bindArgsMethod.nextControlFlow("else ");
                    bindArgsMethod.addStatement("statement.bindBlob(indexOffset, $L)", fieldSimpleName);
                    bindArgsMethod.endControlFlow();
                }

                break;
            case "String":
                bindArgsMethod.beginControlFlow("if (null == $L)", fieldSimpleName);
                bindArgsMethod.addStatement("statement.bindNull(indexOffset)");
                bindArgsMethod.nextControlFlow("else ");
                bindArgsMethod.addStatement("statement.bindString(indexOffset, $L)", fieldSimpleName);
                bindArgsMethod.endControlFlow();
                break;
            default:
                bindArgsMethod.beginControlFlow("if (null == $L)", fieldSimpleName);
                bindArgsMethod.addStatement("statement.bindNull(indexOffset)");
                bindArgsMethod.nextControlFlow("else ");
                bindArgsMethod.addStatement("statement.bindString(indexOffset, String.valueOf($L))", fieldSimpleName);
                bindArgsMethod.endControlFlow();
                break;
        }
        return null;
    }


    private void implementCreateTableModelPropertyMethods(TypeSpec.Builder result) {
        // createTable part
        MethodSpec.Builder createTableMethod = MethodSpec.methodBuilder(GuessClass.ModelProperty.METHOD_NAME_CREATE_TABLE)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addException(Exception.class)
                .addParameter(ClassName.bestGuess(GuessClass.SQLITE_DATABASE_DELEGATE), "db")
                .addParameter(boolean.class, "ifNotExists");

        createTableMethod.addStatement("String ifNotExistsConstraint = $L? $S : $S", "ifNotExists", "IF NOT EXISTS ", "");

        String createTableStuff = "CREATE TABLE ";
        StringBuilder createTableSql = new StringBuilder()
                .append("`").append(getTableName()).append("`").append(" ( ");

        boolean isPkMultiple = mPkColumnList.size() > 1;
        for (int i = 0, len = mColumnList.size(); i < len; i++) {
            ColumnEntry columnEntry = mColumnList.get(i);
            Column column = columnEntry.getColumnAnnotation();
            createTableSql.append("\n")
                    .append("`").append(columnEntry.getColumnName()).append("`")
                    .append(" ").append(columnEntry.getDbType());

            if (!isPkMultiple && column.primaryKey()) {
                createTableSql.append(" PRIMARY KEY ");
            }

            if (!isPkMultiple && column.autoincrement()) {
                createTableSql.append(" AUTOINCREMENT ");
            }

            if (column.notNull()) {
                createTableSql.append(" NOT NULL ");
            }

            if (column.unique()) {
                createTableSql.append(" UNIQUE ");
            }
            String defaultValue;
            if (!Constants.AnnotationNotSetValue.DEFAULT_VALUE.equals(defaultValue = column.defaultValue())) {
                createTableSql.append(" DEFAULT ").append(defaultValue).append(" ");
            }

            if (i != len - 1) { // not last
                createTableSql.append(",");
            } else {
                if (isPkMultiple) {
                    addPrimaryKeyConstraints(mPkColumnList, createTableSql);
                }
            }
        }

        createTableSql.append(");");
        createTableMethod.addStatement("db.execSQL($S + $L + $S)", createTableStuff, "ifNotExistsConstraint", createTableSql);

        // index part
        Index[] indices = tableAnnotation.indices();
        if (indices.length > 0) {
            for (Index index : indices) {
                String value = index.value();
                if (Constants.AnnotationNotSetValue.INDEX_VALUE.equals(value)) {
                    continue;
                }
                String indexName = index.name();
                createTableMethod.addStatement("db.execSQL($S + $L + $S)",
                        index.unique() ? "CREATE UNIQUE INDEX " : "CREATE INDEX ",
                        "ifNotExistsConstraint",
                        (Constants.AnnotationNotSetValue.INDEX_NAME.equals(indexName) ?
//                                "INDEX_" + value.replaceAll("[ ,]", "_").replaceAll("__+", "_").toUpperCase() : indexName) +
                                "INDEX_" + value.replaceAll("( |,){2,}|( |,)", "_").toUpperCase() : indexName) +
                        " ON `" + getTableName() + "`(\"" + value + "\");"
                );
            }
        }




        result.addMethod(createTableMethod.build());
    }

    // 添加主键约束
    private void addPrimaryKeyConstraints(List<ColumnEntry> pkColumnList, StringBuilder sql) {
        // Primary key Constraint
        int pkCount = pkColumnList.size();
        switch (pkCount) {
            case 0: // No primary key
                break;
            case 1: // One primary key
                ColumnEntry columnEntry = pkColumnList.get(0);
                sql.append(",")
                        .append("\n")
                        .append(" PRIMARY KEY (").append(columnEntry.getColumnName()).append(")");
                break;
            default: // Multiple primary keys
                sql.append(",")
                        .append("\n")
                        .append(" PRIMARY KEY (").append(joinPks(pkColumnList)).append(")");
                break;
        }
    }

    // 拼接多个主键
    private String joinPks(List<ColumnEntry> pkColumnList) {
        StringBuilder pks = new StringBuilder();
        for (int i = 0, size = pkColumnList.size(); i < size; i++) {
            pks.append(pkColumnList.get(i).getColumnName());
            if (i != size - 1) {
                pks.append(", ");
            }
        }
        return pks.toString();
    }


    private String getTableName() {
        if (null == tableName) {
            tableName = parseTableName(mSourceClassEle.getSimpleName().toString(), tableAnnotation);
        }
        return tableName;
    }

    /**
     * 获得Table对应的value值（表名）
     *
     * @return 如果Table注解为空，那么直接使用类名作为表名；否则使用value值
     */
    private String parseTableName(String defaultName, Table table) {
        String name = table.name();
        if (Constants.AnnotationNotSetValue.TABLE_NAME.equals(name)) { // 如果类中没有加Table注解，或者Table注解为空，那么直接使用类名作为表名
            return defaultName;
        }
        return name;
    }

    private boolean isBoolean(TypeName typeName) {
        String typeNameStr = typeName.toString();
        return Boolean.class.getCanonicalName().equals(typeNameStr) || boolean.class.getCanonicalName().equals(typeNameStr);
    }

}
