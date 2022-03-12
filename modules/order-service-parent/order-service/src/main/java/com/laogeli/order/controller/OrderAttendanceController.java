package com.laogeli.order.controller;

import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.common.log.annotation.Log;
import com.laogeli.order.api.module.OrderAttendance;
import com.laogeli.order.service.OrderAttendanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 员工考勤
 * @author beifang
 * @Date 2021-09-26 17:04
 **/
@Slf4j
@AllArgsConstructor
@Api("员工考勤管理")
@RestController
@RequestMapping("/v1/attendance")
public class OrderAttendanceController {

    private final OrderAttendanceService orderAttendanceService;

    /**
     * 新增考勤记录
     * @param attendance
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增考勤记录", notes = "新增考勤记录")
    @ApiImplicitParam(name = "考勤记录", value = "考勤记录", required = true, dataType = "OrderAttendance")
    public ResponseBean<Boolean> addAttendance(@RequestBody OrderAttendance attendance){
        attendance.setCommonValue(SysUtil.getUser(),SysUtil.getSysCode());
        orderAttendanceService.insert(attendance);
        return new ResponseBean<>(1>0);
    }

    /**
     * 根据司机id获取考勤记录
     * @param driverId
     * @return
     */
    @GetMapping("getDataByDriverId")
    @ApiOperation(value = "根据司机id获取考勤记录", notes = "根据司机id获取考勤记录")
    @ApiImplicitParam(name = "司机id", value = "司机id", required = true, dataType = "driverId")
    public List<OrderAttendance> getDataByDriverId(
            @RequestParam(value = "driverId",required = true,defaultValue = "") String driverId,
            @RequestParam(value = "belongCompaniesId",required = true,defaultValue = "")String belongCompaniesId){
        OrderAttendance orderAttendance = new OrderAttendance();
        orderAttendance.setDriverId(driverId);
        orderAttendance.setBelongCompaniesId(belongCompaniesId);
        return orderAttendanceService.findAllList(orderAttendance);
    }
}
