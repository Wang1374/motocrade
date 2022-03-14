package com.laogeli.user.service;

import com.laogeli.common.core.service.CrudService;
import com.laogeli.user.api.module.UserRole;
import com.laogeli.user.mapper.UserRoleMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户角色关系Service
 * @author wang
 * @date 2019-12-31
 */
@AllArgsConstructor
@Service
public class UserRoleService extends CrudService<UserRoleMapper, UserRole> {

    private final UserRoleMapper userRoleMapper;

    /**
     * 根据用户ID查询
     *
     * @param userId 用户ID
     * @return List
     */
    public List<UserRole> getByUserId(String userId) {
        return userRoleMapper.getByUserId(userId);
    }

    /**
     * 根据用户ID查询
     *
     * @param userIds 用户ID
     * @return List
     */
    public List<UserRole> getByUserIds(List<String> userIds) {
        return userRoleMapper.getByUserIds(userIds);
    }

    /**
     * 批量插入
     *
     * @param userRoles userRoles
     * @return int
     */
    public int insertBatch(List<UserRole> userRoles) {
        return userRoleMapper.insertBatch(userRoles);
    }
}
