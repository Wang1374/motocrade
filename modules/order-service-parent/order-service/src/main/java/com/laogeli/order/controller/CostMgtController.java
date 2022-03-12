package com.laogeli.order.controller;

import com.github.pagehelper.PageInfo;
import com.google.common.net.HttpHeaders;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.exceptions.CommonException;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.DateUtils;
import com.laogeli.common.core.utils.PageUtil;
import com.laogeli.common.core.utils.Servlets;
import com.laogeli.common.log.annotation.Log;
import com.laogeli.order.api.module.Cost;
import com.laogeli.order.api.vo.ClientCostVo;
import com.laogeli.order.api.vo.ProfitVo;
import com.laogeli.order.mapper.CostMapper;
import com.laogeli.order.api.vo.CostVo;
import com.laogeli.order.service.CostService;
import com.laogeli.order.util.PackExcel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 费用管理
 *
 * @author yangyu
 * @date 2020-08-27
 */
@Slf4j
@AllArgsConstructor
@Api("费用管理")
@RestController
@RequestMapping("/v1/costMgt")
public class CostMgtController {

    private final CostService costService;

    private final CostMapper costMapper;

    /**
     * 通过做箱id与费用类型与费用所属车辆，获取费用信息列表
     *
     * @param mcId
     * @param costTypes
     * @return List<Cost>
     */
    @GetMapping("getCostList")
    @ApiOperation(value = "通过做箱id与费用类型与费用所属车辆，获取费用信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mcId", value = "做箱id", dataType = "String"),
            @ApiImplicitParam(name = "costTypes", value = "订单类型", dataType = "int"),
            @ApiImplicitParam(name = "vehicleCost", value = "订单类型", dataType = "String")
    })
    public List<Cost> getCostList(@RequestParam(value = "mcId") String mcId,
                                  @RequestParam(value = "costTypes") int costTypes,
                                  @RequestParam(value = "vehicleCost", required = false, defaultValue = "") String vehicleCost) {
        Cost cost = new Cost();
        cost.setMcId(mcId);
        cost.setCostTypes(costTypes);
        cost.setVehicleCost(vehicleCost);
        return costService.getList(cost);
    }

    /**
     * 更改费用信息
     *
     * @param costList costList
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更改费用信息", notes = "根据做箱id与费用类型更改费用信息")
    @ApiImplicitParam(name = "updateCost", value = "费用信息实体对象数组", required = true, dataType = "List<Cost>")
    @Log(value = "更改费用信息", type = 3)
    public ResponseBean<Boolean> updateCost(@RequestBody List<Cost> costList) {

        try {
            // 先删除 再新增
            int data = costService.deleteOrAdd(costList);
            return new ResponseBean<>(data > 0);
        } catch (Exception e) {
            log.error("更改费用信息失败！", e);
            throw new CommonException("网络连接超时，请稍后重试");
        }
    }

    /**
     * 费用分页查询
     *
     * @param pageNum           pageNum
     * @param pageSize          pageSize
     * @param sort              sort
     * @param order             order
     * @param belongCompaniesId belongCompaniesId
     * @return PageInfo
     */
    @GetMapping("costListPage")
    @ApiOperation(value = "获取费用列表")
    @PreAuthorize("hasAuthority('cost:detailed:list')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "belongCompaniesId", value = "企业标识id", dataType = "String")
    })
    public PageInfo<CostVo> costListPage(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                         @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                         @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                         @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                         @RequestParam(value = "belongCompaniesId") String belongCompaniesId,
                                         @RequestParam(value = "caseNumber", required = false, defaultValue = "") String caseNumber,
                                         @RequestParam(value = "costTypes", required = false, defaultValue = "0") int costTypes,
                                         @RequestParam(value = "expenseStatus", required = false, defaultValue = "-1") int expenseStatus,
                                         @RequestParam(value = "orderType", required = false, defaultValue = "0") int orderType,
                                         @RequestParam(value = "orderNumber", required = false, defaultValue = "") String orderNumber,
                                         @RequestParam(value = "customerId",required = false,defaultValue = "")String customerId,
                                         @RequestParam(value = "btypeId",required = false,defaultValue = "")String btypeId,
                                         @RequestParam(value = "fleetId",required = false,defaultValue = "")String fleetId,
                                         @RequestParam(value = "partner", required = false, defaultValue = "-1") int partner,
                                         @RequestParam(value = "blNo", required = false, defaultValue = "") String blNo,
                                         @RequestParam(value = "invoiceNo", required = false, defaultValue = "") String invoiceNo,
                                         @RequestParam(value = "vehicle", required = false, defaultValue = "") String vehicle,
                                         @RequestParam(value = "jdBeginTime", required = false, defaultValue = "") String jdBeginTime,
                                         @RequestParam(value = "jdEndTime", required = false, defaultValue = "") String jdEndTime,
                                         @RequestParam(value = "zxBeginTime", required = false, defaultValue = "") String zxBeginTime,
                                         @RequestParam(value = "zxEndTime", required = false, defaultValue = "") String zxEndTime,
                                         @RequestParam(value = "type", required = false, defaultValue = "-1") int type,
                                         @RequestParam(value = "payItems",required = false,defaultValue = "")String payItems) {
        CostVo costVo = new CostVo();
        costVo.setBelongCompaniesId(belongCompaniesId);
        costVo.setOrderType(orderType);
        costVo.setOrderNumber(orderNumber);
        costVo.setCustomerId(customerId);
        costVo.setJdBeginTime(jdBeginTime);
        costVo.setJdEndTime(jdEndTime);

        costVo.setCaseNumber(caseNumber);
//        costVo.setVehicleCost(vehicleCost);
        costVo.setVehicle(vehicle);
        costVo.setZxBeginTime(zxBeginTime);
        costVo.setZxEndTime(zxEndTime);

        costVo.setBlNos(blNo);
        costVo.setCostTypes(costTypes);
        costVo.setExpenseStatus(expenseStatus);
        costVo.setBtypeId(btypeId);
        costVo.setFleetId(fleetId);
        costVo.setPartner(partner);
        costVo.setInvoiceNo(invoiceNo);

        costVo.setPayItems(payItems);

        PageInfo<CostVo> pageInfo = new PageInfo<>();
        if (type == 1) {
            pageInfo = costService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), costVo);
        } else if (type == 0) {
            pageInfo = costService.platformCostListPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), costVo);
        }
        return pageInfo;
    }

    /**
     * 批量修改
     *
     * @param costList
     * @return ResponseBean
     */
    @PutMapping("/batchUpdate")
    @ApiOperation(value = "批量修改", notes = "根据费用id批量修改")
    @PreAuthorize("hasAuthority('cost:detailed:enter')")
    @ApiImplicitParam(name = "Cost", value = "订单费用实体Cost", required = true, dataType = "Cost")
    @Log(value = "批量修改", type = 0)
    public ResponseBean<Boolean> batchUpdate(@RequestBody List<Cost> costList) {
        try {
            return new ResponseBean<>(costService.batchUpdate(costList) > 0);
        } catch (Exception e) {
            log.error("批量修改失败！", e);
            throw new CommonException("网络连接超时，请稍后重试");
        }
    }

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
    @GetMapping("getProfit")
    @ApiOperation(value = "获取业务毛利列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "belongCompaniesId", value = "企业标识id", dataType = "String")
    })
    public PageInfo<ProfitVo> getProfit(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                        @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                        @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                        @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                        @RequestParam(value = "belongCompaniesId") String belongCompaniesId,
                                        @RequestParam(value = "vehicleOwnership", required = false, defaultValue = "") String vehicleOwnership,
                                        @RequestParam(value = "blNoStr", required = false, defaultValue = "") String blNoStr,
                                        @RequestParam(value = "vehicles",required = false) String vehicles,
                                        @RequestParam(value = "customerName", required = false, defaultValue = "") String customerName,
                                        @RequestParam(value = "beginTime", required = false, defaultValue = "") String beginTime,
                                        @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime,
                                        @RequestParam(value = "orderType", required = false, defaultValue = "0") Integer orderType,
                                        @RequestParam(value = "otBeginTime", required = false, defaultValue = "") String otBeginTime,
                                        @RequestParam(value = "otEndTime", required = false, defaultValue = "") String otEndTime,
                                        @RequestParam(value = "orderNumber", required = false, defaultValue = "") String orderNumber) {


        ProfitVo profitVo = new ProfitVo();
        profitVo.setBelongCompaniesId(belongCompaniesId);
        profitVo.setVehicleOwnership(vehicleOwnership);
        profitVo.setOrderType(orderType);
        profitVo.setBlNoStr(blNoStr);
        //profitVo.setBlNos(blNoStr);
        profitVo.setCustomerName(customerName);
        profitVo.setBeginTime(beginTime);
        profitVo.setEndTime(endTime);
        profitVo.setOtBeginTime(otBeginTime);
        profitVo.setOtEndTime(otEndTime);
        profitVo.setOrderNumber(orderNumber);
        profitVo.setVehicles(vehicles);
        if(vehicles!=null){
            String[] vehicle = vehicles.split(",");
            profitVo.setVehicleArray(vehicle);
        }
        PageInfo<ProfitVo> profitPage = costService.findProfitPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), profitVo);
        return profitPage;
    }


    /**
     * 通过往来单位获取利润
     *
     * @param pageNum           pageNum
     * @param pageSize          pageSize
     * @param sort              sort
     * @param order             order
     * @param belongCompaniesId belongCompaniesId
     * @return PageInfo
     */
    @GetMapping("getBtypeProfit")
    @ApiOperation(value = "通过往来单位获取利润")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "belongCompaniesId", value = "企业标识id", dataType = "String")
    })
    public PageInfo<ProfitVo> getBtypeProfit(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                             @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                             @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                             @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                             @RequestParam(value = "belongCompaniesId") String belongCompaniesId,
                                             @RequestParam(value = "customerName", required = false, defaultValue = "") String customerName
    ) {
        ProfitVo profitVo = new ProfitVo();
        profitVo.setBelongCompaniesId(belongCompaniesId);
        profitVo.setBtype(customerName);

        return costService.getRealisticAndShouldProfit(PageUtil.pageInfo(pageNum, pageSize, sort, order), profitVo);

    }


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
        return costService.findPageClient(PageUtil.pageInfo(pageNum, pageSize, sort, order), clientCostVo);
    }
    /**
     * 获取代垫发票url
     *
     * @param mcId
     * @param costTypes
     * @return
     */
    @RequestMapping("getInvoiceUrl")
    @ApiOperation(value = "获取代垫发票url")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "belongCompaniesId", value = "企业标识id", dataType = "String"),
            @ApiImplicitParam(name = "orderType", value = "订单类型", dataType = "Integer"),
            @ApiImplicitParam(name = "caseNumber", value = "箱号", dataType = "String"),
            @ApiImplicitParam(name = "orderNumber", value = "订单编号", dataType = "String")
    })
    public List<Cost> getInvoiceUrlList(
            @RequestParam(value = "mcId", defaultValue = "") String mcId,
            @RequestParam(value = "costTypes", defaultValue = "") Integer costTypes,
            @RequestParam(value = "vehicleCost",defaultValue = "")String vehicleCost) {
        Cost cost = new Cost();
        cost.setMcId(mcId);
        cost.setCostTypes(costTypes);
        cost.setVehicleCost(vehicleCost);
        return costMapper.findInvoiceUrl(cost);

    }

    @RequestMapping("setUrlById")
    @ApiOperation(value = "修改代垫发票url")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "costId", value = "费用id", dataType = ""),
            @ApiImplicitParam(name = "imageUrl", value = "代垫发票url", defaultValue = "")
    })
    public ResponseBean<Boolean> setInvoiceUrlById(
            @RequestParam(value = "costId", defaultValue = "") String costId,
            @RequestParam(value = "imageUrl", defaultValue = "") String imageUrl) {
        return new ResponseBean<>(costMapper.updateInvoiceUrl(costId, imageUrl) > 0);
    }

    @PostMapping("exportFees")
    @ApiOperation(value = "费用导出")
    @PreAuthorize("hasAuthority('cost:detailed:export')")
    @ApiImplicitParam(name = "CostVo", value = "费用实体", dataType = "CostVo")
    public void exportFees(@RequestBody List<CostVo> costVo, HttpServletRequest request, HttpServletResponse response) {
        // 创建excel文档
        HSSFWorkbook wb = new HSSFWorkbook();
        String downloadType = request.getHeader("Download-Type");
        if (downloadType.equals("1")) {
            PackExcel.getExcel1(wb, costVo);
        } else {
            PackExcel.getExcel2(wb, costVo);
        }
        try {
            //清空缓存
            response.reset();
            // 配置response
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, Servlets.getDownName(request, "费用明细" + DateUtils.localDateMillisToString(LocalDateTime.now()) + ".xls"));
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
     * 根据箱id查询毛利
     * @param mcId
     * @return
     */
    @RequestMapping("/{mcId}")
    @ApiOperation(value = "查询箱毛利")
    @ApiImplicitParam(name = "mcId", value = "箱id", dataType = "String")
    public ResponseBean<ProfitVo> getMakingProfit(@PathVariable String mcId){
        ProfitVo result = costMapper.getMakingProfit(mcId);
        return new ResponseBean<>(result);
    }
}
