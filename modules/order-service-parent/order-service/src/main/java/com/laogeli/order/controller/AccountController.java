package com.laogeli.order.controller;

import com.laogeli.common.core.exceptions.CommonException;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.common.log.annotation.Log;
import com.laogeli.order.api.module.Account;
import com.laogeli.order.api.module.Clientele;
import com.laogeli.order.mapper.AccountMapper;
import com.laogeli.order.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 客户账户信息管理
 *
 * @author yangyu
 * @date 2020-06-11
 */
@Slf4j
@AllArgsConstructor
@Api("客户账户信息管理")
@RestController
@RequestMapping("/v1/account")
public class AccountController {

    private final AccountService accountService;

    private final AccountMapper accountMapper;
    private final RedissonClient redissonClient;

    /**
     * 查询客户账户信息列表
     *
     * @param customerId customerId
     * @return List<Account>
     */
    @GetMapping("accountList/{customerId}")
    @ApiOperation(value = "查询客户账户信息列表", notes = "根据客户id查询客户账户信息列表")
    @ApiImplicitParam(name = "customerId", value = "客户ID", required = true, paramType = "path")
    public List<Account> accountList(@PathVariable String customerId) {
        Account account = new Account();
        account.setCustomerId(customerId);
        return accountService.findList(account);
    }

    /**
     * 新增客户账户信息
     *
     * @param account account
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增客户账户信息", notes = "新增客户账户信息")
    @ApiImplicitParam(name = "account", value = "客户账户信息实体account", required = true, dataType = "Account")
    @Log(value = "新增客户账户信息", type = 0)
    public ResponseBean<Integer> addAccount(@RequestBody Account account) {
        RLock lock = redissonClient.getLock(account.getInvoiceTitle()+account.getBelongCompaniesId());
        boolean getLock = false;
        try {
            Integer count = 0;
            if (getLock = lock.tryLock(0, 5, TimeUnit.SECONDS)) {
                account.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
                count = accountService.insertAccount(account);
            } else {
                log.info("Redisson分布式锁没有获得锁:{},ThreadName:{}", account.getInvoiceTitle()+account.getBelongCompaniesId(), Thread.currentThread().getName());
            }
            if (count == 0) {
                return new ResponseBean<>(count, "客户账户信息已经存在");
            } else {
                return new ResponseBean<>(count, "新增成功");
            }
        } catch (InterruptedException e) {
            log.error("新增客户账户信息, Redisson 获取分布式锁异常, 异常信息:{}", e);
            throw new CommonException("新增客户账户信息");
        } finally {
            if (!getLock) {
                return new ResponseBean<>(-1, "请勿重复操作");
            }
            lock.unlock();
            log.info("Redisson分布式锁释放锁:{},ThreadName :{}", account.getInvoiceTitle()+account.getBelongCompaniesId(), Thread.currentThread().getName());
        }
    }

    /**
     * 更新客户账户信息
     *
     * @param account account
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新客户账户信息", notes = "根据客户id更新客户账户信息")
    @ApiImplicitParam(name = "account", value = "客户实体account", required = true, dataType = "Account")
    @Log(value = "修改客户账户信息", type = 0)
    public ResponseBean<Integer> updateAccount(@RequestBody Account account) {

        RLock lock = redissonClient.getLock(account.getInvoiceTitle()+account.getBelongCompaniesId());
        boolean getLock = false;
        try {
            Integer count = 0;
            if (getLock = lock.tryLock(0, 5, TimeUnit.SECONDS)) {
                account.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
                count = accountService.updateAccount(account);
            } else {
                log.info("Redisson分布式锁没有获得锁:{},ThreadName:{}", account.getInvoiceTitle()+account.getBelongCompaniesId(), Thread.currentThread().getName());
            }
            if (count == 0) {
                return new ResponseBean<>(count, "客户账户信息已经存在");
            } else {
                return new ResponseBean<>(count, "修改成功");
            }
        } catch (InterruptedException e) {
            log.error("修改客户账户信息失败, Redisson 获取分布式锁异常, 异常信息:{}", e);
            throw new CommonException("修改客户账户信息失败");
        } finally {
            if (!getLock) {
                return new ResponseBean<>(-1, "请勿重复操作");
            }
            lock.unlock();
            log.info("Redisson分布式锁释放锁:{},ThreadName :{}", account.getInvoiceTitle()+account.getBelongCompaniesId(), Thread.currentThread().getName());
        }
    }

    /**
     * 删除账户
     *
     * @param id id
     * @return ResponseBean
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除账户", notes = "根据ID删除帐户")
    @ApiImplicitParam(name = "id", value = "客户ID", required = true, paramType = "path")
    @Log(value = "删除账户", type = 0)
    public ResponseBean<Boolean> deleteUser(@PathVariable String id) {
        try {
            Account account = new Account();
            account.setId(id);
            return new ResponseBean<>(accountMapper.delete(account) > 0);
        } catch (Exception e) {
            log.error("删除账户失败！", e);
            throw new CommonException("删除账户失败！");
        }
    }
}
