package com.laogeli.order.service;


import com.laogeli.common.core.service.CrudService;
import com.laogeli.order.api.module.CarGuarantee;
import com.laogeli.order.mapper.CarGuaranteeMapper;
import org.springframework.stereotype.Service;

/**
 * 保险费用记录service
 *
 * @author BeiFang
 * @Date 2020-10-15
 **/
@Service
public class CarGuaranteeService extends CrudService<CarGuaranteeMapper, CarGuarantee> {
}
