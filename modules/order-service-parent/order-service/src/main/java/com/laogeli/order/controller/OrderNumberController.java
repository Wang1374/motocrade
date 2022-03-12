package com.laogeli.order.controller;

import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.exceptions.CommonException;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.common.log.annotation.Log;
import com.laogeli.order.api.module.OrderNumber;
import com.laogeli.order.service.OrderNumberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 订单编号管理
 *
 * @author yangyu
 * @date 2020-06-18
 */
@Slf4j
@AllArgsConstructor
@Api("订单编号管理")
@RestController
@RequestMapping("/v1/orderNumber")
public class OrderNumberController {

    private final OrderNumberService orderNumberService;

    private final RedisTemplate redisTemplate;

    /**
     * 订单编号设置查询
     *
     * @param corporateIdentify corporateIdentify
     * @return OrderNumber
     */
    @GetMapping("getOrderNumber/{corporateIdentify}")
    @ApiOperation(value = "获取订单编号设置")
    @ApiImplicitParam(name = "corporateIdentify", value = "企业标识id", required = true, paramType = "path")
    public OrderNumber getOrderNumber(@PathVariable String corporateIdentify) {
        OrderNumber orderNumber = new OrderNumber();
        orderNumber.setBelongCompaniesId(corporateIdentify);
        return orderNumberService.get(orderNumber);
    }

    /**
     * 新增订单编号
     *
     * @param corporateIdentify corporateIdentify
     * @return ResponseBean
     */
    @PostMapping("addOrderNumber/{corporateIdentify}")
    @ApiOperation(value = "新增订单编号", notes = "新增订单编号")
    @ApiImplicitParam(name = "corporateIdentify", value = "企业标识id", required = true, paramType = "path")
    @Log(value = "新增订单编号", type = 0)
    public int addOrderNumber(@PathVariable String corporateIdentify) {
        OrderNumber orderNumber = new OrderNumber();
        orderNumber.setBelongCompaniesId(corporateIdentify);
        orderNumber.setPrefix("");
        orderNumber.setSeriesNumber(5);
        orderNumber.setBusinessRule("1");
        orderNumber.setDateRule("1");
        orderNumber.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
        return orderNumberService.insert(orderNumber);
    }

    /**
     * 更新订单编号设置
     *
     * @param orderNumber orderNumber
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新订单编号设置", notes = "根据订单编号设置id更新订单编号设置")
    @ApiImplicitParam(name = "orderNumber", value = "订单编号设置OrderNumber", required = true, dataType = "OrderNumber")
    @Log(value = "更新订单编号设置", type = 0)
    public ResponseBean<Boolean> updateOrderNumber(@RequestBody OrderNumber orderNumber) {
        try {
            // 删除订单编号设置 缓存
            String orderNumberKey = CommonConstant.ORDER_NUMBER_PREFIX + orderNumber.getBelongCompaniesId();
            redisTemplate.delete(orderNumberKey);
            // 删除全部叠加
            String allAotuKey = CommonConstant.ALL_AOTU_PREFIX + orderNumber.getBelongCompaniesId();
            redisTemplate.delete(allAotuKey);
            // 删除海运出口叠加
            String seAotuKey = CommonConstant.SE_AOTU_PREFIX + orderNumber.getBelongCompaniesId();
            redisTemplate.delete(seAotuKey);
            // 删除海运进口叠加
            String siAotuKey = CommonConstant.SI_AOTU_PREFIX + orderNumber.getBelongCompaniesId();
            redisTemplate.delete(siAotuKey);
            // 删除空运出口叠加
            String aeAotuKey = CommonConstant.AE_AOTU_PREFIX + orderNumber.getBelongCompaniesId();
            redisTemplate.delete(aeAotuKey);
            // 删除空运出口叠加
            String aiAotuKey = CommonConstant.AI_AOTU_PREFIX + orderNumber.getBelongCompaniesId();
            redisTemplate.delete(aiAotuKey);

            orderNumber.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
            return new ResponseBean<>(orderNumberService.update(orderNumber) > 0);
        } catch (Exception e) {
            log.error("更新订单编号设置失败！", e);
            throw new CommonException("更新订单编号设置失败！");
        }
    }
}
