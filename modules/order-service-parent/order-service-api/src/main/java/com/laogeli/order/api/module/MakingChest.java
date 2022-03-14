package com.laogeli.order.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 做箱信息
 *
 * @author wang
 * @date 2020-07-09
 **/
@Data
public class MakingChest extends BaseEntity<MakingChest> {

    /**
     * 订单id
     */
    private String businessOrderId;

    /**
     * 订单编号
     */
    private String orderNumber;

    /**
     * 做箱车队id
     */
    private String companyId;

    /**
     * 做箱状态
     */
    private Integer makingStatus;

    /**
     * 取消原因
     */
    private String makingReason;

    /**
     * 取消时间
     */
    private Date cancelTime;

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
     * 司机电话
     */
    private String phone;

    /**
     * 司机id
     */
    private String driverId;


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
     * 件毛体信息
     */
    private List<Lcl> lclList;

    /**
     * 做箱--中的提单号
     */
    private String blNoStr;

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
     * 做箱备注
     */
    private String bmRemarks;

    /**
     * 操作类型
     */
    private int operationType;

    /**
     * 是否派单 0: 未派单(默认) 1：已派单
     */
    private int isDispatch;

    /**
     * 是否发送过消息
     */
    private int isNews;

    /**
     * 消息发送放sendUser
     */
    private String companyName;

    /**
     * 操作记录
     */
    private String exceptionRecordCar;

    /**
     * 寄单地址
     */
    private String sendReceipAddress;




}
