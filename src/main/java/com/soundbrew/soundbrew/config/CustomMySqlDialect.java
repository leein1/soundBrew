package com.soundbrew.soundbrew.config;

import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class CustomMySqlDialect extends MySQLDialect {
    public CustomMySqlDialect() {
        super();
        // 사용자 정의 함수 등록
        registerFunction("group_concat", new StandardSQLFunction("group_concat", StandardBasicTypes.STRING));
    }
}
