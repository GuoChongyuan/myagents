package com.test.agent.myagent_assient;

import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.io.File;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.util.*;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.none;


/**
 * 加强被调用的类，进行方法的重新编写
 * 这里需要注意：
 * 1. 虽然可以将编写后的方法打印出来，但是改变的也是vm里边的方法
 * 2. 这个是针对jvm进程而言的，模板方法被注入后，agent的加载就算完成了
 * 3. 真正的使用这里编织后的方法是在jvm进程调用到切点的时候
 *
 * @date 2018/4/6
 */
@Slf4j
public class Enhancer {
    // 这里是加强了类，后续需要以aop的形式进行替换，main方法是看不到结果了，因为一个方法只能触发一次，如果是多次，就能看到结果了
    private final static AgentBuilder agentBuilder = new AgentBuilder.Default()
            .ignore(none())
            .disableClassFormatChanges()
            .with(AgentBuilder.InitializationStrategy.NoOp.INSTANCE)
            .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION);

    /**
     * 方法增强
     *
     * @param inst
     * @param methodKeys
     * @return
     */
    public boolean enhance(final Instrumentation inst, final List<MethodKey> methodKeys, Class targetClass) {
        ClassFileTransformer classFileTransform = null;
        try {
            AgentBuilder.Identified.Extendable transform = null;

            log.info("设置文件保存地址");
            final File after = new File("C:\\Users\\chongyuan.guo\\Desktop");

            for (final MethodKey methodKey : methodKeys) {
                String className = methodKey.getClassName();
                log.info("agent builder 创建 transform ,注解类");
                transform = agentBuilder.type(ElementMatchers.<TypeDescription>named(className))
                        .transform(new AgentBuilder.Transformer() {
                            @SneakyThrows
                            @Override
                            public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule) {
                                // 这里声明的是一个模板类，仅作模板使用
                                builder = builder.visit(Advice.to(Invoker.class)
                                        // 切面的产生，这里产生切面后，会调用模板中的进方法和出方法的处理
                                        .on(ElementMatchers.isAnnotatedWith(named("com.test.myagenttest.test_class.TimeLog"))));
                                builder.make() // 编译
                                        .saveIn(after); // 保存到执行的位置，方便进行查看
                                return builder;
                            }
                        });
                // 组装
                log.info("装载编制器");
                classFileTransform = transform.installOn(inst);


                /** 编织目标方法成功后，需要做哪些东西 ，下面两个方法就是相关的逻辑体现 **/
                // 处理方法，承载了处理的方法函数，类似于listener
                Class<?> methodEventListenerHandlerClass = MyAgent.class.getClassLoader().loadClass("com.test.agent.myagent_assient.MethodEventListenerHandler");
                Method handleEnterEventMethod = getMethodByName(methodEventListenerHandlerClass, "handleEnterEvent");
                Method handleExitEventMethod = getMethodByName(methodEventListenerHandlerClass, "handleExitEvent");

                // 模板类
                Class<?> invokerClass = MyAgent.class.getClassLoader().loadClass("com.test.agent.myagent_assient.Invoker");
                Method invokerInitMethod = getMethodByName(invokerClass, "init");
                // 声明的模板里边进行反射的时候需要绑定两个方法，所以这里写了，其实不写也能进行切面处理
                log.info("反射执行编制后的方法，invoker一个类的两个方法而已");
                invokerInitMethod.invoke(null, handleEnterEventMethod, handleExitEventMethod);


            }
        } catch (Exception e) {
            log.error("exception", e);
        } finally {
            // 卸载
            log.info("agent加载完成");
            inst.removeTransformer(classFileTransform);
        }

        return true;
    }

    private static Method getMethodByName(Class<?> clazz, String methodName) {
        if (clazz == null || methodName == null || methodName.length() == 0) {
            return null;
        }
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }
}
