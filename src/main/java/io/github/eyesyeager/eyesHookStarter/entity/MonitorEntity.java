package io.github.eyesyeager.eyesHookStarter.entity;

import io.github.eyesyeager.eyesHookStarter.entity.monitor.JdbcMonitorEntity;
import io.github.eyesyeager.eyesHookStarter.entity.monitor.RedisMonitorEntity;
import lombok.Data;

/**
 * @author eyesYeager
 * data 2025/1/18 16:04
 */

@Data
public class MonitorEntity {
	private JdbcMonitorEntity jdbc;

	private RedisMonitorEntity redis;
}
