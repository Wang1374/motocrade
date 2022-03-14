package com.laogeli.order.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.order.api.module.Cost;
import org.apache.ibatis.annotations.Mapper;

/**
 * 车辆利润分析mapper
 *
 * @author wang
 * @date 2020-10-10
 **/
@Mapper
public interface CarProfitMapper extends CrudMapper<Cost> {
}
