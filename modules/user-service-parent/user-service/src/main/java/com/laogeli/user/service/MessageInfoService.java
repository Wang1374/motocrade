package com.laogeli.user.service;

import com.laogeli.common.core.service.CrudService;
import com.laogeli.user.api.module.MessageInfo;
import com.laogeli.user.mapper.MessageInfoMapper;
import org.springframework.stereotype.Service;

/**
 * @author beifang
 * @Date 2021-02-02 10:00
 **/
@Service
public class MessageInfoService  extends CrudService<MessageInfoMapper, MessageInfo> {
}
