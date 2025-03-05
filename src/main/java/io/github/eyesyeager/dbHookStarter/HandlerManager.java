package io.github.eyesyeager.dbHookStarter;

import io.github.eyesyeager.dbHookStarter.entity.HandlerEntity;
import io.github.eyesyeager.dbHookStarter.entity.HookEntity;
import org.springframework.beans.factory.BeanFactory;

/**
 * @author eyesYeager
 * data 2025/1/18 15:59
 */

public class HandlerManager {

    public AbstractHook loadHandler(BeanFactory beanFactory, HookEntity entity) {
        HandlerEntity handler = entity.getHandler();
        AbstractHook instance = (AbstractHook) beanFactory.getBean(handler.getBeanName());
        // 参数赋值
        instance.executorEntity = entity.getExecutor();
        instance.monitorEntity = entity.getMonitor();
        instance.handlerEntity = handler;
        instance.params = handler.getParams();
        return instance;
    }
}
