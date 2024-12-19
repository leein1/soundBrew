package com.soundbrew.soundbrew.config;

import org.hibernate.dialect.MySQL57Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class CustomMySQLDialect extends MySQL57Dialect {
    public CustomMySQLDialect() {
        super();
        // JPA QueryDSL에서 group_concat 함수를 사용할 수 있도록 등록
        registerFunction("group_concat_distinct", new SQLFunctionTemplate(StandardBasicTypes.STRING, "group_concat(distinct ?1)"));
        registerFunction("group_concat", new StandardSQLFunction("group_concat", StandardBasicTypes.STRING));
    }
}