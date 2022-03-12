package com.laogeli.user.service;

import com.google.common.collect.Lists;
import com.laogeli.common.core.service.CrudService;
import com.laogeli.user.api.dto.UserDto;
import com.laogeli.user.api.module.StaffRole;
import com.laogeli.user.api.module.User;
import com.laogeli.user.api.module.UserAuths;
import com.laogeli.user.api.module.UserStaffRole;
import com.laogeli.user.mapper.StaffRoleMapper;
import com.laogeli.user.mapper.UserStaffRoleMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 员工角色关系Service
 * @author yangyu
 * @date 2020-06-03
 */
@AllArgsConstructor
@Service
public class UserStaffRoleService extends CrudService<UserStaffRoleMapper, UserStaffRole> {

    private final UserStaffRoleMapper userStaffRoleMapper;

    private final StaffRoleMapper staffRoleMapper;

    /**
     * 根据用户ID查询
     *
     * @param userIds 用户ID
     * @return List
     */
    public List<UserStaffRole> getByUserIds(List<String> userIds) {
        return userStaffRoleMapper.getByUserIds(userIds);
    }

    /**
     * 批量查询员工的角色
     *
     * @param users users
     * @return List
     */
    public List<StaffRole> getStaffRoles(List<User> users) {
        // 流处理获取用户ID集合，根据用户ID批量查找角色
        List<UserStaffRole> userStaffRole = userStaffRoleMapper.getByUserIds(users.stream().map(User::getId).collect(Collectors.toList()));
        List<StaffRole> staffRoleList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(userStaffRole)) {
            StaffRole staffRole = new StaffRole();
            // 获取角色ID集合，转成字节数组
            staffRole.setIds(userStaffRole.stream().map(UserStaffRole::getRoleId).distinct().toArray(String[]::new));
            // 批量查找角色
            staffRoleList = staffRoleMapper.findListById(staffRole);
        }
        return staffRoleList;
    }

    /**
     * 组装员工数据，包括账号、角色
     *
     * @param tempUser      tempUser
     * @param userAuths     userAuths
     * @param userStaffRols     userStaffRols
     * @param staffRoleList staffRoleList
     * @return UserDto
     */
    public UserDto getUserDtoByUserAndUserAuths(User tempUser, List<UserAuths> userAuths, List<UserStaffRole> userStaffRols, List<StaffRole> staffRoleList) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(tempUser, userDto);
        // 设置账号信息
        if (CollectionUtils.isNotEmpty(userAuths)) {
            userAuths.stream().filter(tempUserAuths -> tempUserAuths.getUserId().equals(tempUser.getId()))
                    .findFirst().ifPresent(tempUserAuths -> userDto.setIdentifier(tempUserAuths.getIdentifier()));
        }
        // 设置角色信息
        if (CollectionUtils.isNotEmpty(userStaffRols)) {
            List<StaffRole> userRoleList = new ArrayList<>();
            userStaffRols.stream()
                    // 过滤
                    .filter(tempUserRole -> tempUser.getId().equals(tempUserRole.getUserId()))
                    .forEach(tempUserRole -> staffRoleList.stream()
                            .filter(role -> role.getId().equals(tempUserRole.getRoleId()))
                            .forEach(userRoleList::add));
            userDto.setStaffRoleList(userRoleList);
        }
        return userDto;
    }
}
