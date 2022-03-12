package com.laogeli.order.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 司机信息
 *
 * @author BeiFang
 * @date 2020-06-30
 **/
@Data
@ToString(callSuper = true)
public class Driver extends BaseEntity<Driver> {

    /**
     * 司机姓名
     */
    private String driverName;

    /**
     * 司机修改前姓名
     */
    private String rawDriverName;

    /**
     * 原手机号
     */
    private String rawDriverPhone;

    /**
     * 司机归属
     */
    private String driverBelongState;
    /**
     * 司机手机号
     */
    private String driverPhone;

    /**
     * 绑定车辆
     */
    private String vehicles;

    /**
     * 紧急联系人
     */
    private String emergentPerson;

    /**
     * 紧急联系电话
     */
    private String emergentPhone;

    /**
     * 司机年龄
     */
    private String age;

    /**
     * 籍贯
     */
    private String nativePlace;

    /**
     * 身份证号码
     */
    private String idNumber;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String area;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 入职时间
     */
    private Date entryDate;

    /**
     * 入职状态
     */
    private String inState;

    /**
     * 身份证正面url
     */
    private String frontUrl;

    /**
     * 身份证反面url
     */
    private String reverseUrl;

    /**
     * 驾驶证url
     */
    private String drivingLicenceUrl;

    /**
     * 从业资格证url
     */
    private String qcUrl;

    /**
     * 所属公司名称
     */
    private String companyName;

}
