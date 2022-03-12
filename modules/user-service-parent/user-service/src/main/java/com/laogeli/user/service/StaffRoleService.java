package com.laogeli.user.service;

import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.service.CrudService;
import com.laogeli.user.api.module.Role;
import com.laogeli.user.api.module.StaffRole;
import com.laogeli.user.mapper.StaffRoleMapper;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 员工角色service
 *
 * @author yangyu
 * @date 2020-06-03
 */
@AllArgsConstructor
@Service
public class StaffRoleService extends CrudService<StaffRoleMapper, StaffRole> {

    /**
     * 查询所有角色
     *
     * @param saffRole saffRole
     * @return List
     */
    @Override
//    @Cacheable(value = "role#" + CommonConstant.CACHE_EXPIRE, key = "#saffRole.corporateIdentify")
    public List<StaffRole> findAllList(StaffRole saffRole) {
        return super.findAllList(saffRole);
    }
}
