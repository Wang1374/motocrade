package com.laogeli.order.service;

import com.laogeli.common.core.service.CrudService;
import com.laogeli.order.api.module.CarRoad;
import com.laogeli.order.api.module.CarTyre;
import com.laogeli.order.mapper.CarRoadCostMapper;
import com.laogeli.order.mapper.CarTyreMapper;
import org.springframework.stereotype.Service;

/**
 * @author wang
 * @Date 2021-09-09 13:45
 **/
@Service
public class CarTyreService extends CrudService<CarTyreMapper, CarTyre> {
}
