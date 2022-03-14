package com.laogeli.order.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.order.api.module.ContactWay;
import com.laogeli.order.api.vo.ContactWayVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 客户联系方式mapper
 *
 * @author wang
 * @date 2020-06-11
 */
@Mapper
public interface ContactWayMapper extends CrudMapper<ContactWay> {

    /**
     * 通过客户id数组 查询联系人
     *
     * @param ids
     * @return List<ContactWayVo>
     */
    List<ContactWayVo> getListByIds(String[] ids);

    /**
     * 查询客户是否存在
     *
     * @param contactWay
     * @return
     */
    ContactWay getByContact(ContactWay contactWay);
}
