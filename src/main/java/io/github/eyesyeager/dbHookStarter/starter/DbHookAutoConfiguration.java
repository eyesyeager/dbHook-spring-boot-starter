package io.github.eyesyeager.dbHookStarter.starter;

import io.github.eyesyeager.dbHookStarter.HookConfig;
import io.github.eyesyeager.dbHookStarter.HookExecutor;

import javax.sql.DataSource;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author eyesYeager
 * data 2025/1/18 16:02
 */

@Configuration
@EnableConfigurationProperties(DbHookProperties.class)
@ComponentScan({"io.github.eyesyeager.dbHookStarter"})
public class DbHookAutoConfiguration {
	@Resource
	private DbHookProperties dbHookProperties;

	@Resource
	private ResourceLoader resourceLoader;

	@Resource
	private DataSource dataSource;

	@Resource
	private BeanFactory beanFactory;

	@PostConstruct
	public void run() throws Exception {
		HookConfig hookConfig = new HookConfig();
		hookConfig.setDbHookProperties(dbHookProperties);
		hookConfig.setResourceLoader(resourceLoader);
		hookConfig.setDataSource(dataSource);
		hookConfig.setBeanFactory(beanFactory);
		new HookExecutor(hookConfig).run();
	}
}
