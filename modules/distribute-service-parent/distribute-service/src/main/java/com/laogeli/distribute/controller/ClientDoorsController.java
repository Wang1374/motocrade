package com.laogeli.distribute.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.exceptions.CommonException;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.DateUtils;
import com.laogeli.common.core.utils.PageUtil;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.common.log.annotation.Log;
import com.laogeli.distribute.api.module.ClientDoors;
import com.laogeli.distribute.api.vo.DoorsVo;
import com.laogeli.distribute.config.ProducerMessage;
import com.laogeli.distribute.mapper.ClientDoorsMapper;
import com.laogeli.distribute.service.ClientDoorsService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 客户端门点管理
 *
 * @author wang
 * @date 2020-12-11
 */
@Slf4j
@RestController
@RequestMapping("/v1/clientDoors")
@AllArgsConstructor
public class ClientDoorsController {

    private final ClientDoorsMapper clientDoorsMapper;

    private final RedissonClient redissonClient;

    private final ClientDoorsService clientDoorsService;

    private final RedisTemplate redisTemplate;

    private final ProducerMessage producerMessage;
    /**
     * 新增门点
     *
     * @param clientDoors clientDoors
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增门店", notes = "新增门店")
    @ApiImplicitParam(name = "doors", value = "门店实体doors", required = true, dataType = "Doors")
    @Log(value = "新增门店", type = 0)
    public ResponseBean<Integer> addDoors(@RequestBody ClientDoors clientDoors) {
        RLock lock = redissonClient.getLock(clientDoors.getDoorAs() + clientDoors.getBelongCompaniesId());
        boolean getLock = false;
        try {
            Integer count = 0;
            if (getLock = lock.tryLock(0, 5, TimeUnit.SECONDS)) {
                clientDoors.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
                count = clientDoorsService.insertDoors(clientDoors);
                String doorKey = CommonConstant.DOOR_KEY+clientDoors.getBelongCompaniesId();
                //判断有没有缓存
                if(redisTemplate.hasKey(doorKey)){
                    //删除缓存
                    redisTemplate.delete(doorKey);
                }
            } else {
                log.info("Redisson分布式锁没有获得锁:{},ThreadName:{}", clientDoors.getDoorAs() + clientDoors.getBelongCompaniesId(), Thread.currentThread().getName());
            }
            if (count == 0) {
                return new ResponseBean<>(count, "门点已经存在");
            } else {
                clientDoors.setMsgType("insertDoor");
                producerMessage.sendMsg(JSON.toJSONString(clientDoors));
                return new ResponseBean<>(count, "新增成功");
            }
        } catch (InterruptedException e) {
            log.error("新增门点信息失败, Redisson 获取分布式锁异常, 异常信息:{}", e);
            throw new CommonException("新增门点信息失败");
        } finally {
            if (!getLock) {
                return new ResponseBean<>(-1, "请勿重复操作");
            }
            lock.unlock();
            log.info("Redisson分布式锁释放锁:{},ThreadName :{}", clientDoors.getDoorAs() + clientDoors.getBelongCompaniesId(), Thread.currentThread().getName());
        }
    }

    /**
     * 更新门店
     *
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新门店信息", notes = "根据门店id更新门店信息")
    @ApiImplicitParam(name = "clientDoors", value = "门店实体ClientDoors", required = true, dataType = "ClientDoors")
    @Log(value = "更新门店", type = 0)
    public ResponseBean<Integer> updateDoors(@RequestBody ClientDoors clientDoors) {
        RLock lock = redissonClient.getLock(clientDoors.getDoorAs() + clientDoors.getBelongCompaniesId());
        boolean getLock = false;
        try {
            Integer count = 0;
            if (getLock = lock.tryLock(0, 5, TimeUnit.SECONDS)) {
                clientDoors.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
                count = clientDoorsService.updateDoors(clientDoors);
                String doorKey = CommonConstant.DOOR_KEY+clientDoors.getBelongCompaniesId();
                //判断有没有缓存
                if(redisTemplate.hasKey(doorKey)){
                    //删除缓存
                    redisTemplate.delete(doorKey);
                }
            } else {
                log.info("Redisson分布式锁没有获得锁:{},ThreadName:{}", clientDoors.getDoorAs() + clientDoors.getBelongCompaniesId(), Thread.currentThread().getName());
            }
            if (count == 0) {
                return new ResponseBean<>(count, "门点已经存在");
            } else {

                clientDoors.setMsgType("updateDoor");
                producerMessage.sendMsg(JSON.toJSONString(clientDoors));

                return new ResponseBean<>(count, "修改成功");
            }
        } catch (InterruptedException e) {
            log.error("更新门点信息失败, Redisson 获取分布式锁异常, 异常信息:{}", e);
            throw new CommonException("更新门点信息失败");
        } finally {
            if (!getLock) {
                return new ResponseBean<>(-1, "请勿重复操作");
            }
            lock.unlock();
            log.info("Redisson分布式锁释放锁:{},ThreadName :{}", clientDoors.getDoorAs() + clientDoors.getBelongCompaniesId(), Thread.currentThread().getName());
        }
    }

    /**
     * 门店信息分页查询
     *
     * @param pageNum           pageNum
     * @param pageSize          pageSize
     * @param sort              sort
     * @param order             order
     * @param corporateIdentify corporateIdentify
     * @return PageInfo
     */
    @GetMapping("doorsList")
    @ApiOperation(value = "获取门店信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "corporateIdentify", value = "企业标识id", dataType = "String")
    })
    public PageInfo<ClientDoors> doorsList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                           @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                           @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                           @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                           @RequestParam(value = "searchParam", required = false, defaultValue = "") String searchParam,
                                           @RequestParam(value = "corporateIdentify") String corporateIdentify,
                                           @RequestParam(value = "contacts", required = false) String contacts,
                                           @RequestParam(value = "contactNumber", required = false) String contactNumber,
                                           @RequestParam(value = "province", required = false) String province,
                                           @RequestParam(value = "city", required = false) String city,
                                           @RequestParam(value = "area", required = false) String area,
                                           @RequestParam(value = "address", required = false) String address) {
        ClientDoors clientDoors = new ClientDoors();
        clientDoors.setBelongCompaniesId(corporateIdentify);
        clientDoors.setSearchParam(searchParam);
        clientDoors.setContacts(contacts);
        clientDoors.setContactNumber(contactNumber);
        clientDoors.setProvince(province);
        clientDoors.setCity(city);
        clientDoors.setArea(area);
        clientDoors.setAddress(address);
        return clientDoorsService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), clientDoors);
    }


    /**
     * 删除门点
     *
     * @param id id
     * @return ResponseBean
     */
    @DeleteMapping
    @PreAuthorize("hasAuthority('order:doors:del')")
    @ApiOperation(value = "删除门店", notes = "根据ID删除门店")
    @ApiImplicitParam(name = "id", value = "门店ID", required = true, paramType = "path")
    @Log(value = "删除门店", type = 0)
    public ResponseBean<Boolean> deleteDoors(@RequestParam String id,@RequestParam String belongCompaniesId) {
        try {
            ClientDoors clientDoors = new ClientDoors();
            clientDoors.setId(id);
            int num = clientDoorsService.delete(clientDoors);
            if(num>0){
                String doorKey = CommonConstant.DOOR_KEY+belongCompaniesId;
                //判断有没有缓存
                if(redisTemplate.hasKey(doorKey)){
                    //删除缓存
                    redisTemplate.delete(doorKey);
                }
            }
            return new ResponseBean<>(num > 0);
        } catch (Exception e) {
            log.error("删除门店失败！", e);
            throw new CommonException("删除门店失败！");
        }
    }

    @RequestMapping("findAll")
    @ApiOperation(value = "查询所有门点", notes = "查询所有门点")
    @Log(value = "查询所有门点", type = 0)
    public List<DoorsVo> getAllDoors(@RequestParam String belongCompaniesId) {
        String doorKey = CommonConstant.DOOR_KEY+belongCompaniesId;
        List<DoorsVo> doorResult;
        if(redisTemplate.hasKey(doorKey)){
            //查缓存数据
            doorResult = redisTemplate.opsForList().range(doorKey, 0, -1);
        }else{
            //查数据库
            doorResult = clientDoorsMapper.findAllDoors(belongCompaniesId);
            if(doorResult.size()>0){
                //存入redis
                redisTemplate.opsForList().rightPushAll(doorKey, doorResult);
                Long expireTime = DateUtils.getSecondsApart();
                redisTemplate.expire(doorKey, expireTime * 1000, TimeUnit.MILLISECONDS);
            }


        }
        return doorResult;
    }
}
