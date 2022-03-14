package com.laogeli.distribute.api.vo;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 我的订单vo
 *
 * @author wang
 * @date 2020-12-21
 */
@Data

public class MineOrderVo extends BaseEntity<MineOrderVo>{

    /**
     * 下单id
     */
    private String id;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 下单编号
     */
    private String placeOrderNumber;

    /**
     * 下单时间
     */
    private String placeOrderTime;

    /**
     * 订单状态 1:未接单  2：已接单  3：已取消
     */
    private Integer orderStatus;

    /**
     * 业务类型 1： 海运出口 2： 海运进口 3： 空运出口 4： 空运进口
     */
    private Integer orderType;

    /**
     * 提单号下单
     */
    private String blNos0;

    /**
     * 提单号接单
     */
    private String blNos1;

    /**
     * 业务编号未接单
     */
    private String customerNum;

    /**
     * 下单公司
     */
    private String companyName;

    /**
     * 船名航次下单
     */
    private String vesselAndVoyage0;

    /**
     * 船名航次接单
     */
    private String vesselAndVoyage1;

    /**
     * 箱型箱量下单
     */
    private String boxQuantity0;

    /**
     * 箱型箱量
     */
    private String boxQuantity1;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系方式
     */
    private String contactNumber;

    /**
     * 起运港下单
     */
    private String portOfLoading0;

    /**
     * 起运港接单
     */
    private String portOfLoading1;

    /**
     * 中转港下单
     */
    private String portOfDischarge0;

    /**
     * 中转港接单
     */
    private String portOfDischarge1;


    /**
     * 目的港下单
     */
    private String placeOfDelivery0;

    /**
     * 目的港接单
     */
    private String placeOfDelivery1;

    /**
     * 停靠码头下单
     */
    private String dock0;

    /**
     * 停靠码头接单
     */
    private String dock1;

    /**
     * 实际靠泊
     */
    private Date actualBerthing;

    /**
     * 计划靠泊
     */
    private Date plannedDocking;

    /**
     * 开港日期
     */
    private Date openingDate;

    /**
     * 截港日期
     */
    private Date cutOffDate;

    /**
     * 开船日期
     */
    private Date shippingDay;

    /**
     * 截单日期
     */
    private Date dueDate;

    /**
     * 船公司
     */
    private String shippingCompany;

    /**
     * 附件名
     */
    private String appendixName;

    /**
     * 附件url
     */
    private String appendixUrl;


    /**
     * 放箱时间
     */
    private Date putTheBoxTime;

    /**
     * 放箱公司
     */
    private String putTheBoxCompany;

    /**
     * 操作员
     */
    private String operator;

    /**
     * 业务员
     */
    private String salesman;

    /**
     * 接单日期
     */
    private Date orderTime;

    /**
     * 客户名
     */
    private String singleUser;

    /**
     * 下单手机号
     *
     */
    private String singlePhone;

    /**
     * 取消原因
     */
    private String reason;

    /**
     * 做箱备注
     */
    private String remark;

    /**
     * 客户名
     */
    private String customerName;



}
