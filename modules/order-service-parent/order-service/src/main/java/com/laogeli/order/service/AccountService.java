package com.laogeli.order.service;

import com.laogeli.common.core.service.CrudService;
import com.laogeli.order.api.module.Account;
import com.laogeli.order.mapper.AccountMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 客户账户信息service
 *
 * @author yangyu
 * @date 2020-06-11
 */
@Slf4j
@AllArgsConstructor
@Service
public class AccountService extends CrudService<AccountMapper, Account> {

    private final AccountMapper accountMapper;

    /**
     * 新增客户
     * 1,判断客户是否存在
     * 2,新增
     * @param account
     * @return
     */
    public Integer insertAccount(Account account) {
        Integer num = 0;
        Account obj = dao.getByAccount(account);
        if (obj == null) {
            num = dao.insert(account);
        }
        return num;
    }

    public Integer updateAccount(Account account) {
        Account result = accountMapper.getByAccount(account);
        if (result == null) {
            //执行修改
            accountMapper.update(account);
            return 1;
        } else {
            if (result.getId().equals(account.getId())) {
                //判断是否是当前数据
                //执行修改
                accountMapper.update(account);
                return 1;
            } else {
                return 0;
            }
        }
    }
}