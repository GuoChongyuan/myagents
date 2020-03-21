import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import com.sun.tools.attach.spi.AttachProvider;
import sun.tools.attach.WindowsVirtualMachine;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.List;

/**
 * 参数的绑定
 * 1. 有启动入口的函数，使用java -jar的形式进行运行
 * 2. load agent的时候，一定要把参数传过去
 * 3. bind参数来源于启动参数
 */
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


    public static void main(String[] args) throws Exception {
        String pid = args[2];
        String agentPath = args[0];
        String arg = agentPath + ";" +args[1];
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
        vm.loadAgent(agentPath,arg);
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
