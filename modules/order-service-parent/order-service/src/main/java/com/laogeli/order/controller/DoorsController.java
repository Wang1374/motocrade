package com.laogeli.order.controller;

import com.github.pagehelper.PageInfo;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.exceptions.CommonException;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.PageUtil;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.common.log.annotation.Log;
import com.laogeli.order.api.module.Doors;
import com.laogeli.order.mapper.DoorsMapper;
import com.laogeli.order.service.DoorsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 门店信息管理
 *
 * @author yangyu
 * @date 2020-06-15
 */
@Slf4j
@AllArgsConstructor
@Api("门店信息管理")
@RestController
@RequestMapping("/v1/doors")
public class DoorsController {

    private final DoorsService doorsService;

    private final DoorsMapper doorsMapper;

    private final RedissonClient redissonClient;

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
    @PreAuthorize("hasAuthority('order:doors:list')")
    @ApiOperation(value = "获取门店信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "corporateIdentify", value = "企业标识id", dataType = "String")
    })
    public PageInfo<Doors> doorsList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                     @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                     @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                     @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                     @RequestParam(value = "customerName", required = false, defaultValue = "") String customerName,
                                     @RequestParam(value = "customerId",required = false,defaultValue = "")String customerId,
                                     @RequestParam(value = "searchParam", required = false, defaultValue = "") String searchParam,
                                     @RequestParam(value = "corporateIdentify") String corporateIdentify,
                                     @RequestParam(value = "contacts", required = false) String contacts,
                                     @RequestParam(value = "contactNumber", required = false) String contactNumber,
                                     @RequestParam(value = "province", required = false) String province,
                                     @RequestParam(value = "city", required = false) String city,
                                     @RequestParam(value = "area", required = false) String area,
                                     @RequestParam(value = "address", required = false) String address) {
        Doors doors = new Doors();
        doors.setBelongCompaniesId(corporateIdentify);
        doors.setCustomerName(customerName);
        doors.setCustomerId(customerId);
        doors.setSearchParam(searchParam);
        doors.setContacts(contacts);
        doors.setContactNumber(contactNumber);
        doors.setProvince(province);
        doors.setCity(city);
        doors.setArea(area);
        doors.setAddress(address);

        return doorsService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), doors);
    }

    /**
     * 新增门店
     *
     * @param doors doors
     * @return ResponseBean
     */
    @PostMapping
    @PreAuthorize("hasAuthority('order:doors:add')")
    @ApiOperation(value = "新增门店", notes = "新增门点")
    @ApiImplicitParam(name = "doors", value = "门店实体doors", required = true, dataType = "Doors")
    @Log(value = "新增门店", type = 0)
    public ResponseBean<Integer> addDoors(@RequestBody Doors doors) {
        RLock lock = redissonClient.getLock(doors.getDoorAs() + doors.getCustomerId() + doors.getBelongCompaniesId());
        boolean getLock = false;
        try {
            Integer count = 0;
            if (getLock = lock.tryLock(0, 5, TimeUnit.SECONDS)) {
                doors.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
                count = doorsService.insertDoors(doors);
            } else {
                log.info("Redisson分布式锁没有获得锁:{},ThreadName:{}", doors.getDoorAs() + doors.getCustomerId() + doors.getBelongCompaniesId(), Thread.currentThread().getName());
            }
            if (count == 0) {
                return new ResponseBean<>(count, "门点已经存在");
            } else {
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
            log.info("Redisson分布式锁释放锁:{},ThreadName :{}", doors.getDoorAs() + doors.getCustomerId() + doors.getBelongCompaniesId(), Thread.currentThread().getName());
        }
    }

    /**
     * 更新门店
     *
     * @param doors doors
     * @return ResponseBean
     */
    @PutMapping
    @PreAuthorize("hasAuthority('order:doors:edit')")
    @ApiOperation(value = "更新门店信息", notes = "根据门店id更新门店信息")
    @ApiImplicitParam(name = "doors", value = "门店实体Doors", required = true, dataType = "Doors")
    @Log(value = "更新门店", type = 0)
    public ResponseBean<Integer> updateDoors(@RequestBody Doors doors) {
        RLock lock = redissonClient.getLock(doors.getDoorAs() + doors.getCustomerId() + doors.getBelongCompaniesId());
        boolean getLock = false;
        try {
            Integer count = 0;
            if (getLock = lock.tryLock(0, 5, TimeUnit.SECONDS)) {
                doors.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
                count = doorsService.updateDoors(doors);
            } else {
                log.info("Redisson分布式锁没有获得锁:{},ThreadName:{}", doors.getDoorAs() + doors.getCustomerId() + doors.getBelongCompaniesId(), Thread.currentThread().getName());
            }
            if (count == 0) {
                return new ResponseBean<>(count, "门点已经存在");
            } else {
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
            log.info("Redisson分布式锁释放锁:{},ThreadName :{}", doors.getDoorAs() + doors.getCustomerId() + doors.getBelongCompaniesId(), Thread.currentThread().getName());
        }
    }

    /**
     * 删除门店
     *
     * @param id id
     * @return ResponseBean
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('order:doors:del')")
    @ApiOperation(value = "删除门店", notes = "根据ID删除门店")
    @ApiImplicitParam(name = "id", value = "门店ID", required = true, paramType = "path")
    @Log(value = "删除门店", type = 0)
    public ResponseBean<Boolean> deleteDoors(@PathVariable String id) {
        try {
            Doors doors = new Doors();
            doors.setId(id);
            return new ResponseBean<>(doorsService.delete(doors) > 0);
        } catch (Exception e) {
            log.error("删除门店失败！", e);
            throw new CommonException("删除门店失败！");
        }
    }

    @RequestMapping("doorsWay/{customerId}")
    @ApiOperation(value = "查询门点信息", notes = "根据customerId查询门点信息")
    @ApiImplicitParam(name = "customerId", value = "客户id", required = true, paramType = "String")
    public List<Doors> findBycustomerId(@PathVariable String customerId) {
        return doorsMapper.findListByCustomerId(customerId);
    }
}
