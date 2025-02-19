package io.github.eyesyeager.dbHookStarter;

import io.github.eyesyeager.dbHookStarter.entity.HandlerEntity;
import org.springframework.beans.factory.BeanFactory;

/**
 * @author eyesYeager
 * data 2025/1/18 15:59
 */

public class HandlerManager {

    public AbstractHook loadHandler(BeanFactory beanFactory, HandlerEntity entity) {
        AbstractHook instance = (AbstractHook) beanFactory.getBean(entity.getBeanName());
        instance.params = entity.getParams();
        return instance;
    }
}
