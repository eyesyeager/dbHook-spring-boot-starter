package io.github.eyesyeager.eyesHookStarter.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author eyesYeager
 * data 2025/1/18 16:05
 */

@Getter
@AllArgsConstructor
public enum ExecutorTypeEnum {
	CRON("cron"),

	FIXED_RATE("fixedRate");

	private final String value;
}
