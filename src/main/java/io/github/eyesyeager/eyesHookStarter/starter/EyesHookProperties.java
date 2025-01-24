package io.github.eyesyeager.eyesHookStarter.starter;

import io.github.eyesyeager.eyesHookStarter.constants.ConfigConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author eyesYeager
 * data 2025/1/18 16:02
 */

@Data
@Component
@ConfigurationProperties(ConfigConstants.PACKAGE_ROOT)
public class EyesHookProperties {
	/**
	 * 配置文件地址
	 */
	private String configPath = ConfigConstants.DEFAULT_CONFIG_PATH;
}