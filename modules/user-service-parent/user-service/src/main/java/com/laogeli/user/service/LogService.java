package com.laogeli.user.service;

import com.laogeli.common.core.model.Log;
import com.laogeli.common.core.service.CrudService;
import com.laogeli.common.core.vo.LogVo;
import com.laogeli.user.mapper.LogMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 日志service
 *
 * @author yangyu
 * @date 2019-12-31
 */
@Service
public class LogService extends CrudService<LogMapper, Log> {

    /**
     * 根据订单编号获取日志信息
     *
     * @param orderNumber
     * @return List<LogVo>
     */
    public List<LogVo> getOrderLog(String orderNumber) {
        return dao.getOrderLog(orderNumber);
    }
}
