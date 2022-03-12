package com.laogeli.order.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.order.api.module.CarRoad;
import com.laogeli.order.api.vo.VehicleCostVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 车辆过路费用记录mapper
 *
 * @author BeiFang
 * @Date 2020-10-27
 **/
@Mapper
public interface CarRoadCostMapper extends CrudMapper<CarRoad> {

    /**
     * 查询车辆过路费用
     *
     * @param vehicleArray      车牌号数组
     * @param belongCompaniesId 所属公司id
     * @param beginTime         开始时间
     * @param endTime           结束时间
     * @return
     */
    List<VehicleCostVo> findRoadCost(@Param("vehicleArray") Set<String> vehicleArray,
                                     @Param("belongCompaniesId") String belongCompaniesId,
                                     @Param("beginTime") String beginTime,
                                     @Param("endTime") String endTime);

    /**
     * 查询每月车辆过路费用
     *
     * @param vehicleArray      车牌号数组
     * @param belongCompaniesId 所属公司id
     * @param year              年份
     * @return
     */
    List<VehicleCostVo> monthlyRoadCost(@Param("vehicleArray") List<String> vehicleArray,
                                        @Param("belongCompaniesId") String belongCompaniesId,
                                        @Param("year") String year);

}
