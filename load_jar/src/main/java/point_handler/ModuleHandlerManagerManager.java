package point_handler;

import java.util.HashMap;
import java.util.Map;

/**
 * 1. 名称-切点对应
 * 2. 名称-处理模块对应
 * 3. 切点 - 处理模块可通过名称进行关联
 * 本部分为切点 - 处理模块关联
 */
public class ModuleHandlerManagerManager {

    private Map<String, ModuleHandlerManager> moduleHandlerManagerMap = new HashMap<String, ModuleHandlerManager>();

    public void register(String component, ModuleHandlerManager moduleHandlerManager) {
        moduleHandlerManagerMap.put(component, moduleHandlerManager);
    }

    public ModuleHandlerManager getModuleHandlerManager(String component) {
        return moduleHandlerManagerMap.get(component);
    }

    public Map<String, ModuleHandlerManager> getModuleHandlerManagerMap() {
        return moduleHandlerManagerMap;
    }
}