package com.laogeli.order.service;

import com.laogeli.common.core.service.CrudService;
import com.laogeli.order.api.module.CarRepair;
import com.laogeli.order.mapper.CarRepairMapper;
import org.springframework.stereotype.Service;

/**
 * 车辆维修费用service
 *
 * @author BeiFang
 * @date 2020-10-26
 */
@Service
public class CarRepairService extends CrudService<CarRepairMapper, CarRepair> {
}
