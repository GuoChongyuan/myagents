import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

public class MyAgent {
    /**
     * 1. 此处使用的是加载jar包进行运行，这样方便进行代码管理
     * 2. 必须得指定入口类
     * 3. 参数是bind模块传进来的
     * @param arg
     * @param inst
     */
    public static void premain(String arg, Instrumentation inst) {
        start(inst, arg);
    }

    public static void agentmain(String arg, Instrumentation inst) {
        start(inst, arg);
    }

    private static void start(Instrumentation inst, String arg) {
        System.out.println("入参是"+arg);
        String[] args = arg.split(";");
        try {
            inst.appendToSystemClassLoaderSearch(new JarFile(args[0]));

            final ClassLoader agentClassLoader = loadOrDefineClassLoader(args[1]);
            Class<?> target = agentClassLoader.loadClass("LoadJarTest");
            Method method = getMethodByName(target, "start");
            method.invoke(target,"test");

            System.out.println("加载完成");
        } catch (Exception e) {
            System.out.println(e);
        }
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

    private static ClassLoader loadOrDefineClassLoader(String jarPath) throws Exception {
        final ClassLoader classLoader;

        URL[] urls = findAllPluginsAndCore(jarPath);
        for (URL url : urls) {
            System.out.println(url.getContent());
        }
        classLoader = new AgentClassLoader(urls);

        return classLoader;
    }

    private static URL[] findAllPluginsAndCore(String jarPath) throws MalformedURLException {
        int lastIndexOf = jarPath.lastIndexOf(File.separator);
        String filePath = jarPath.substring(0, lastIndexOf);
        File file = new File(filePath);
        List<String> plugins = new ArrayList<>();
        if (file.canRead() && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                System.out.println("file:" + jarPath);
                return new URL[]{new URL("file:" + jarPath)};
            }
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().contains("plugin")) {
                    System.out.println("plugin" + files[i].getAbsolutePath());
                    plugins.add(files[i].getAbsolutePath());
                }
            }
        }
        if (plugins.size() == 0) {
            System.out.println("file:" + jarPath);
            return new URL[]{new URL("file:" + jarPath)};
        }
        URL[] urls = new URL[plugins.size() + 1];
        for (int i = 0; i < plugins.size(); i++) {
            System.out.println("file:" + plugins.get(i));
            urls[i] = new URL("file:" + plugins.get(i));
        }
        urls[plugins.size()] = new URL("file:" + jarPath);
        return urls;
    }
}
