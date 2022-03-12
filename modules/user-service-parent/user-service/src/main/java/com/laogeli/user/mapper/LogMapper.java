package com.laogeli.user.mapper;

import com.laogeli.common.core.model.Log;
import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.common.core.vo.LogVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 日志
 *
 * @author yangyu
 * @date 2019-12-31
 */
@Mapper
public interface LogMapper extends CrudMapper<Log> {

    /**
     * 根据订单编号获取日志信息
     *
     * @param orderNumber
     * @return List<LogVo>
     */
    List<LogVo> getOrderLog(@Param("orderNumber")String orderNumber);
}
