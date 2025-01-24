package io.github.eyesyeager.eyesHookStarter.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author eyesYeager
 * data 2025/1/18 16:06
 */

@Getter
@AllArgsConstructor
public enum MonitorStateTypeEnum {
	INT("int"),

	TINYINT("tinyint"),

	SMALLINT("smallint"),

	BIGINT("bigint"),

	FLOAT("float"),

	DOUBLE("double"),

	DECIMAL("decimal"),

	DATE("date"),

	DATETIME("datetime"),

	TIMESTAMP("timestamp"),

	LONG("long"),
	;

	private final String value;
}
