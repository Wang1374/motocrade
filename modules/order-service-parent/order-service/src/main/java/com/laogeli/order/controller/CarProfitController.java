package com.laogeli.order.controller;

import com.github.pagehelper.PageInfo;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.utils.PageUtil;
import com.laogeli.order.api.vo.ProfitVo;
import com.laogeli.order.service.CarProfitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 车辆利润分析管理
 *
 * @author wang
 * @Date 2020-10-10
 **/
@Slf4j
@AllArgsConstructor
@Api("车辆利润分析管理")
@RestController
@RequestMapping("/v1/carProfit")
public class CarProfitController {

    private final CarProfitService carProfitService;

    /**
     * @return
     */
    @RequestMapping("/getCarProfit")
    @ApiOperation(value = "获取车辆利润")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "belongCompaniesId", value = "企业标识id", dataType = "String")
    })
    public PageInfo<ProfitVo> getCarProfit(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                           @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                           @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                           @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                           @RequestParam(value = "belongCompaniesId") String belongCompaniesId,
                                           @RequestParam(value = "beginTime", required = false, defaultValue = "") String beginTime,
                                           @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime,
                                           @RequestParam(value = "vehicle", required = false, defaultValue = "") String vehicle,
                                           @RequestParam(value = "vehicles", required = false, defaultValue = "") String vehicles,
                                           @RequestParam(value = "isManage", required = true, defaultValue = "") String isManage
    ) {
        ProfitVo profitVo = new ProfitVo();
        profitVo.setBelongCompaniesId(belongCompaniesId);
        profitVo.setBeginTime(beginTime);
        profitVo.setEndTime(endTime);
        profitVo.setVehicle(vehicle);
        profitVo.setVehicles(vehicles);
        profitVo.setIsManage(isManage);
        long t1 = System.currentTimeMillis();
        PageInfo<ProfitVo> pageInfo = carProfitService.findProfitPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), profitVo);
        long t2 = System.currentTimeMillis();
        return pageInfo;
    }

    /**
     * 根据年份获取车辆平均每月利润
     *
     * @return
     */
    @RequestMapping("/getMonthlyProfit")
    @ApiOperation(value = "根据年份获取车辆平均每月利润")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "belongCompaniesId", value = "企业标识id", dataType = "String"),
            @ApiImplicitParam(name = "year", value = "年份", dataType = "String")
    })
    public List<ProfitVo> getMonthlyProfit(@RequestParam(value = "belongCompaniesId", defaultValue = "") String belongCompaniesId,
                                           @RequestParam(value = "year", defaultValue = "") String year,
                                           @RequestParam(value = "isManage", required = false, defaultValue = "") String isManage,
                                           @RequestParam(value = "vehicles", required = false, defaultValue = "") String vehicles) {
        ProfitVo profitVo = new ProfitVo();
        profitVo.setBelongCompaniesId(belongCompaniesId);
        profitVo.setYear(year);
        profitVo.setIsManage(isManage);
        profitVo.setVehicles(vehicles);
        List<ProfitVo> profitVoList = carProfitService.findMonthlyProfit(profitVo);
        return profitVoList;
    }
}
