package com.laogeli.order.controller;

import com.github.pagehelper.PageInfo;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.PageUtil;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.order.api.module.ExtraCost;
import com.laogeli.order.mapper.CarExtraCostMapper;
import com.laogeli.order.service.CarExtraCostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 车辆额外费用管理
 *
 * @author BeiFang
 * @Date 2020-10-04
 **/
@Slf4j
@AllArgsConstructor
@Api("车辆额外费用记录管理")
@RestController
@RequestMapping("/v1/extraCost")
public class CarExtraCostController {

    private CarExtraCostMapper carExtraCostMapper;

    private CarExtraCostService carExtraCostService;

    /* *
     *  新增额外费用记录
     * @Param [extraCost]
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增额外费用记录", notes = "新增额外费用记录")
    @ApiImplicitParam(name = "extraCost", value = "额外费用实体", required = true, dataType = "ExtraCost")
    public ResponseBean<Boolean> addExtraCost(@RequestBody ExtraCost extraCost) {

        extraCost.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
        return new ResponseBean<>(carExtraCostMapper.addListObj(extraCost) > 0);
    }

    /**
     * 分页查询额外费用记录
     *
     * @param pageName          分页页码
     * @param pageSize          分页大小
     * @param sort              排序字段
     * @param order             排序方向
     * @param corporateIdentify 所属公司标识
     * @return
     */
    @RequestMapping("/getExtraCost")
    @ApiOperation(value = "分页查询额外费用记录", notes = "分页查询额外费用记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "corporateIdentify", value = "企业标识id", dataType = "String")

    })
    public PageInfo<ExtraCost> findPage(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageName,
                                        @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                        @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.SORT) String sort,
                                        @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.ORDER) String order,
                                        @RequestParam(value = "corporateIdentify") String corporateIdentify,
                                        @RequestParam(value = "vehicle", required = false, defaultValue = "") String vehicle,
                                        @RequestParam(value = "beginTime", required = false, defaultValue = "") String beginTime,
                                        @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime,
                                        @RequestParam(value = "invoiceNum",required = false,defaultValue = "")String invoiceNum) {
        ExtraCost extraCost = new ExtraCost();
        extraCost.setVehicle(vehicle);
        extraCost.setBelongCompaniesId(corporateIdentify);
        extraCost.setBeginTime(beginTime);
        extraCost.setEndTime(endTime);
        extraCost.setInvoiceNum(invoiceNum);
        return carExtraCostService.findPage(PageUtil.pageInfo(pageName, pageSize, sort, order), extraCost);
    }

    /**
     * 修改额外费用记录
     *
     * @param extraCost
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改额外费用记录", notes = "修改额外费用记录")
    @ApiImplicitParam(name = "extraCost", value = "修改额外费用记录", required = true, dataType = "ExtraCost")
    public ResponseBean<Boolean> editExtraCost(@RequestBody ExtraCost extraCost) {
        extraCost.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
        return new ResponseBean<>(carExtraCostMapper.update(extraCost) > 0);

    }

    /**
     * 根据id删除额外费用记录
     *
     * @param id 额外费用记录id
     * @return
     */
    @RequestMapping("/{id}")
    @ApiOperation(value = "删除额外费用记录", notes = "删除额外费用记录")
    @ApiImplicitParam(name = "id", value = "额外费用记录id", required = true, dataType = "id")
    public ResponseBean<Boolean> deleteMaintain(@PathVariable String id) {
        ExtraCost extraCost = new ExtraCost();
        extraCost.setId(id);
        return new ResponseBean<>(carExtraCostMapper.delete(extraCost) > 0);
    }

}
