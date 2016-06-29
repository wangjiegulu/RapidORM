package com.wangjie.rapidorm.compiler;

import com.google.auto.common.MoreElements;
import com.google.auto.service.AutoService;

import com.wangjie.rapidorm.api.annotations.Column;
import com.wangjie.rapidorm.api.annotations.Table;
import com.wangjie.rapidorm.compiler.base.BaseAbstractProcessor;
import com.wangjie.rapidorm.compiler.objs.TableEntry;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/28/16.
 */
@AutoService(Processor.class)
public class TableProcessor extends BaseAbstractProcessor {
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportedTypesSet = new HashSet<>();
        supportedTypesSet.add(Table.class.getCanonicalName());
        supportedTypesSet.add(Column.class.getCanonicalName());
        return supportedTypesSet;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        long start = System.currentTimeMillis();
        logger("[process]annotations: " + Arrays.toString(annotations.toArray()));
        logger("[process]roundEnv: " + roundEnv);
        try {

            HashMap<String, TableEntry> tableMapper = new HashMap<>();

            for (Element e : roundEnv.getElementsAnnotatedWith(Table.class)) {
                doTableAnnotation(e, tableMapper);
            }

            for (Map.Entry<String, TableEntry> entry : tableMapper.entrySet()) {
                String key = entry.getKey();
                TableEntry tableEntry = entry.getValue();
                try {
                    logger("TableConfig generate START -> " + key);
                    tableEntry.brewJava().writeTo(filer);
                    logger("TableConfig generate END -> " + key + ", tableEntry: " + tableEntry);
                } catch (RuntimeException e) {
                    throw e;
                } catch (Throwable throwable) {
                    logger("TableConfig generate FAILED -> " + key + ", tableEntry: " + tableEntry);
                    loggerE(throwable);
                }
            }

        }/*catch (Throwable throwable) {
            loggerE(throwable);
        }*/ finally {
            logger("[process] tasks: " + (System.currentTimeMillis() - start) + "ms");
        }


        return true;
    }

    private void doTableAnnotation(Element ele, HashMap<String, TableEntry> tableMapper) {
        obtainTableEntrySafe(ele, tableMapper);

    }

    private TableEntry obtainTableEntrySafe(Element ele, HashMap<String, TableEntry> tableMapper) {
        Element classEle = getElementOwnerElement(ele);
        String className = classEle.asType().toString();
        TableEntry tableEntry = tableMapper.get(MoreElements.asType(classEle).getQualifiedName().toString());
        if (null == tableEntry) {
            tableEntry = new TableEntry();
            tableEntry.setSourceClassEle(classEle);
            tableMapper.put(className, tableEntry);
        }
        return tableEntry;
    }


}
