这是一款数据库数据监控工具，通过 yml 配置自定义规则去监控数据库的数据变化，并将反馈程序用以执行自定义的业务逻辑。

简而言之，你可以通过编写 yml 配置去创建定时任务，定时任务会执行配置中的 sql 去检查数据库的数据变化，若数据存在变化，则定时任务会将变化数据注入到自定义的钩子类（Hook）中，并调用自定义方法去处理变化数据。

# 快速开始

先引入最新版的 maven 包，最新版本号见：[maven](https://repo1.maven.org/maven2/io/github/eyesyeager/dbHook-spring-boot-starter)

```xml
<dependency>
    <groupId>io.github.eyesyeager</groupId>
    <artifactId>dbHook-spring-boot-starter</artifactId>
    <version>x.x.x</version>
</dependency>
```

然后在自己的数据库中创建 state 表，用于记录和恢复定时任务执行进度，表名可自定义，后续可在配置文件中更改表名指向。

```sql
-- ----------------------------
-- Table structure for db_hoot_state
-- ----------------------------
DROP TABLE IF EXISTS `db_hoot_state`;
CREATE TABLE `db_hoot_state`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `executor` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `state` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `delete_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `executor_unique`(`executor` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;
```

然后在 application 配置文件中按如下规则配置：

```yml
db-hook:
  # 自定义 hook 规则配置文件 所处文件夹路径
  # 默认 dbHook
  config-path: dbHook
  # 状态表 名字
  # 默认读取 db_hoot_state
  state-table-name: hook_state
```

至此接入完成。

# hook 配置

以下是一个钩子任务的配置：

```
# 执行器，用于配置定时任务的属性与行为
executor:
  # 执行器名，需保证全局唯一
  name: musicBackupHook
  # 定时任务执行规则
  cron: 0 0/1 * * * ?

# 监控器，用于配置数据变化的判断规则
monitor:
  # 通过执行sql判断是否存在数据变化
  judgeSql: "select * from music where create_time > ? order by create_time limit 10"
  # 程序会通过反射机制将sql结果构建成实体类进行注入
  resultType: entity
  resultClass: com.eyes.eyesspace.dbHook.entity.FieldFileHookEntity
  # 记录每次定时任务的执行状态
  state:
    enabled: true
    fieldName: create_time
    fieldType: datetime
    defaultValue: 2000-01-01 00:00:00

# 自定义业务类，若定时任务发现数据变化，会创建新的 bean，并注入变化数据，执行自定义处理逻辑
handler:
  beanName: fieldFileDbHook
  # 自定义配置参数，支持任意类型数据格式
  params:
    appName: eyesspace
```

自定义业务类需继承 AbstractHook 类：

```java
import io.github.eyesyeager.dbHookStarter.AbstractHook;


@Scope("prototype")  // handler 需配置多例模式，因为一个 handler 可能存在多个定时任务配置
@Component
public class FieldFileDbHook extends AbstractHook {

    /**
     * 程序执行入口
     * @param result 变化的数据库数据
     */
    @Override
    public void execute(List<Object> result) {
        // 执行器名
        String executorName = executorEntity.getName();
        // 自定义参数
        Map<String, String> paramMap = (Map) params;
        // 更多参数请参考 AbstractHook 成员变量
        // ......

        // 自定义业务逻辑
        // ......
    }
}
```
