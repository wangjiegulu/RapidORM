package ${package};

import android.database.Cursor;
import com.wangjie.rapidorm.core.generate.withoutreflection.IModelProperty;
import com.wangjie.rapidorm.core.generate.templates.ModelFieldMapper;

import ${clazzName};

import java.util.List;

// THIS CODE IS GENERATED BY RapidORM, DO NOT EDIT.
/**
* Property of ${clazzSimpleName}
*/
public class ${clazzPropertyName} implements IModelProperty<${clazzSimpleName}> {


    <#list fieldMappers as fp>
    public static final ModelFieldMapper ${fp.name} = new ModelFieldMapper(${fp.order}, "${fp.fieldName}", "${fp.columnName}");
    </#list>

    public ${clazzPropertyName}() {
    }


    @Override
    public void bindInsertArgs(${clazzSimpleName} model, List<Object> insertArgs) {
        <#list insertArgs as ia>
        ${ia.argsType} ${ia.argsName} = model.${ia.getOrIsMethod}();
        insertArgs.add(null == ${ia.argsName} ? null : ${ia.argsName}${ia.booleanCase});

        </#list>
    }

    @Override
    public void bindUpdateArgs(${clazzSimpleName} model, List<Object> updateArgs) {
        <#list insertArgs as ia>
        ${ia.argsType} ${ia.argsName} = model.${ia.getOrIsMethod}();
        updateArgs.add(null == ${ia.argsName} ? null : ${ia.argsName}${ia.booleanCase});

        </#list>
    }

    @Override
    public void bindPkArgs(${clazzSimpleName} model, List<Object> pkArgs) {
        <#list insertArgs as ia>
        ${ia.argsType} ${ia.argsName} = model.${ia.getOrIsMethod}();
        pkArgs.add(null == ${ia.argsName} ? null : ${ia.argsName}${ia.booleanCase});

        </#list>
    }

    @Override
    public ${clazzSimpleName} parseFromCursor(Cursor cursor) {
        ${clazzSimpleName} model = new ${clazzSimpleName}();
        int index;
        <#list cursorProperties as cp>
        index = cursor.getColumnIndex("${cp.columnName}");
        if(-1 != index){
            model.set${cp.name?cap_first}(cursor.isNull(index) ? null : (cursor.get${cp.dataType}(index)${cp.booleanCase}));
        }

        </#list>
        return model;
    }

}