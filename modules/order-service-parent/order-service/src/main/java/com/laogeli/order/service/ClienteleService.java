package com.laogeli.order.service;

import com.laogeli.common.core.service.CrudService;
import com.laogeli.order.api.module.Account;
import com.laogeli.order.api.module.Clientele;
import com.laogeli.order.api.module.ContactWay;
import com.laogeli.order.api.module.Doors;
import com.laogeli.order.api.vo.ClienteleVo;
import com.laogeli.order.mapper.AccountMapper;
import com.laogeli.order.mapper.ClienteleMapper;
import com.laogeli.order.mapper.ContactWayMapper;
import com.laogeli.order.mapper.DoorsMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 客户信息service
 *
 * @author yangyu
 * @date 2020-06-09
 */
@Slf4j
@AllArgsConstructor
@Service
public class ClienteleService extends CrudService<ClienteleMapper, Clientele> {

    private final AccountMapper accountMapper;

    private final ContactWayMapper contactWayMapper;

    private final ClienteleMapper clienteleMapper;

    private final DoorsMapper doorsMapper;

    /**
     * 删除客户信息，账户信息 与 联系方式
     *
     * @param clientele clientele
     * @return int
     */
    @Transactional
    public int delClientele(Clientele clientele) {
        int del;

        Account account = new Account();
        account.setCustomerId(clientele.getId());
        accountMapper.delete(account);

        ContactWay contactWay = new ContactWay();
        contactWay.setCustomerId(clientele.getId());
        contactWayMapper.delete(contactWay);

        del = dao.delete(clientele);
        return del;
    }

    /**
     * 通过企业标识id 查询客户
     *
     * @param corporateIdentify
     * @return List<ClienteleVo>
     */
    public List<ClienteleVo> getListById(String corporateIdentify) {
        return dao.getListById(corporateIdentify);
    }

    /**
     * 新增客户
     */
    public Integer insertClientele(Clientele clientele) {
        Integer num = 0;
        Clientele obj = dao.get(clientele);
        if (obj == null) {
            num = dao.insert(clientele);
        }
        return num;
    }

    /**
     * 修改客户
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer updateClientele(Clientele clientele) {
        Integer num = 0;
        Clientele obj = dao.get(clientele);
        if (obj != null) {
            // 判断 是否修改的当前数据 如果id 与 公司名相同 为当前数据
            if (obj.getId().equals(clientele.getId())) {
                num = dao.update(clientele);
            } else {
                num = 0;
            }
        } else {
            //公司抬头已经修改
            // 1修改客户
            num = dao.update(clientele);
            // 2 根据客户id 修改相关的门点以及账户信息
            Account account = new Account();
            account.setCustomerId(clientele.getId());
            account.setInvoiceTitle(clientele.getCompanyName());
            accountMapper.updateByClienteleId(account);

            Doors doors  = new Doors();
            doors.setCustomerId(clientele.getId());
            doors.setCustomerName(clientele.getCompanyName());
            doorsMapper.updateByClienteleId(doors);
        }
        return num;
    }


    /**
     * 查找往来单位数据
     * @param query
     * @return
     */
    public int   updateClientByName(String query,String updateData){
        return clienteleMapper.updateClientByName(query,updateData);
    }

}
