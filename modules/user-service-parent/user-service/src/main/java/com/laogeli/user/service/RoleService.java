package com.laogeli.user.service;

import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.service.CrudService;
import com.laogeli.user.api.module.Role;
import com.laogeli.user.mapper.RoleMapper;
import com.laogeli.user.mapper.RoleMenuMapper;
import com.laogeli.user.mapper.UserRoleMapper;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色service
 *
 * @author yangyu
 * @date 2019-12-31
 */
@AllArgsConstructor
@Service
public class RoleService extends CrudService<RoleMapper, Role> {

    private final RoleMenuMapper roleMenuMapper;

    private final UserRoleMapper userRoleMapper;

    /**
     * 查询所有角色
     *
     * @param role role
     * @return List
     */
    @Override
    @Cacheable(value = "role#" + CommonConstant.CACHE_EXPIRE, key = "#role.applicationCode")
    public List<Role> findAllList(Role role) {
        return super.findAllList(role);
    }

    /**
     * 根据角色code查询
     *
     * @param role role
     * @return Role
     */
    @Cacheable(value = "role#" + CommonConstant.CACHE_EXPIRE, key = "#role.roleCode")
    public Role findByRoleCode(Role role) {
        return this.dao.findByRoleCode(role);
    }

    /**
     * 新增
     *
     * @param role
     * @return int
     */
    @Override
    @Transactional
    @CacheEvict(value = "role", key = "#role.roleCode")// 可以不要
    public int insert(Role role) {
        return super.insert(role);
    }

    /**
     * 更新
     *
     * @param role role
     * @return int
     */
    @Override
    @Transactional
    @CacheEvict(value = "role", key = "#role.roleCode")
    public int update(Role role) {
        return super.update(role);
    }

    /**
     * 删除
     *
     * @param role role
     * @return int
     */
    @Override
    @Transactional
    @CacheEvict(value = "role", key = "#role.roleCode")
    public int delete(Role role) {
        // 删除角色菜单关系
        roleMenuMapper.deleteByRoleId(role.getId());
        // 删除用户角色关系
        userRoleMapper.deleteByRoleId(role.getId());
        return super.delete(role);
    }
}
