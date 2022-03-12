package com.laogeli.user.api.vo;

import lombok.Data;

/**
 * @author beifang
 * @Date 2021-03-09 14:40
 **/
@Data
public class EmployeeVo {

    /**
     * key
     */
    private String id;

    /**
     * 当前公司员工名
     */
    private String label;

    /**
     * 当前公司员工名
     */
    private String value;
}
