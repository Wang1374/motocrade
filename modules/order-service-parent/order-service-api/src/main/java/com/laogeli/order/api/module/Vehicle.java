package com.laogeli.order.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 车辆信息
 *
 * @author BeiFang
 * @Date 2020-07-08
 **/
@Data
@ToString(callSuper = true)
public class Vehicle extends BaseEntity<Vehicle> {

    /**
     * 车牌号
     */
    private String licensePlateNumber;

    /**
     * 挂牌号
     */
    private String listingNumber;

    /**
     * 车辆使用状态
     */
    private String vehicleState;

//    /**
//     * 司机姓名
//     */
//    private String driverName;

    /**
     * 司机id
     */
    private String driverId;

//    /**
//     * 司机手机号
//     */
//    private String driverPhone;

    /**
     * 受益人
     */
    private String profitUserIds;

    /**
     * 驾驶证有效期
     */
    private Date drivingLicenseDate;

    /**
     * 车辆归属公司状态,1代表 公司车辆，2代表外部车辆
     */
    private String vehicleBelongState;

    /**
     * 车架号
     */
    private String frameNumber;

    /**
     * 发动机号
     */
    private String engineNumber;

    /**
     * 营运许可证url
     */
    private String operationPermitUrl;

    /**
     * 行驶证url
     */
    private String drivingLicenseUrl;

}
