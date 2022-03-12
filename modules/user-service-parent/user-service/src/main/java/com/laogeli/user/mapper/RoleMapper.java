package com.laogeli.user.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.user.api.module.Role;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色mapper
 *
 * @author yangyu
 * @date 2019-12-31
 */
@Mapper
public interface RoleMapper extends CrudMapper<Role> {

    /**
     * 根据角色code查询
     *
     * @param role role
     * @return Role
     */
    Role findByRoleCode(Role role);
}
