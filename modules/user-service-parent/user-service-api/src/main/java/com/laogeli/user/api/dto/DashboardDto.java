package com.laogeli.user.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * DashboardDto
 *
 * @author wang
 * @date 2019-12-31
 */
@Data
public class DashboardDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 在线用户数量
     */
    private String onlineUserNumber;

    /**
     * 参与人数
     */
    private String examUserNumber;
}
