package com.laogeli.distribute.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 下单接单
 *
 * @author yangyu
 * @date 2020-11-17
 */
@Data
public class BusinessCenter extends BaseEntity<BusinessCenter> {

    /**
     * 下单编号
     */
    private String placeOrderNumber;

    /**
     * 订单类型 1： 海运出口 2： 海运进口 3： 空运出口 4： 空运进口
     */
    private Integer orderType;

    /**
     * 船名航次
     */
    private String vesselAndVoyage;

    /**
     * 提单号
     */
    private String blNos;

    /**
     * 箱型箱量
     */
    private String boxQuantity;


    /**
     * 起云港
     */
    private String portOfLoading;

    /**
     * 中转港
     */
    private String portOfDischarge;

    /**
     * 目的港
     */
    private String placeOfDelivery;

    /**
     * 停靠码头
     */
    private String dock;

    /**
     * 附件名
     */
    private String appendixName;

    /**
     * 附件url
     */
    private String appendixUrl;

    /**
     * 订单状态: 0.已取消，1 待接单，2 已接单，3.已派单 ,4.已完结
     */
    private Integer orderStatus;

    /**
     * 下单用户
     */
    private String singleUser;

    /**
     * 下单用户手机号
     */
    private String singlePhone;

    /**
     * 下单公司名
     */
    private String companyName;

    /**
     * 下单客户编号
     */
    private String customerNum;

    /**
     * 平台接单操作员
     */
    private String operator;

    /**
     * 平台接单时间
     */
    private Date operatorTime;

    /**
     * 取消原因
     */
    private String reason;

    /**
     * 取消时间
     */
    private Date cancelTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 做箱数据
     */
    private List<MakingChestCenter> makingChestCenterList;

    /**
     * 做箱id数组
     */
    private String[] mcIds;

    /**
     * 消息类型
     */
    private String msgType;
}
