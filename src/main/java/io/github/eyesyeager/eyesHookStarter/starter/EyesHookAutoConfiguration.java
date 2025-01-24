package io.github.eyesyeager.eyesHookStarter.starter;

import io.github.eyesyeager.eyesHookStarter.HookExecutor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author eyesYeager
 * data 2025/1/18 16:02
 */

@Configuration
@EnableConfigurationProperties(EyesHookProperties.class)
public class EyesHookAutoConfiguration {
	@Resource
	private EyesHookProperties eyesHookProperties;

	@Resource
	private ResourceLoader resourceLoader;

	@PostConstruct
	public void run() throws Exception {
		new HookExecutor(eyesHookProperties, resourceLoader).run();
	}
}
