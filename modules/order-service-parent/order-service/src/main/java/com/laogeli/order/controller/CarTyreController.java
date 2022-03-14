package com.laogeli.order.controller;

import com.github.pagehelper.PageInfo;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.exceptions.CommonException;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.PageUtil;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.common.log.annotation.Log;
import com.laogeli.order.api.module.CarRepair;
import com.laogeli.order.api.module.CarTyre;
import com.laogeli.order.service.CarTyreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 轮胎费用管理
 * @author wang
 * @Date 2021-09-09 13:42
 **/
@Slf4j
@AllArgsConstructor
@Api("轮胎费用管理")
@RestController
@RequestMapping("/v1/carTyre")
public class CarTyreController {

    private final CarTyreService carTyreService;

    /**
     * 新增车辆轮胎费用记录
     * @param carTyre
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增车辆轮胎费用记录", notes = "新增车辆轮胎费用记录")
    @ApiImplicitParam(name = "carTyre", value = "车辆轮胎费用记录实体", required = false, defaultValue = "carTyre")
    @Log(value = "新增车辆轮胎费用记录", type = 0)
    public ResponseBean<Boolean> addCarRepair(@RequestBody CarTyre carTyre) {
        carTyre.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
        carTyre.setExpenseStatus(0);
        carTyre.setAffirmState(false);
        carTyre.setInvoiceState(false);
        carTyre.setPaymentReceivedState(false);
        return new ResponseBean<>(carTyreService.insert(carTyre) > 0);
    }


    /**
     * 分页查询轮胎费用记录
     * @param pageName          分页页码
     * @param pageSize          分页大小
     * @param sort              排序字段
     * @param order             排序方向
     * @param corporateIdentify 企业标识id
     * @return
     */
    @GetMapping
    @ApiOperation(value = "分页查询轮胎费用记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "corporateIdentify", value = "企业标识id", dataType = "String")
    })
    public PageInfo<CarTyre> getCarRepair(
            @RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageName,
            @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
            @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.SORT) String sort,
            @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.ORDER) String order,
            @RequestParam(value = "corporateIdentify") String corporateIdentify,
            @RequestParam(value = "employeeId",required = false,defaultValue = "")String employeeId,
            @RequestParam(value = "vehicle",required = false,defaultValue = "")String vehicle,
            @RequestParam(value = "invoiceNum",required = false,defaultValue = "")String invoiceNum,
            @RequestParam(value = "beginTime", required = false, defaultValue = "") String beginTime,
            @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime
    ) {
        CarTyre carTyre = new CarTyre();
        carTyre.setBelongCompaniesId(corporateIdentify);
        carTyre.setEmployeeId(employeeId);
        carTyre.setVehicle(vehicle);
        carTyre.setInvoiceNum(invoiceNum);
        carTyre.setBeginTime(beginTime);
        carTyre.setEndTime(endTime);
        return carTyreService.findPage(PageUtil.pageInfo(pageName, pageSize, sort, order), carTyre);
    }


    /**
     * 修改车辆轮胎费用信息
     * @param carTyre 车辆轮胎费用记录
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改车辆轮胎费用信息", notes = "修改车辆轮胎费用信息")
    @ApiImplicitParam(name = "carTyre", value = "车辆轮胎费用记录实体", required = false, defaultValue = "carTyre")
    @Log(value = "修改车辆轮胎费用信息", type = 0)
    public ResponseBean<Boolean> updateCarRepair(@RequestBody CarTyre carTyre) {
        try {
            carTyre.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
            return new ResponseBean<>(carTyreService.update(carTyre) > 0);
        } catch (Exception e) {
            log.error("修改车辆轮胎费用信息失败", e);
            throw new CommonException("修改车辆轮胎费用信息失败");
        }
    }

    /**
     * 根据id删除车辆轮胎费用记录
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseBean<Boolean> deleteCarTyre(@PathVariable String id) {
        try {
            CarTyre carTyre = new CarTyre();
            carTyre.setId(id);
            return new ResponseBean<>(carTyreService.delete(carTyre) > 0);
        } catch (Exception e) {
            log.error("删除车辆维修记录失败", e);
            throw new CommonException("删除车辆维修记录失败");
        }
    }

}
