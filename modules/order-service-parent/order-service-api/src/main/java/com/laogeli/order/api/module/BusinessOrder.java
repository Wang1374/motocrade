package com.laogeli.order.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 业务订单
 *
 * @author yangyu
 * @date 2020-06-18
 */
@Data
public class BusinessOrder extends BaseEntity<BusinessOrder> {

    /**
     * 客户id
     */

    private String customerId;

    /**
     * 外部订单所属公司id，为了方便外部订单查询
     */
    private String foreignBelongCompaniesId;

    /**
     * 附件url
     */
    private String appendixUrl;

    /**
     * 附件名字
     */
    private String appendixName;

    /**
     * 下单备注
     */
    private String placeOrderRemark;
    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 合作伙伴： 1 固定  2 非固定
     */
    private  Integer partner;

    /**
     * 车队ids
     */
    private String companyIds;

    /**
     * 客户编号
     */
    private String customerNum;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系电话
     */
    private String contactNumber;

    /**
     * 订单编号
     */
    private String orderNumber;

    /**
     * 订单类型 1： 海运出口 2： 海运进口 3： 空运出口 4： 空运进口
     */
    private int orderType;

    /**
     * 提单号
     */
    private String blNos;

    /**
     * 船名航次
     */
    private String vesselAndVoyage;

    /**
     * 计划靠泊
     */
    private Date plannedDocking;

    /**
     * 实际靠泊
     */
    private Date actualBerthing;

    /**
     * 开船日期
     */
    private Date shippingDay;

    /**
     * 船公司
     */
    private String shippingCompany;

    /**
     * 开港日期
     */
    private Date openingDate;

    /**
     * 截港日期
     */
    private Date cutOffDate;

    /**
     * 截单日期
     */
    private Date dueDate;

    /**
     * 停靠码头
     */
    private String dock;

    /**
     * 起运港
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
     * 放箱公司
     */
    private String putTheBoxCompany;

    /**
     * 放箱时间
     */
    private Date putTheBoxTime;

    /**
     * 箱型箱量
     */
    private String boxQuantity;

    /**
     * 异常记录
     */
    private String exceptionRecord;

    /**
     * 车队的操作记录
     */
    private String exceptionRecordCar;

    /**
     * 操作员
     */
    private String operator;

    /**
     * 业务员
     */
    private String salesman;

    /**
     * 接单时间
     */
    private Date orderTime;

    /**
     * 业务状态: 0: 已取消 1:操作中（未完结） 2:操作完成（已完结）
     */
    private Integer businessState;

    /**
     * 取消原因
     */
    private String reason;

    /**
     * 下单编号
     */
    private String placeOrderNumber;

    /**
     * 类型： 0.自有订单 1.平台订单
     */
    private int dispatchType;

    /**
     * 下单公司名
     */
    private String companyName;

    /**
     * 下单联系人
     */
    private String singleUser;

    /**
     * 下单联系机号
     */
    private String singlePhone;

    /**
     * 用户id
     */
    private String userId;
}
