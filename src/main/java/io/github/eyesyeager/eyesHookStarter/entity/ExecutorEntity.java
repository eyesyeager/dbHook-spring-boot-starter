package io.github.eyesyeager.eyesHookStarter.entity;

import lombok.Data;

/**
 * @author eyesYeager
 * data 2025/1/18 16:04
 */

@Data
public class ExecutorEntity {

	private String name;

	private String type;

	private String cron;

	private String fixedRate;
}
