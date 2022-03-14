package com.laogeli.gateway.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.gateway.module.Route;
import org.apache.ibatis.annotations.Mapper;

/**
 * Route mapper
 *
 * @author wang
 * @date 2019-12-31
 */
@Mapper
public interface RouteMapper extends CrudMapper<Route> {
}
