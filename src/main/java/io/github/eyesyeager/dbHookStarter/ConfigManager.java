package io.github.eyesyeager.dbHookStarter;

import io.github.eyesyeager.dbHookStarter.entity.HookEntity;
import io.github.eyesyeager.dbHookStarter.starter.DbHookProperties;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.yaml.snakeyaml.Yaml;

/**
 * @author artonyu
 */

public class ConfigManager {

    private final Yaml yaml = new Yaml();

    @Getter
    private final DbHookProperties dbHookProperties;

    private final ResourceLoader resourceLoader;

    public ConfigManager(DbHookProperties dbHookProperties, ResourceLoader resourceLoader) {
        this.dbHookProperties = dbHookProperties;
        this.resourceLoader = resourceLoader;
    }

    /**
     * 加载任务配置文件
     * @throws IOException 异常
     */
    public List<HookEntity> loadHookConfigFile() throws IOException {
        String configPath = dbHookProperties.getConfigPath();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(resourceLoader);
        Resource[] resources = resolver.getResources("classpath:" + configPath + "/*");
        List<HookEntity> hookEntityList = new ArrayList<>();
        for (Resource resource : resources) {
            File file = resource.getFile();
            if (!file.isFile()) {
                continue;
            }
            String relativePath = configPath  + "/" + file.getName();
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(relativePath);
            // 文件映射
            HookEntity hookEntity = yaml.loadAs(resourceAsStream, HookEntity.class);
            hookEntityList.add(hookEntity);
        }
        return hookEntityList;
    }
}
