package com.laogeli.order.api.vo;

import lombok.Data;

/**
 * 司机联系方式返回对象
 *
 * @author wang
 * @date 2020-07-08
 */
@Data
public class DriverVo {

    /**
     * key
     */
    private String id;

    /**
     * 在职状态
     */
    private String inState;

    /**
     * 司机姓名
     */
    private String label;

    /**
     * 司机姓名
     */
    private String value;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 司机归属
     */
    private String driverBelongState;

    /**
     * 车辆
     */
    private String vehicles;
}
