package com.laogeli.order.api.vo;

import com.laogeli.order.api.module.Lcl;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author wang
 * @Date 2021-09-29 14:19
 **/
@Data
public class WxMakingChestVo {



    /**
     * 做箱状态
     */
    private Integer makingStatus;

    /**
     * 取消原因
     */
    private String makingReason;


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
     * 箱皮重
     */
    private Double tareWeight;

    /**
     * 总件数
     */
    private Double total;

    /**
     * 总毛重
     */
    private Double grossWeight;

    /**
     * 总体积
     */
    private Double cbm;

    /**
     * 提箱点
     */
    private String suitcasePoint;

    /**
     * 还箱点
     */
    private String returnPoint;

    /**
     * 排计划
     */
    private Date scheduleTime;

    /**
     * 做箱时间
     */
    private Date packingTime;

    /**
     * 预计做箱
     */
    private Date planPackingTime;

    /**
     * 门点简称
     */
    private String door;

    /**
     * 门点地址
     */
    private String address;

    /**
     * 运输方式 1：自做 2：外发
     */
    private int typeOfShipping;

    /**
     * 承运公司
     */
    private String carrierCompany;

    /**
     * 车辆
     */
    private String vehicle;

    /**
     * 车辆数组
     */
    private String[] vehicleList;

    /**
     * 司机
     */
    private String driver;

    /**
     * 司机数组
     */
    private String[] driverList;

    /**
     * 车辆归属,1代表 公司车辆，2代表外部车辆
     */
    private String vehicleOwnership;

    /**
     * 电话
     */
    private String phone;


    /**
     * 回执单url
     */
    private String receiptUrl;

    /**
     * 寄单状态 1 待寄 ，2 不寄 ，3 已寄
     */
    private int  sendReceipStatus;

    /**
     * 箱门URL
     */
    private String doorUrl;

    /**
     * 封条URL
     */
    private String sealUrl;

    /**
     * 设备交接单URL
     */
    private String eirUrl;

    /**
     * 签收照片URL
     */
    private String signForUrl;

    /**
     * 做箱--中的提单号
     */
    private String blNoStr;



    /**
     * 做箱备注
     */
    private String bmRemarks;

    /**
     * 操作类型
     */
    private int operationType;

    /**
     * 操作记录
     */
    private String exceptionRecordCar;

    /**
     * 订单id
     */
    private String businessOrderId;

    /**
     * 订单编号
     */
    private String orderNumber;

    /**
     * 船名航次
     */
    private String vesselAndVoyage;

    /**
     * 查询参数
     */
    private String searchValue;

}
