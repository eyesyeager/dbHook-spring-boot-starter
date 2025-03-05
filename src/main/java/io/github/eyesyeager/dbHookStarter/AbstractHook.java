package io.github.eyesyeager.dbHookStarter;

import io.github.eyesyeager.dbHookStarter.entity.ExecutorEntity;
import io.github.eyesyeager.dbHookStarter.entity.HandlerEntity;
import io.github.eyesyeager.dbHookStarter.entity.MonitorEntity;

import java.util.List;

/**
 * @author eyesYeager
 * data 2025/1/18 15:59
 */

public abstract class AbstractHook {

    protected Object params;

    protected ExecutorEntity executorEntity;

    protected MonitorEntity monitorEntity;

    protected HandlerEntity handlerEntity;

    public abstract void execute(List<Object> result);
}
