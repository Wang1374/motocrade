package com.laogeli.order.controller;

import com.laogeli.common.core.exceptions.CommonException;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.common.log.annotation.Log;
import com.laogeli.order.api.module.Account;
import com.laogeli.order.api.module.ContactWay;
import com.laogeli.order.mapper.ContactWayMapper;
import com.laogeli.order.service.ContactWayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 客户联系方式管理
 *
 * @author yangyu
 * @date 2020-06-11
 */
@Slf4j
@AllArgsConstructor
@Api("客户联系方式管理")
@RestController
@RequestMapping("/v1/contactWay")
public class ContactWayController {

    private final ContactWayService contactWayService;
    private final ContactWayMapper contactWayMapper;
    private final RedissonClient redissonClient;

    /**
     * 查询客户联系方式列表
     *
     * @param customerId customerId
     * @return List<Account>
     */
    @GetMapping("contactWayList/{customerId}")
    @ApiOperation(value = "查询客户联系方式列表", notes = "根据客户id查询客户联系方式列表")
    @ApiImplicitParam(name = "customerId", value = "客户ID", required = true, paramType = "path")
    public List<ContactWay> contactWayList(@PathVariable String customerId) {
        ContactWay contactWay = new ContactWay();
        contactWay.setCustomerId(customerId);
        return contactWayService.findList(contactWay);
    }

    /**
     * 新增客户联系方式
     *
     * @param contactWay contactWay
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增客户联系方式", notes = "新增客户联系方式")
    @ApiImplicitParam(name = "account", value = "客户联系方式实体account", required = true, dataType = "Account")
    @Log(value = "新增客户联系方式", type = 0)
    public ResponseBean<Integer> addContactWay(@RequestBody ContactWay contactWay) {
        RLock lock = redissonClient.getLock(contactWay.getContacts() + contactWay.getPhone() + contactWay.getBelongCompaniesId());
        boolean getLock = false;
        try {
            Integer count = 0;
            if (getLock = lock.tryLock(0, 5, TimeUnit.SECONDS)) {
                contactWay.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
                count = contactWayService.insertContactWay(contactWay);
            } else {
                log.info("Redisson分布式锁没有获得锁:{},ThreadName:{}", contactWay.getContacts() + contactWay.getPhone() + contactWay.getBelongCompaniesId());
            }
            if (count == 0) {
                return new ResponseBean<>(count, "客户联系人已经存在");
            } else {
                return new ResponseBean<>(count, "新增成功");
            }
        } catch (InterruptedException e) {
            log.error("新增客户联系方式失败, Redisson 获取分布式锁异常, 异常信息:{}", e);
            throw new CommonException("新增客户联系方式失败");
        } finally {
            if (!getLock) {
                return new ResponseBean<>(-1, "请勿重复操作");
            }
            lock.unlock();
            log.info("Redisson分布式锁释放锁:{},ThreadName :{}", contactWay.getContacts() + contactWay.getPhone() + contactWay.getBelongCompaniesId(), Thread.currentThread().getName());
        }
    }

    /**
     * 更新客户联系方式
     *
     * @param contactWay contactWay
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新客户联系方式", notes = "根据客户id更新客户联系方式")
    @ApiImplicitParam(name = "account", value = "客户实体account", required = true, dataType = "Account")
    @Log(value = "修改客户联系方式", type = 0)
    public ResponseBean<Integer> updateContactWay(@RequestBody ContactWay contactWay) {
        RLock lock = redissonClient.getLock(contactWay.getContacts() + contactWay.getPhone() + contactWay.getBelongCompaniesId());
        boolean getLock = false;
        try {
            Integer count = 0;
            if (getLock = lock.tryLock(0, 5, TimeUnit.SECONDS)) {
                contactWay.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
                count = contactWayService.updateContact(contactWay);
            } else {
                log.info("Redisson分布式锁没有获得锁:{},ThreadName:{}", contactWay.getContacts() + contactWay.getPhone() + contactWay.getBelongCompaniesId());
            }
            if (count == 0) {
                return new ResponseBean<>(count, "客户联系人已经存在");
            } else {
                return new ResponseBean<>(count, "修改成功");
            }
        } catch (InterruptedException e) {
            log.error("修改客户联系方式失败, Redisson 获取分布式锁异常, 异常信息:{}", e);
            throw new CommonException("修改客户联系方式失败");
        } finally {
            if (!getLock) {
                return new ResponseBean<>(-1, "请勿重复操作");
            }
            lock.unlock();
            log.info("Redisson分布式锁释放锁:{},ThreadName :{}", contactWay.getContacts() + contactWay.getPhone() + contactWay.getBelongCompaniesId(), Thread.currentThread().getName());
        }
    }


    /**
     * 删除联系人信息
     *
     * @param id id
     * @return ResponseBean
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除客户联系方式", notes = "根据ID删除客户联系方式")
    @ApiImplicitParam(name = "id", value = "客户ID", required = true, paramType = "path")
    @Log(value = "删除客户联系方式", type = 0)
    public ResponseBean<Boolean> deleteUser(@PathVariable String id) {
        try {
            ContactWay contactWay = new ContactWay();
            contactWay.setId(id);
            return new ResponseBean<>(contactWayMapper.delete(contactWay) > 0);
        } catch (Exception e) {
            log.error("删除客户联系方式失败！", e);
            throw new CommonException("删除客户联系方式失败！");
        }
    }
}
