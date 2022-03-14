package com.laogeli.distribute.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.exceptions.CommonException;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.DateUtils;
import com.laogeli.common.core.utils.PageUtil;
import com.laogeli.common.core.utils.StringUtils;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.common.log.annotation.Log;
import com.laogeli.distribute.api.form.BusinessOrderForm;
import com.laogeli.distribute.api.module.BusinessCenter;
import com.laogeli.distribute.api.vo.BusinessCenterVo;
import com.laogeli.distribute.config.ProducerMessage;
import com.laogeli.distribute.service.BusinessCenterService;
import com.laogeli.user.api.feign.UserServiceClient;
import com.laogeli.user.api.module.MessageInfo;
import io.goeasy.GoEasy;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 下单接单管理
 *
 * @author wang
 * @date 2020-11-17
 */
@Slf4j
@AllArgsConstructor
@Api("下单接单管理")
@RestController
@RequestMapping("/v1/businessCenter")
public class BusinessCenterController {

    private final BusinessCenterService businessCenterService;

    private final ProducerMessage producerMessage;

    private final UserServiceClient userServiceClient;

    /**
     * 下单接单列表分页查询
     *
     * @param pageNum           pageNum
     * @param pageSize          pageSize
     * @param sort              sort
     * @param order             order
     * @param corporateIdentify corporateIdentify
     * @return PageInfo
     */
    @GetMapping("businessCenterList")
    @ApiOperation(value = "获取下单接单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "corporateIdentify", value = "企业标识id", dataType = "String")
    })
    public PageInfo<BusinessCenter> businessCenterList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                       @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                       @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                       @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                       @RequestParam(value = "corporateIdentify", required = false) String corporateIdentify,
                                                       @RequestParam(value = "orderStatus", required = false) Integer orderStatus,
                                                       @RequestParam(value = "blNos",required = false)String blNos) {
        BusinessCenter businessCenter = new BusinessCenter();
        businessCenter.setBelongCompaniesId(corporateIdentify);
        businessCenter.setOrderStatus(orderStatus);
        businessCenter.setBlNos(blNos);
        return businessCenterService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), businessCenter);
    }

    /**
     * 新增下单数据
     *
     * @param businessCenter 订单实体
     * @return
     */
    @RequestMapping("addMaking")
    @ApiOperation(value = "新增订单", notes = "新增下单数据")
    @ApiImplicitParam(name = "businessCenter", value = "下单实体", defaultValue = "BusinessCenter")
    @Log(value = "新增下单", type = 0)
    public ResponseBean<Boolean> saveBusinessCenter(@RequestBody BusinessCenter businessCenter) {
        //新增锦线订单

        try {
            return new ResponseBean<>(businessCenterService.insertBusiness(businessCenter) > 0);
        } catch (Exception e) {
            log.error("新增订单失败", e);
            throw new CommonException("新增订单失败");
        }
    }

    /**
     * 修改订单
     *
     * @param businessCenter 订单实体
     * @return
     */
    @RequestMapping("editMaking")
    @ApiOperation(value = "修改订单", notes = "修改订单")
    @ApiImplicitParam(name = "businessCenter", value = "下单实体", defaultValue = "BusinessCenter")
    @Log(value = "修改下单", type = 0)
    public ResponseBean<Integer> editBusinessCenter(@RequestBody BusinessCenter businessCenter) {
        try {
            int orderStatus = businessCenterService.getOrderStatus(businessCenter.getId());
            if (orderStatus == 2 || orderStatus == 3 || orderStatus == 4) {
                return new ResponseBean<>(-1, "已接单,无法修改");
            } else if (orderStatus == 0) {
                return new ResponseBean<>(-1, "订单已经取消,无法修改");
            } else if (orderStatus == 1) {
                businessCenter.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
                int n = businessCenterService.editPlaceOrderInfo(businessCenter);
                return new ResponseBean<>(n);
            }
            return new ResponseBean<>(1);
        } catch (Exception e) {
            log.error("修改失败", e);
            throw new CommonException("修改失败");
        }
    }

    /**
     * 取消待接单订单
     *
     * @param id
     * @return
     */
    @RequestMapping("cancelOrder")
    @ApiOperation(value = "取消订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "String"),
            @ApiImplicitParam(name = "belongCompaniesId", value = "公司id", dataType = "String"),
            @ApiImplicitParam(name = "reason", value = "取消原因", dataType = "String"),
            @ApiImplicitParam(name = "type", value = "取消类型1：客户端取消，0：平台取消", dataType = "int")
    })
    public ResponseBean<Integer> checkAndCancel(@RequestParam String id,
                                                @RequestParam(value = "belongCompaniesId", required = false, defaultValue = "") String belongCompaniesId,
                                                @RequestParam String reason,
                                                @RequestParam(value = "placeOrderNumber",required = false,defaultValue = "")String placeOrderNumber,
                                                @RequestParam int type,
                                                @RequestParam(value = "companyName", required = false, defaultValue = "") String companyName,
                                                @RequestParam(value = "blNosAndBox",required = false,defaultValue = "")String blNosAndBox) {
        try {
            int orderStatus = businessCenterService.getOrderStatus(id);
            if (orderStatus == 2 || orderStatus == 4) {
                return new ResponseBean<>(-1, "已接单无法取消");
            } else if (orderStatus == 0) {
                return new ResponseBean<>(-1, "订单已经取消,请勿重复操作");
            } else if (orderStatus == 1) {
                BusinessCenter businessCenter = new BusinessCenter();
                businessCenter.setId(id);
                businessCenter.setOrderStatus(0);
                businessCenter.setReason(reason);
                businessCenter.setCancelTime(DateUtils.asDate(LocalDateTime.now()));
                int num = businessCenterService.updateById(businessCenter);
                if (type == 0) {
                    // 平台取消订单 返回提单号 只刷新 弹出消息
                    GoEasy goEasy = new GoEasy("https://rest-hangzhou.goeasy.io", "BC-577d22cac6404d699b1cde65380e0f31");
                    goEasy.publish(belongCompaniesId, "003");
                    //将消息保存到sys_message表
                    MessageInfo messageInfo = new MessageInfo();
                    messageInfo.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
                    messageInfo.setBelongCompaniesId(belongCompaniesId);
                    messageInfo.setSendUser(companyName);
                    messageInfo.setReadStatus(0);
                    messageInfo.setNumberParams(placeOrderNumber);
                    messageInfo.setRouterAddress("Myorder");
                    String messageContext = "取消订单,下单编号："+placeOrderNumber + reason;
                    messageInfo.setMessageContext(blNosAndBox);
                    messageInfo.setMessageTitle(messageContext);
                    userServiceClient.insertMessage(messageInfo);

                } else {
                    // 客户取消订单 返回提单号 只刷新 弹出消息
                    GoEasy goEasy = new GoEasy("https://rest-hangzhou.goeasy.io", "BC-577d22cac6404d699b1cde65380e0f31");
                    goEasy.publish("792071937215041536", "003");
                    //将消息保存到sys_message表
                    MessageInfo messageInfo = new MessageInfo();
                    messageInfo.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
                    messageInfo.setBelongCompaniesId("792071937215041536");
                    messageInfo.setRouterAddress("Orderlist");
                    messageInfo.setSendUser(companyName);
                    messageInfo.setNumberParams(placeOrderNumber);
                    messageInfo.setReadStatus(0);
                    String messageContext = "取消订单,下单编号："+placeOrderNumber+", " + reason;
                    messageInfo.setMessageContext(blNosAndBox);
                    messageInfo.setMessageTitle(messageContext);
                    userServiceClient.insertMessage(messageInfo);

                }
                return new ResponseBean<>(num, "取消成功");
            }
            return new ResponseBean<>(1);
        } catch (Exception e) {
            log.error("取消失败", e);
            throw new CommonException("取消失败");
        }
    }

    /**
     * 接单
     *
     * @param businessOrderForm 订单form
     * @return
     */
    @PostMapping("orderReceiving")
    @ApiOperation(value = "接单", notes = "接单")
    @ApiImplicitParam(name = "BusinessOrderForm", value = "订单form实体", defaultValue = "BusinessOrderForm")
    @Log(value = "接单", type = 0)
    public ResponseBean<Integer> orderReceiving(@RequestBody BusinessOrderForm businessOrderForm) {
        try {
            int orderStatus = businessCenterService.getOrderStatus(businessOrderForm.getId());
            int num = 0;
            Date date = DateUtils.asDate(LocalDateTime.now());
            if (orderStatus == 1) {
                BusinessCenter businessCenter = new BusinessCenter();
                businessCenter.setId(businessOrderForm.getId());
                businessCenter.setOrderStatus(2);
                businessCenter.setOperator(businessOrderForm.getOperator());
                businessCenter.setOperatorTime(date);
                businessCenter.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
                num = businessCenterService.updateById(businessCenter);
            } else if (orderStatus == 2) {
                return new ResponseBean<>(-1, "已接单，请勿重复操作");
            } else if (orderStatus == 3) {
                return new ResponseBean<>(-1, "已取消，请勿重复操作");
            }
            if (num >= 1) {
                // 防止发送消息失败，将发送消息存入本地。
                businessOrderForm.setCreator(SysUtil.getUser());
                businessOrderForm.setOperatorTime(date);
                businessOrderForm.setMsgType("order");
                producerMessage.sendMsg(JSON.toJSONString(businessOrderForm));
                //发送消息成功，将消息存入消息列表
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setCommonValue(SysUtil.getUser(),SysUtil.getSysCode());
                messageInfo.setRouterAddress("Myorder");
                messageInfo.setReadStatus(0);
                messageInfo.setNumberParams(businessOrderForm.getPlaceOrderNumber());
                messageInfo.setBelongCompaniesId(businessOrderForm.getBelongCompaniesId());
                messageInfo.setMessageContext("提单号: "+businessOrderForm.getBlNos()+"; 箱型箱量: "+businessOrderForm.getBoxQuantity());
                messageInfo.setMessageTitle("订单已经被接单,下单编号为："+businessOrderForm.getPlaceOrderNumber());
                messageInfo.setSendUser(businessOrderForm.getOperator());
                userServiceClient.insertMessage(messageInfo);
            }

            return new ResponseBean<>(num);
        } catch (Exception e) {
            log.error("接单失败", e);
            throw new CommonException("接单失败");
        }
    }

    /**
     * 派单列表分页查询
     *
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @param sort     sort
     * @param order    order
     * @return PageInfo
     */
    @GetMapping("dispatchList")
    @ApiOperation(value = "获取派单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "corporateIdentify", value = "企业标识id", dataType = "String")
    })
    public PageInfo<BusinessCenterVo> dispatchList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                   @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                   @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                   @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                   @RequestParam(value = "orderStatus", required = false) Integer orderStatus,
                                                   @RequestParam(value = "orderType",required = false)Integer orderType,
                                                   @RequestParam(value = "orderNumber",required = false,defaultValue = "")String orderNumber,
                                                   @RequestParam(value = "blNos",required = false,defaultValue = "")String blNos,
                                                   @RequestParam(value = "beginTime", required = false, defaultValue = "") String beginTime,
                                                   @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime,
                                                   @RequestParam(value = "dueDate",required = false)Date dueDate) {
        BusinessCenterVo businessCenterVo = new BusinessCenterVo();
        businessCenterVo.setOrderStatus(orderStatus);
        businessCenterVo.setOrderType(orderType);
        businessCenterVo.setBlNos(blNos);
        businessCenterVo.setOrderNumber(orderNumber);
        businessCenterVo.setBeginTime(beginTime);
        businessCenterVo.setEndTime(endTime);
        return businessCenterService.findDispatchPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), businessCenterVo);
    }

    /**
     * 取消已接单订单 整票取消
     *
     * @param id
     * @return
     */
    @RequestMapping("cancelDisparcherOrder")
    @ApiOperation(value = "取消订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "String"),
            @ApiImplicitParam(name = "reason", value = "取消原因", dataType = "String")
    })

    public ResponseBean<Integer> cancelDisparcherOrder(@RequestParam String id,
                                                       @RequestParam String placeId,
                                                       @RequestParam String reason,
                                                       @RequestParam(value = "companyIds", required = false, defaultValue = "") String companyIds,
                                                       @RequestParam(value = "orderNumber", required = false, defaultValue = "") String orderNumber,
                                                       @RequestParam(value = "companyName", required = false, defaultValue = "") String companyName,
                                                       @RequestParam(value = "blNosAndBox",required = false,defaultValue = "")String blNosAndBox) {
        try {
            Integer orderStatus = businessCenterService.getOrderStatus(placeId);
            if (orderStatus == 0) {
                return new ResponseBean<>(-1, "订单已经取消,请勿重复操作");
            } else if (orderStatus == 4) {
                return new ResponseBean<>(-1, "订单已完结，无法取消");
            } else if (orderStatus == 2 || orderStatus == 3) {
                BusinessCenter businessCenter = new BusinessCenter();
                businessCenter.setId(placeId);
                businessCenter.setOrderStatus(0);
                businessCenter.setReason(reason);
                businessCenter.setCancelTime(DateUtils.asDate(LocalDateTime.now()));
                //1 根据Id 修改下单表订单状态
                businessCenterService.updateById(businessCenter);
                //2 根据订单id 修改订单状态businessState为0 已取消
                businessCenterService.updateByOrderId(id, 0);
                //3 根据订单表orderId 修改做箱状态为0 已取消
                businessCenterService.updateMakingStatus(id);
                if (!StringUtils.isEmpty(companyIds)) {
                    String[] companyArray = companyIds.split(",");
                    for (String companyId : companyArray
                    ) {
                        // 遍历发送socket消息给车队，推送提示消息，让其刷新列表
                        GoEasy goEasy = new GoEasy("https://rest-hangzhou.goeasy.io", "BC-577d22cac6404d699b1cde65380e0f31");
                        goEasy.publish(companyId, "005" + orderNumber);
                        //将消息保存到sys_message表
                        MessageInfo messageInfo = new MessageInfo();
                        messageInfo.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
                        messageInfo.setBelongCompaniesId(companyId);
                        messageInfo.setRouterAddress("Platformorders");
                        messageInfo.setSendUser(companyName);
                        messageInfo.setNumberParams(orderNumber);
                        messageInfo.setReadStatus(0);
                        String messageContext = "订单取消, " + reason+",订单编号: "+orderNumber;
                        messageInfo.setMessageContext(blNosAndBox);
                        messageInfo.setMessageTitle(messageContext);
                        userServiceClient.insertMessage(messageInfo);
                    }
                }

                return new ResponseBean<>(1);
            }
            return new ResponseBean<>(1);
        } catch (Exception e) {
            log.error("取消失败", e);
            throw new CommonException("取消失败");
        }
    }

    /**
     * 根据下单编号 修改订单状态
     *
     * @param placeOrderNumber,orderStatus
     * @return
     */
    @RequestMapping("dispatchOrder")
    @ApiOperation(value = "根据下单编号 修改订单状态 ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "placeOrderNumber", value = "订单编号", dataType = "String"),
            @ApiImplicitParam(name = "orderStatus", value = "订单状态", dataType = "int")
    })
    public ResponseBean<Integer> dispatchOrder(@RequestParam String id,
                                               @RequestParam String placeOrderNumber,
                                               @RequestParam int orderStatus) {
        try {
            // 更改订单表 与 下单表  状态
            int num = 0;
            if (orderStatus == 4) {
                // 根据订单id 更改 订单表状态为 2：已完结
                num = businessCenterService.updateByOrderId(id, 2);
            } else {
                num = businessCenterService.updateByOrderId(id, 1);
            }
            if (num > 0) {
                return new ResponseBean<>(businessCenterService.updateByNumber(placeOrderNumber, orderStatus));
            } else {
                throw new CommonException("修改订单状态失败");
            }
        } catch (Exception e) {
            log.error("修改订单状态失败", e);
            throw new CommonException("修改订单状态失败");
        }
    }

}
