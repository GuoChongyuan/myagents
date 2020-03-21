package point_handler;

import java.util.HashMap;
import java.util.Map;

/**
 * 1. 名称-切点对应
 * 2. 名称-处理模块对应
 * 3. 切点 - 处理模块可通过名称进行关联
 */
public class DubboModuleHandlerManager implements ModuleHandlerManager {

    private static Map<String, ModuleHandler> moduleHandlerMap = new HashMap<String, ModuleHandler>();

    static {
        moduleHandlerMap.put("DUBBO_INVOKE_Test",new DubboMockHandler());

    }

    @Override
    public ModuleHandler getModuleHandler(String pointCutId, String actionId) {
        return moduleHandlerMap.get(pointCutId + "_" + actionId);
    }

    @Override
    public String getComponent() {
        return "DUBBO";
    }
}
