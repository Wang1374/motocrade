package com.laogeli.order.service;

import com.laogeli.common.core.service.CrudService;
import com.laogeli.common.core.utils.DateUtils;
import com.laogeli.common.core.utils.IdGen;
import com.laogeli.common.core.utils.StringUtils;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.order.api.module.MakingChest;
import com.laogeli.order.api.module.OrderAttendance;
import com.laogeli.order.api.module.SendReceipAddress;
import com.laogeli.order.mapper.OrderAttendanceMapper;
import com.laogeli.order.mapper.SendReceipMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author beifang
 * @Date 2021-09-26 17:08
 **/
@Service
@Slf4j
@AllArgsConstructor
public class OrderAttendanceService extends CrudService<OrderAttendanceMapper, OrderAttendance> {

    private final OrderAttendanceMapper orderAttendanceMapper;

    /**
     * 新增考勤记录
     *
     * @param orderAttendance
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int insert(OrderAttendance orderAttendance) {
        int success = 0;
        // 1先根据driverId,years,months,days 删除
        success = orderAttendanceMapper.delete(orderAttendance);
        // 2 然后新增
        if (!StringUtils.isEmpty(orderAttendance.getContent())) {
            success = orderAttendanceMapper.insert(orderAttendance);
        }
        return success;
    }

    /**
     * 更新
     *
     * @param makingChests
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int updateByMakingStatus(List<MakingChest> makingChests) {

        // 过滤，提取指定字段组成新的数组，然后去重
        List<TempVo> driverList = makingChests.stream().filter(y -> y.getDriverId() != null && y.getPackingTime() != null).map(
                s -> new TempVo(s.getBelongCompaniesId(), s.getDriverId(), DateUtils.dateToStringSingle(s.getPackingTime()), s.getId())).distinct().collect(Collectors.toList());

        List<OrderAttendance> attendanceList = new ArrayList<>();
        for (TempVo vo : driverList
        ) {
            OrderAttendance orderAttendance = new OrderAttendance();
            String[] dateString = vo.getPackingTime().split("-");
            orderAttendance.setDriverId(vo.getDriverId());
            orderAttendance.setBelongCompaniesId(vo.getBelongCompaniesId());
            orderAttendance.setYears(dateString[0]);
            orderAttendance.setMonths(dateString[1]);
            orderAttendance.setDays(dateString[2]);
            orderAttendance.setMcId(vo.getMcId());
            // 2 代表出车
            orderAttendance.setContent("2");
            attendanceList.add(orderAttendance);
        }
        int success = 0;
        for (OrderAttendance dataAttendence : attendanceList
        ) {
            // 先根据mc_id 查询有没有值，
            OrderAttendance result = orderAttendanceMapper.findIsExist(dataAttendence);
            if(result!=null){
                orderAttendanceMapper.deleteByMcId(dataAttendence);
                // 然后新增
                dataAttendence.setId(IdGen.snowflakeId());
                success = orderAttendanceMapper.insert(dataAttendence);
            }else {
                // 没有值
                // 根据driverId,years,months,day,查询是否存在
                OrderAttendance obj = orderAttendanceMapper.getEntityByDriverId(dataAttendence);
                if(obj!=null){
                    success = 1;
                }else {
                    // 新增
                    dataAttendence.setId(IdGen.snowflakeId());
                    success = orderAttendanceMapper.insert(dataAttendence);
                }
            }

        }
        return success;
    }

}


@Data
@AllArgsConstructor
class TempVo {
    private String belongCompaniesId;
    private String driverId;
    private String packingTime;
    private String mcId;
}
