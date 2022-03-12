package com.laogeli.order.controller;

import com.github.pagehelper.PageInfo;
import com.google.common.net.HttpHeaders;
import com.laogeli.chatim.api.feign.ChatImClient;
import com.laogeli.chatim.api.redis.service.RedisCacheService;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.exceptions.CommonException;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.*;
import com.laogeli.common.log.annotation.Log;
import com.laogeli.order.api.module.BusinessOrder;
import com.laogeli.order.api.utils.OrderExcel;
import com.laogeli.order.api.vo.OrderVo;
import com.laogeli.order.mapper.BusinessOrderMapper;
import com.laogeli.order.service.BusinessOrderService;
import com.laogeli.order.service.MakingChestService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 业务订单管理
 *
 * @author yangyu
 * @date 2020-06-18
 */
@Slf4j
@AllArgsConstructor
@Api("业务订单管理")
@RestController
@RequestMapping("/v1/businessOrder")
public class BusinessOrderController {

    private final BusinessOrderService businessOrderService;

    private final BusinessOrderMapper businessOrderMapper;

    private final MakingChestService makingChestService;

    private final RedisCacheService redisCacheService;

    private final ChatImClient chatImClient;

    /**
     * 订单分页查询
     *
     * @param pageNum           pageNum
     * @param pageSize          pageSize
     * @param sort              sort
     * @param order             order
     * @param corporateIdentify corporateIdentify
     * @return PageInfo
     */
    @GetMapping("businessOrderList")
    @ApiOperation(value = "获取订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "corporateIdentify", value = "企业标识id", dataType = "String")
    })
    public PageInfo<BusinessOrder> businessOrderList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                     @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                     @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                     @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                     @RequestParam(value = "corporateIdentify") String corporateIdentify,
                                                     @RequestParam(value = "orderNumber", required = false, defaultValue = "") String orderNumber,
                                                     @RequestParam(value = "orderType") int orderType,
                                                     @RequestParam(value = "customerName", required = false, defaultValue = "") String customerName,
                                                     @RequestParam(value = "customerId", required = false, defaultValue = "") String customerId,
                                                     @RequestParam(value = "blNo", required = false, defaultValue = "") String blNo,
                                                     @RequestParam(value = "beginTime", required = false, defaultValue = "") String beginTime,
                                                     @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime) {
        BusinessOrder businessOrder = new BusinessOrder();
        businessOrder.setBelongCompaniesId(corporateIdentify);
        businessOrder.setOrderNumber(orderNumber);
        businessOrder.setOrderType(orderType);
        businessOrder.setCustomerName(customerName);
        businessOrder.setCustomerId(customerId);
        businessOrder.setBlNos(blNo);
        businessOrder.setBeginTime(beginTime);
        businessOrder.setEndTime(endTime);
        return businessOrderService.findBusinessOrderPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), businessOrder);
    }

    /**
     * 平台订单分页查询
     *
     * @param pageNum           pageNum
     * @param pageSize          pageSize
     * @param sort              sort
     * @param order             order
     * @param corporateIdentify corporateIdentify
     * @return PageInfo
     */
    @GetMapping("platformOrderList")
    @ApiOperation(value = "获取平台订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "corporateIdentify", value = "企业标识id", dataType = "String")
    })
    public PageInfo<BusinessOrder> platformOrderList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                     @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                     @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                     @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                     @RequestParam(value = "corporateIdentify") String corporateIdentify,
                                                     @RequestParam(value = "orderNumber", required = false, defaultValue = "") String orderNumber,
                                                     @RequestParam(value = "orderType") int orderType,
                                                     @RequestParam(value = "businessState",required = false,defaultValue = "-1")Integer businessState,
                                                     @RequestParam(value = "blNo", required = false, defaultValue = "") String blNo,
                                                     @RequestParam(value = "beginTime", required = false, defaultValue = "") String beginTime,
                                                     @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime) {
        BusinessOrder businessOrder = new BusinessOrder();
        businessOrder.setBelongCompaniesId(corporateIdentify);
        businessOrder.setOrderNumber(orderNumber);
        businessOrder.setOrderType(orderType);
        businessOrder.setBusinessState(businessState);
        businessOrder.setBlNos(blNo);
        businessOrder.setBeginTime(beginTime);
        businessOrder.setEndTime(endTime);
        return businessOrderService.findPlatformOrderPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), businessOrder);
    }


    /**
     * 客户端订单分页查询
     *
     * @param pageNum           pageNum
     * @param pageSize          pageSize
     * @param sort              sort
     * @param order             order
     * @param corporateIdentify corporateIdentify
     * @return PageInfo
     */
    @GetMapping("platformClientList")
    @ApiOperation(value = "获取客户端订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "corporateIdentify", value = "企业标识id", dataType = "String")
    })
    public PageInfo<BusinessOrder> platformClientList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                     @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                     @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                     @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                     @RequestParam(value = "corporateIdentify") String corporateIdentify) {
        BusinessOrder businessOrder = new BusinessOrder();
        businessOrder.setBelongCompaniesId(corporateIdentify);
        return businessOrderService.findPlatformClientPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), businessOrder);
    }

    /**
     * 新增订单
     *
     * @param businessOrder businessOrder
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增订单", notes = "新增订单")
    @ApiImplicitParam(name = "businessOrder", value = "业务订单实体BusinessOrder", required = true, dataType = "BusinessOrder")
    @Log(value = "新增订单", type = 1)
    public ResponseBean<String> addBusinessOrder(@RequestBody BusinessOrder businessOrder) {
        businessOrder.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
        businessOrderService.insertBusinessOrder(businessOrder);
        return new ResponseBean<>(businessOrder.getId());
    }

    /**
     * 新增客户订单
     *
     * @param businessOrder businessOrder
     * @return ResponseBean
     */
    @PostMapping("addClientBusinessOrder")
    @ApiOperation(value = "新增客户订单", notes = "新增客户订单")
    @ApiImplicitParam(name = "businessOrder", value = "业务订单实体BusinessOrder", required = true, dataType = "BusinessOrder")
    @Log(value = "新增客户订单", type = 1)
    public ResponseBean<String> addClientBusinessOrder(@RequestBody BusinessOrder businessOrder) {
        businessOrder.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
        businessOrderService.insertClientBusinessOrder(businessOrder);
        return new ResponseBean<>(businessOrder.getId());
    }

    /**
     * 更新redis中用户操作订单状态
     *
     * @param userId
     * @param orderId
     * @return
     */
    @RequestMapping("updateStatusChat")
    @ApiOperation(value = "更新用户操作订单状态", notes = "更新用户操作订单状态")
    public ResponseBean<Boolean> updateStatusChat(@RequestParam("userId") String userId,
                                                  @RequestParam("orderId") String orderId,
                                                  @RequestParam("identifier")String identifier,
                                                  @RequestParam("name")String name) {

        // 判断是否是最后一个
        boolean isLast = false;
        //查找第一个
        Set sets = redisCacheService.rangeRange("chat-" + orderId, 0, -1);
        List<String> userIdLists = new ArrayList<>(sets);
        if(userIdLists.size()>0){
            String data = userIdLists.get(userIdLists.size() - 1);
            if ((userId+"#"+name).equals(data)) {
                isLast = true;
            }
        }

        //不是当前第一个
        redisCacheService.zrem("chat-" + orderId, userId+"#"+name);
        //查找第一个
        Set set = redisCacheService.rangeRange("chat-" + orderId, 0, 1);
        List<String> userIdList = new ArrayList<>(set);
        if (userIdList.size() > 0 && !isLast) {
            String str = userIdList.get(0);
            int n = str.indexOf("#");
            String userIdTop = str.substring(0, n);
            chatImClient.pushMessage(userIdTop, "消息", name+"  操作完成，请继续操作", 102);
        }
        return new ResponseBean<>(1 > 0);
    }

    /**
     * 更新订单
     *
     * @param businessOrder businessOrder
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新订单信息", notes = "根据订单id更新订单信息")
    @ApiImplicitParam(name = "businessOrder", value = "订单实体BusinessOrder", required = true, dataType = "BusinessOrder")
//    @Log(value = "修改订单", type = 1)
    public ResponseBean<Boolean> updateBusinessOrder(@RequestBody BusinessOrder businessOrder) {
        try {
            // 1 redis
            businessOrder.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
            return new ResponseBean<>(businessOrderService.update(businessOrder) > 0);
        } catch (Exception e) {
            log.error("更新订单信息失败！", e);
            throw new CommonException("更新订单信息失败！");
        }
    }

    /**
     * 根据id 修改订单状态
     *
     * @param id,businessState
     * @return
     */
    @RequestMapping("updateState")
    @ApiOperation(value = "根据id 修改订单状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "String"),
            @ApiImplicitParam(name = "businessState", value = "订单状态", dataType = "int"),
            @ApiImplicitParam(name = "reason", value = "取消原因", dataType = "String")
    })
    public ResponseBean<Integer> updateState(@RequestParam String id,
                                             @RequestParam int businessState,
                                             @RequestParam(value = "reason", required = false, defaultValue = "") String reason) {
        try {
            // 若是取消
            if (businessState == 0) {
                BusinessOrder businessOrder = new BusinessOrder();
                businessOrder.setId(id);
                businessOrder = businessOrderService.get(businessOrder);
                int status = businessOrder.getBusinessState();
                if (status == 0) {
                    return new ResponseBean<>(-1, "订单已经取消,请勿重复操作");
                } else if (status == 2) {
                    return new ResponseBean<>(-1, "订单已完结，无法取消");
                } else {
                    // 根据订单表orderId修改做箱状态为0 已取消
                    makingChestService.updateMakingStatus(id);
                    return new ResponseBean<>(businessOrderService.updateById(id, businessState, reason));
                }
            } else {
                return new ResponseBean<>(businessOrderService.updateById(id, businessState, reason));
            }
        } catch (Exception e) {
            log.error("修改订单状态失败", e);
            throw new CommonException("修改订单状态失败");
        }
    }

    /**
     * 删除订单
     *
     * @param businessOrder businessOrder
     * @return ResponseBean
     */
    @DeleteMapping
    @ApiOperation(value = "删除订单", notes = "根据ID删除订单")
    @ApiImplicitParam(name = "businessOrder", value = "订单实体BusinessOrder", required = true, dataType = "BusinessOrder")
    @Log(value = "删除订单", type = 1)
    public ResponseBean<Boolean> delete(@RequestBody BusinessOrder businessOrder) {
        try {
            return new ResponseBean<>(businessOrderService.delete(businessOrder) > 0);
        } catch (Exception e) {
            log.error("删除订单失败！", e);
            throw new CommonException("删除订单失败！");
        }
    }

    @RequestMapping("/export")
    @ApiOperation(value = "导出数据")
    @ApiImplicitParam(name = "orderExport", value = "订单信息", required = true)
    @Log(value = "导出信息", type = 0)
    public void orderExport(@RequestBody List<OrderVo> orderVo,
                            HttpServletRequest request, HttpServletResponse response) {
        //获取数据
        List<OrderVo> list = businessOrderService.getOrderExportData(orderVo);
        HSSFWorkbook wb = new HSSFWorkbook();
        OrderExcel.getExcel(wb, list);

        try {
            //清空缓存
            response.reset();
            // 配置response
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, Servlets.getDownName(request, "做箱信息" + DateUtils.localDateMillisToString(LocalDateTime.now()) + ".xls"));
            OutputStream osOut = response.getOutputStream();
            // 将指定的字节写入此输出流
            wb.write(osOut);
            // 刷新此输出流并强制将所有缓冲的输出字节被写出
            osOut.flush();
            // 关闭流
            osOut.close();
        } catch (Exception e) {
            throw new CommonException("导出失败");
        }
    }

    /**
     * 通过下单编号 查询 订单信息
     *
     * @param placeOrderNumber placeOrderNumber
     * @return ResponseBean<BusinessOrder>
     */
    @GetMapping("getBusinessOrder/{placeOrderNumber}")
    @ApiOperation(value = "通过下单编号 查询 订单信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "businessOrderId", value = "订单id", dataType = "String")})
    public ResponseBean<BusinessOrder> getBusinessOrder(@PathVariable String placeOrderNumber) {
        BusinessOrder businessOrder = new BusinessOrder();
        businessOrder.setPlaceOrderNumber(placeOrderNumber);
        return new ResponseBean<>(businessOrderService.get(businessOrder));
    }

    /**
     * 标记订单已读
     *
     * @param companyId
     * @return
     */
    @RequestMapping("alreadyReaderOrder")
    @ApiOperation(value = "标记订单为已读")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "companyId", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "id", dataType = "String")
    })
    public ResponseBean<Integer> alreadyReaderOrder(@RequestParam String id,
                                                    @RequestParam String companyId) {
        try {
            //1 根据订单id,以及companyId查询当前已读/未读状态
            BusinessOrder result = businessOrderMapper.getAlreadyOrder(id, companyId);
            //2
            if (result != null) {
                //已读
                return new ResponseBean<>(-1, "已经标记为已读");
            } else {
                //未读
                //修改 添加当前公司id到company_ids
                int num = businessOrderMapper.updateCompanyIdsById(id, companyId);
                return new ResponseBean<>(num, "已读成功");
            }
            //return new ResponseBean<>(1);
        } catch (Exception e) {
            log.error("标记已读成功", e);
            throw new CommonException("标记已读失败");
        }
    }

    /**
     * 根据订单编号查询信息
     *
     * @param orderNumber
     * @return
     */
    @GetMapping("getDataByOrderNumber/{orderNumber}")
    @ApiOperation(value = "根据订单编号查询订单信息")
    @ApiImplicitParam(name = "orderNumber", value = "订单编号", dataType = "String")
    public ResponseBean<BusinessOrder> getDataByOrderNumber(@PathVariable String orderNumber) {
        return new ResponseBean<>(businessOrderMapper.getDataByOrderNumber(orderNumber));
    }

    /**
     * 根据订单id和用户id查询redis中有没有/保存
     *
     * @return ResponseBean responseBean = chatImClient.pushMessage(userId, "消息", "有其他用户正在操作当前订单，当前只能浏览", 102);
     * <p>
     * redisCacheService.zset("chat-loginId",key,System.currentTimeMillis());
     */
    @RequestMapping("/messageRedis")
    @ApiOperation(value = "根据订单id和用户id查询redis中有没有/保存")
    public ResponseBean findOrSaveRedis(@RequestParam("userId") String userId,
                                        @RequestParam("orderId") String orderId,
                                        @RequestParam("identifier")String identifier,
                                        @RequestParam(value = "name",required = false)String name,
                                        @RequestParam(value = "businessState",required = false)Integer businessState) {
        // 判断订单状态，如果businessState==3，修改为操作中  1
//        if(businessState==3){
//            BusinessOrder businessOrder = new BusinessOrder();
//            businessOrder.setBusinessState(1);
//            businessOrder.setId(orderId);
//            businessOrderService.updateBusinessState(businessOrder);
//        }

        // 删除过期的元素   10分钟  10*60*1000
        long t1 = System.currentTimeMillis() - 2 * 60 * 1000;  // 最小时间  5 分钟前
        long t2 = System.currentTimeMillis() - 10000 * 60 * 1000;  // 最大时间
        redisCacheService.zremrangebyscore("chat-" + orderId, t2, t1);

        Integer rank = redisCacheService.rank("chat-" + orderId, userId+"#"+name);
        if (rank != -1) {
            // 判断是不是第一个
            //查找第一个
            Set set = redisCacheService.rangeRange("chat-" + orderId, 0, 1);
            List<String> userIdList = new ArrayList<>(set);
            // 获取操作订单的用户
            int n = userIdList.get(0).indexOf("#");
            String userName = userIdList.get(0).substring(n+1);
            if(userIdList.get(0).equals(userId+"#"+name)){
                chatImClient.pushMessage(userId, "消息", "请继续操作", 102);
            }else {
                chatImClient.pushMessage(userId, "消息", userName+ " 正在操作当前订单，当前只能浏览", 101);
            }
        } else {
            //不存在
            // 1 判断是否有人操作这条订单
            Set set = redisCacheService.rangeRange("chat-" + orderId, 0, 1);

            if (set.size() > 0) {
                // 获取操作订单的用户
                List<String> setList = new ArrayList<>(set);
                int n = setList.get(0).indexOf("#");
                String userName = setList.get(0).substring(n+1);

                // 1.1 有人正在操作，发送消息提醒给前端
                chatImClient.pushMessage(userId, "消息", userName+" 正在操作当前订单，当前只能浏览", 101);
                //1.2  保存到chat-orderId 等待他人操作完成
                redisCacheService.zset("chat-" + orderId, userId+"#"+name, System.currentTimeMillis());
                return new ResponseBean("当前只能浏览", 101);
            } else {
                // 无人操作  正常操作  无需发送消息通知
                redisCacheService.zset("chat-" + orderId, userId+"#"+name, System.currentTimeMillis());
                return new ResponseBean("正常操作", 102);
            }

        }
        return new ResponseBean(1 > 0);
    }


}
