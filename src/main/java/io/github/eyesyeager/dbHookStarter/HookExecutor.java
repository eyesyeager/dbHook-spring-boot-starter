package io.github.eyesyeager.dbHookStarter;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import io.github.eyesyeager.dbHookStarter.entity.ExecutorEntity;
import io.github.eyesyeager.dbHookStarter.entity.HookEntity;
import io.github.eyesyeager.dbHookStarter.starter.DbHookProperties;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;
import java.util.List;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * @author eyesYeager
 * data 2025/1/18 15:59
 */

public class HookExecutor {

	private static final Logger logger = LoggerFactory.getLogger(HookExecutor.class);

	private ScheduledExecutorService scheduler;

	CronParser parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ));

	private final DbHookProperties properties;

	private final ConfigManager configManager;

	private final HookConfig hookConfig;

	private final HandlerManager handlerManager = new HandlerManager();

	public HookExecutor(HookConfig hookConfig) {
		this.hookConfig = hookConfig;
		this.properties = hookConfig.getDbHookProperties();
		configManager = new ConfigManager(properties, hookConfig.getResourceLoader());
	}

	/**
	 * 组装任务，开始监听
	 * @throws Exception 异常
	 */
	public void run() throws Exception {
		// 加载任务配置文件
		List<HookEntity> hookEntityList;
		try {
			hookEntityList = configManager.loadHookConfigFile();
		} catch (Exception e) {
			throw new Exception("fail to load config file! ", e);
		}
		if (hookEntityList.isEmpty()) {
			logger.warn("no hook task to load!");
			return;
		}
		// 初始化线程池
		scheduler = Executors.newScheduledThreadPool(hookEntityList.size());
		// 初始化任务
		for (HookEntity hookEntity : hookEntityList) {
			ExecutorEntity executor = hookEntity.getExecutor();
			DataMonitor monitor = new DataMonitor(
					hookConfig,
					executor.getName(),
					hookEntity.getMonitor(),
					properties.getStateTableName()
			);
			AbstractHook handler = handlerManager.loadHandler(hookConfig.getBeanFactory(), hookEntity);
			beginTask(executor, monitor, handler);
		}
	}

	/**
	 * 启动定时任务
	 * @param monitor 监视器
	 * @param handler 执行器
	 */
	public void beginTask(ExecutorEntity entity, DataMonitor monitor, AbstractHook handler) {
		// 解析 cron 表达式
		Cron cron = parser.parse(entity.getCron());
		ExecutionTime executionTime = ExecutionTime.forCron(cron);

		// 计算下一个执行时间
		ZonedDateTime now = ZonedDateTime.now();
		Optional<Duration> durationOptional = executionTime.timeToNextExecution(now);
		if (durationOptional.isEmpty()) {
			logger.info(entity.getName() + " task execution completed!");
			return;
		}
		Duration duration = durationOptional.get();
		if (duration.getSeconds() <= 0) {
			ZonedDateTime zonedDateTime = executionTime.nextExecution(now).get();
			duration = executionTime.timeToNextExecution(zonedDateTime).get();
		}
		scheduler.schedule(() -> {
			logger.debug(entity.getName() + " task is scheduled to execute at " + ZonedDateTime.now());
			try {
				List<Object> result = monitor.doMonitor();
				if (!CollectionUtils.isEmpty(result)) {
					handler.execute(result);
				}
			} catch (Exception e) {
				logger.error(entity.getName() + " task run error!", e);
			}
			beginTask(entity, monitor, handler);
		}, duration.getSeconds(), TimeUnit.SECONDS);
	}
}
