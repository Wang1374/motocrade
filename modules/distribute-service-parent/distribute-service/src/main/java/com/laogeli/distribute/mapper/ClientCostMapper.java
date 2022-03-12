package com.laogeli.distribute.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.distribute.api.vo.ClientCostVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 客户端费用mapper
 */
@Mapper
public interface ClientCostMapper extends CrudMapper<ClientCostVo> {
}
