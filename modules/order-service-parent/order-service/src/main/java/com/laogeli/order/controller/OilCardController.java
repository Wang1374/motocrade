package com.laogeli.order.controller;

import com.github.pagehelper.PageInfo;
import com.google.common.net.HttpHeaders;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.exceptions.CommonException;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.*;
import com.laogeli.common.log.annotation.Log;
import com.laogeli.order.api.module.OilCard;
import com.laogeli.order.api.utils.OrderUitls;
import com.laogeli.order.api.vo.CostVo;
import com.laogeli.order.api.vo.OilVoImport;
import com.laogeli.order.service.OilCardService;
import com.laogeli.order.util.PackExcel;
import com.laogeli.user.api.dto.UserInfoDto;
import com.laogeli.user.api.module.Attachment;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 油卡记录信息管理
 *
 * @author BeiFang
 * @Date 2020-07-19
 **/
@Slf4j
@AllArgsConstructor
@Api("油卡记录信息管理")
@RestController
@RequestMapping("/v1/oilCard")
public class OilCardController {

    private final OilCardService oilCardService;

    /* *
     *  新增邮费记录
     * @Param
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增油卡记录", notes = "新增油卡记录信息")
    @ApiImplicitParam(name = "oilCard", value = "油卡记录", required = true, dataType = "OilCard")
    @Log(value = "新增油卡记录", type = 0)
    public ResponseBean<Boolean> addRepair(@RequestBody OilCard oilCard) {
        oilCard.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
        return new ResponseBean<>(oilCardService.insert(oilCard) > 0);
    }

    /**
     * 分页查询找油网邮费记录
     *
     * @return
     */
    @GetMapping("/getOilzyCardList")
    @ApiOperation(value = "查询找油网邮费记录", notes = "分页查询所有记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间", defaultValue = ""),
            @ApiImplicitParam(name = "endTime", value = "结束时间", defaultValue = ""),
            @ApiImplicitParam(name = "zyCompanyId", value = "找油网客户id", defaultValue = "0"),
            @ApiImplicitParam(name = "pageNum", value = "分页页码", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", defaultValue = "10"),
            @ApiImplicitParam(name = "truckLicenseNumber", value = "车牌号", defaultValue = "")
    })
    public ResponseBean getOilCardList(
            @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime,
            @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime,
            @RequestParam(value = "zyCompanyId", defaultValue = "0") Integer zyCompanyId,
            @RequestParam(value = "pageNum", defaultValue = "1") String pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") String pageSize,
            @RequestParam(value = "truckLicenseNumber", required = false, defaultValue = "") String truckLicenseNumber) {

        return oilCardService.getZywOilPrice(startTime, endTime, zyCompanyId, pageNum, pageSize, truckLicenseNumber);
    }


    /**
     * 分页查询系统油费记录
     *
     * @param pageName          分页页码
     * @param pageSize          分页大小
     * @param sort              排序字段
     * @param order             排序方向
     * @param corporateIdentify 所属公司id
     * @return
     */
    @GetMapping("/getOilxtCardList")
    @ApiOperation(value = "查询所有车辆邮费记录", notes = "分页查询所有记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "corporateIdentify", value = "企业标识id", dataType = "String")})
    public PageInfo<OilCard> getOilxtCardList(
            @RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageName,
            @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
            @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.SORT) String sort,
            @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.ORDER) String order,
            @RequestParam(value = "corporateIdentify") String corporateIdentify,
            @RequestParam(value = "vehicle", required = false, defaultValue = "") String vehicle,
            @RequestParam(value = "beginTime", required = false, defaultValue = "") String beginTime,
            @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime,
            @RequestParam(value = "invoiceNum",required = false,defaultValue = "")String invoiceNum) {
        OilCard oilCard = new OilCard();
        oilCard.setBelongCompaniesId(corporateIdentify);
        oilCard.setVehicle(vehicle);
        oilCard.setBeginTime(beginTime);
        oilCard.setEndTime(endTime);
        oilCard.setInvoiceNum(invoiceNum);
        return oilCardService.findPage(PageUtil.pageInfo(pageName, pageSize, order, sort), oilCard);
    }


    /**
     * 根据id删除邮费记录
     *
     * @param id 邮费记录id
     * @return
     */
    @RequestMapping("/{id}")
    @ApiOperation(value = "删除邮费记录", notes = "根据邮费记录id删除记录")
    @ApiImplicitParam(name = "id", value = "邮费记录id", defaultValue = "")
    @Log(value = "删除邮费记录", type = 0)
    public ResponseBean<Boolean> deleteOilById(@PathVariable String id) {
        OilCard oilCard = new OilCard();
        oilCard.setId(id);
        return new ResponseBean<>(oilCardService.delete(oilCard) > 0);
    }

    /**
     * 修改邮费记录
     *
     * @param oilCard
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改邮费信息", notes = "根据邮费id修改邮费信息")
    @ApiImplicitParam(name = "oilCard", value = "邮费记录")
    @Log(value = "修改邮费信息", type = 0)
    public ResponseBean<Integer> updateOil(@RequestBody OilCard oilCard) {
        //先删除
        try {
            oilCard.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
            return new ResponseBean<>(oilCardService.updateOil(oilCard));
        } catch (Exception e) {
            log.error("修改邮费记录失败", e);
            throw new CommonException("修改邮费信息失败");
        }
    }


    /**
     * 上传文件
     *
     * @param file       file
     */
    @PostMapping("importOil")
    @ApiOperation(value = "上传文件", notes = "上传文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "busiType", value = "业务分类", dataType = "String"),
            @ApiImplicitParam(name = "busiId", value = "业务Id", dataType = "String"),
            @ApiImplicitParam(name = "busiModule", value = "业务模块", dataType = "String"),
    })
    public ResponseBean<Boolean> importOil(@ApiParam(value = "要上传的文件", required = true)
                                               @RequestParam("file") MultipartFile file,
                                           @RequestParam("belongCompaniesId")String belongCompaniesId) {
        try {
            log.debug("开始导入邮费数据");
            List<OilVoImport> oilCards = MapUtil.map2Java(OilVoImport.class,
                    ExcelToolUtil.importExcelOil(file.getInputStream(), OrderUitls.getOilMap()));
            for (OilVoImport value :
                    oilCards) {
                value.setOilDate(value.getOilDate()+" "+value.getOilTime());
            }
            if (CollectionUtils.isEmpty(oilCards))
                throw new CommonException("无油费记录数据导入.");
            return new ResponseBean<>(oilCardService.importOils(oilCards,belongCompaniesId)>0);
        } catch (Exception e) {
            log.error("导入油费数据失败！", e);
            throw new CommonException("导入油费数据失败！");
        }
    }


    /**
     * 油费记录模板下载
     * @param request
     * @param response
     */
    @PostMapping("downLoad")
    @ApiOperation(value = "油费记录模板下载")
    public void exportFees(HttpServletRequest request, HttpServletResponse response) {
        // 创建excel文档
        HSSFWorkbook wb = new HSSFWorkbook();
        PackExcel.getExcel3(wb);
        try {
            //清空缓存
            response.reset();
            // 配置response
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, Servlets.getDownName(request, "油费记录" + DateUtils.localDateMillisToString(LocalDateTime.now()) + ".xls"));
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


}