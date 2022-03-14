package com.laogeli.user.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * @author wang
 * @Date 2021-02-02 9:49
 **/
@Data
public class MessageInfo extends BaseEntity<MessageInfo> {

    /**
     * 所属公司id
     */
    private String belongCompaniesId;

    /**
     * 消息内容
     */
    private String messageContext;

    /**
     * 消息主题
     */
    private String messageTitle;
    /**
     * 发送人
     */
    private String sendUser;

    /**
     * 当前页面路由地址
     */
    private String routerAddress;

    /**
     * 阅读状态
     */
    private Integer readStatus;

    /**
     * 下单或者订单编号
     */
    private String numberParams;
}
