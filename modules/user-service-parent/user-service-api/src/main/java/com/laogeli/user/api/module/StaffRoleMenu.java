package com.laogeli.user.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 员工角色菜单关联
 *
 * @author yangyu
 * @date 2020-06-04
 */
@Data
public class StaffRoleMenu extends BaseEntity<StaffRoleMenu> {

    private String roleId;

    private String menuId;
}