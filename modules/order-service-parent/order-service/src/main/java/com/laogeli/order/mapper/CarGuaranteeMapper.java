package com.laogeli.order.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.order.api.module.CarGuarantee;
import com.laogeli.order.api.module.CarMaintain;
import com.laogeli.order.api.vo.VehicleCostVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 保险费用记录mapper
 *
 * @author BeiFang
 * @Date 2020-10-15
 **/
@Mapper
public interface CarGuaranteeMapper extends CrudMapper<CarGuarantee> {
}
