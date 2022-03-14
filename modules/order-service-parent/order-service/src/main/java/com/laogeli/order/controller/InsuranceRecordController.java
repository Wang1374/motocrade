package com.laogeli.order.controller;

import com.laogeli.common.core.exceptions.CommonException;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.common.log.annotation.Log;
import com.laogeli.order.api.form.ListObjectForm;
import com.laogeli.order.api.module.InsuranceDetail;
import com.laogeli.order.api.module.InsuranceRecord;
import com.laogeli.order.mapper.InsuranceRecordMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 保险记录信息管理
 *
 * @author wang
 * @Date 2020-07-20
 **/
@Slf4j
@AllArgsConstructor
@RestController
@Api("保险记录管理")
@RequestMapping("/v1/maintenance")
public class InsuranceRecordController {

    private final InsuranceRecordMapper insuranceRecordMapper;

    /* *
     *  新增保险记录
     * @Param []
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增保险记录", notes = "新增保险记录信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "insuranceRecordList", value = "车辆保险数组", required = true, dataType = "List<InsuranceRecord>"),
            @ApiImplicitParam(name = "insuranceDetailList", value = "车辆保险详情记录数组", required = true, dataType = "List<InsuranceDetail>")
    })
    @Transactional
    public ResponseBean<Boolean> addRepair(@RequestBody ListObjectForm form) {
        List<InsuranceRecord> insuranceRecordList = form.getInsuranceRecordList();
        List<InsuranceDetail> insuranceDetailList = form.getInsuranceDetails();
        Boolean success = false;

        for (InsuranceRecord insuranceRecore : insuranceRecordList
        ) {
            insuranceRecore.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
        }
        //先保存保险详情信息
        if (insuranceDetailList.size() != 0) {
            for (InsuranceDetail insuranceDetail : insuranceDetailList) {
                insuranceDetail.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
            }
            success = insuranceRecordMapper.addListDetailObj(insuranceDetailList) > 0;
        }

        //然后保存保险记录
        success = insuranceRecordMapper.addListObj(insuranceRecordList) > 0;
        return new ResponseBean<>(success);
    }

    /*
     * 根据车辆id查询保养记录列表
     * @Param  carId
     * @return  List<OilCard>
     */
    @GetMapping("getmaintenanceListList/{carId}")
    @ApiOperation(value = "查询所有车辆维修记录", notes = "根据车辆id查询所有车辆维修记录")
    @ApiImplicitParam(name = "carId", value = "车俩id", dataType = "String")
    public List<InsuranceRecord> getmaintenanceListList(@PathVariable String carId) {
        InsuranceRecord insuranceRecord = new InsuranceRecord();
        insuranceRecord.setCarId(carId);
        return insuranceRecordMapper.findList(insuranceRecord);
    }

    /*
     * 根据车辆id查询保养记录列表
     * @Param  carId
     * @return  List<OilCard>
     */
    @GetMapping("getInsuranceDetail/{insuranceDetailId}")
    @ApiOperation(value = "查询所有车辆维修记录", notes = "根据车辆id查询所有车辆维修记录")
    @ApiImplicitParam(name = "carId", value = "车俩id", dataType = "String")
    public List<InsuranceDetail> getInsuranceDetail(@PathVariable String insuranceDetailId) {
        InsuranceDetail insuranceDetail = new InsuranceDetail();
        insuranceDetail.setInsuranceDetailId(insuranceDetailId);
        return insuranceRecordMapper.findListDetail(insuranceDetail);
    }

    @PutMapping
    @ApiOperation(value = "编辑保养记录", notes = "编辑保养记录信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "insuranceRecordList", value = "车辆保险数组", required = true, dataType = "List<InsuranceRecord>"),
            @ApiImplicitParam(name = "insuranceDetailList", value = "车辆保险详情记录数组", required = true, dataType = "List<InsuranceDetail>")
    })
    public ResponseBean<Boolean> editCarOut(@RequestBody ListObjectForm form) {

        String detailId;
        List<InsuranceRecord> insuranceRecordList = form.getInsuranceRecordList();
        List<InsuranceDetail> insuranceDetailList = form.getInsuranceDetails();

        Boolean success = false;

        if (insuranceDetailList.size() > 0) {
            //根据insuranceDetailId删除保险详情
            //得到insuranceDetailId
            detailId = insuranceDetailList.get(0).getInsuranceDetailId();
            //执行删除
            success = insuranceRecordMapper.deleteDetailById(detailId) > 0;

        }

        //删除保险记录
        //1 得到保险记录的id数组
        List<String> insuranceRecordIds = insuranceRecordList.stream().map(u -> u.getId()).collect(Collectors.toList());

        //2 批量删除保养记录
        success = insuranceRecordMapper.deleteAll(insuranceRecordIds) > 0;

        //然后新增维护详情
        if (insuranceDetailList.size() != 0) {
            for (InsuranceDetail insuranceDetail : insuranceDetailList) {
                insuranceDetail.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
            }
            success = insuranceRecordMapper.addListDetailObj(insuranceDetailList) > 0;
        }
        if (insuranceRecordList.size() != 0) {
            for (InsuranceRecord insuranceRecord : insuranceRecordList) {
                insuranceRecord.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
            }
            success = insuranceRecordMapper.addListObj(insuranceRecordList) > 0;
        }
        return new ResponseBean<>(success);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除维修详情信息", notes = "根据维修详情id删除维修详情信息")
    @ApiImplicitParam(name = "id", value = "维修详情id", required = true, paramType = "path")
    @Log(value = "根据维修详情id删除维修详情信息", type = 0)
    public ResponseBean<Boolean> deleteinsuranceDetail(@PathVariable String id) {
        try {
            InsuranceDetail insuranceDetail = new InsuranceDetail();
            insuranceDetail.setId(id);
            return new ResponseBean<>(insuranceRecordMapper.deleteByDetailId(insuranceDetail) > 0);
        } catch (Exception e) {
            log.error("删除维修详情失败", e);
            throw new CommonException(e);
        }
    }

    /*
     * 根据保险详情insuranceDetailId删除记录
     * @Param
     * @return
     */
    @DeleteMapping("deleteMaintence/{insuranceDetailId}")
    @ApiOperation(value = "删除维修详情信息", notes = "根据维修详情id删除维修详情信息")
    @ApiImplicitParam(name = "id", value = "维修详情id", required = true, paramType = "path")
    @Log(value = "根据维修详情id删除维修详情信息", type = 0)
    public ResponseBean<Boolean> deleteMaintenance(@PathVariable String insuranceDetailId) {
        Boolean success = false;
        try {
            //先删除维修详情
            success = insuranceRecordMapper.deleteDetailById(insuranceDetailId) > 0;
            //再删除保养记录
            success = insuranceRecordMapper.deleteRecordById(insuranceDetailId) > 0;
            return new ResponseBean<>(success);
        } catch (Exception e) {
            log.error("删除保养记录失败", e);
            throw new CommonException(e);
        }
    }
}
