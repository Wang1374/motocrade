package com.laogeli.order.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.order.api.module.Vehicle;
import com.laogeli.order.api.vo.VehicleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 车辆信息mapper
 *
 * @author BeiFang
 * @date 2020-07-08
 **/
@Mapper
public interface VehicleMapper extends CrudMapper<Vehicle> {

    /**
     * 通过企业标识id 获取车辆与默认司机
     *
     * @param belongCompaniesId
     * @return List<VehicleVo>
     */
    List<VehicleVo> getListById(@Param("belongCompaniesId") String belongCompaniesId);

    /**
     * 通过车牌号查询车辆是否存在
     *
     * @param vehicle
     * @return List<Vehicle>
     */
    Vehicle getByPlageNumber(Vehicle vehicle);

    List<VehicleVo> getListByIds(Vehicle vehicle);

}
