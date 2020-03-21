package com.test.agent.myagent_classfiletransfer;

import java.lang.instrument.Instrumentation;


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
     * @param arg
     * @param inst
     */
    private static void start(Instrumentation inst, String arg) {
        TestClassFileTransformer transformer = new TestClassFileTransformer();

        try {
            System.out.println("agent 代理开始执行");
            Class targetClass = getTargetClass(inst);

            System.out.println("加入转化类");
            inst.addTransformer(transformer, true);

            System.out.println("进行转化");
            inst.retransformClasses(targetClass);
        }catch (Exception e){
            System.out.println(e);
        }finally {
            System.out.println("转化完成之后释放");
            inst.removeTransformer(transformer);
        }


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
