package com.laogeli.common.core.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yangyu
 * @date 2019-12-31
 */
@Data
public class SysConfigDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * fastDfs服务器的HTTP地址
     */
    private String fdfsHttpHost;

    /**
     * 上传地址
     */
    private String uploadUrl;

    /**
     * 默认头像
     */
    private String defaultAvatar;

    /**
     * 管理员账号
     */
    private String adminUser;
}
