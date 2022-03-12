package com.laogeli.order.api.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ZywOrder {

    private String orderSn;

    private String orderTime;

    private int branchId;

    private String branchName;

    private int truckId;

    private String truckLicenseNumber;

    private int truckerId;

    private String truckerName;

    private String truckerMobile;

    private String cardNo;

    private int stationId;

    private String stationName;

    private int city;

    private String cityName;

    private int gunId;

    private String gunName;

    private String skuCode;

    private String skuName;

    private BigDecimal price;

    private BigDecimal litre;

    private BigDecimal amount;

    private BigDecimal paymentAmount;

    private BigDecimal rebateAmount;

    private String paymentStatus;

    private String status;

    private String machineAmount;

    private String paymentType;

    private String paymentTime;

    private String basePrice;

    private String hcOrderSn;

    private String companyId;

    private String modeType;

    private String rechargeAmount;

    private String identityId;

    private String listedPrice;
}
