package com.laogeli.common.core.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * logVo
 *
 * @author wang
 * @date 2019-12-31
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogVo {

    protected String id;

    /**
     * 日志标题
     */
    private String title;

    /**
     * 订单日志编号
     */
    private String orderNumberLog;

    /**
     * 创建者唯一标识
     */
    protected String creator;

    /**
     * 创建日期
     */
    protected Date createDate;

}
