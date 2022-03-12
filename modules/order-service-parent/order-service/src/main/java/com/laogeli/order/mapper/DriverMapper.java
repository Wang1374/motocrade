package com.laogeli.order.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.order.api.module.Driver;
import com.laogeli.order.api.vo.DriverVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 司机信息mapper
 *
 * @author BeiFang
 * @date 2020-07-01
 **/
@Mapper
public interface DriverMapper extends CrudMapper<Driver> {

    /**
     * 通过企业标识id 获取司机与联系方式
     *
     * @param belongCompaniesId
     * @return List<DriverVo>
     */
    List<DriverVo> getListById(@Param("belongCompaniesId") String belongCompaniesId);

    /**
     * 根据司机姓名和手机号查询司机是否存在
     *
     * @param driver
     * @return List<Driver>
     */
    Driver getByNameAndPhone(Driver driver);
}
