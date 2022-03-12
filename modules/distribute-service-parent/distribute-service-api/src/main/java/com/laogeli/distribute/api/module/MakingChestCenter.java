package com.laogeli.distribute.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 下单接单做箱信息
 *
 * @author beifang
 * @date 2020-12-14
 */
@Data
public class MakingChestCenter extends BaseEntity<MakingChestCenter> {

    /**
     * 下单id
     */
    private String placeOrderId;

    /**
     * 预计做箱时间
     */
    private Date planPackingTime;

    /**
     * 车队id
     */
    private String companyId;

    /**
     * 箱型
     */
    private String boxPile;

    /**
     * 箱号
     */
    private String caseNumber;

    /**
     * 提单号
     */
    private String blNoStr;

    /**
     * 总件数
     */
    private String total;

    /**
     * 总毛重
     */
    private String grossWeight;

    /**
     * 总体积
     */
    private String cbm;

    /**
     * 品名
     */
    private String productNames;

    /**
     * 门点简称
     */
    private String door;

    /**
     * 门点地址
     */
    private String address;

    /**
     * 做箱要求
     */
    private String bmRemarks;

    /**
     * 件毛体
     */
    private List<LclCenter> lclList;

}
