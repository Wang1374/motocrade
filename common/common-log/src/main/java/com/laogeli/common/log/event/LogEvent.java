package com.laogeli.common.log.event;

import com.laogeli.common.core.model.Log;
import org.springframework.context.ApplicationEvent;

/**
 * 日志事件
 *
 * @author yangyu
 * @date 2019-12-31
 */
public class LogEvent extends ApplicationEvent {
    public LogEvent(Log source) {
        super(source);
    }
}
