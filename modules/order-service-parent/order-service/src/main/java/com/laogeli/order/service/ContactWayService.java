package com.laogeli.order.service;

import com.laogeli.common.core.service.CrudService;
import com.laogeli.order.api.module.ContactWay;
import com.laogeli.order.api.vo.ContactWayVo;
import com.laogeli.order.mapper.ContactWayMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 客户联系方式service
 *
 * @author wang
 * @date 2020-06-11
 */
@Slf4j
@AllArgsConstructor
@Service
public class ContactWayService extends CrudService<ContactWayMapper, ContactWay> {

    private ContactWayMapper contactWayMapper;

    /**
     * 通过客户id数组 查询联系人
     *
     * @param ids
     * @return List<ContactWayVo>
     */
    public List<ContactWayVo> getListByIds(String[] ids) {
        return dao.getListByIds(ids);
    }

    /**
     * 新增客户
     * 1,判断客户是否存在
     * 2,新增
     *
     * @param contactWay
     * @return
     */
    public Integer insertContactWay(ContactWay contactWay) {
        Integer num = 0;
        ContactWay obj = dao.getByContact(contactWay);
        if (obj == null) {
            num = dao.insert(contactWay);
        }
        return num;
    }

    public Integer updateContact(ContactWay contactWay) {
        ContactWay result = contactWayMapper.getByContact(contactWay);
        if (result == null) {
            contactWayMapper.update(contactWay);
            return 1;
        } else {
            //判断是否是当前数据
            if (result.getId().equals(contactWay.getId())) {
                contactWayMapper.update(contactWay);
                return 1;
            } else {
                return 0;
            }
        }
    }
}
