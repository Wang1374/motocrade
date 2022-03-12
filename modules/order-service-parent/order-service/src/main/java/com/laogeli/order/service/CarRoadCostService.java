package com.laogeli.order.service;

import com.laogeli.common.core.service.CrudService;
import com.laogeli.order.api.module.CarRoad;
import com.laogeli.order.api.vo.VehicleCostVo;
import com.laogeli.order.mapper.CarRoadCostMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 车辆过路费用记录service
 *
 * @author BeiFang
 * @Date 2020-10-27
 **/
@Service
public class CarRoadCostService extends CrudService<CarRoadCostMapper, CarRoad> {

    List<VehicleCostVo> findRoadCost(Set<String> vehicleArray,
                                 String belongCompaniesId,
                                 String beginTime,
                                 String endTime){
//        try{
//            TimeUnit.SECONDS.sleep(3);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        return  dao.findRoadCost(vehicleArray,belongCompaniesId,beginTime,endTime);
    }
}
