package io.github.eyesyeager.dbHookStarter.constants;

import java.sql.Types;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author eyesYeager
 * data 2025/1/18 16:06
 */

@Getter
@AllArgsConstructor
public enum MonitorStateTypeEnum {
	TINYINT("tinyint", Types.TINYINT),

	SMALLINT("smallint", Types.SMALLINT),

	INT("int", Types.INTEGER),

	BIGINT("bigint", Types.BIGINT),

	FLOAT("float", Types.FLOAT),

	DOUBLE("double", Types.DOUBLE),

	DATE("date", Types.DATE),

	DATETIME("datetime", Types.TIMESTAMP),

	TIMESTAMP("timestamp", Types.TIMESTAMP),
	;

	private final String name;

	private final int value;

	public static Integer getValue(String name) {
		for (MonitorStateTypeEnum monitorStateTypeEnum : MonitorStateTypeEnum.values()) {
			if (monitorStateTypeEnum.getName().equals(name)) {
				return monitorStateTypeEnum.getValue();
			}
		}
		return null;
	}

	public static boolean compare(Object a, Object b, String name) {
		if (a == null) {
			return false;
		}
		if (b == null) {
			return true;
		}
		if (TINYINT.name.equals(name) || SMALLINT.name.equals(name) || INT.name.equals(name)) {
			return Integer.parseInt(a.toString()) > Integer.parseInt(b.toString());
		}
		if (BIGINT.name.equals(name) || TIMESTAMP.name.equals(name)) {
			return Long.parseLong(a.toString()) > Long.parseLong(b.toString());
		}
		if (FLOAT.name.equals(name) || DOUBLE.name.equals(name)) {
			return Double.parseDouble(a.toString()) > Double.parseDouble(b.toString());
		}
		if (DATE.name.equals(name) || DATETIME.name.equals(name)) {
			return a.toString().compareTo(b.toString()) > 0;
		}
		throw new IllegalArgumentException("compare error, undefined state type: " + name);
	}
}
