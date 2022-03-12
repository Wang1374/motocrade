package com.laogeli.distribute.api.vo;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 客户端费用vo
 *
 * @author beifnag
 * @date 2020-12-30
 */
@Data
public class ClientCostVo extends BaseEntity<ClientCostVo> {

    /**
     * id
     */
    private String id;

    /**
     * 箱id
     */
    private String mcId;


    /**
     * 下单编号
     */
    private String placeOrderNumber;

    /**
     * 箱号
     */
    private String caseNumber;

    /**
     * 提单号
     */
    private String blNoStr;

    /**
     * 业务类型
     */
    private int orderType;

    /**
     * 费用类型 1：应收费用 2：应付费用 3：其他应收费用 4：其他应付费用
     */
    private int costTypes;

    /**
     * 箱型
     */
    private String boxPile;

    /**
     * 做箱时间
     */
    private String packingTime;

    /**
     * 下单时间
     */
    private String placeOrderTime;

    /**
     * 接单时间
     */
    private String orderTime;

    /**
     * 价格
     */
    private BigDecimal searchPrice;

    /**
     * 应付
     */
    private BigDecimal pay;

    /**
     * 下单人
     */
    private String singleUser;

    /**
     * 客户编号
     */
    private String customerNum;

    /**
     * 客户名称
     */
    private String customerName;

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
     * 门点简称
     */
    private String door;

    /**
     * 门点地址
     */
    private String address;


    /**
     * 承运公司
     */
    private String carrierCompany;

    /**
     * 毛重
     */
    private String grossWeight;

    /**
     * 平台应收状态 0：应收未全 1：应收已全
     */
    private Integer ptysState;





}
