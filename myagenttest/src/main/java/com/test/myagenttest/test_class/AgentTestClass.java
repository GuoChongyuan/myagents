package com.test.myagenttest.test_class;

import java.lang.management.ManagementFactory;

public class AgentTestClass {
    // java -jar D:\flight_program\myagents\agent_bind\target\binder-jar-with-dependencies.jar   D:\flight_program\myagents\myagent\target\myagent.jar test 12372
    // java -jar bind包 agentjar 参数 12372
    public static void main(String[] args) throws InterruptedException {
        printPid();
        Foo foo = new Foo();
        while (true){

            System.out.println(foo.hello("JavaProgress"));
            Thread.sleep(3000);
        }
    }

    private static void printPid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        System.out.println(name);
        String pid = name.split("@")[0];
        System.out.println("Pid is:" + pid);
    }
}
