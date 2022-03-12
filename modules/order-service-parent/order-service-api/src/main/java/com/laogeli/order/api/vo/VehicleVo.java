package com.laogeli.order.api.vo;

import lombok.Data;

/**
 * 车辆与默认司机方式返回对象
 *
 * @author yangyu
 * @date 2020-07-20
 */
@Data
public class VehicleVo {

    /**
     * key
     */
    private String id;

    /**
     * 车牌号
     */
    private String label;

    /**
     * 车牌号
     */
    private String value;

    /**
     * 挂牌号
     */
    private String listingNumber;

    /**
     * 默认司机
     */
    private String driverName;

    /**
     * 默认司机电话
     */
    private String driverPhone;

    /**
     * 车辆归属公司状态
     */
    private String vehicleBelongState;
}
