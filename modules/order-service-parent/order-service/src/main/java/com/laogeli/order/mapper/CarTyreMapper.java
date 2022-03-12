package com.laogeli.order.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.order.api.module.CarRoad;
import com.laogeli.order.api.module.CarTyre;
import com.laogeli.order.api.vo.VehicleCostVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 轮胎费用mapper
 * @author beifang
 * @Date 2021-09-09 13:43
 **/
@Mapper
public interface CarTyreMapper extends CrudMapper<CarTyre> {

    /**
     * 查询车辆轮胎费用
     *
     * @param vehicleArray      车牌号数组
     * @param belongCompaniesId 所属公司id
     * @param beginTime         开始时间
     * @param endTime           结束时间
     * @return
     */
    List<VehicleCostVo> findTyreCost(@Param("vehicleArray") Set<String> vehicleArray,
                                       @Param("belongCompaniesId") String belongCompaniesId,
                                       @Param("beginTime") String beginTime,
                                       @Param("endTime") String endTime);

}
