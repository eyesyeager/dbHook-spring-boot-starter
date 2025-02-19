package io.github.eyesyeager.dbHookStarter.entity;

import lombok.Data;

/**
 * @author eyesYeager
 * data 2025/1/18 16:04
 */

@Data
public class HandlerEntity {
	private String beanName;

	private Object params;
}
