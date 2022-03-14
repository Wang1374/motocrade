package com.laogeli.order.controller;

import com.github.pagehelper.PageInfo;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.exceptions.CommonException;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.PageUtil;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.common.log.annotation.Log;
import com.laogeli.order.api.module.StorageYard;
import com.laogeli.order.service.StorageYardService;
import io.swagger.annotations.Api;
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

import java.util.concurrent.TimeUnit;

/**
 * 堆场信息管理
 *
 * @author wang
 * @date 2020-06-15
 */
@Slf4j
@AllArgsConstructor
@Api("堆场信息管理")
@RestController
@RequestMapping("/v1/storageYard")
public class StorageYardController {

    private final StorageYardService storageYardService;

    private final RedisTemplate redisTemplate;

    private final RedissonClient redissonClient;

    /**
     * 堆场信息分页查询
     *
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @param sort     sort
     * @param order    order
     * @return PageInfo
     */
    @GetMapping("storageYardList")
    @PreAuthorize("hasAuthority('order:storageyard:list')")
    @ApiOperation(value = "获取堆场信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String")
    })
    public PageInfo<StorageYard> storageYardList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                 @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                 @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                 @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                 @RequestParam(value = "searchParam", required = false, defaultValue = "") String searchParam) {
        StorageYard storageYard = new StorageYard();
        storageYard.setSearchParam(searchParam);

        return storageYardService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), storageYard);
    }

    /**
     * 新增堆场
     *
     * @param storageYard storageYard
     * @return ResponseBean
     */
    @PostMapping
    @PreAuthorize("hasAuthority('order:storageyard:add')")
    @ApiOperation(value = "新增堆场", notes = "新增堆场")
    @ApiImplicitParam(name = "storageYard", value = "堆场实体StorageYard", required = true, dataType = "StorageYard")
    @Log(value = "新增堆场", type = 0)
    public ResponseBean<Integer> addStorageYard(@RequestBody StorageYard storageYard) {
        RLock lock = redissonClient.getLock(storageYard.getName());
        boolean getLock = false;
        try {
            Integer count = 0;
            if (getLock = lock.tryLock(0, 5, TimeUnit.SECONDS)) {
                String dockKey = CommonConstant.DOCK_KEY;
                redisTemplate.delete(dockKey);
                storageYard.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
                count = storageYardService.insertStorageYard(storageYard);
            } else {
                log.info("Redisson分布式锁没有获得锁:{},ThreadName:{}", storageYard.getName(), Thread.currentThread().getName());
            }
            if (count == 0) {
                return new ResponseBean<>(count, "堆场名称已经存在");
            } else {
                return new ResponseBean<>(count, "新增成功");
            }
        } catch (InterruptedException e) {
            log.error("新增堆场失败, Redisson 获取分布式锁异常, 异常信息:{}", e);
            throw new CommonException("新增堆场失败");
        } finally {
            if (!getLock) {
                return new ResponseBean<>(-1, "请勿重复操作");
            }
            lock.unlock();
            log.info("Redisson分布式锁释放锁:{},ThreadName :{}", storageYard.getName(), Thread.currentThread().getName());
        }
    }

    /**
     * 更新堆场
     *
     * @param storageYard storageYard
     * @return ResponseBean
     */
    @PutMapping
    @PreAuthorize("hasAuthority('order:storageyard:edit')")
    @ApiOperation(value = "更新堆场信息", notes = "根据堆场id更新堆场信息")
    @ApiImplicitParam(name = "storageYard", value = "门店实体StorageYard", required = true, dataType = "StorageYard")
    @Log(value = "更新堆场", type = 0)
    public ResponseBean<Integer> updateStorageYard(@RequestBody StorageYard storageYard) {
        RLock lock = redissonClient.getLock(storageYard.getName());
        boolean getLock = false;
        try {
            Integer count = 0;
            if (getLock = lock.tryLock(0, 5, TimeUnit.SECONDS)) {
                String dockKey = CommonConstant.DOCK_KEY;
                redisTemplate.delete(dockKey);
                storageYard.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
                count = storageYardService.updateStorageYard(storageYard);
            } else {
                log.info("Redisson分布式锁没有获得锁:{},ThreadName:{}", storageYard.getName(), Thread.currentThread().getName());
            }
            if (count == 0) {
                return new ResponseBean<>(count, "堆场名称已经存在");
            } else {
                return new ResponseBean<>(count, "修改成功");
            }
        } catch (InterruptedException e) {
            log.error("更新堆场信息失败, Redisson 获取分布式锁异常, 异常信息:{}", e);
            throw new CommonException("更新堆场信息失败");
        } finally {
            if (!getLock) {
                return new ResponseBean<>(-1, "请勿重复操作");
            }
            lock.unlock();
            log.info("Redisson分布式锁释放锁:{},ThreadName :{}", storageYard.getName(), Thread.currentThread().getName());
        }
    }

    /**
     * 删除堆场
     *
     * @param id id
     * @return ResponseBean
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('order:storageyard:del')")
    @ApiOperation(value = "删除堆场", notes = "根据ID删除堆场")
    @ApiImplicitParam(name = "id", value = "堆场ID", required = true, paramType = "path")
    @Log(value = "删除堆场", type = 0)
    public ResponseBean<Boolean> deleteStorageYard(@PathVariable String id) {
        try {
            StorageYard storageYard = new StorageYard();
            storageYard.setId(id);
            String dockKey = CommonConstant.DOCK_KEY;
            redisTemplate.delete(dockKey);
            return new ResponseBean<>(storageYardService.delete(storageYard) > 0);
        } catch (Exception e) {
            log.error("删除堆场失败！", e);
            throw new CommonException("删除堆场失败！");
        }
    }
}
