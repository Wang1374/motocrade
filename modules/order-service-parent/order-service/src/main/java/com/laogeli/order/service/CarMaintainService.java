package com.laogeli.order.service;

import com.laogeli.common.core.service.CrudService;
import com.laogeli.order.api.module.CarMaintain;
import com.laogeli.order.mapper.CarMaintainMapper;
import org.springframework.stereotype.Service;

/**
 * 车辆维修service
 *
 * @author
 * @Date 2020-07-19
 **/
@Service
public class CarMaintainService extends CrudService<CarMaintainMapper, CarMaintain> {
}
