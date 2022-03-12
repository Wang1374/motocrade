package com.laogeli.distribute.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.distribute.api.module.ClientDoors;
import com.laogeli.distribute.api.vo.DoorsVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.*;

/**
 * 客户端门点mapper
 *
 * @author beifang
 * @date 220-12-11
 */
@Mapper
public interface ClientDoorsMapper extends CrudMapper<ClientDoors> {

    /**
     * 查询所有的门点
     *
     * @return
     */
    List<DoorsVo> findAllDoors(String belongCompaniesId);
}
