package io.github.eyesyeager.dbHookStarter;

import java.util.List;

/**
 * @author eyesYeager
 * data 2025/1/18 15:59
 */

public abstract class AbstractHook {

    protected Object params;

    public abstract void execute(List<Object> result);
}
