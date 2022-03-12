package com.laogeli.order.api.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 导出订单数据vo对象
 * @author
 * @Date 2020-07-31
 **/
@Data
public class OrderVo {

    private String id;

    private String orderNumber;

    private Integer orderType;

    private String customerName;

    private String customerNum;

    private String contacts;

    private String blNos;

    private String vesselAndVoyage;

    private String dock;

    private Date orderTime;

    private String boxPile;

    private String caseNumber;

    private Date packingTime;

    private String door;

    private String suitcasePoint;

    private String returnPoint;

    private String[] orderIdArray;
}
