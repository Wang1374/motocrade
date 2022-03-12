package com.laogeli.distribute.service;

import com.laogeli.common.core.service.CrudService;
import com.laogeli.distribute.api.vo.ClientCostVo;
import com.laogeli.distribute.mapper.ClientCostMapper;
import org.springframework.stereotype.Service;

/**
 * 客户端费用service
 */
@Service
public class ClientCostService extends CrudService<ClientCostMapper,ClientCostVo> {
}
