package com.laogeli.order.api.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.*;

/**
 * 利润分析vo对象
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfitVo extends BaseEntity<ProfitVo> {

    /**
     * vo id
     */
    private String id;

    /**
     * 箱id
     */
    private String mcId;

    /**
     * 做箱状态
     */
    private Integer makingStatus;

    /**
     * 做箱原因
     */
    private String makingReason;

    /**
     * 企业唯一标识
     */
    private String belongCompaniesId;

    /**
     * 车牌号
     */
    private String vehicle;

    /**
     * 利润绑定车牌号
     */
    private String vehicleCost;

    /**
     * 司机姓名
     */
    private String driver;

    /**
     * 车牌号字符串
     */
    private String vehicles;

    /**
     * 车牌号数组
     */
    private String[] vehicleArray;

    /**
     * 是否为管理员账户
     */
    private String isManage;

    /**
     * 车牌号数组
     */
    private List<String> carArrays;

    /**
     * 往来单位
     */
    private String btype;

    /**
     * 往来单位id
     */
    private String btypeId;

    /**
     * 应收
     */
    private BigDecimal achieve;

    /**
     * 应付
     */
    private BigDecimal pay;

    /**
     * 其它应收
     */
    private BigDecimal extraAchieve;

    /**
     * 其它应付
     */
    private BigDecimal extraPay;

    /**
     * 总应收
     */
    private BigDecimal achieveSum;

    /**
     * 总应付
     */
    private BigDecimal paySum;

    /**
     * 额外总应收
     */
    private BigDecimal extraAchieveSum;

    /**
     * 额外总应付
     */
    private BigDecimal extraPaySum;

    /**
     * 油费
     */
    private BigDecimal carOilPrice;

    /**
     * 其它费用
     */
    private BigDecimal carExtraPrice;

    /**
     * 路费
     */
    private BigDecimal carRoadPrice;

    /**
     * 维修费用
     */
    private BigDecimal carRepairPrice;

    /**
     * 轮胎费用
     */
    private BigDecimal carTyrePrice;

    /**
     * 保养费用
     */
    private BigDecimal carMaintainPrice;

    /**
     * 实收
     */
    private BigDecimal realisticAchieve;

    /**
     * 实付
     */
    private BigDecimal realisticPay;

    /**
     * 额外实收
     */
    private BigDecimal realisticExtraAchieve;

    /**
     * 额外实付
     */
    private BigDecimal realisticExtraPay;

    /**
     * 提单号
     */
    private String blNos;

    /**
     * 订单编号
     */
    private String orderNumber;

    /**
     * 订单类型 1： 海运出口 2： 海运进口 3： 空运出口 4： 空运进口
     */
    private int orderType;

    /**
     * 箱号
     */
    private String caseNumber;

    /**
     * 做箱时间
     */
    private String packingTime;

    /**
     * 接单日期
     */
    private String orderTime;

    /**
     * 车辆归属,1代表 公司车辆，2代表外部车辆
     */
    private String vehicleOwnership;

    /**
     * 客户名称
     */
    private String customerName;

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
     * 提箱点
     */
    private String suitcasePoint;

    /**
     * 还箱点
     */
    private String returnPoint;

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
     * 做箱提单号
     */
    private String blNoStr;

    /**
     * 毛重
     */
    private String grossWeight;

    /**
     * 操作记录
     */
    private String exceptionRecord;

    /**
     * 接单开始时间
     */
    private String otBeginTime;

    /**
     * 接单结束时间
     */
    private String otEndTime;

    /**
     * 应收费用状态 0：应付未全 1：应付已全
     */
    private Integer ysState;


    /**
     * 应付费用状态 0：应付未全 1：应付已全
     */
    private Integer yfState;

    /**
     * 年份
     */
    private String year;

    /**
     * 一月费用
     */
    private BigDecimal Jan;

    /**
     * 二月费用
     */
    private BigDecimal Feb;

    /**
     * 三月费用
     */
    private BigDecimal Mar;

    /**
     * 四月费用
     */
    private BigDecimal Apr;

    /**
     * 五月费用
     */
    private BigDecimal May;

    /**
     * 六月费用
     */
    private BigDecimal June;

    /**
     * 七月费用
     */
    private BigDecimal July;

    /**
     * 八月费用
     */
    private BigDecimal Aug;

    /**
     * 九月费用
     */
    private BigDecimal Sept;

    /**
     * 十月费用
     */
    private BigDecimal Oct;

    /**
     * 十一费用
     */
    private BigDecimal Nov;

    /**
     * 十二费用
     */
    private BigDecimal Dece;
}
