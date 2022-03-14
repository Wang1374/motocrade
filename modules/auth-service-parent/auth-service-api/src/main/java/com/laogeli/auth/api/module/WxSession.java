package com.laogeli.auth.api.module;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @Desecription: 封装微信登录状态
 * @Param:
 * @Return:
 * @Author: wang
 * @Date: 2021/8/25 22:10
 */
@Data
@AllArgsConstructor
public class WxSession implements Serializable {

    private static final long serialVersionUID = 1L;

    private String openId;

    private String sessionKey;

}