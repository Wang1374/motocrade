package com.laogeli.user.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 用户角色关系
 *
 * @author yangyu
 * @date 2019-12-31
 */
@Data
public class UserRole extends BaseEntity<UserRole> {

    private String userId;

    private String roleId;
}
