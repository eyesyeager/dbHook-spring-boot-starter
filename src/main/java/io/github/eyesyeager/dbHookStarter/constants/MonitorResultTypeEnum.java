package io.github.eyesyeager.dbHookStarter.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author artonyu
 */

@Getter
@AllArgsConstructor
public enum MonitorResultTypeEnum {

    FIELD("field"),

    ENTITY("entity"),
    ;

    private final String value;
}
