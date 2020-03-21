package com.test.agent.myagent_classfiletransfer;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class TestClassFileTransformer implements ClassFileTransformer {
    // 这里是将字节码直接替换了，所以可以直接看到运行的结果
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        System.out.println("className: " + className);
        ClassPool classPool = ClassPool.getDefault();
        classPool.appendClassPath(new LoaderClassPath(loader));
        CtClass targetCtClass = null;
        byte[] bytecode;

        try {
            final String clazzName = className.replaceAll("/", ".");
            targetCtClass = classPool.getCtClass(clazzName);
            for (CtMethod targetMethod : targetCtClass.getDeclaredMethods()) {
                if (targetMethod.getName().equals("hello")){
                    targetMethod.insertBefore("System.out.println(\"insert by transformer\");\n");
                    targetMethod.insertAfter("System.out.println(\"after by transformer\");\n");
                }
            }
            bytecode = targetCtClass.toBytecode();
        } catch (Throwable t) {
            t.printStackTrace();
            bytecode = null;
        } finally {
            if (targetCtClass != null){
                targetCtClass.defrost();
            }
        }
        return bytecode;
    }
}
