package com.dellife.jpa.dialect;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class MyH2Dialect extends H2Dialect {


    public MyH2Dialect() {
        // 이 파일 persistence.xml 에 등록해줘야함.
        // 이거 어떻게 등록해야하는지는 H2Dialect 들어가보면 알 수 있음.
        registerFunction("group_concat", new StandardSQLFunction("group_concat", StandardBasicTypes.STRING));
    }
}
