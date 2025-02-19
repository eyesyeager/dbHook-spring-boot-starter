package io.github.eyesyeager.dbHookStarter.entity;

import lombok.Data;

/**
 * @author eyesYeager
 * data 2025/1/18 16:04
 */

@Data
public class MonitorEntity {
	private String judgeSql;

	private String resultType;

	private String resultClass;

	private State state;

	@Data
	public static class State {
		private Boolean enabled = false;

		private String fieldName;

		private String fieldType;

		private String defaultValue;
	}
}
