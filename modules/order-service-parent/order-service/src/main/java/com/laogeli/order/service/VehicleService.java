package com.laogeli.order.service;

import com.laogeli.common.core.service.CrudService;
import com.laogeli.common.core.utils.StringUtils;
import com.laogeli.order.api.module.Driver;
import com.laogeli.order.api.module.Vehicle;
import com.laogeli.order.api.vo.VehicleVo;
import com.laogeli.order.mapper.DriverMapper;
import com.laogeli.order.mapper.VehicleMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 车辆信息service
 *
 * @author wang
 * @Date 2020-07-08
 **/
@Slf4j
@AllArgsConstructor
@Service
public class VehicleService extends CrudService<VehicleMapper, Vehicle> {

    private final VehicleMapper vehicleMapper;

    private final DriverMapper driverMapper;

    /**
     * 通过企业标识id 获取车辆与默认司机
     *
     * @param corporateIdentify
     * @return List<VehicleVo>
     */
    public List<VehicleVo> getListById(String corporateIdentify) {
        return dao.getListById(corporateIdentify);
    }

    public List<VehicleVo> getListByIds(Vehicle vehicle) {
        return dao.getListByIds(vehicle);
    }

    /**
     * 新增车辆
     * 1,判断车牌号是否存在
     * 2,新增
     * @param vehicle
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer insertVehicle(Vehicle vehicle) {
        Integer num = 0;
        Vehicle obj = dao.getByPlageNumber(vehicle);
        if (obj == null) {
            num = dao.insert(vehicle);
        }
        return num;
    }

//    public void bindDriver(Vehicle vehicle) {
//        if (!StringUtils.isEmpty(vehicle.getDriverName())) {
//            // 执行绑定
//            Driver driver = new Driver();
//            driver.setId(vehicle.getDriverId());
//            driver.setVehicles(vehicle.getLicensePlateNumber());
//            driverMapper.update(driver);
//        }
//    }

    /**
     * 修改
     *
     * @param vehicle
     * @return
     */
    public Integer updateVehicle(Vehicle vehicle) {
        Vehicle result = vehicleMapper.getByPlageNumber(vehicle);
        if (result == null) {
            //执行修改
            vehicleMapper.update(vehicle);
            return 1;
        } else {
            if (result.getId().equals(vehicle.getId())) {
                vehicleMapper.update(vehicle);
                return 1;
            } else {
                return 0;
            }
        }
    }
}
