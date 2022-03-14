package com.laogeli.user.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.user.api.module.StaffRoleMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 员工角色菜单mapper
 *
 * @author wang
 * @date 2020-06-04
 */
@Mapper
public interface StaffRoleMenuMapper extends CrudMapper<StaffRoleMenu> {

    /**
     * 根据角色ID删除
     *
     * @param roleId 角色ID
     * @return int
     */
    int deleteByRoleId(String roleId);

    /**
     * 批量保存
     *
     * @param staffRoleMenus staffRoleMenus
     * @return int
     */
    int insertBatch(List<StaffRoleMenu> staffRoleMenus);
}
