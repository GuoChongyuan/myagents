package com.test.myagenttest.test_class;

import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import com.sun.tools.attach.spi.AttachProvider;
import sun.tools.attach.WindowsVirtualMachine;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Constructor;
import java.util.List;

public class AgentTestClass {
    // java -jar D:\flight_program\myagents\agent_bind\target\binder-jar-with-dependencies.jar   D:\flight_program\myagents\myagent\target\myagent.jar test 12372
    // java -jar bind包 agentjar 参数 12372
    public static void main(String[] args) throws Exception {
        printPid();
        Foo foo = new Foo();

        boolean sign = true;

        while (true){

            System.out.println(foo.hello("JavaProgress"));
            Thread.sleep(3000);
            if (sign){
                String name = ManagementFactory.getRuntimeMXBean().getName();
                System.out.println(name);

                String pid = name.split("@")[0];
                String agentPath = "D:\\flight_program\\myagents\\myagent\\target\\myagent.jar";
                String arg = "arg";
                loadAgent(pid, agentPath, arg);
                sign = false;
            }
        }
    }

    private static void printPid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        System.out.println(name);
        String pid = name.split("@")[0];
        System.out.println("Pid is:" + pid);
    }
    private static final AttachProvider ATTACH_PROVIDER = new AttachProvider() {
        @Override
        public String name() {
            return null;
        }

        @Override
        public String type() {
            return null;
        }

        @Override
        public VirtualMachine attachVirtualMachine(String id) {
            return null;
        }

        @Override
        public List<VirtualMachineDescriptor> listVirtualMachines() {
            return null;
        }
    };
    private static void loadAgent(String pid, String agentPath, String arg) throws Exception, AttachNotSupportedException {
        VirtualMachine vm = null;
        System.out.println("创建agent provider开始");
        if (AttachProvider.providers().isEmpty()) {
            Class<? extends VirtualMachine> vmClass = findVirtualMachineClassAccordingToOS();
            Class<?>[] parameterTypes = {AttachProvider.class, String.class};

            // This is only done with Reflection to avoid the JVM pre-loading all the XyzVirtualMachine classes.
            Constructor<? extends VirtualMachine> vmConstructor = vmClass.getConstructor(parameterTypes);
            vm = vmConstructor.newInstance(ATTACH_PROVIDER, pid);
            System.out.println("agent provider 不存在，进行创建");

        } else {
            System.out.println("agent provider 存在，直接进行使用");
            vm =  VirtualMachine.attach(pid);
        }
        System.out.println("加载agent");
        vm.loadAgent(agentPath);
        System.out.println("去除粘滞");
        vm.detach();
        System.out.println("绑定成功");

    }

    private static Class<? extends VirtualMachine> findVirtualMachineClassAccordingToOS() {
        if (File.separatorChar == '\\') {
            return WindowsVirtualMachine.class;
        }

        String osName = System.getProperty("os.name");


        throw new IllegalStateException("Cannot use Attach API on unknown OS: " + osName);
    }
}
