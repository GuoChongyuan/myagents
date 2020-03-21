package point_handler;

import java.util.Map;

/**
 * 一个切面一个处理方法，这里是针对不同的切面设置不同的方法
 */
public interface ModuleHandler {

    Object onEnter();

    Map<String, Object> onExit();
    // id主要用来做区分
    String getId();

}
