package com.laogeli.order.controller;

import com.github.pagehelper.PageInfo;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.utils.PageUtil;
import com.laogeli.common.core.utils.StringUtils;
import com.laogeli.order.api.module.BoxMaking;
import com.laogeli.order.service.BoxMakingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 做箱列表管理
 *
 * @author wang
 * @date 2020-08-17
 */
@Slf4j
@AllArgsConstructor
@Api("做箱列表管理")
@RestController
@RequestMapping("/v1/boxMaking")
public class BoxMakingController {

    private final BoxMakingService boxMakingService;

    /**
     * 做箱列表分页查询
     *
     * @param pageNum           pageNum
     * @param pageSize          pageSize
     * @param sort              sort
     * @param order             order
     * @param corporateIdentify corporateIdentify
     * @return PageInfo
     */
    @GetMapping("boxMakingList")
    @ApiOperation(value = "获取做箱列表")
    @PreAuthorize("hasAuthority('boxmaking:makebox:list')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "corporateIdentify", value = "企业标识id", dataType = "String")
    })
    public PageInfo<BoxMaking> boxMakingList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                             @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                             @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                             @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                             @RequestParam(value = "businessState", required = false, defaultValue = "") int businessState,
                                             @RequestParam(value = "blNo", required = false, defaultValue = "") String blNo,
                                             @RequestParam(value = "corporateIdentify") String corporateIdentify,
                                             @RequestParam(value = "orderType") int orderType,
                                             @RequestParam(value = "customerName", required = false, defaultValue = "") String customerName,
                                             @RequestParam(value = "dispatchType", required = false, defaultValue = "-1") int dispatchType,
                                             @RequestParam(value = "type") int type,
                                             @RequestParam(value = "orderNumber", required = false, defaultValue = "") String orderNumber,
                                             @RequestParam(value = "caseNumber", required = false, defaultValue = "") String caseNumber,
                                            // @RequestParam(value = "sealNumber", required = false, defaultValue = "") String sealNumber,
                                             @RequestParam(value = "door",required = false,defaultValue = "")String door,
                                             @RequestParam(value = "beginTime", required = false, defaultValue = "") String beginTime,
                                             @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime,
                                             @RequestParam(value = "vehicle", required = false, defaultValue = "") String vehicle,
                                             @RequestParam(value = "vehicles",required = false,defaultValue = "")String vehicles,
                                             @RequestParam(value = "driver", required = false, defaultValue = "") String driver,
                                             @RequestParam(value = "phone", required = false, defaultValue = "") String phone,
                                             @RequestParam(value = "typeOfShipping", required = false) Integer typeOfShipping,
                                             @RequestParam(value = "carrierCompany", required = false, defaultValue = "") String carrierCompany,
                                             @RequestParam(value = "sendReceipStatus",required = false)Integer sendReceipStatus,
                                             @RequestParam(value = "ysState", required = false, defaultValue = "-1") int ysState,
                                             @RequestParam(value = "yfState", required = false, defaultValue = "-1") int yfState,
                                             @RequestParam(value = "ptysState", required = false, defaultValue = "-1") int ptysState,
                                             @RequestParam(value = "partner",required = false)Integer partner
    ) {
        BoxMaking boxMaking = new BoxMaking();
        // 判断如果是平台 条件为订单表，若是车队条件为 做箱表
        if (type == 0) {
            boxMaking.setBelongCompaniesId(corporateIdentify);
        } else {
            boxMaking.setCompanyId(corporateIdentify);
        }
        boxMaking.setBusinessState(businessState);
        boxMaking.setBlNos(blNo);
        boxMaking.setSendReceipStatus(sendReceipStatus);
        //箱提单号
        boxMaking.setBlNoStr(blNo);
        boxMaking.setOrderType(orderType);
        boxMaking.setCustomerName(customerName);
        boxMaking.setDispatchType(dispatchType);
        boxMaking.setOrderNumber(orderNumber);
        boxMaking.setCaseNumber(caseNumber);
        //boxMaking.setSealNumber(sealNumber);
        boxMaking.setBeginTime(beginTime);
        boxMaking.setEndTime(endTime);
        boxMaking.setVehicle(vehicle);///////////
        boxMaking.setDoor(door);
        boxMaking.setPartner(partner);
        boxMaking.setVehicles(vehicles);
        if(!StringUtils.isEmpty(vehicles)){
            String[] carArray = vehicles.split(",");
            boxMaking.setVehicleArray(carArray);
        }
        boxMaking.setDriver(driver);
        boxMaking.setPhone(phone);
        boxMaking.setTypeOfShipping(typeOfShipping);
        boxMaking.setCarrierCompany(carrierCompany);
        boxMaking.setYsState(ysState);
        boxMaking.setYfState(yfState);
        boxMaking.setPtysState(ptysState);

        return boxMakingService.findBoxMakingPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), boxMaking);
    }


    /**
     * 客户端做箱列表分页查询
     *
     * @param pageNum           pageNum
     * @param pageSize          pageSize
     * @param sort              sort
     * @param order             order
     * @param corporateIdentify corporateIdentify
     * @return PageInfo
     */
    @GetMapping("clientBoxMakingList")
    @ApiOperation(value = "获取客户端做箱列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "corporateIdentify", value = "企业标识id", dataType = "String")
    })
    public PageInfo<BoxMaking> clientBoxMakingList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                   @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                   @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                   @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                   @RequestParam(value = "corporateIdentify") String corporateIdentify,
                                                   @RequestParam(value = "blNos", required = false, defaultValue = "") String blNos,
                                                   @RequestParam(value = "orderType", required = false) Integer orderType,
                                                   @RequestParam(value = "caseNumber", required = false, defaultValue = "") String caseNumber,
                                                   @RequestParam(value = "sealNumber", required = false, defaultValue = "") String sealNumber,
                                                   @RequestParam(value = "beginTime", required = false, defaultValue = "") String beginTime,
                                                   @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime

    ) {
        BoxMaking boxMaking = new BoxMaking();
        boxMaking.setBelongCompaniesId(corporateIdentify);
        boxMaking.setBlNos(blNos);

        //箱提单号
        boxMaking.setBlNoStr(blNos);
        boxMaking.setOrderType(orderType);
        boxMaking.setCaseNumber(caseNumber);
        boxMaking.setSealNumber(sealNumber);
        boxMaking.setBeginTime(beginTime);
        boxMaking.setEndTime(endTime);
        return boxMakingService.findClientBoxList(PageUtil.pageInfo(pageNum, pageSize, sort, order), boxMaking);
    }
}
