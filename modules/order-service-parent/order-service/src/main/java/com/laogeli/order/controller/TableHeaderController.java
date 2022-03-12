package com.laogeli.order.controller;

import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.DateUtils;
import com.laogeli.order.api.module.TableHeader;
import com.laogeli.order.mapper.TableHeaderMapper;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Api("表头信息管理")
@AllArgsConstructor
@RestController
@RequestMapping("/v1/tableHeader")
public class TableHeaderController {

    private final TableHeaderMapper tableHeaderMapper;

    private final RedisTemplate redisTemplate;

    @RequestMapping("/getHeader")
    public List<TableHeader> getTableHeader(@RequestParam(value = "tableId", required = true, defaultValue = "") String tableId,
                                            @RequestParam(value = "userId", required = true, defaultValue = "") String userId) {
        String tableHeaderKey = CommonConstant.TABLE_HEADER_KEY + userId + ":" + tableId;
        List<TableHeader> list;
        if (redisTemplate.hasKey(tableHeaderKey)) {
            list = redisTemplate.opsForList().range(tableHeaderKey, 0, -1);
        } else {
            //根据tableId和userId查询数据库
            list = tableHeaderMapper.findTableHeader(tableId, userId);
            if (list.size() != 0) {
                redisTemplate.opsForList().rightPushAll(tableHeaderKey, list);
                Long expireTime = DateUtils.getExpireTime();
                redisTemplate.expire(tableHeaderKey, expireTime * 1000, TimeUnit.MILLISECONDS);
            }
        }
        if (list.size() == 0) {
            //没有数据查询默认值
            String tableHeaderDefaultKey = CommonConstant.TABLE_HEADER_DEFAULT_KEY + tableId;
            if (redisTemplate.hasKey(tableHeaderDefaultKey)) {
                List<TableHeader> tableHeaderList = redisTemplate.opsForList().range(tableHeaderDefaultKey, 0, -1);
                return tableHeaderList;
            } else {
                //放入缓存
                List<TableHeader> tableHeaderList = tableHeaderMapper.findHeaderDefault(tableId);
                redisTemplate.opsForList().rightPushAll(tableHeaderDefaultKey, tableHeaderList);
                Long expireTime = DateUtils.getSecondsApart();
                redisTemplate.expire(tableHeaderDefaultKey, expireTime * 1000, TimeUnit.MILLISECONDS);
                return tableHeaderList;
            }
        }
        return list;
    }

    @RequestMapping("/saveTableHeader")
    @Transactional
    public ResponseBean<Boolean> updateTableHeader(@RequestBody List<TableHeader> tableHeader) {
        Boolean success = false;
        String userId = tableHeader.get(0).getUserId();
        String tableKeyDefault = CommonConstant.TABLE_HEADER_KEY + userId + ":" + tableHeader.get(0).getTableId();
        //根据tableId先删除全部
        if (tableHeader.size() > 0) {
            success = tableHeaderMapper.deleteByTableId(tableHeader.get(0).getTableId(), tableHeader.get(0).getUserId()) > 0;
            //判断key是否存在
            if (redisTemplate.hasKey(tableKeyDefault)) {
                redisTemplate.delete(tableKeyDefault);
            }
            //再新增
            success = tableHeaderMapper.insertListTableHeader(tableHeader) > 0;
        }
        return new ResponseBean<>(success);
    }

    @RequestMapping("/insertHeader")
    public ResponseBean<Boolean> insertTableHeader(@RequestBody List<TableHeader> tableHeader) {
        Boolean success = tableHeaderMapper.insertListTableHeader(tableHeader) > 0;
        return new ResponseBean<>(success);
    }
}
