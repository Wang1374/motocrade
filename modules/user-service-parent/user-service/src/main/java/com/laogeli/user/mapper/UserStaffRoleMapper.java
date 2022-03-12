package com.laogeli.user.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.user.api.module.UserStaffRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 员工角色关系Mapper
 *
 * @author yangyu
 * @date 2020-06-03
 */
@Mapper
public interface UserStaffRoleMapper extends CrudMapper<UserStaffRole> {

    /**
     * 根据用户ID查询
     *
     * @param userId 用户ID
     * @return List
     */
    List<UserStaffRole> getByUserId(String userId);

    /**
     * 根据用户ID批量查询
     *
     * @param userIds 用户ID集合
     * @return List
     */
    List<UserStaffRole> getByUserIds(List<String> userIds);

    /**
     * 根据用户ID删除
     *
     * @param userId 用户ID
     * @return int
     */
    int deleteByUserId(String userId);

}
