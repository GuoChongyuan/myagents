package com.test.myagenttest.bind_class;

import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import com.sun.tools.attach.spi.AttachProvider;
import sun.tools.attach.WindowsVirtualMachine;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.List;

public class AgentBindClass {

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

    /**
     * 前提：
     * 必须得有运行中的java进程信息
     * 运行的最简单的一种测试方式，利用main方法进行绑定，运行的流程如下：
     * 1. 绑定编写好的agent
     * 2. 进行代码运行
     * @param args
     * @throws Exception
     */

    public static void main(String[] args) throws Exception {
        String pid = "20200";
        String agentPath = "D:\\flight_program\\myagents\\myagent\\target\\myagent.jar";
        String arg = "arg";
        loadAgent(pid, agentPath, arg);
    }

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
