package com.laogeli.user.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 角色菜单关联
 *
 * @author yangyu
 * @date 2019-12-31
 */
@Data
public class RoleMenu extends BaseEntity<RoleMenu> {

    private String roleId;

    private String menuId;
}
