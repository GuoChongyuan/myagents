package com.test.agent.myagent_assient;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 仅仅是一个模板方法而已，这里的方法会被用来编织切点的数据，实际运行中是没有它的
 */
@Slf4j
public class Invoker {

    private static final String RETURNED = "RETURNED";
    private static final String THROWABLE = "THROWABLE";
    public static volatile Method doEnterMethod;
    public static volatile Method doExitMethod;

    public static void init(Method doEnterMethod, Method doExitMethod) {
        Invoker.doEnterMethod = doEnterMethod;
        Invoker.doExitMethod = doExitMethod;
    }

    /**
     * 方法进来
     * 本质是切面的Aop
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
    public static long onEnter(@Advice.Origin Method method,
                                 @Advice.AllArguments Object[] args) throws Throwable {
        try {
            throw new RuntimeException("进来了。。。");
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return 0L;
    }

    /**
     * 方法退出
     * 本质是切面的AOP
     * @param method
     * @param args
     * @param returned
     * @param throwable
     * @throws Throwable
     */
    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void onExit(
            @Advice.Origin Method method,
            @Advice.AllArguments Object[] args,
            @Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object returned,
            @Advice.Thrown(readOnly = false, typing = Assigner.Typing.DYNAMIC) Throwable throwable,
            @Advice.Enter long startTime) throws Throwable {
        try {
            Map<String, Object> result = (Map<String, Object>) doExitMethod.invoke(null, null, method, args, returned, throwable, startTime);
            if (result != null) {
                if (result.containsKey(RETURNED)) {
                    returned = result.get(RETURNED);
                }
                if (result.containsKey(THROWABLE)) {
                    Object t = result.get(THROWABLE);
                    if (t instanceof Throwable) {
                        throwable = (Throwable) t;
                    } else {
                        throwable = null;
                    }
                }
            }
            log.info("=={}=======",System.currentTimeMillis() - startTime);
            log.info("@Advice.OnMethodExit(onThrowable = Throwable.class)");
            System.out.println("@Advice.OnMethodExit(onThrowable = Throwable.class)");
        } catch (Throwable t) {
            //ignore
        }
    }

}
