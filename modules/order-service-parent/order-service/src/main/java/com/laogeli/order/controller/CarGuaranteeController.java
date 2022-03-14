package com.laogeli.order.controller;

import com.github.pagehelper.PageInfo;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.exceptions.CommonException;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.PageUtil;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.order.api.module.CarGuarantee;
import com.laogeli.order.service.CarGuaranteeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 保险费用记录管理
 *
 * @author wang
 * @Date 2020-10-15
 **/
@RestController
@Api("保险费用记录管理")
@AllArgsConstructor
@RequestMapping("/v1/carGuarantee/")
@Slf4j
public class CarGuaranteeController {

    private final CarGuaranteeService carGuaranteeService;

    /**
     * 新增保险费用记录
     *
     * @param carGuarantee 保险记录实体
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增保险费用记录")
    @ApiImplicitParam(name = "carGuarantee", value = "保险费用记录实体", required = true, dataType = "CarGuarantee")
    public ResponseBean<Boolean> addGuarantee(@RequestBody CarGuarantee carGuarantee) {
        carGuarantee.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
        return new ResponseBean<>(carGuaranteeService.insert(carGuarantee) > 0);
    }

    /**
     * 修改保险费用记录
     *
     * @param carGuarantee 保险记录实体
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改保险费用记录")
    @ApiImplicitParam(name = "carGuarantee", value = "保险费用记录实体", required = true, dataType = "CarGuarantee")
    public ResponseBean<Boolean> editGuarantee(@RequestBody CarGuarantee carGuarantee) {
        try {
            carGuarantee.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
            return new ResponseBean<>(carGuaranteeService.update(carGuarantee) > 0);
        } catch (Exception e) {
            log.error("修改保险费用记录失败", e);
            throw new CommonException("修改保险费用记录失败");
        }

    }

    /**
     * 分页查询保险费用记录
     *
     * @param pageName          分页页码
     * @param pageSize          分页大小
     * @param sort              排序字段
     * @param order             排序方向
     * @param corporateIdentify 所属公司标识
     * @return
     */
    @GetMapping("getCarGuarantee")
    @ApiOperation(value = "分页查询保险费用记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "corporateIdentify", value = "企业标识id", dataType = "String")

    })
    public PageInfo<CarGuarantee> findPage(
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


        CarGuarantee carGuarantee = new CarGuarantee();
        carGuarantee.setBelongCompaniesId(corporateIdentify);
        carGuarantee.setSearchParam(searchParam);
        carGuarantee.setVehicle(vehicle);
        carGuarantee.setBeginTime(beginTime);
        carGuarantee.setEndTime(endTime);
        return carGuaranteeService.findPage(PageUtil.pageInfo(pageName, pageSize, sort, order), carGuarantee);
    }

    /**
     * 删除车辆保险费用记录
     *
     * @param id 保险费用记录id
     * @return
     */
    @RequestMapping("/{id}")
    @ApiOperation(value = "删除车辆保险费用记录", notes = "删除车辆保险费用记录")
    @ApiImplicitParam(name = "id", value = "车辆保险费用记录id", required = true, dataType = "id")
    public ResponseBean<Boolean> deleteCarGuarantee(@PathVariable String id) {
        CarGuarantee carGuarantee = new CarGuarantee();
        carGuarantee.setId(id);
        return new ResponseBean<>(carGuaranteeService.delete(carGuarantee) > 0);
    }
}
