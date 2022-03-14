package com.laogeli.order.controller;

import com.github.pagehelper.PageInfo;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.exceptions.CommonException;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.IdGen;
import com.laogeli.common.core.utils.PageUtil;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.common.log.annotation.Log;
import com.laogeli.order.api.module.CostsSet;
import com.laogeli.order.mapper.CostsSetMapper;
import com.laogeli.order.service.CostsSetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 费用设置信息管理
 *
 * @author wang
 * @date 2020-06-16
 */
@Slf4j
@AllArgsConstructor
@Api("费用设置信息管理")
@RestController
@RequestMapping("/v1/costsSet")
public class CostsSetController {

    private final CostsSetService costsSetService;

    private final CostsSetMapper costsSetMapper;

    private final RedissonClient redissonClient;

    /**
     * 费用设置信息分页查询
     *
     * @param pageNum           pageNum
     * @param pageSize          pageSize
     * @param sort              sort
     * @param order             order
     * @param corporateIdentify corporateIdentify
     * @return PageInfo
     */
    @GetMapping("costsSetList")
    @ApiOperation(value = "获取费用设置信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "corporateIdentify", value = "企业标识id", dataType = "String")
    })
    public PageInfo<CostsSet> costsSetList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                           @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                           @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                           @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                           @RequestParam(value = "corporateIdentify") String corporateIdentify) {
        CostsSet costsSet = new CostsSet();
        costsSet.setBelongCompaniesId(corporateIdentify);
        return costsSetService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), costsSet);
    }

    /**
     * 新增费用
     *
     * @param costsSet costsSet
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增费用", notes = "新增费用")
    @ApiImplicitParam(name = "costsSet", value = "费用实体CostsSet", required = true, dataType = "CostsSet")
    @Log(value = "新增费用", type = 0)
    public ResponseBean<Integer> addCostsSet(@RequestBody CostsSet costsSet) {
        RLock lock = redissonClient.getLock(costsSet.getFeeName());
        boolean getLock = false;
        try {
            Integer count = 0;
            if (getLock = lock.tryLock(0, 5, TimeUnit.SECONDS)) {
                costsSet.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
                count = costsSetService.insertCostsSet(costsSet);
            } else {
                log.info("Redisson分布式锁没有获得锁:{},ThreadName:{}", costsSet.getFeeName(), Thread.currentThread().getName());
            }
            if (count == 0) {
                return new ResponseBean<>(count, "费用名称已经存在");
            } else {
                return new ResponseBean<>(count, "新增成功");
            }
        } catch (InterruptedException e) {
            log.error("新增费用失败, Redisson 获取分布式锁异常, 异常信息:{}", e);
            throw new CommonException("新增费用失败");
        } finally {
            if (!getLock) {
                return new ResponseBean<>(-1, "请勿重复操作");
            }
            lock.unlock();
            log.info("Redisson分布式锁释放锁:{},ThreadName :{}", costsSet.getFeeName(), Thread.currentThread().getName());
        }
    }

    /**
     * 更新费用
     *
     * @param costsSet costsSet
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新费用信息", notes = "根据费用id更新更新费用")
    @ApiImplicitParam(name = "costsSet", value = "费用实体CostsSet", required = true, dataType = "CostsSet")
    @Log(value = "更新费用", type = 0)
    public ResponseBean<Integer> updateCostsSet(@RequestBody CostsSet costsSet) {
        RLock lock = redissonClient.getLock(costsSet.getFeeName());
        boolean getLock = false;
        try {
            Integer count = 0;
            if (getLock = lock.tryLock(0, 5, TimeUnit.SECONDS)) {
                costsSet.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
                count = costsSetService.updateCostsSet(costsSet);
            } else {
                log.info("Redisson分布式锁没有获得锁:{},ThreadName:{}", costsSet.getFeeName(), Thread.currentThread().getName());
            }
            if (count == 0) {
                return new ResponseBean<>(count, "费用名称已经存在");
            } else {
                return new ResponseBean<>(count, "修改成功");
            }
        } catch (InterruptedException e) {
            log.error("更新费用信息失败, Redisson 获取分布式锁异常, 异常信息:{}", e);
            throw new CommonException("更新费用信息失败");
        } finally {
            if (!getLock) {
                return new ResponseBean<>(-1, "请勿重复操作");
            }
            lock.unlock();
            log.info("Redisson分布式锁释放锁:{},ThreadName :{}", costsSet.getFeeName(), Thread.currentThread().getName());
        }
    }

    /**
     * 删除费用
     *
     * @param id id
     * @return ResponseBean
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除费用", notes = "根据ID删除费用")
    @ApiImplicitParam(name = "id", value = "费用ID", required = true, paramType = "path")
    @Log(value = "删除费用", type = 0)
    public ResponseBean<Boolean> deleteCostsSet(@PathVariable String id) {
        try {
            CostsSet costsSet = new CostsSet();
            costsSet.setId(id);
            return new ResponseBean<>(costsSetService.delete(costsSet) > 0);
        } catch (Exception e) {
            log.error("删除费用信息失败！", e);
            throw new CommonException("删除费用信息失败！");
        }
    }

    /**
     * 根据企业标识id查询费用设置
     *
     * @param belongCompaniesId
     * @return ResponseBean
     */
    @RequestMapping("findCostsSetList/{belongCompaniesId}")
    @ApiOperation(value = "查询费用设置", notes = "根据企业标识id查询费用设置")
    @ApiImplicitParam(name = "belongCompaniesId", value = "企业标识id", required = true, paramType = "String")
    public List<CostsSet> findCostsSetList(@PathVariable String belongCompaniesId) {
        return costsSetMapper.getListQyId(belongCompaniesId);
    }

    /**
     * 新增默认费用参数
     *
     * @param corporateIdentify corporateIdentify
     * @return ResponseBean
     */
    @PostMapping("batchCostsSet/{corporateIdentify}")
    @ApiOperation(value = "新增默认费用参数", notes = "新增默认费用参数")
    @ApiImplicitParam(name = "corporateIdentify", value = "企业标识id", required = true, paramType = "path")
    @Log(value = "新增默认费用参数", type = 0)
    public int batchCostsSet(@PathVariable String corporateIdentify) {
        List<CostsSet> costsSetList = new ArrayList<>();
        String[] feeName = {"超期费", "船公司修箱费", "坏污箱费", "修洗箱费", "二次搬移", "理货费", "港杂费", "港建费", "进口打单费", "暂落箱",
                "预进港", "路桥费", "调箱门", "取单费", "改还箱点", "改提箱点", "还箱费", "预录费", "提箱费", "出口打单费", "放箱费",
                "租卡费", "出车费", "运费"};
        String[] feeCode = {"CQF", "CGSXXF", "HWXF", "XXXF", "RCBY", "LHF", "GZF", "GJF", "JKDDF", "ZLF", "YJG", "LQF", "DXM", "QDF", "GHXD",
                "GTXD", "HXF", "YLF", "TXF", "CKDDF", "FXF", "ZKF", "CCF", "YF"};
        for (int i = 0; i < feeName.length; i++) {
            CostsSet costsSet = new CostsSet();
            costsSet.setFeeName(feeName[i]);
            costsSet.setFeeCode(feeCode[i]);
            costsSet.setCurrency("CNY");
            costsSet.setBelongCompaniesId(corporateIdentify);
            costsSet.setId(IdGen.snowflakeId());
            costsSetList.add(costsSet);
        }
        return costsSetMapper.batchCostsSet(costsSetList);
    }
}
