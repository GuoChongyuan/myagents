package point_handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 处理类具体实现
 */
@AllArgsConstructor
public class DubboMockHandler implements ModuleHandler{

    @Override
    public Object onEnter() {
        return null;
    }

    @Override
    public Map<String, Object> onExit() {
        return null;
    }

    public String getId() {
        return "DUBBO_INVOKE_MOCK_EXECUTE";
    }

//    没有什么用
//    @Override
//    public Object onEnter(BaseComponentEnterEvent enterEvent) {
//        // todo 这里可以做逻辑，根据入参判断是否返回为空
//        // todo 为什么不这样写呢？因为有的MockHandler的enterEvent没有做解析
//        return "for_mieba";
//    }
}
