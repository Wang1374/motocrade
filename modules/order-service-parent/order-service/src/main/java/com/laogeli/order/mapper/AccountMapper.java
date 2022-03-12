package com.laogeli.order.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.order.api.module.Account;
import org.apache.ibatis.annotations.Mapper;

/**
 * 客户账户信息mapper
 *
 * @author yangyu
 * @date 2020-06-11
 */
@Mapper
public interface AccountMapper extends CrudMapper<Account> {

    /**
     * 根据发票抬头判断是否存在
     * @param account
     * @return
     */
    Account getByAccount(Account account);

    /**
     * 根据客户id修改账户的抬头
     * @param account
     * @return
     */
    int updateByClienteleId(Account account);
}
