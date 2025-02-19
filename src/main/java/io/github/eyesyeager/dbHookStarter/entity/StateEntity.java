package io.github.eyesyeager.dbHookStarter.entity;

import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author artonyu
 */

@Data
@AllArgsConstructor
public class StateEntity {
    private Integer id;

    private String executor;

    private String state;

    private Date createTime;

    private Date updateTime;

    private Date deleteTime;
}
