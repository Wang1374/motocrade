package com.laogeli.user.api.module;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 员工角色
 *
 * @author wang
 * @date 2020-06-03
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class StaffRole extends BaseEntity<StaffRole> {

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
     * 数据范围（1：所有数据权限；2：本人数据权限；）
     * */
    private String dataScope;

    /**
     * 企业标识
     * */
    private String corporateIdentify;

    /**
     * 菜单组
     * */
    private List<String> menuIds;
}
