package com.laogeli.order.api.vo;


import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 费用返回对象
 *
 * @author yangyu
 * @date 2020-09-23
 */
@Data
@ToString(callSuper = true)
public class CostVo extends BaseEntity<CostVo> {

    /*---------------------费用信息--------------*/
    /**
     * id
     */
    private String id;

    /**
     * 企业唯一标识
     */
    private String belongCompaniesId;

    /**
     * 做箱id
     */
    private String mcId;

    /**
     * 费用所属车辆
     */
    private String vehicleCost;

    /**
     * 往来单位
     */
    private String btype;

    /**
     * 往来单位id
     */
    private String btypeId;

    /**
     * 合作伙伴： 1 固定  2 非固定
     */
    private int partner;

    /**
     * 付款人
     */
    private String payer;

    /**
     * 收费项目
     */
    private String payItems;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 数量
     */
    private BigDecimal amount;

    /**
     * 总金额
     */
    private BigDecimal totalPrice;

    /**
     * 费用状态 1：确认 2：应收开票/应付开票 3：实收/实付
     */
    private int expenseStatus;

    /**
     * 发票号
     */
    private String invoiceNo;

    /**
     * 代垫发票url
     */
    private String invoiceUrl;

    /**
     * 备注
     */
    private String remark;

    /**
     * 费用类型 1：应收费用 2：应付费用 3：其他应收费用 4：其他应付费用 5: 平台应收费用
     */
    private int costTypes;

    /**
     * 应付
     */
    private BigDecimal payTotal;

    /**
     * 车队id/也存放客户id
     */
    private String fleetId;

    /**
     * 车队名/也存放客户名
     */
    private String fleetName;

    /**
     * 创建时间
     */
    private Date createDate;

    /*-----------------做箱信息--------------------*/
    /**
     * 箱型
     */
    private String boxPile;

    /**
     * 箱号
     */
    private String caseNumber;

    /**
     * 做箱中提单号
     */
    private String blNoStr;

    /**
     * 总毛重
     */
    private Double grossWeight;

    /**
     * 提/还箱点
     */
    private String prPoint;

    /**
     * 门点地址
     */
    private String address;

    /**
     * 运输方式
     */
    private int typeOfShipping;

    /**
     * 承运公司
     */
    private String carrierCompany;

    /**
     * 门点简称
     */
    private String door;

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
     * 车辆
     */
    private String vehicle;

    /**
     * 司机
     */
    private String driver;

    /**
     * 车辆归属
     */
    private String vehicleOwnership;

    /**
     * 做箱时间
     */
    private Date packingTime;

    /**
     * 做箱开始时间
     */
    private String zxBeginTime;

    /**
     * 做箱结束时间
     */
    private String zxEndTime;

    /*------------------订单信息------------------*/

    /**
     * 逗号相隔提单号
     */
    private String blNos;

    /**
     * 订单类型 1： 海运出口 2： 海运进口 3： 空运出口 4： 空运进口
     */
    private int orderType;

    /**
     * 订单编号
     */
    private String orderNumber;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 客户id
     */
    private String customerId;

    /**
     * 客户编号
     */
    private String customerNum;

    /**
     * 船名航次
     */
    private String vesselAndVoyage;

    /**
     * 停靠码头
     */
    private String dock;

    /**
     * 接单日期
     */
    private Date orderTime;

    /**
     * 接单开始时间
     */
    private String jdBeginTime;

    /**
     * 接单结束时间
     */
    private String jdEndTime;

    /**
     * 接单开始时间
     */
    private String otBeginTime;

    /**
     * 接单结束时间
     */
    private String otEndTime;

    /**
     * 操作记录
     */
    private String exceptionRecord;

    /**
     * 下单公司名
     */
    private String companyName;

    /*--------------------导出费用-----------------*/
    /**
     * 应收合计
     */
    private BigDecimal ysTotalPrice;

    /**
     * 应收费用名目
     */
    private String ysPayItems;

    /**
     * 应付合计
     */
    private BigDecimal yfTotalPrice;

    /**
     * 应付费用名目
     */
    private String yfPayItems;

    /**
     * 其他应收合计
     */
    private BigDecimal qtYsTotalPrice;

    /**
     * 其他应收费用名目
     */
    private String qtYsPayItems;

    /**
     * 其他应收合计
     */
    private BigDecimal qtYfTotalPrice;

    /**
     * 其他应收费用名目
     */
    private String qtYfPayItems;

    /**
     * 应收
     */
    private BigDecimal achieve;

    /**
     * 应付
     */
    private BigDecimal pay;

    /**
     * 额外应收
     */
    private BigDecimal extraAchieve;

    /**
     * 额外应付
     */
    private BigDecimal extraPay;


    /**
     * 联系方式
     */
    private String contactNumber;

}
