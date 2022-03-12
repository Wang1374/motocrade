package com.laogeli.order.service;

import com.laogeli.common.core.service.CrudService;
import com.laogeli.order.api.module.SendReceipAddress;
import com.laogeli.order.api.vo.DoorsVo;
import com.laogeli.order.api.vo.SendReceipVo;
import com.laogeli.order.mapper.SendReceipMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author beifang
 * @Date 2021-07-01 14:13
 **/
@Service
@Slf4j
@AllArgsConstructor
public class SendReceipService extends CrudService<SendReceipMapper, SendReceipAddress> {


    /**
     * 新增客户信息寄单地址
     * @param sendReceipAddress
     * @return
     */
    public int insertSendReceip(SendReceipAddress sendReceipAddress){
        return  dao.insert(sendReceipAddress);
    }

    /**
     * 修改客户信息寄单地址
     * @param sendReceipAddress
     * @return
     */
    public int updateReceip(SendReceipAddress sendReceipAddress){
        return dao.update(sendReceipAddress);
    }


    /**
     * 通过客户ids 查询门点
     *
     * @param ids
     * @return List<DoorsVo>
     */
    public List<SendReceipVo> getListByIds(String[] ids) {
        return dao.getListByIds(ids);
    }

}
