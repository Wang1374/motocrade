package com.laogeli.order.controller;

import com.github.pagehelper.PageInfo;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.exceptions.CommonException;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.PageUtil;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.common.log.annotation.Log;
import com.laogeli.order.api.module.CarRoad;
import com.laogeli.order.mapper.CarRoadCostMapper;
import com.laogeli.order.service.CarRoadCostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 车辆过路费用记录管理
 *
 * @author BeiFang
 * @Date 2020-10-27
 **/
@RestController
@Api("过路费用记录管理")
@AllArgsConstructor
@RequestMapping("/v1/roadCost/")
@Slf4j
public class CarRoadCostController {

    private final CarRoadCostMapper carRoadCostMapper;

    private final CarRoadCostService carRoadCostService;

    /**
     * 新增车辆过路费用
     *
     * @param carRoad 车辆路费实体
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增车辆过路费用", notes = "新增车辆过路费用")
    @ApiImplicitParam(name = "carRoad", value = "车辆过路费用实体", required = false, defaultValue = "CarRoad")
    @Log(value = "新增车辆过路费用", type = 0)
    public ResponseBean<Boolean> addCarRoadCost(@RequestBody CarRoad carRoad) {
        carRoad.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
        return new ResponseBean<>(carRoadCostMapper.insert(carRoad) > 0);
    }

    /**
     * 分页查询过路费用记录
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
    public PageInfo<CarRoad> getCarRoadCost(
            @RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageName,
            @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
            @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.SORT) String sort,
            @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.ORDER) String order,
            @RequestParam(value = "corporateIdentify") String corporateIdentify,
            @RequestParam(value = "searchParam", required = false, defaultValue = "") String searchParam,
            @RequestParam(value = "vehicle", required = false, defaultValue = "") String vehicle,
            @RequestParam(value = "beginTime", required = false, defaultValue = "") String beginTime,
            @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime
    ) {

        CarRoad carRoad = new CarRoad();
        carRoad.setBelongCompaniesId(corporateIdentify);
        carRoad.setSearchParam(searchParam);
        carRoad.setVehicle(vehicle);
        carRoad.setBeginTime(beginTime);
        carRoad.setEndTime(endTime);
        return carRoadCostService.findPage(PageUtil.pageInfo(pageName, pageSize, sort, order), carRoad);
    }

    /**
     * 修改车辆过路费用记录信息
     *
     * @param carRoad 车辆过路费用
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改车辆过路费用记录信息", notes = "修改车辆过路费用记录信息")
    @ApiImplicitParam(name = "carRoad", value = "车辆过路费用记录实体", required = false, defaultValue = "carRoad")
    @Log(value = "修改车辆费用记录", type = 0)
    public ResponseBean<Boolean> updateCarRoadCost(@RequestBody CarRoad carRoad) {
        try {
            carRoad.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
            return new ResponseBean<>(carRoadCostMapper.update(carRoad) > 0);
        } catch (Exception e) {
            log.error("修改车辆维修记录失败", e);
            throw new CommonException("修改车辆维修记录失败");
        }
    }

    /**
     * 删除车辆过路费用记录
     *
     * @param id 车辆过路费用记录id
     * @return
     */
    @RequestMapping("/{id}")
    @ApiOperation(value = "删除过路费用记录", notes = "删除过路费用记录")
    @ApiImplicitParam(name = "id", value = "车辆过路费用记录id", required = true, dataType = "id")
    public ResponseBean<Boolean> deleteCarRoadCost(@PathVariable String id) {
        CarRoad carRoad = new CarRoad();
        carRoad.setId(id);
        return new ResponseBean<>(carRoadCostMapper.delete(carRoad) > 0);
    }
}
