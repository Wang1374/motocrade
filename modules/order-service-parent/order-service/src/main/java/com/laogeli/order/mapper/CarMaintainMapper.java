package com.laogeli.order.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.order.api.module.CarMaintain;
import com.laogeli.order.api.module.OilCard;
import com.laogeli.order.api.vo.VehicleCostVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 车辆维修记录mapper
 *
 * @author wang
 * @date 2020-07-19
 **/
@Mapper
public interface CarMaintainMapper extends CrudMapper<CarMaintain> {

    /* *
     * 新增车俩保养维修记录
     * @Param [carRepairList]
     * @return int
     * @Date: 2020-07-17 15:59
     */
    int addListObj(CarMaintain carMaintain);

    /**
     * 查询车辆保养费用
     *
     * @param vehicleArray      车牌号数组
     * @param belongCompaniesId 所属公司id
     * @param beginTime         开始时间
     * @param endTime           结束时间
     * @return
     */
    List<VehicleCostVo> findMaintainCost(@Param("vehicleArray") Set<String> vehicleArray,
                                         @Param("belongCompaniesId") String belongCompaniesId,
                                         @Param("beginTime") String beginTime,
                                         @Param("endTime") String endTime);

    /**
     * 查询每月车辆保养费用
     *
     * @param vehicleArray      车牌号数组
     * @param belongCompaniesId 所属公司id
     * @param year              年份
     * @return
     */
    List<VehicleCostVo> monthlyMaintainCost(@Param("vehicleArray") List<String> vehicleArray,
                                        @Param("belongCompaniesId") String belongCompaniesId,
                                        @Param("year") String year);

}
