package com.laogeli.order.controller;

import com.github.pagehelper.PageInfo;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.exceptions.CommonException;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.DateUtils;
import com.laogeli.common.core.utils.PageUtil;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.common.log.annotation.Log;
import com.laogeli.order.api.module.Vehicle;
import com.laogeli.order.api.vo.VehicleVo;
import com.laogeli.order.service.VehicleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 车辆信息管理
 *
 * @author
 * @Date 2020-07-08
 **/
@Slf4j
@AllArgsConstructor
@Api("车辆信息")
@RestController
@RequestMapping("/v1/vehicle")
public class VehicleController {

    private final VehicleService vehicleService;

    private final RedissonClient redissonClient;



    /**
     * 新增车辆信息你
     *
     * @param vehicle
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAuthority('vehicle:car:add')")
    @ApiOperation(value = "新增车辆", notes = "新增车辆")
    @ApiImplicitParam(name = "vehicle", value = "车辆实体Vecicle", required = true, dataType = "Vehicle")
//    @Log(value = "新增车辆", type = 0)
    public ResponseBean<Integer> addVehicle(@RequestBody Vehicle vehicle) {
        RLock lock = redissonClient.getLock(vehicle.getLicensePlateNumber() + vehicle.getBelongCompaniesId());
        boolean getLock = false;
        try {
            Integer count = 0;
            if (getLock = lock.tryLock(0, 5, TimeUnit.SECONDS)) {
                vehicle.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
                count = vehicleService.insertVehicle(vehicle);
            } else {
                log.info("Redisson分布式锁没有获得锁:{},ThreadName:{}", vehicle.getLicensePlateNumber() + vehicle.getBelongCompaniesId(), Thread.currentThread().getName());
            }
            if (count == 0) {
                return new ResponseBean<>(count, "车牌号已经存在");
            } else {
                return new ResponseBean<>(count, "新增成功");
            }
        } catch (InterruptedException e) {
            log.error("新增车辆失败, Redisson 获取分布式锁异常, 异常信息:{}", e);
            throw new CommonException("新增车辆失败");
        } finally {
            if (!getLock) {
                return new ResponseBean<>(-1, "请勿重复操作");
            }
            lock.unlock();
            log.info("Redisson分布式锁释放锁:{},ThreadName :{}", vehicle.getLicensePlateNumber() + vehicle.getBelongCompaniesId(), Thread.currentThread().getName());
        }

//        vehicle.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
//        return new ResponseBean<>(vehicleService.insertVehicle(vehicle));
    }

    /**
     * 更新车辆信息
     *
     * @param vehicle
     * @return
     */
    // http://localhost/api/order/v1/vehicle/updateCar
    @PostMapping("/updateCar")
    @PreAuthorize("hasAuthority('vehicle:car:edit')")
    @ApiOperation(value = "更新车辆信息", notes = "更新车辆")
    @ApiImplicitParam(name = "vehicle", value = "车辆实体Vehicle", required = true, dataType = "Vehicle")
    @Log(value = "修改车辆", type = 0)
    public ResponseBean<Integer> updateCar(@RequestBody Vehicle vehicle) {
        RLock lock = redissonClient.getLock(vehicle.getLicensePlateNumber() + vehicle.getBelongCompaniesId());
        boolean getLock = false;
        try {
            Integer count = 0;
            if (getLock = lock.tryLock(0, 5, TimeUnit.SECONDS)) {
                vehicle.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
                count = vehicleService.updateVehicle(vehicle);
            } else {
                log.info("Redisson分布式锁没有获得锁:{},ThreadName:{}", vehicle.getLicensePlateNumber() + vehicle.getBelongCompaniesId(), Thread.currentThread().getName());
            }
            if (count == 0) {
                return new ResponseBean<>(count, "车牌号已经存在");
            } else {
                return new ResponseBean<>(count, "修改成功");
            }
        } catch (InterruptedException e) {
            log.error("更新车辆失败, Redisson 获取分布式锁异常, 异常信息:{}", e);
            throw new CommonException("更新车辆失败");
        } finally {
            if (!getLock) {
                return new ResponseBean<>(-1, "请勿重复操作");
            }
            lock.unlock();
            log.info("Redisson分布式锁释放锁:{},ThreadName :{}", vehicle.getLicensePlateNumber() + vehicle.getBelongCompaniesId(), Thread.currentThread().getName());
        }
    }

    /**
     * 分页查询车辆列表
     *
     * @param
     * @return
     */
    @GetMapping("/carList")
    @PreAuthorize("hasAuthority('vehicle:car:list')")
    @ApiOperation(value = "查询车辆", notes = "查询车辆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "corporateIdentify", value = "企业标识id", dataType = "String")

    })
    @Log(value = "查询车辆", type = 0)
    public PageInfo<Vehicle> listCars(
            @RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageName,
            @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
            @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.SORT) String sort,
            @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.ORDER) String order,
            @RequestParam(value = "corporateIdentify") String corporateIdentify,
            @RequestParam(value = "licensePlateNumber", required = false, defaultValue = "") String licensePlateNumber,
            @RequestParam(value = "driverName", required = false, defaultValue = "") String driverName,
            @RequestParam(value = "driverPhone", required = false, defaultValue = "") String driverPhone,
            @RequestParam(value = "engineNumber", required = false, defaultValue = "") String engineNumber,
            @RequestParam(value = "frameNumber", required = false, defaultValue = "") String frameNumber,
            @RequestParam(value = "listingNumber", required = false, defaultValue = "") String listingNumber,
            @RequestParam(value = "vehicleBelongState", required = false, defaultValue = "") String vehicleBelongState,
            @RequestParam(value = "vehicleState", required = false, defaultValue = "") String vehicleState,
            @RequestParam(value = "drivingLicenseDate", required = false, defaultValue = "") String drivingLicenseDate) {
        Vehicle vehicle = new Vehicle();
        vehicle.setBelongCompaniesId(corporateIdentify);
        vehicle.setLicensePlateNumber(licensePlateNumber);
        vehicle.setEngineNumber(engineNumber);
        vehicle.setFrameNumber(frameNumber);
        vehicle.setListingNumber(listingNumber);
        vehicle.setVehicleBelongState(vehicleBelongState);
        vehicle.setVehicleState(vehicleState);
        vehicle.setDrivingLicenseDate(DateUtils.strToDate(drivingLicenseDate));
        return vehicleService.findPage(PageUtil.pageInfo(pageName, pageSize, sort, order), vehicle);
    }


    /**
     * 根据id删除车辆信息
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('vehicle:car:del')")
    @ApiOperation(value = "删除车辆信息", notes = "根据id删除车辆信息")
    @ApiImplicitParam(name = "id", value = "车辆id", required = true, paramType = "path")
    @Log(value = "删除车辆信息", type = 0)
    public ResponseBean<Boolean> delCar(@PathVariable String id) {
        try {
            Vehicle vehicle = new Vehicle();
            vehicle.setId(id);
            return new ResponseBean<>(vehicleService.delete(vehicle) > 0);
        } catch (Exception e) {
            log.error("删除车辆信息失败", e);
            throw new CommonException("删除车辆信息失败");
        }
    }

    /**
     * 根据id批量删除司机信息
     *
     * @param vehicle
     * @return
     */
    @PostMapping("/deleteAllByIds")
    @PreAuthorize("hasAuthority('vehicle:car:del')")
    @ApiOperation(value = "批量删除车辆信息", notes = "根据id批量删除车辆信息")
    @ApiImplicitParam(value = "vehicle", name = "车辆实体", dataType = "vehicle")
    public ResponseBean<Boolean> deleteAllByIds(@RequestBody Vehicle vehicle) {
        Boolean success = Boolean.FALSE;
        try {
            if (vehicle.getIds().length > 0) {
                success = vehicleService.deleteAll(vehicle.getIds()) > 0;
                return new ResponseBean<>(success);
            }
        } catch (Exception e) {
            log.error("删除车辆信息失败", e);
            throw new CommonException("删除车辆信息失败");
        }
        return new ResponseBean<>(success);
    }

    /**
     * 获取车辆与默认司机
     *
     * @param corporateIdentify corporateIdentify
     * @return List<VehicleVo>
     */
    @GetMapping("getVehicleAndDriver")
    @ApiOperation(value = "获取车辆与默认司机")
    @ApiImplicitParams({@ApiImplicitParam(name = "corporateIdentify", value = "企业标识id", dataType = "String")})
    public List<VehicleVo> getVehicleAndDriver(@RequestParam(value = "corporateIdentify") String corporateIdentify,
                                               @RequestParam(value = "vehicleBelongState", required = false, defaultValue = "") String vehicleBelongState) {
        Vehicle vehicle = new Vehicle();
        vehicle.setBelongCompaniesId(corporateIdentify);
        vehicle.setVehicleBelongState(vehicleBelongState);
        return vehicleService.getListByIds(vehicle);
    }
}
