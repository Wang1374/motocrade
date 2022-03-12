package com.laogeli.order.service;


import com.laogeli.common.core.service.CrudService;
import com.laogeli.order.api.module.ExtraCost;
import com.laogeli.order.api.vo.VehicleCostVo;
import com.laogeli.order.mapper.CarExtraCostMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class CarExtraCostService extends CrudService<CarExtraCostMapper, ExtraCost> {


    List<VehicleCostVo>  findCost( Set<String> vehicleArray,
                                   String belongCompaniesId,
                                 String beginTime,
                                   String endTime){
//        try{
//            TimeUnit.SECONDS.sleep(3);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        return  dao.findCost(vehicleArray,belongCompaniesId,beginTime,endTime);
    }
}
