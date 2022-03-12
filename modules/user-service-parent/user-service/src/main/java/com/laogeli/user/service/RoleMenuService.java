package com.laogeli.user.service;

import com.laogeli.common.core.service.CrudService;
import com.laogeli.common.core.utils.IdGen;
import com.laogeli.user.api.module.RoleMenu;
import com.laogeli.user.mapper.RoleMenuMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yangyu
 * @date 2019-12-31
 */
@AllArgsConstructor
@Service
public class RoleMenuService extends CrudService<RoleMenuMapper, RoleMenu> {

	private final RoleMenuMapper roleMenuMapper;

	/**
	 * @param roleId  roleId
	 * @param menus 菜单ID集合
	 * @return int
	 */
	@Transactional
	@CacheEvict(value = "menu", allEntries = true)
	public int saveRoleMenus(String roleId, List<String> menus) {
		int update = -1;
		if (CollectionUtils.isNotEmpty(menus)) {
			// 删除旧的管理数据
			roleMenuMapper.deleteByRoleId(roleId);
			List<RoleMenu> roleMenus = new ArrayList<>();
			for (String menuId : menus) {
				RoleMenu roleMenu = new RoleMenu();
				roleMenu.setId(IdGen.snowflakeId());
				roleMenu.setRoleId(roleId);
				roleMenu.setMenuId(menuId);
				roleMenus.add(roleMenu);
			}
			update = roleMenuMapper.insertBatch(roleMenus);
		}
		return update;
	}

	/**
	 * 批量保存
	 *
	 * @param roleMenus roleMenus
	 * @return int
	 */
	@Transactional
	public int insertBatch(List<RoleMenu> roleMenus) {
		return roleMenuMapper.insertBatch(roleMenus);
	}

	/**
	 * 根据roleId查询
	 *
	 * @param roleMenu roleMenu
	 * @return List
	 */
	public List<RoleMenu> getByRoleId(RoleMenu roleMenu) {
		return roleMenuMapper.getByRoleId(roleMenu);
	}

	/**
	 * 根据menuId查询
	 *
	 * @param roleMenu roleMenu
	 * @return List
	 */
	public List<RoleMenu> getByMenuId(RoleMenu roleMenu) {
		return roleMenuMapper.getByMenuId(roleMenu);
	}

	/**
	 * 根据menuId列表查询
	 *
	 * @param roleMenus roleMenus
	 * @return List
	 */
	public List<RoleMenu> getByMenuIds(List<RoleMenu> roleMenus) {
		return roleMenuMapper.getByMenuIds(roleMenus);
	}
}
