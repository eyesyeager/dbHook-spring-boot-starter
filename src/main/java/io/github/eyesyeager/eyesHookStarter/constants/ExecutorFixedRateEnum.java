package io.github.eyesyeager.eyesHookStarter.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author eyesYeager
 * data 2025/1/18 16:05
 */

@Getter@AllArgsConstructor
public enum ExecutorFixedRateEnum {
	SECOND("s"),

	MINUTE("min"),

	HOUR("h");

	private final String value;
}
