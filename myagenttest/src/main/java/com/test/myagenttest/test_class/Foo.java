package com.test.myagenttest.test_class;

public class Foo {
    @TimeLog
    public String hello(String name) {
        return "hello " + name;
    }
}
