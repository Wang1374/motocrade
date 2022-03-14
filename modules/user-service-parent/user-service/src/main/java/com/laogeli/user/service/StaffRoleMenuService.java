package com.laogeli.user.service;

import com.laogeli.common.core.service.CrudService;
import com.laogeli.common.core.utils.IdGen;
import com.laogeli.user.api.module.StaffRoleMenu;
import com.laogeli.user.mapper.StaffRoleMenuMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 员工角色-权限关系service
 * @author wang
 * @date 2020-06-04
 */
@AllArgsConstructor
@Service
public class StaffRoleMenuService extends CrudService<StaffRoleMenuMapper, StaffRoleMenu> {

    private final StaffRoleMenuMapper staffRoleMenuMapper;

    /**
     * @param roleId  roleId
     * @param menus 菜单ID集合
     * @return int
     */
    @Transactional
    @CacheEvict(value = "menu", allEntries = true)
    public int saveStaffRoleMenus(String roleId, List<String> menus) {
        int update = -1;
        if (CollectionUtils.isNotEmpty(menus)) {
            // 删除旧的管理数据
            staffRoleMenuMapper.deleteByRoleId(roleId);
            List<StaffRoleMenu> staffRoleMenus = new ArrayList<>();
            for (String menuId : menus) {
                StaffRoleMenu staffRoleMenu = new StaffRoleMenu();
                staffRoleMenu.setId(IdGen.snowflakeId());
                staffRoleMenu.setRoleId(roleId);
                staffRoleMenu.setMenuId(menuId);
                staffRoleMenus.add(staffRoleMenu);
            }
            update = staffRoleMenuMapper.insertBatch(staffRoleMenus);
        }
        return update;
    }

}
