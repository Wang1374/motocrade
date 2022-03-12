package com.laogeli.common.core.vo;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * @Description:
 * @author yangyu
 * @date 2019-12-31
 */
@Data
public class UserInfoVo extends BaseEntity<UserInfoVo> {

    /**
     * 登录名
     */
    private String identifier;

    /**
     * 登录姓名
     */
    private String userName;

}
