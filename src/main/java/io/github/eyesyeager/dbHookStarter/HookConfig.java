package io.github.eyesyeager.dbHookStarter;

import io.github.eyesyeager.dbHookStarter.starter.DbHookProperties;
import javax.sql.DataSource;
import lombok.Data;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.io.ResourceLoader;

/**
 * @author artonyu
 */

@Data
public class HookConfig {
    private DbHookProperties dbHookProperties;

    private ResourceLoader resourceLoader;

    private DataSource dataSource;

    private BeanFactory beanFactory;
}
