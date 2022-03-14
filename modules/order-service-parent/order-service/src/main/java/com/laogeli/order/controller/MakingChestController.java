package com.laogeli.order.controller;

import com.laogeli.common.core.exceptions.CommonException;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.common.log.annotation.Log;
import com.laogeli.order.api.module.BusinessOrder;
import com.laogeli.order.api.module.Lcl;
import com.laogeli.order.api.module.MakingChest;
import com.laogeli.order.api.vo.WxMakingChestVo;
import com.laogeli.order.mapper.MakingChestMapper;
import com.laogeli.order.service.BusinessOrderService;
import com.laogeli.order.service.MakingChestService;
import com.laogeli.order.service.OrderAttendanceService;
import com.laogeli.user.api.feign.UserServiceClient;
import com.laogeli.user.api.module.MessageInfo;
import io.goeasy.GoEasy;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 做箱信息管理
 *
 * @author wang
 * @date 2020-07-09
 */
@Slf4j
@AllArgsConstructor
@Api("做箱信息管理")
@RestController
@RequestMapping("/v1/makingChest")
public class MakingChestController {

    private final MakingChestService makingChestService;

    private final BusinessOrderService businessOrderService;

    private final MakingChestMapper makingChestMapper;

    private final OrderAttendanceService orderAttendanceService;
    /**
     * 获取做箱信息
     *
     * @param businessOrderId businessOrderId
     * @return List<MakingChest>
     */
    @GetMapping("getMakingChestList")
    @ApiOperation(value = "获取做箱信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "businessOrderId", value = "订单id", dataType = "String"),
            @ApiImplicitParam(name = "companyId", value = "做箱车队id", dataType = "String")
    })
    public List<MakingChest> getMakingChestList(@RequestParam String businessOrderId,
                                                @RequestParam(value = "companyId", required = false, defaultValue = "") String companyId) {
        MakingChest makingChest = new MakingChest();
        makingChest.setCompanyId(companyId);
        makingChest.setBusinessOrderId(businessOrderId);
        // 获取做箱信息
        List<MakingChest> mcList = makingChestService.findList(makingChest);
        if (mcList.size() > 0) {
            // 遍历出做箱id 存入数组，通过id数组 批量查询 门点 与 件毛体
            String idString = "";
            for (MakingChest mc : mcList) {
                idString += mc.getId() + ",";
            }
            List<Lcl> lclList = makingChestService.getLclList((idString.substring(0, idString.length() - 1)).split(","));
            for (MakingChest mc : mcList) {
                // 将逗号字符串 转 数组
                if (!StringUtils.isBlank(mc.getVehicle())) {
                    mc.setVehicleList(mc.getVehicle().split(","));
                }
                if (!StringUtils.isBlank(mc.getDriver())) {
                    mc.setDriverList(mc.getDriver().split(","));
                }
                List<Lcl> list = new ArrayList<>();
                for (Lcl lcl : lclList) {
                    if (lcl.getMcId().equals(mc.getId())) {
                        list.add(lcl);
                    }
                }
                mc.setLclList(list);
            }
        }
        return mcList;
    }

    /**
     * 根据做箱id获取件毛体
     *
     * @param mcId mcId
     * @return List<Lcl>
     */
    @GetMapping("getLclList/{mcId}")
    @ApiOperation(value = "根据下单id获取做箱信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "mcId", value = "做箱id", dataType = "String")})
    public List<Lcl> getLclCenterList(@PathVariable String mcId) {
        String[] str = new String[1];
        str[0] = mcId;
        return makingChestService.getLclList(str);
    }

    private UserServiceClient userServiceClient;
    private static int corePoolSize = Runtime.getRuntime().availableProcessors() * 2;
    /**
     * 更改做箱信息
     *
     * @param makingChestList makingChestList
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更改做箱信息", notes = "根据做箱id更改做箱信息")
    @ApiImplicitParam(name = "cudMakingChest", value = "做箱信息实体对象数组", required = true, dataType = "List<MakingChest>")
    @Log(value = "更改做箱信息", type = 0)
    public ResponseBean<Boolean> cudMakingChest(@RequestBody List<MakingChest> makingChestList) {

        //核心线程数不能超过最大线程数
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
                1000,
                2L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());

        try {
            int data = 0;
            List<MakingChest> addList = new ArrayList<>();
            List<MakingChest> updateList = new ArrayList<>();
            String idString1 = "";
            String idString2 = "";
            Set<String> set = new HashSet();
            for (MakingChest makingChest : makingChestList) {
                // 将数组 转为 字符串 （车辆 and 司机）
                if (makingChest.getOperationType() == 1) {// 新增
                    addList.add(makingChest);
                } else if (makingChest.getOperationType() == 2) {// 删除
                    idString1 += makingChest.getId() + ",";
                } else if (makingChest.getOperationType() == 3) {// 修改
                    updateList.add(makingChest);
                    idString2 += makingChest.getId() + ",";
                }
                // 可获取箱型与箱量 存入订单表中
                // isDispatch 等于1 说明是派单时的操作，需要发送消息给车队。
                if (makingChest.getIsNews() == 0 && makingChest.getIsDispatch() == 1) {
                    // 存入set 避免重复发送
                    set.add(makingChest.getCompanyId());
                }
            }
            if (addList.size() > 0)
                data = makingChestService.addMcAndLcl(addList);
            if (!"".equals(idString1))
                data = makingChestService.delCostLclMc((idString1.substring(0, idString1.length() - 1)).split(","));
            if (updateList.size() > 0)
                data = makingChestService.updateLclMc(updateList, (idString2.substring(0, idString2.length() - 1)).split(","));
            //保存车队操作记录
            if(makingChestList.size()!=0){
                BusinessOrder businessOrder = new BusinessOrder();
                businessOrder.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
                businessOrder.setId(makingChestList.get(0).getBusinessOrderId());
                businessOrder.setExceptionRecordCar(makingChestList.get(0).getExceptionRecordCar());
                businessOrderService.updateExcception(businessOrder);
            }
            //  是否需要开启异步线程 去执行任务操作
            CompletableFuture.runAsync(()->{
                // 提取 driverId ,做箱时间转为新的数组
                orderAttendanceService.updateByMakingStatus(makingChestList);
            }, threadPoolExecutor).exceptionally((e) -> {
                e.printStackTrace();
                throw new CommonException("查询路费失败");
            });
            return new ResponseBean<>(data > 0);
        } catch (Exception e) {
            log.error("更改做箱信息失败！", e);
            throw new CommonException("系统内部错误.");
        }
    }

    private void m1(@RequestBody List<MakingChest> makingChestList, Set<String> set) {
        for (String companyId : set) {
            // 遍历发送socket消息给车队（以公司id为信道），推送提示消息，让其刷新列表
            GoEasy goEasy = new GoEasy("https://rest-hangzhou.goeasy.io", "BC-577d22cac6404d699b1cde65380e0f31");
            goEasy.publish(companyId, "004");
            //将消息保存到sys_message表
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setCommonValue(SysUtil.getUser(),SysUtil.getSysCode());
            messageInfo.setBelongCompaniesId(companyId);
            messageInfo.setReadStatus(0);
            messageInfo.setNumberParams(makingChestList.get(0).getOrderNumber());
            messageInfo.setRouterAddress("Platformorders");
            messageInfo.setSendUser(makingChestList.get(0).getCompanyName());
            messageInfo.setMessageTitle("有新订单了请注意查看");
            messageInfo.setMessageContext("订单编号: "+makingChestList.get(0).getOrderNumber());
//                userServiceClient.insertMessage(messageInfo);
        }
    }

    /**
     * 修改已全/未全
     *
     * @param makingChest makingChest
     * @return ResponseBean
     */
    @PutMapping("updateMakingChest")
    @ApiOperation(value = "修改已全/未全", notes = "根据做箱id修改已全/未全")
    @ApiImplicitParam(name = "makingChest", value = "订单实体MakingChest", required = true, dataType = "MakingChest")
    @Log(value = "修改已全/未全", type = 0)
    public ResponseBean<Boolean> updateMakingChest(@RequestBody MakingChest makingChest) {
        try {
            makingChest.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
            return new ResponseBean<>(makingChestService.update(makingChest) > 0);
        } catch (Exception e) {
            log.error("修改已全/未全失败！", e);
            throw new CommonException("修改已全/未全失败！");
        }
    }


    /**
     * 取消已接单订单 部分取消
     *
     * @param id
     * @return
     */
    @RequestMapping("updateMakStatusById")
    @ApiOperation(value = "取消订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "String"),
            @ApiImplicitParam(name = "reason", value = "取消原因", dataType = "String")
    })
    @Log(value = "部分取消", type = 2)
    public ResponseBean<Integer> updateMakStatusById(@RequestParam String id,
                                                     @RequestParam String makingReason,
                                                     @RequestParam String orderId,
                                                     @RequestParam String placeOrderNumber,
                                                     @RequestParam(value="companyId",required = false,defaultValue = "") String companyId,
                                                     @RequestParam(value="orderNumber",required = false,defaultValue = "") String orderNumber,
                                                     @RequestParam(value = "companyName",required = false,defaultValue = "") String companyName) {
        try {
            Integer makingStatus = makingChestService.getMakingStatus(id);
            int num = 0;
            String zpcencel = "";
            if (null == makingStatus || makingStatus == 1) {
                MakingChest makingChest = new MakingChest();
                makingChest.setId(id);
                makingChest.setMakingStatus(0);
                makingChest.setMakingReason(makingReason);
                makingChest.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
                //1 通过做箱id修改做箱状态
                num = makingChestService.updateById(makingChest);

                //根据订单id查询是否还有正常做箱数据
                int makingCount = makingChestService.getIsMakingStatus(orderId);
                if (makingCount == 0) {
                    //判断是否为车队取消，如果否，则执行取消下单状态
                    if (!"-1".equals(placeOrderNumber)) {
                        //执行修改下单表订单状态为0 已整票取消 ,根据下单编号
                        makingChestService.updateOrderCenter(placeOrderNumber);
                    }
                    //根据订单id修改订单表状态 businessState为0  已取消
                    num  = businessOrderService.updateById(orderId, 0, "整票取消");
                    //整票取消赋值
                    zpcencel = String.valueOf(num);
                }
                if(!StringUtils.isEmpty(companyId)){
                    GoEasy goEasy = new GoEasy("https://rest-hangzhou.goeasy.io", "BC-577d22cac6404d699b1cde65380e0f31");
                    goEasy.publish(companyId, "006"+orderNumber);
                    MessageInfo messageInfo = new MessageInfo();
                    messageInfo.setCommonValue(SysUtil.getUser(),SysUtil.getSysCode());
                    messageInfo.setSendUser(companyName);
                    messageInfo.setReadStatus(0);
                    messageInfo.setNumberParams(orderNumber);
                    messageInfo.setRouterAddress("Platformorders");
                    messageInfo.setBelongCompaniesId(companyId);
                    messageInfo.setMessageTitle("取消订单");
                    messageInfo.setMessageContext("订单编号为"+orderNumber+"有一个箱子被取消了,请注意查看,取消原因："+makingReason);
//                    userServiceClient.insertMessage(messageInfo);
                }
                return new ResponseBean<>(num, "取消做箱成功",zpcencel);
            } else if (makingStatus == 0) {
                return new ResponseBean<>(-1, "已经取消做箱,请勿重复操作");
            }
            return new ResponseBean<>(1);
        } catch (Exception e) {
            log.error("取消失败", e);
            throw new CommonException("取消失败");
        }
    }


    /**
     * 根据做箱id保存/修改箱货照片
     * @param makingChest
     * @return
     */
    @RequestMapping("/updatePicture")
    @Log(value = "保存/修改箱货照片",type = 0)
    public ResponseBean<Boolean> updatePicture(@RequestBody MakingChest makingChest){
        makingChest.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
        try{
            return new ResponseBean<>(makingChestMapper.updatePictureById(makingChest)>0);
        }catch (Exception e){
            log.error("上传失败",e);
            throw new CommonException("上传失败");
        }
    }

    /**
     * 根据做箱id获取做箱信息
     * @param id
     * @return
     */
    @GetMapping("/findMakingChestById/{id}")
    @ApiOperation(value = "根据做箱id获取做箱信息",notes = "根据做箱id获取做箱信息",httpMethod = "GET")
    public MakingChest findMakingChestById(@PathVariable String id){
        MakingChest makingChest = new MakingChest();
        makingChest.setId(id);
        return makingChestMapper.get(makingChest);
    }

    /**
     * 根据id修改回执单信息
     * @param id
     * @param receiptUrl
     * @return
     */
    @RequestMapping("updateUrlById")
    @ApiOperation(value = "根据id修改回执单信息",notes = "根据id修改回执单信息",httpMethod = "GET")
    public ResponseBean updateUrlById(@RequestParam("id")String id,@RequestParam("receiptUrl")String receiptUrl){
        try{
            return new ResponseBean( makingChestMapper.updateUrlById(id,receiptUrl)>0);
        }catch (Exception e){
            log.error("修改失败",e);
            throw new CommonException("修改失败");
        }

    }

    /**
     * 微信小程序获取
     */
    // TODO 司机姓名，公司id
    @GetMapping("getMakingChestByWx")
    @ApiOperation(value = "根据司机手机号获取箱货信息",notes = "根据司机手机号获取箱货信息",httpMethod = "GET")
    public ResponseBean getMakingChestByWx(@RequestParam("phone")String phone,
                                           @RequestParam(value = "searchValue",required = false,defaultValue = "")String searchValue){
        WxMakingChestVo wxMakingChestVo = new WxMakingChestVo();
        wxMakingChestVo.setPhone(phone);
        wxMakingChestVo.setSearchValue(searchValue);
        return new ResponseBean(makingChestMapper.getByPhoneNumber(wxMakingChestVo));
    }
}
