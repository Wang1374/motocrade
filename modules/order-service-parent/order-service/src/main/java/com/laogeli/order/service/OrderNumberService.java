package com.laogeli.order.service;

import com.laogeli.common.core.service.CrudService;
import com.laogeli.order.api.module.OrderNumber;
import com.laogeli.order.mapper.OrderNumberMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 订单编号 service
 *
 * @author wang
 * @date 2020-06-18
 */
@Slf4j
@AllArgsConstructor
@Service
public class OrderNumberService extends CrudService<OrderNumberMapper, OrderNumber> {
}
