package com.laogeli.user.utils;

import com.laogeli.common.core.properties.SysProperties;
import com.laogeli.common.core.utils.SpringContextHolder;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.common.core.vo.RoleVo;
import com.laogeli.user.api.dto.UserInfoDto;
import com.laogeli.user.api.module.Role;
import com.laogeli.user.api.module.StaffRole;
import com.laogeli.user.api.module.User;
import com.laogeli.user.api.module.UserAuths;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户工具类
 *
 * @author yangyu
 * @date 2019-12-31
 */
public class UserUtils {

    /**
     * 获取User属性的map
     *
     * @return LinkedHashMap
     * @author yangyu
     * @date 2019-12-31
     */
    public static LinkedHashMap<String, String> getUserMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id", "用户id");
        map.put("identifier", "账号");
        map.put("identityType", "账号类型");
        map.put("credential", "密码");
        map.put("name", "姓名");
        map.put("phone", "联系电话");
        map.put("email", "邮箱");
        //map.put("born", "出生日期");
        map.put("remark", "备注");
        map.put("status", "状态");
        map.put("deptId", "部门ID");
        map.put("applicationCode", "系统编码");
        return map;
    }

    /**
     * Role 转 RoleVo
     *
     * @param roles roles
     * @return List
     * @author yangyu
     * @date 2019-12-31
     */
    public static List<RoleVo> rolesToVo(List<Role> roles) {
        return roles.stream().map(role -> {
            RoleVo roleVo = new RoleVo();
            roleVo.setRoleCode(role.getRoleCode());
            roleVo.setRoleName(role.getRoleName());
            roleVo.setDataScope(role.getDataScope());
            // roleVo.setRoleDesc(role.getRoleDesc());
            return roleVo;
        }).collect(Collectors.toList());
    }

    /**
     * StaffRole 转 RoleVo
     *
     * @param roles roles
     * @return List
     * @author yangyu
     * @date 2019-12-31
     */
    public static List<RoleVo> staffRolesToVo(List<StaffRole> roles) {
        return roles.stream().map(role -> {
            RoleVo roleVo = new RoleVo();
            roleVo.setRoleCode(role.getRoleCode());
            roleVo.setRoleName(role.getRoleName());
            roleVo.setDataScope(role.getDataScope());
            return roleVo;
        }).collect(Collectors.toList());
    }

    /**
     * 转DTO
     *
     * @param userInfoDto userInfoDto
     * @param user        user
     * @param userAuths   userAuths
     * @return UserInfoDto
     * @author yangyu
     * @date 2019-12-31
     */
    public static void toUserInfoDto(UserInfoDto userInfoDto, User user, UserAuths userAuths) {
        BeanUtils.copyProperties(userAuths, userInfoDto,getNullPropertyNames(userAuths));
        BeanUtils.copyProperties(user, userInfoDto,getNullPropertyNames(user));

    }

    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
    /**
     * 是否为管理员
     *
     * @param identifier identifier
     * @return boolean
     * @author yangyu
     * @date 2019-12-31
     */
    public static boolean isAdmin(String identifier) {
        SysProperties sysProperties = SpringContextHolder.getApplicationContext().getBean(SysProperties.class);
        return identifier.equals(sysProperties.getAdminUser());
    }

    /**
     * 是否为管理员
     *
     * @return boolean
     * @author yangyu
     * @date 2019-12-31
     */
    public static boolean isAdmin() {
        return isAdmin(SysUtil.getUser());
    }
}
