package com.laogeli.user.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 员工角色关系
 *
 * @author yangyu
 * @date 2020-06-03
 */
@Data
public class UserStaffRole extends BaseEntity<UserStaffRole> {

    private String userId;

    private String roleId;
}
