package com.laogeli.user.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 角色
 *
 * @author wang
 * @date 2019-12-31
 */
@Data
public class Role extends BaseEntity<Role> {

    /**
     * 角色名称
     * */
    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    /**
     * 角色代码
     * */
    @NotBlank(message = "角色标识不能为空")
    private String roleCode;

    /**
     * 角色排序
     * */
    private String roleSort;

    /**
     * 数据范围（1：所有数据权限；2：本人数据权限；3：本部门数据权限；4：本部门及以下数据权限）
     * */
    private String dataScope;

    /**
     * 角色描述
     * */
    private String roleDesc;

    /**
     * 角色状态（0正常 1停用）
     * */
    private Integer status;

    /**
     * 菜单组
     * */
    private List<String> menuIds;

    /**
     * 是否默认
     */
    private Integer isDefault;
}
