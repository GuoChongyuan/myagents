package point_handler;

/**
 * 用来进行moduleHandler的分组管理
 */
public interface ModuleHandlerManager {
    /**
     * moduleHandler的具体内容
     * @param pointCutId
     * @param actionId
     * @return
     */
    ModuleHandler getModuleHandler(String pointCutId, String actionId);

    /**
     * 可以理解为分组信息
     * @return
     */
    String getComponent();
}
