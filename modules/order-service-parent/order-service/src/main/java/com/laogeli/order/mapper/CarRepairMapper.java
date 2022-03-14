package com.laogeli.order.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.order.api.module.CarRepair;
import com.laogeli.order.api.vo.VehicleCostVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 车辆维修费用mapper
 *
 * @author wang
 * @date 2020-10-26
 */
@Mapper
public interface CarRepairMapper extends CrudMapper<CarRepair> {

    /**
     * 查询车辆维修费用
     *
     * @param vehicleArray      车牌号数组
     * @param belongCompaniesId 所属公司id
     * @param beginTime         开始时间
     * @param endTime           结束时间
     * @return
     */
    List<VehicleCostVo> findRepairCost(@Param("vehicleArray") Set<String> vehicleArray,
                                       @Param("belongCompaniesId") String belongCompaniesId,
                                       @Param("beginTime") String beginTime,
                                       @Param("endTime") String endTime);


    /**
     * 查询车辆维修记录  每月
     *
     * @param vehicleArray      车牌号数组
     * @param belongCompaniesId 所属公司id
     * @param year              年份
     * @return
     */
    List<VehicleCostVo> monthlyRepairCost(@Param("vehicleArray") List<String> vehicleArray,
                                       @Param("belongCompaniesId") String belongCompaniesId,
                                       @Param("year") String year);

}
