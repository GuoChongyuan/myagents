package com.test.agent.myagent_assient;

import lombok.extern.slf4j.Slf4j;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;


@Slf4j
public class MyAgent {
    public static void premain(String arg, Instrumentation inst) {
        start(inst, arg);
    }

    public static void agentmain(String arg, Instrumentation inst) {
        start(inst, arg);
    }

    /**
     * 1. 此处使用的是加载agent加载的类，然后进行的处理
     * 2. 必须得指定入口类
     * 3. 参数是bind模块传进来的
     *
     * @param arg
     * @param inst
     */
    private static void start(Instrumentation inst, String arg) {

        log.info("agent 代理开始执行");
        Class targetClass = getTargetClass(inst);
        MethodKey methodKey = getTargetMethodKey();
        ArrayList<MethodKey> methodKeys = new ArrayList<MethodKey>();
        methodKeys.add(methodKey);

        log.info("agent 加强类");
        new Enhancer().enhance(inst, methodKeys,targetClass);
    }

    private static MethodKey getTargetMethodKey() {
        // 获取所有的加载的类
        return new MethodKey(
                "com.test.myagenttest.test_class.Foo",
                "hello",
                new String[]{"java.lang.String"}
                );
    }

    private static Class getTargetClass(Instrumentation inst) {
        // 获取所有的加载的类
        Class[] allLoadedClasses = inst.getAllLoadedClasses();
        // 获取目标类
        for (Class clazz : allLoadedClasses) {
            // 类的全路径
            if (clazz.getName()=="com.test.myagenttest.test_class.Foo"){
                return clazz;
            }
        }
        return null;
    }
}
