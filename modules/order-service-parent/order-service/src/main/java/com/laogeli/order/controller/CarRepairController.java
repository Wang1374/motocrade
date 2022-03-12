package com.laogeli.order.controller;

import com.github.pagehelper.PageInfo;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.exceptions.CommonException;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.PageUtil;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.common.log.annotation.Log;
import com.laogeli.order.api.module.CarRepair;
import com.laogeli.order.mapper.CarRepairMapper;
import com.laogeli.order.service.CarRepairService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 车辆维修费用记录管理
 *
 * @author BeiFang
 * @date 2020-10-26
 */

@RestController
@Slf4j
@AllArgsConstructor
@Api("车辆维修费用记录管理")
@RequestMapping("/v1/carRepair")
public class CarRepairController {

    private final CarRepairMapper carRepairMapper;

    private final CarRepairService carRepairService;

    /**
     * 新增车辆维修记录信息
     *
     * @param carRepair
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增车辆维修记录", notes = "新增车辆维修记录信息")
    @ApiImplicitParam(name = "carRepair", value = "车辆维修记录实体", required = false, defaultValue = "carRepair")
    @Log(value = "新增车辆维修记录", type = 0)
    public ResponseBean<Boolean> addCarRepair(@RequestBody CarRepair carRepair) {
        carRepair.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
        return new ResponseBean<>(carRepairMapper.insert(carRepair) > 0);
    }

    /**
     * 分页查询维修费用详情
     *
     * @param pageName          分页页码
     * @param pageSize          分页大小
     * @param sort              排序字段
     * @param order             排序方向
     * @param corporateIdentify 企业标识id
     * @return
     */
    @GetMapping
    @ApiOperation(value = "分页查询维修费用记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "corporateIdentify", value = "企业标识id", dataType = "String")

    })
    public PageInfo<CarRepair> getCarRepair(
            @RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageName,
            @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
            @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.SORT) String sort,
            @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.ORDER) String order,
            @RequestParam(value = "corporateIdentify") String corporateIdentify,
            @RequestParam(value = "searchParam", required = false, defaultValue = "") String searchParam,
            @RequestParam(value = "vehicle", required = false, defaultValue = "") String vehicle,
            @RequestParam(value = "beginTime", required = false, defaultValue = "") String beginTime,
            @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime,
            @RequestParam(value = "repairProject",required = false,defaultValue = "")String repairProject
    ) {

        CarRepair carRepair = new CarRepair();
        carRepair.setBelongCompaniesId(corporateIdentify);
        carRepair.setSearchParam(searchParam);
        carRepair.setVehicle(vehicle);
        carRepair.setBeginTime(beginTime);
        carRepair.setEndTime(endTime);
        carRepair.setRepairProject(repairProject);
        return carRepairService.findPage(PageUtil.pageInfo(pageName, pageSize, sort, order), carRepair);
    }

    /**
     * 修改车辆维修记录信息
     *
     * @param carRepair 车辆维修记录
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改车辆维修记录", notes = "修改车辆维修记录")
    @ApiImplicitParam(name = "carRepair", value = "车辆维修记录实体", required = false, defaultValue = "carRepair")
    @Log(value = "修改车辆维修记录", type = 0)
    public ResponseBean<Boolean> updateCarRepair(@RequestBody CarRepair carRepair) {
        try {
            carRepair.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
            return new ResponseBean<>(carRepairMapper.update(carRepair) > 0);
        } catch (Exception e) {
            log.error("修改车辆维修记录失败", e);
            throw new CommonException("修改车辆维修记录失败");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseBean<Boolean> deleteCarRepair(@PathVariable String id) {
        try {
            CarRepair carRepair = new CarRepair();
            carRepair.setId(id);
            return new ResponseBean<>(carRepairMapper.delete(carRepair) > 0);
        } catch (Exception e) {
            log.error("删除车辆维修记录失败", e);
            throw new CommonException("删除车辆维修记录失败");
        }
    }
}
