package com.laogeli.order.controller;

import com.laogeli.common.core.exceptions.CommonException;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.common.log.annotation.Log;
import com.laogeli.order.api.module.Account;
import com.laogeli.order.api.module.SendReceipAddress;
import com.laogeli.order.service.SendReceipService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * 客户寄单地址信息管理
 * @author beifang
 * @Date 2021-07-01 14:04
 **/
@Slf4j
@AllArgsConstructor
@Api("客户寄单地址信息管理")
@RestController
@RequestMapping("/v1/receip")
public class SendReceipController {

    private final SendReceipService sendReceipService;



    /**
     * 查询客户寄单息列表
     *
     * @param customerId customerId
     * @return List<Account>
     */
    @GetMapping("receipList/{customerId}")
    @ApiOperation(value = "查询客户寄单息列表", notes = "查询客户寄单息列表")
    @ApiImplicitParam(name = "customerId", value = "客户ID", required = true, paramType = "path")
    public List<SendReceipAddress> accountList(@PathVariable String customerId) {
        SendReceipAddress sendReceipAddress = new SendReceipAddress();
        sendReceipAddress.setCustomerId(customerId);
        return sendReceipService.findList(sendReceipAddress);
    }


    /**
     * 新增客户账户信息
     *
     * @param sendReceipAddress sendReceipAddress
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增客户寄单地址信息", notes = "新增客户寄单地址信息")
    @ApiImplicitParam(name = "sendReceipAddress", value = "寄单地址", required = true, dataType = "SendReceipAddress")
   // @Log(value = "新增客户寄单地址信息", type = 0)
    public ResponseBean<Integer> addAccount(@RequestBody SendReceipAddress sendReceipAddress) {
        try{
            sendReceipAddress.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
            return new ResponseBean<>(sendReceipService.insertSendReceip(sendReceipAddress));
        }catch (Exception e){
            log.error("新增客户寄单地址失败",e);
            throw new CommonException("新增客户寄单地址失败");
        }
    }


    /**
     * 更新客户账户信息
     *
     * @param sendReceipAddress sendReceipAddress
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新客户账户信息", notes = "根据客户id更新客户账户信息")
    @ApiImplicitParam(name = "account", value = "客户实体account", required = true, dataType = "Account")
    @Log(value = "修改客户账户信息", type = 0)
    public ResponseBean<Integer> updateAccount(@RequestBody SendReceipAddress sendReceipAddress) {
        try{
            sendReceipAddress.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
            return new ResponseBean<>(sendReceipService.updateReceip(sendReceipAddress));
        }catch (Exception e){
            log.error("修改客户寄单地址失败",e);
            throw new CommonException("修改客户寄单地址失败");
        }
    }


}
