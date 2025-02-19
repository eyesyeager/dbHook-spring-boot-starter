package io.github.eyesyeager.dbHookStarter.starter;

import io.github.eyesyeager.dbHookStarter.constants.ConfigConstants;
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
public class DbHookProperties {
	/**
	 * 配置文件地址
	 */
	private String configPath = ConfigConstants.DEFAULT_CONFIG_PATH;

	/**
	 * 状态表表名
	 */
	private String stateTableName = ConfigConstants.DEFAULT_TABLE_NAME;
}