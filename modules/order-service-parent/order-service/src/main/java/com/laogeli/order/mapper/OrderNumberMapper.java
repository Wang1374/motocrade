package com.laogeli.order.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.order.api.module.OrderNumber;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单编号mapper
 *
 * @author yangyu
 * @date 2020-06-18
 */
@Mapper
public interface OrderNumberMapper extends CrudMapper<OrderNumber> {
}
