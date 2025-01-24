package io.github.eyesyeager.eyesHookStarter;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import io.github.eyesyeager.eyesHookStarter.entity.HookEntity;
import io.github.eyesyeager.eyesHookStarter.starter.EyesHookProperties;
import io.github.eyesyeager.eyesHookStarter.utils.CheckUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author eyesYeager
 * data 2025/1/18 15:59
 */

public class HookExecutor {

	private final Yaml yaml = new Yaml();

	private ScheduledExecutorService scheduler;

	CronParser parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ));

	private final ResourceLoader resourceLoader;

	private final EyesHookProperties eyesHookProperties;

	public HookExecutor(EyesHookProperties eyesHookProperties, ResourceLoader resourceLoader) {
		this.eyesHookProperties = eyesHookProperties;
		this.resourceLoader = resourceLoader;
	}

	/**
	 * 初始化狗子执行器
	 * @throws Exception 异常
	 */
	public void run() throws Exception {
		String configPath = eyesHookProperties.getConfigPath();
		List<HookEntity> hookEntityList;
		try {
			hookEntityList = loadConfigFile(configPath);
		} catch (Exception e) {
			throw new Exception("fail to load config file! configPath: " + configPath, e);
		}
		// 初始化线程池
		scheduler = Executors.newScheduledThreadPool(hookEntityList.size());
		// 投入任务
		for (HookEntity dbHookTaskEntity : hookEntityList) {
			beginTask(dbHookTaskEntity);
		}
	}

	/**
	 * 加载任务配置文件
	 * @param configPath 配置文件路径
	 * @throws IOException 异常
	 */
	private List<HookEntity> loadConfigFile(String configPath) throws IOException {
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(resourceLoader);
		Resource[] resources = resolver.getResources("classpath*:/" + configPath + "*/**/*");
		List<HookEntity> hookEntityList = new ArrayList<>();
		for (Resource resource : resources) {
			File file = resource.getFile();
			if (!file.isFile()) {
				continue;
			}
			String relativePath = configPath  + "/" + file.getName();
			InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(relativePath);
			HookEntity hookEntity =  yaml.loadAs(resourceAsStream, HookEntity.class);
			hookEntityList.add(hookEntity);
		}
		return hookEntityList;
	}

	/**
	 * 启动定时任务
	 */
	private void beginTask(HookEntity config) {
		// 校验 hook 配置
		CheckUtils.checkHookEntity(config);
		// 解析 cron 表达式
		Cron cron = parser.parse("123");
		ExecutionTime executionTime = ExecutionTime.forCron(cron);

		// 计算下一个执行时间
		ZonedDateTime now = ZonedDateTime.now();
		Optional<Duration> durationOptional = executionTime.timeToNextExecution(now);
		if (durationOptional.isEmpty()) {
			System.out.println("任务执行结束！");
			return;
		}
		Duration duration = durationOptional.get();
		if (duration.getSeconds() <= 0) {
			ZonedDateTime zonedDateTime = executionTime.nextExecution(now).get();
			duration = executionTime.timeToNextExecution(zonedDateTime).get();
		}
		scheduler.schedule(() -> {
			System.out.println("Task is executed at " + ZonedDateTime.now());
		}, duration.getSeconds(), TimeUnit.SECONDS);
	}
}
