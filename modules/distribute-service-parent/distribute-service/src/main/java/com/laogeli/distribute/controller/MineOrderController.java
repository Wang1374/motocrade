package com.laogeli.distribute.controller;

import com.github.pagehelper.PageInfo;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.utils.PageUtil;
import com.laogeli.distribute.api.vo.MineOrderVo;
import com.laogeli.distribute.service.MineOrderService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 我的订单管理
 *
 * @author beifang
 * @date 2020-12-21
 */
@RestController
@RequestMapping("/v1/mineOrder")
@Slf4j
@ApiOperation("我的订单管理")
@AllArgsConstructor
public class MineOrderController {

    private final MineOrderService mineOrderService;



    @GetMapping
    @ApiOperation(value = "获取下单接单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "corporateIdentify", value = "企业标识id", dataType = "String")
    })
    public PageInfo<MineOrderVo> getMineOrder(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                              @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                              @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                              @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                              @RequestParam(value = "belongCompaniesId", required = false) String belongCompaniesId,
                                              @RequestParam(value = "placeOrderNumber", required = false) String placeOrderNumber,
                                              @RequestParam(value = "orderStatus",required = false)Integer orderStatus,
                                              @RequestParam(value="orderType",required = false) Integer orderType,
                                              @RequestParam(value = "beginTime",required = false,defaultValue = "")String beginTime,
                                              @RequestParam(value = "endTime",required = false,defaultValue = "")String endTime,
                                              @RequestParam(value="blNos" ,required = false,defaultValue = "")String blNos,
                                              @RequestParam(value = "customerNum",required = false,defaultValue = "")String customerNum) {
        MineOrderVo mineOrderVo = new MineOrderVo();
        mineOrderVo.setBelongCompaniesId(belongCompaniesId);
        mineOrderVo.setPlaceOrderNumber(placeOrderNumber);
        mineOrderVo.setOrderStatus(orderStatus);
        mineOrderVo.setOrderType(orderType);
        mineOrderVo.setBeginTime(beginTime);
        mineOrderVo.setEndTime(endTime);
        mineOrderVo.setBlNos0(blNos);
        mineOrderVo.setCustomerNum(customerNum);
        return mineOrderService.findMineOrder(PageUtil.pageInfo(pageNum, pageSize, sort, order), mineOrderVo);
    }

}
