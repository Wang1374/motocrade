package com.laogeli.order.controller;

import com.github.pagehelper.PageInfo;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.exceptions.CommonException;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.IdGen;
import com.laogeli.common.core.utils.PageUtil;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.common.log.annotation.Log;
import com.laogeli.order.api.module.Clientele;
import com.laogeli.order.api.vo.*;
import com.laogeli.order.service.ClienteleService;
import com.laogeli.order.service.ContactWayService;
import com.laogeli.order.service.DoorsService;
import com.laogeli.order.service.SendReceipService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 客户信息管理
 *
 * @author wang
 * @date 2020-06-09
 */
@Slf4j
@AllArgsConstructor
@Api("客户信息管理")
@RestController
@RequestMapping("/v1/clientele")
public class ClienteleController {

    private final ClienteleService clienteleService;

    private final ContactWayService contactWayService;

    private final DoorsService doorsService;

    private final SendReceipService sendReceipService;

    private final RedissonClient redissonClient;

    /**
     * 客户信息分页查询
     *
     * @param pageNum           pageNum
     * @param pageSize          pageSize
     * @param sort              sort
     * @param order             order
     * @param corporateIdentify corporateIdentify
     * @return PageInfo
     */
    @GetMapping("clienteleList")
    @PreAuthorize("hasAuthority('order:clientele:list')")
    @ApiOperation(value = "获取客户信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "corporateIdentify", value = "企业标识id", dataType = "String")
    })
    public PageInfo<Clientele> clienteleList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                             @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                             @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                             @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                             @RequestParam(value = "searchParam", required = false, defaultValue = "") String searchParam,
                                             @RequestParam(value = "corporateIdentify") String corporateIdentify) {
        Clientele clientele = new Clientele();
        clientele.setBelongCompaniesId(corporateIdentify);
        clientele.setNature(1);
        clientele.setSearchParam(searchParam);
        return clienteleService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), clientele);
    }

    /**
     * 供应商信息分页查询
     *
     * @param pageNum           pageNum
     * @param pageSize          pageSize
     * @param sort              sort
     * @param order             order
     * @param corporateIdentify corporateIdentify
     * @return PageInfo
     */
    @GetMapping("supplierList")
    @PreAuthorize("hasAuthority('order:supplier:list')")
    @ApiOperation(value = "获取供应商信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "corporateIdentify", value = "企业标识id", dataType = "String")
    })
    public PageInfo<Clientele> supplierList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                            @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                            @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                            @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                            @RequestParam(value = "searchParam", required = false, defaultValue = "") String searchParam,
                                            @RequestParam(value = "corporateIdentify") String corporateIdentify,
                                            @RequestParam(value = "queryType") int queryType) {
        Clientele clientele = new Clientele();
        clientele.setBelongCompaniesId(corporateIdentify);
        clientele.setNature(2);
        clientele.setSearchParam(searchParam);
        clientele.setQueryType(queryType);
        return clienteleService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), clientele);
    }

    /**
     * 往来单位信息分页查询
     *
     * @param pageNum           pageNum
     * @param pageSize          pageSize
     * @param sort              sort
     * @param order             order
     * @param corporateIdentify corporateIdentify
     * @return PageInfo
     */
    @GetMapping("btypeList")
    @PreAuthorize("hasAuthority('order:btype:list')")
    @ApiOperation(value = "获取往来单位信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "corporateIdentify", value = "企业标识id", dataType = "String")
    })
    public PageInfo<Clientele> btypeList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                         @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                         @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                         @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                         @RequestParam(value = "searchParam", required = false, defaultValue = "") String searchParam,
                                         @RequestParam(value = "corporateIdentify") String corporateIdentify,
                                         @RequestParam(value = "queryType") int queryType) {
        Clientele clientele = new Clientele();
        clientele.setBelongCompaniesId(corporateIdentify);
        clientele.setSearchParam(searchParam);
        clientele.setQueryType(queryType);
        return clienteleService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), clientele);
    }

    /**
     * 新增客户
     *
     * @param clientele clientele
     * @return ResponseBean
     */
    @PostMapping
    @PreAuthorize("hasAuthority('order:clientele:add') or hasAuthority('order:supplier:add') or hasAuthority('order:btype:add')")
    @ApiOperation(value = "新增客户", notes = "新增客户")
    @ApiImplicitParam(name = "clientele", value = "客户实体clientele", required = true, dataType = "Clientele")
    @Log(value = "新增客户", type = 0)
    public ResponseBean<Integer> addClientele(@RequestBody Clientele clientele) {
        RLock lock = redissonClient.getLock(clientele.getCompanyName());
        boolean getLock = false;
        try {
            Integer count = 0;
            if (getLock = lock.tryLock(0, 5, TimeUnit.SECONDS)) {
                clientele.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
                count = clienteleService.insertClientele(clientele);
            } else {
                log.info("Redisson分布式锁没有获得锁:{},ThreadName:{}", clientele.getCompanyName(), Thread.currentThread().getName());
            }
            if (count == 0) {
                return new ResponseBean<>(count, "公司抬头已经存在");
            } else {
                return new ResponseBean<>(count, "新增成功", clientele.getId());
            }
        } catch (InterruptedException e) {
            log.error("新增客户失败, Redisson 获取分布式锁异常, 异常信息:{}", e);
            throw new CommonException("新增客户失败");
        } finally {
            if (!getLock) {
                return new ResponseBean<>(-1, "请勿重复操作");
            }
            lock.unlock();
            log.info("Redisson分布式锁释放锁:{},ThreadName :{}", clientele.getCompanyName(), Thread.currentThread().getName());
        }
    }

    /**
     * 更新客户
     *
     * @param clientele clientele
     * @return ResponseBean
     */
    @PutMapping
    @PreAuthorize("hasAuthority('order:clientele:edit') or hasAuthority('order:supplier:edit') or hasAuthority('order:btype:edit')")
    @ApiOperation(value = "更新客户信息", notes = "根据客户id更新客户信息")
    @ApiImplicitParam(name = "clientele", value = "客户实体Clientele", required = true, dataType = "Clientele")
    @Log(value = "修改客户", type = 0)
    public ResponseBean<Integer> updateClientele(@RequestBody Clientele clientele) {
        RLock lock = redissonClient.getLock(clientele.getCompanyName());
        boolean getLock = false;
        try {
            Integer count = 0;
            if (getLock = lock.tryLock(0, 5, TimeUnit.SECONDS)) {
                clientele.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
                count = clienteleService.updateClientele(clientele);
            } else {
                log.info("Redisson分布式锁没有获得锁:{},ThreadName:{}", clientele.getCompanyName(), Thread.currentThread().getName());
            }
            if (count == 0) {
                return new ResponseBean<>(count, "公司抬头已经存在");
            } else {
                return new ResponseBean<>(count, "修改成功");
            }
        } catch (InterruptedException e) {
            log.error("修改客户失败, Redisson 获取分布式锁异常, 异常信息:{}", e);
            throw new CommonException("新增客户失败");
        } finally {
            if (!getLock) {
                return new ResponseBean<>(-1, "请勿重复操作");
            }
            lock.unlock();
            log.info("Redisson分布式锁释放锁:{},ThreadName :{}", clientele.getCompanyName(), Thread.currentThread().getName());
        }
    }

    /**
     * 删除客户
     *
     * @param id id
     * @return ResponseBean
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('order:clientele:del') or hasAuthority('order:supplier:del') or hasAuthority('order:btype:del')")
    @ApiOperation(value = "删除客户", notes = "根据ID删除客户")
    @ApiImplicitParam(name = "id", value = "客户ID", required = true, paramType = "path")
    @Log(value = "删除客户", type = 0)
    public ResponseBean<Boolean> deleteUser(@PathVariable String id) {
        try {
            Clientele clientele = new Clientele();
            clientele.setId(id);
            return new ResponseBean<>(clienteleService.delClientele(clientele) > 0);
        } catch (Exception e) {
            log.error("删除客户失败！", e);
            throw new CommonException("删除客户失败！");
        }
    }

    /**
     * 客户信息列表
     *
     * @param corporateIdentify corporateIdentify
     * @return List<Clientele>
     */
    @GetMapping("allClienteleList/{corporateIdentify}")
    @ApiOperation(value = "获取客户信息列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "corporateIdentify", value = "企业标识id", dataType = "String")})
    public List<Clientele> allClienteleList(@PathVariable String corporateIdentify) {
        Clientele clientele = new Clientele();
        clientele.setBelongCompaniesId(corporateIdentify);
        return clienteleService.findList(clientele);
    }

    /**
     * 获取客户与联系方式
     *
     * @param corporateIdentify corporateIdentify
     * @return List<Clientele>
     */
    @GetMapping("getClienteleAndContact/{corporateIdentify}")
    @ApiOperation(value = "获取客户与联系方式")
    @ApiImplicitParams({@ApiImplicitParam(name = "corporateIdentify", value = "企业标识id", dataType = "String")})
    //TODO 优化
    public List<ClienteleVo> getClienteleAndContact(@PathVariable String corporateIdentify) {
        List<ClienteleVo> clienteleVoList = clienteleService.getListById(corporateIdentify);

        String idStr = "";
        for (ClienteleVo clienteleVo : clienteleVoList) {
            idStr += clienteleVo.getId() + ",";
        }
        // 通过客户id数组 查询联系人
        List<ContactWayVo> contactWayVoList = contactWayService.getListByIds(idStr.split(","));
        List<DoorsVo> doorsVoList = doorsService.getListByIds(idStr.split(","));
        //查询寄单地址
        List<SendReceipVo> receipVoList = sendReceipService.getListByIds(idStr.split(","));

        for (ClienteleVo clienteleVo : clienteleVoList) {
            List<ContactWayVo> list1 = new ArrayList<>();
            List<DoorsVo> list2 = new ArrayList<>();
            List<SendReceipVo> list3 = new ArrayList<>();

            // 联系人
            for (ContactWayVo contactWayVo : contactWayVoList) {
                if (clienteleVo.getId().equals(contactWayVo.getCustomerId())) {
                    list1.add(contactWayVo);
                }
            }
            //门点
            for (DoorsVo doorsVo : doorsVoList) {
                if (clienteleVo.getId().equals(doorsVo.getCustomerId())) {
                    list2.add(doorsVo);
                }
            }

            //寄单地址
            for (SendReceipVo sendReceipVo : receipVoList) {
                if (clienteleVo.getId().equals(sendReceipVo.getCustomerId())) {
                    list3.add(sendReceipVo);
                }
            }
            clienteleVo.setContactWayVoList(list1);
            clienteleVo.setDoorsVoList(list2);
            clienteleVo.setReceipVoList(list3);
        }
        return clienteleVoList;
    }

    /**
     * 新增平台客户
     *
     * @param companyName companyName
     * @return ResponseBean
     */
    @PostMapping("addPlatformCustomers")
    @ApiOperation(value = "新增平台客户", notes = "新增平台客户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "corporateIdentify", value = "企业标识id", dataType = "String"),
            @ApiImplicitParam(name = "companyName", value = "客户公司抬头", dataType = "String"),
            @ApiImplicitParam(name = "salesman", value = "业务人员", dataType = "String")
    })
    public int addPlatformCustomers(@RequestParam String corporateIdentify,
                                    @RequestParam String companyName,
                                    @RequestParam String salesman) {
        Clientele clientele = new Clientele();
        clientele.setId(IdGen.snowflakeId());
        clientele.setBelongCompaniesId(corporateIdentify);
        clientele.setCompanyName(companyName);
        clientele.setPartner(1);
        clientele.setNature(1);
        clientele.setType(1);
        clientele.setSalesman(salesman);
        clientele.setHowToAccount(1);
        clientele.setClearingForm(1);
        clientele.setSettlementInterval(1);
        clientele.setPaymentDays(30);
        clientele.setApplicationCode("EXAM");
        return clienteleService.insert(clientele);
    }
}
