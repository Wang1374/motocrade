package com.laogeli.distribute.controller;

import com.github.pagehelper.PageInfo;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.utils.PageUtil;
import com.laogeli.distribute.api.vo.ClientCostVo;
import com.laogeli.distribute.service.ClientCostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 客户端费用管理
 *
 * @author wang
 * @date 2020-12-30
 */
@Slf4j
@RestController
@AllArgsConstructor
@Api("客户端费用管理")
@RequestMapping("/v1/clientCost")
public class ClientCostController {

    private final ClientCostService clientCostService;

    /**
     * 查询费用，获取利润，业务毛利
     *
     * @param pageNum           pageNum
     * @param pageSize          pageSize
     * @param sort              sort
     * @param order             order
     * @param belongCompaniesId belongCompaniesId
     * @return PageInfo
     */
    @GetMapping("getClientCost")
    @ApiOperation(value = "获取费用列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "belongCompaniesId", value = "企业标识id", dataType = "String")
    })
    public PageInfo<ClientCostVo> getProfit(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                            @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                            @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                            @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                            @RequestParam(value = "belongCompaniesId") String belongCompaniesId,
                                            @RequestParam(value = "blNos", required = false, defaultValue = "") String blNos,
                                            @RequestParam(value = "beginTime",required = false,defaultValue = "")String beginTime,
                                            @RequestParam(value = "endTime",required = false,defaultValue = "")String endTime){

        ClientCostVo clientCostVo = new ClientCostVo();
        clientCostVo.setBelongCompaniesId(belongCompaniesId);
        clientCostVo.setBlNoStr(blNos);
        clientCostVo.setBeginTime(beginTime);
        clientCostVo.setEndTime(endTime);
        return clientCostService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), clientCostVo);
    }


}
