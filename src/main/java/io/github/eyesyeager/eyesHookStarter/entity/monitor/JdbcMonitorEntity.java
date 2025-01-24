package io.github.eyesyeager.eyesHookStarter.entity.monitor;

import lombok.Data;

/**
 * @author eyesYeager
 * data 2025/1/18 17:27
 */

@Data
public class JdbcMonitorEntity {

	private Conn conn;

	private String stateName;

	private String stateDefault;

	private String stateType;

	private Boolean stateClearWhenBoot = false;

	private String judgeSql;

	@Data
	static class Conn {
		private String url;

		private String username;

		private String password;
	}
}
