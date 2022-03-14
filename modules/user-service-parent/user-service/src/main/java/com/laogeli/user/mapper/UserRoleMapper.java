package com.laogeli.user.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.user.api.module.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 员工角色关系
 *
 * @author wang
 * @date 2020-06-03
 */
@Mapper
public interface UserRoleMapper extends CrudMapper<UserRole> {

    /**
     * 根据用户ID查询
     *
     * @param userId 用户ID
     * @return List
     */
    List<UserRole> getByUserId(String userId);

    /**
     * 根据用户ID批量查询
     *
     * @param userIds 用户ID集合
     * @return List
     */
    List<UserRole> getByUserIds(List<String> userIds);


    /**
     * 根据用户ID删除
     *
     * @param userId 用户ID
     * @return int
     */
    int deleteByUserId(String userId);

    /**
     * 根据角色ID删除
     *
     * @param roleId 角色ID
     * @return int
     */
    int deleteByRoleId(String roleId);

    /**
     * 批量插入
     *
     * @param userRoles userRoles
     * @return int
     */
    int insertBatch(List<UserRole> userRoles);
}
