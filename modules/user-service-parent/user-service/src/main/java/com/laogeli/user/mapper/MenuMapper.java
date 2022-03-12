package com.laogeli.user.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.user.api.module.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单mapper
 *
 * @author yangyu
 * @date 2019-12-31
 */
@Mapper
public interface MenuMapper extends CrudMapper<Menu> {

    /**
     * 根据角色查找菜单
     *
     * @param role       角色标识
     * @return List
     */
    List<Menu> findByRole(@Param("role") String role);

    /**
     * 根据员工角色code查找菜单
     *
     * @param role       角色标识
     * @return List
     */
    List<Menu> findByStaffRole(@Param("role") String role);

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return List 选中菜单列表
     */
    List<String> selectMenuListByRoleId(String roleId);

    /**
     * 根据员工角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return List 选中菜单列表
     */
    List<String> selectMenuListByStaffRoleId(String roleId);
}
