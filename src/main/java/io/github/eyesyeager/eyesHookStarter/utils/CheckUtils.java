package io.github.eyesyeager.eyesHookStarter.utils;

import io.github.eyesyeager.eyesHookStarter.constants.ExecutorTypeEnum;
import io.github.eyesyeager.eyesHookStarter.entity.ExecutorEntity;
import io.github.eyesyeager.eyesHookStarter.entity.HandlerEntity;
import io.github.eyesyeager.eyesHookStarter.entity.HookEntity;
import io.github.eyesyeager.eyesHookStarter.entity.MonitorEntity;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.util.StringUtils;

/**
 * @author eyesYeager
 * data 2025/1/18 16:01
 */

public class CheckUtils {

	public static void checkHookEntity(HookEntity entity) {
		checkExecutorEntity(entity.getExecutor());
		checkMonitorEntity(entity.getMonitor());
		checkHandlerEntity(entity.getHandler());
	}

	public static void checkExecutorEntity(ExecutorEntity entity) {
		if (entity == null) {
			throw new IllegalArgumentException("executor must not be null!");
		}
		// 任务命名校验
		if (StringUtils.hasLength(entity.getName())) {
			throw new IllegalArgumentException("executor.name must not be null!");
		}
		// 任务类型校验
		if (StringUtils.hasLength(entity.getType())) {
			throw new IllegalArgumentException("executor.type must not be null!");
		}
		if (ExecutorTypeEnum.CRON.getValue().equals(entity.getType())) {
			if (StringUtils.hasLength(entity.getCron())) {
				throw new IllegalArgumentException("executor.cron must not be null!");
			}
			// 校验 cron 表达式
			if (checkCron(entity.getCron())) {
				throw new IllegalArgumentException(entity.getCron() + " is not a valid cron expression!");
			}
		} else if (ExecutorTypeEnum.FIXED_RATE.getValue().equals(entity.getType())) {
			// 校验固定速率配置
			checkFixedRate(entity.getType());
		} else {
			throw new IllegalArgumentException("executor.type must be 'cron' or 'fixedRate'!");
		}
	}

	public static void checkMonitorEntity(MonitorEntity entity) {
		if (entity == null) {
			throw new IllegalArgumentException("monitor must not be null!");
		}
		if (StringUtils.hasLength(entity.getStateName())) {
			throw new IllegalArgumentException("monitor.stateName must not be null!");
		}
		if (StringUtils.hasLength(entity.getStateDefault())) {
			throw new IllegalArgumentException("monitor.stateDefault must not be null!");
		}
		if (StringUtils.hasLength(entity.getStateType())) {
			throw new IllegalArgumentException("monitor.stateType must not be null!");
		}
		if (StringUtils.hasLength(entity.getJudgeSql())) {
			throw new IllegalArgumentException("monitor.judgeSql must not be null!");
		}
	}

	public static void checkHandlerEntity(HandlerEntity entity) {

	}

	/**
	 * 校验cron表达式
	 * @param cron cron表达式
	 * @return true or false
	 */
	public static Boolean checkCron(String cron) {
		if (!StringUtils.hasLength(cron)) {
			return false;
		}
		return CronExpression.isValidExpression(cron);
	}

	/**
	 * 校验固定速率配置
	 * @param fixedRate 固定速率
	 */
	public static void checkFixedRate(String fixedRate) {
		if (!StringUtils.hasLength(fixedRate)) {
			throw new IllegalArgumentException("executor.fixedRate must not be null!");
		}
		if (!fixedRate.matches("[0-9]+[s,mh]")) {
			throw new IllegalArgumentException("executor.fixedRate must be a positive integer and unit must be 's', 'm' or 'h'!");
		}
	}
}