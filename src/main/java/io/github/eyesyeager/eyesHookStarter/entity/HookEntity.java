package io.github.eyesyeager.eyesHookStarter.entity;

import lombok.Data;

/**
 * @author eyesYeager
 * data 2025/1/18 16:04
 */

@Data
public class HookEntity {
	private ExecutorEntity executor;

	private MonitorEntity monitor;

	private HandlerEntity handler;
}
