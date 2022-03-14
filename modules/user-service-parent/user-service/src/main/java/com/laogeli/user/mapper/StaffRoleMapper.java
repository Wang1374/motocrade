package com.laogeli.user.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.user.api.module.StaffRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 员工角色mapper
 *
 * @author wang
 * @date 2020-06-03
 */
@Mapper
public interface StaffRoleMapper extends CrudMapper<StaffRole> {

    /**
     * 批量添加员工角色
     *
     * @param list
     * @return boolean
     */
    int insertForeach(List<StaffRole> list);
}
