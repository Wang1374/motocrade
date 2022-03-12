package com.laogeli.common.core.vo;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 角色
 *
 * @author yangyu
 * @date 2019-12-31
 */
@Data
public class RoleVo extends BaseEntity<RoleVo> {

    /**
     * 角色名称
     * */
    private String roleName;

    /**
     * 角色代码
     * */
    private String roleCode;

    /**
     * 数据范围（1：所有数据权限；2：本人数据权限；）
     * */
    private String dataScope;

    /**
     * 角色描述
     * */
    //private String roleDesc;

}
