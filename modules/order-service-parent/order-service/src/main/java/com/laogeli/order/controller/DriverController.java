package com.laogeli.order.controller;

import com.github.pagehelper.PageInfo;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.exceptions.CommonException;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.PageUtil;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.common.log.annotation.Log;
import com.laogeli.order.api.module.Driver;
import com.laogeli.order.api.vo.DriverVo;
import com.laogeli.order.mapper.DriverMapper;
import com.laogeli.order.service.DriverService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 司机信息管理
 *
 * @author yangyu
 * @date 2020-06-16
 */
@Slf4j
@AllArgsConstructor
@Api("司机信息")
@RestController
@RequestMapping("/v1/driver")
public class DriverController {

    private final DriverService driverService;

    private final RedissonClient redissonClient;

    /**
     * 新增司机  同时把司机姓名+手机号   增加至往来单位
     *
     * @return ResponseBean
     */
    @PostMapping
    @PreAuthorize("hasAuthority('vehicle:driver:add')")
    @ApiOperation(value = "新增司机", notes = "新增车辆")
    @ApiImplicitParam(name = "driver", value = "司机实体Driver", dataType = "Driver")
    @Log(value = "新增司机", type = 0)
    public ResponseBean<Integer> addDriver(@RequestBody Driver driver) {
        RLock lock = redissonClient.getLock(driver.getDriverName() + "" + driver.getDriverPhone() + "" + driver.getBelongCompaniesId());
        boolean getLock = false;
        try {
            Integer count = 0;
            if (getLock = lock.tryLock(0, 5, TimeUnit.SECONDS)) {
                driver.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
                count = driverService.insertDriver(driver);
            } else {
                log.info("Redisson分布式锁没有获得锁:{},ThreadName:{}", driver.getDriverName() + "" + driver.getDriverPhone() + driver.getBelongCompaniesId(), Thread.currentThread().getName());
            }
            if (count == 0) {
                return new ResponseBean<>(count, "司机已经存在");
            } else {
                return new ResponseBean<>(count, "新增成功");
            }
        } catch (InterruptedException e) {
            log.error("新增司机失败, Redisson 获取分布式锁异常, 异常信息:{}", e);
            throw new CommonException("新增司机失败");
        } finally {
            if (!getLock) {
                return new ResponseBean<>(-1, "请勿重复操作");
            }
            lock.unlock();
            log.info("Redisson分布式锁释放锁:{},ThreadName :{}", driver.getDriverName() + driver.getPhone() + driver.getBelongCompaniesId(), Thread.currentThread().getName());
        }

    }
    /**
     * 分页查询司机信息列表
     */
    @GetMapping("/driverList")
    @PreAuthorize("hasAuthority('vehicle:driver:list')")
    @ApiOperation("查询司机信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "corporateIdentify", value = "企业标识id", dataType = "String")}
    )
    public PageInfo<Driver> listDriver(
            @RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageName,
            @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
            @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String sort,
            @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
            @RequestParam(value = "searchParam", required = false, defaultValue = "") String searchParam,
            @RequestParam(value = "corporateIdentify") String corporateIdentify,
            @RequestParam(value = "driverBelongState", required = false, defaultValue = "") String driverBelongState,
            @RequestParam(value = "inState",required = false)String inState) {

        Driver driver = new Driver();
        driver.setBelongCompaniesId(corporateIdentify);
        driver.setSearchParam(searchParam);
        driver.setDriverBelongState(driverBelongState);
        driver.setInState(inState);
        return driverService.findPage(PageUtil.pageInfo(pageName, pageSize, sort, order), driver);

    }

    /**
     * 实现根据id删除司机信息
     *
     * @param id 参数id
     * @return
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('vehicle:driver:del')")
    @ApiOperation(value = "删除司机信息", notes = "根据司机id删除司机信息")
    @ApiImplicitParam(name = "id", value = "司机id", required = true, paramType = "path")
    @Log(value = "根据id删除司机信息", type = 0)
    public ResponseBean<Boolean> deleteDriver(@PathVariable String id) {
        try {
            Driver driver = new Driver();
            driver.setId(id);
            return new ResponseBean<>(driverService.delete(driver) > 0);
        } catch (Exception e) {
            log.error("删除司机失败", e);
            throw new CommonException(e);
        }
    }

    /**
     * 根据id批量删除司机信息
     *
     * @param driver
     * @return
     */
    @PostMapping("/driverAll")
    @PreAuthorize("hasAuthority('vehicle:driver:del')")
    @ApiOperation(value = "批量删除司机信息", notes = "根据id批量删除司机")
    @ApiImplicitParam(name = "driver", value = "用户信息", dataType = "driver")
    public ResponseBean<Boolean> deleteAll(@RequestBody Driver driver) {

        Boolean success = Boolean.FALSE;
        try {
            if (driver.getIds().length > 0) {
                success = driverService.deleteAll(driver.getIds()) > 0;
                return new ResponseBean<>(success);
            }
        } catch (Exception e) {
            log.error("删除用户失败", e);
            throw new CommonException("删除用户失败");
        }
        return new ResponseBean<>(success);

    }

    /**
     * 修改司机用户信息
     *
     * @param driver 参数数据
     * @return
     */
    @PostMapping("/updateDriver")
    @PreAuthorize("hasAuthority('vehicle:driver:edit')")
    @ApiOperation(value = "修改司机信息", notes = "根据id修改司机信息")
    @ApiImplicitParam(value = "driver", name = "司机数据", required = true, dataType = "driver")
    @Log(value = "修改司机信息", type = 0)
    public ResponseBean<Integer> updateDriver(@RequestBody Driver driver) {

        RLock lock = redissonClient.getLock(driver.getDriverName() + driver.getPhone() + driver.getBelongCompaniesId());
        boolean getLock = false;
        try {
            Integer count = 0;
            if (getLock = lock.tryLock(0, 5, TimeUnit.SECONDS)) {
                driver.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
                count = driverService.updateDriver(driver);
            } else {
                log.info("Redisson分布式锁没有获得锁:{},ThreadName:{}", driver.getDriverName() + driver.getPhone() + driver.getBelongCompaniesId(), Thread.currentThread().getName());
            }
            if (count == 0) {
                return new ResponseBean<>(count, "司机已经存在");
            } else {
                return new ResponseBean<>(count, "修改成功");
            }
        } catch (InterruptedException e) {
            log.error("修改司机失败, Redisson 获取分布式锁异常, 异常信息:{}", e);
            throw new CommonException("修改司机失败");
        } finally {
            if (!getLock) {
                return new ResponseBean<>(-1, "请勿重复操作");
            }
            lock.unlock();
            log.info("Redisson分布式锁释放锁:{},ThreadName :{}", driver.getDriverName() + driver.getPhone() + driver.getBelongCompaniesId(), Thread.currentThread().getName());
        }
    }

    /**
     * 获取司机与联系方式
     *
     * @param corporateIdentify corporateIdentify
     * @return List<DriverVo>
     */
    @GetMapping("getDriverAndContact/{corporateIdentify}")
    @ApiOperation(value = "获取司机与联系方式")
    @ApiImplicitParams({@ApiImplicitParam(name = "corporateIdentify", value = "企业标识id", dataType = "String")})
    public List<DriverVo> getDriverAndContact(@PathVariable String corporateIdentify) {
        return driverService.getListById(corporateIdentify);
    }

    /**
     * 新增司机  同时把司机姓名+手机号   增加至往来单位
     * @return ResponseBean
     */
    @PostMapping("/wxDriver")
    @ApiOperation(value = "新增司机", notes = "新增司机")
    @ApiImplicitParam(name = "driver", value = "司机实体Driver", dataType = "Driver")
    public ResponseBean<Integer> addWxDriver(@RequestBody Driver driver) {
        RLock lock = redissonClient.getLock(driver.getDriverName() + "" + driver.getDriverPhone() + "" + driver.getBelongCompaniesId());
        boolean getLock = false;
        try {
            Integer count = 0;
            if (getLock = lock.tryLock(0, 5, TimeUnit.SECONDS)) {
                driver.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
                count = driverService.insertDriver(driver);
            } else {
                log.info("Redisson分布式锁没有获得锁:{},ThreadName:{}", driver.getDriverName() + "" + driver.getDriverPhone() + driver.getBelongCompaniesId(), Thread.currentThread().getName());
            }
            if (count == 0) {
                return new ResponseBean<>(count, "司机已经存在");
            } else {
                return new ResponseBean<>(count, "新增成功");
            }
        } catch (InterruptedException e) {
            log.error("新增司机失败, Redisson 获取分布式锁异常, 异常信息:{}", e);
            throw new CommonException("新增司机失败");
        } finally {
            if (!getLock) {
                return new ResponseBean<>(-1, "请勿重复操作");
            }
            lock.unlock();
            log.info("Redisson分布式锁释放锁:{},ThreadName :{}", driver.getDriverName() + driver.getPhone() + driver.getBelongCompaniesId(), Thread.currentThread().getName());
        }

    }
}
