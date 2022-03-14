package com.laogeli.order.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 做箱列表信息
 *
 * @author wang
 * @date 2020-08-18
 */
@Data
public class BoxMaking extends BaseEntity<BoxMaking> {
    /**
     * 客户名称
     */
    private String customerName;

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
     * 合作伙伴
     *
     */
    private Integer partner;

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
     * 车队平台操作记录
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
     * 业务状态
     */
    private Integer businessState;

    /**
     * 类型： 0.内部订单 1.平台订单
     */
    private int dispatchType;

    /**
     * 下单编号
     */
    private String placeOrderNumber;

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

    // ------------------------- 做箱信息 ----------------------------------------

    /**
     * 做箱id
     */
    private String mcId;

    /**
     * 做箱车队id
     */
    private String companyId;

    /**
     * 做箱状态
     */
    private Integer makingStatus;

    /**
     * 是否寄单
     */
    private Integer sendReceipStatus;

    /**
     * 取消原因
     */
    private String makingReason;

    /**
     * 逗号相隔提单号
     */
    private String blNoStr;

    /**
     * 箱型
     */
    private String boxPile;

    /**
     * 箱号
     */
    private String caseNumber;

    /**
     * 封号
     */
    private String sealNumber;

    /**
     * 提箱点
     */
    private String suitcasePoint;

    /**
     * 还箱点
     */
    private String returnPoint;

    /**
     * 门点简称
     */
    private String door;

    /**
     * 门点地址
     */
    private String address;

    /**
     * 运输方式
     */
    private Integer typeOfShipping;

    /**
     * 承运公司
     */
    private String carrierCompany;

    /**
     * 车牌
     */
    private String vehicle;

    /**
     * 车牌号数组字符串
     */
    private String vehicles;

    /**
     * 车牌号数组
     */
    private String[] vehicleArray;

    /**
     * 司机
     */
    private String driver;

    /**
     * 电话
     */
    private String phone;

    /**
     * 排计划
     */
    private Date scheduleTime;

    /**
     * 实际做箱时间
     */
    private Date packingTime;

    /**
     * 预计做箱时间
     */
    private Date planPackingTime;

    /**
     * 费用状态 0：应收未全 1：应收已全
     */
    private int ysState;

    /**
     * 费用状态 0：应付未全 1：应付已全
     */
    private int yfState;

    /**
     * 平台应收状态 0：应收未全 1：应收已全
     */
    private int ptysState;

    /**
     *箱门url
     */
    private String doorUrl;

    /**
     *封条URL
     */
    private String sealUrl;

    /**
     *设备交接单URL
     */
    private String eirUrl;

    /**
     *签收照片URL
     */
    private String signForUrl;

    /**
     * 箱皮重
     */
    private String tareWeight;

}
