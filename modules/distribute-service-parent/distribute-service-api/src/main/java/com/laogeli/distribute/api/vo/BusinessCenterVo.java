package com.laogeli.distribute.api.vo;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 派单vo
 *
 * @author ynagyu
 * @date 2020-12-22
 */
@Data
public class BusinessCenterVo extends BaseEntity<BusinessCenterVo> {

    // 订单信息----------------------

    /**
     * id
     */
    protected String id;

    /**
     * 客户编号
     */
    private String customerNum;

    /**
     * 下单公司名
     */
    private String companyName;

    /**
     * 下单用户
     */
    private String singleUser;

    /**
     * 下单用户手机号
     */
    private String singlePhone;

    /**
     * 订单编号
     */
    private String orderNumber;

    /**
     * 订单类型 1： 海运出口 2： 海运进口 3： 空运出口 4： 空运进口
     */
    private Integer orderType;

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
     * 业务状态
     */
    private Integer businessState;

    /**
     * 下单编号
     */
    private String placeOrderNumber;

    /**
     * 创建者唯一标识
     */
    protected String creator;

    /**
     * 创建日期
     */
    protected Date createDate;

    /**
     * 更新者
     */
    protected String modifier;

    /**
     * 更新日期
     */
    protected Date modifyDate;

    // 下单信息-----------

    /**
     * 下单id
     */
    private String placeId;

    /**
     * 下单用户名
     */
    private String singleName;

    /**
     * 附件名
     */
    private String appendixName;

    /**
     * 附件url
     */
    private String appendixUrl;

    /**
     * 订单状态: 1 未接单，2 已接单， 3 已取消
     */
    private Integer orderStatus;

    /**
     * 下单备注
     */
    private String remark;

    /**
     * 取消原因
     */
    private String reason;

    /**
     * 下单时间
     */
    private Date singleTime;
}
