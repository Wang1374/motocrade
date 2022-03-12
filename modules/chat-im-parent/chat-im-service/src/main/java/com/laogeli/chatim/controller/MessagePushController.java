package com.laogeli.chatim.controller;

import com.laogeli.chatim.api.model.MessagePush;
import com.laogeli.chatim.api.redis.service.RedisCacheService;
import com.laogeli.chatim.api.result.ResponseResult;
import com.laogeli.chatim.common.IMConstant;
import com.laogeli.chatim.service.impl.OwnWebsocketServiceImpl;
import com.laogeli.common.core.model.ResponseBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * description: hongpei-work com.xunke.chat.controller
 *
 * @author HongPei
 * @date 2021/6/9 17:37
 */
@Api(tags = "系统推送消息")
@Slf4j
@RestController
@RequestMapping(path = "/v1/chat-im/")
public class MessagePushController {

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    OwnWebsocketServiceImpl ownWebsocketService;

    @ApiOperation(value = "根据获取离线推送消息")
    @RequestMapping(path = "/getOffLinePushMsg", method = RequestMethod.GET)
    public ResponseResult getMyCustomerDetailConfirm(@RequestParam String userId, @RequestParam String client) {
        // TODO 根据用户id，查询redis中，离线消息，根据查询
        String key = String.format("%s-%s", IMConstant.CHAT_USER_UNREAD_CONTACT_PREFIX, userId + "#" + client);
        List<MessagePush> list = redisCacheService.getList(key);
        long listSize = redisCacheService.getListSize(key);
        /*//查询列表长度
        System.out.println(redisTemplate.opsForList().size(key));
        redisTemplate.opsForList().remove(key, 0, 9);
        List<Object> list = redisTemplate.opsForList().range(key, 0, 9);
        log.info("查处0-10 {}", list);*/
        redisCacheService.remove(key);
        return new ResponseResult().success().setData("data", list).setData("size", listSize);
    }

    @GetMapping("/hello")
    public String test(){
        return "hello world";
    }


    /**
     * 推送消息
     * @param employeeId
     * @param title
     * @param content
     * @param requestType
     * @return
     */
    @ApiOperation(value = "推送消息")
    @RequestMapping(path = "/pushMessage", method = RequestMethod.GET)
    public ResponseBean pushMessage(String employeeId, String title, String content, Integer requestType) {
        ownWebsocketService.sendSystemMessage(employeeId, title, content, requestType);
        return new ResponseBean("发送成功");
    }
}
