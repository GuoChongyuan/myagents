package com.test.agent.myagent_assient;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * invoker反射调用需要两个函数，这里进行函数的声明和方法的实现，类似于一个listener
 * 在 Invoker 中设置。 @Advice.OnMethodEnter 这个注解表明的方法会调用夏明的如下方法。
 */
@Slf4j
public class MethodEventListenerHandler {

    private static final Map<Method, MethodKey> methodKeyMap = new ConcurrentHashMap<Method, MethodKey>();

    /**
     * 处理进来的事件
     * 1、流量打标
     * 1、在 Mock 的时候，现在 发出请求里面设置 entryId。如果又 entryId，那么就是回放流量
     *
     * @param self
     * @param method
     * @param args
     * @return
     */
    public static Object handleEnterEvent(Object self, Method method, Object[] args) {
        try {

            log.info("----------------handleEnterEvent--{}------------------",method);
        } catch (Throwable t) {
            log.error("handleEnterEvent error", t);
        }
        return null;
    }

    /**
     * 处理退出
     *
     * @param self
     * @param method
     * @param args
     * @param returned
     * @param throwable
     * @param enter
     * @return
     */
    public static Map<String, Object> handleExitEvent(Object self,
                                                      Method method,
                                                      Object[] args,
                                                      Object returned,
                                                      Throwable throwable,
                                                      Object enter) {
        try {

            log.info("~~~~~~~~~~~~~~handleExitEvent~~~~~~~~~~~~~~");

        } catch (Throwable t) {
            log.error("handleExitEvent error", t);
        }
        return null;
    }


}
