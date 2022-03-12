package com.laogeli.user.api.vo;

import lombok.Data;

/**
 * 车队返回对象
 * @author  BeiFang
 * @Date 2020-12-01
 */
@Data
public class MotorcadeVo {

    /**
     * key
     */
    private String id;

    /**
     * 车队名称
     */
    private String label;

    /**
     * 车队id
     */
    private String value;
}
