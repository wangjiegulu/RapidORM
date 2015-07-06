package com.wangjie.rapidorm.example.database.generate;

import com.wangjie.rapidorm.core.generate.templates.ModelPropertyGenerator;
import com.wangjie.rapidorm.example.database.model.Person;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 7/2/15.
 */
public class MyGenerator {
    public static void main(String[] args) throws Exception {

        Class tableClazz = Person.class;
        new ModelPropertyGenerator().generate(tableClazz,
                "example/src/main/java",
                tableClazz.getPackage().getName() + ".config");

    }



}
