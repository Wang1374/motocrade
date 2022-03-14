package com.laogeli.order.api.vo;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wang
 * @Date 2021-07-19 15:25
 **/
@Data
public class OilVoImport extends BaseEntity<OilVoImport> {


    /**
     * 车牌号
     */
    private String vehicle;



    /**
     * 每L油价
     */
    private BigDecimal oilPrice;

    /**
     * 订单金额
     */
    private BigDecimal oilTotalPrice;

    /**
     * 服务时间
     */
    private String oilDate;


    private String oilTime;

    /**
     * 加油量
     */
    private String oilCapacity;




    /**
     * 发票号
     */
    private String invoiceNum;

}
