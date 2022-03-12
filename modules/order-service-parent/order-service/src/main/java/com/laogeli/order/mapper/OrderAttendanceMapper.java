package com.laogeli.order.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.order.api.module.OrderAttendance;
import com.laogeli.order.api.module.SendReceipAddress;
import org.apache.ibatis.annotations.Mapper;
import org.aspectj.weaver.ast.Or;
import org.springframework.core.Ordered;

import java.util.List;

/**
 * 考勤mapper
 * @author beifang
 * @Date 2021-09-26 17:09
 **/
@Mapper
public interface OrderAttendanceMapper extends CrudMapper<OrderAttendance> {

    /**
     * 查询是否存在
     * @param orderAttendance
     * @return
     */
    OrderAttendance findIsExist(OrderAttendance orderAttendance);

    /**
     * 根据箱子id删除
     * @param orderAttendance
     * @return
     */
    int deleteByMcId(OrderAttendance orderAttendance);

    OrderAttendance getEntityByDriverId(OrderAttendance dataAttendence);
}
