package com.laogeli.order.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.order.api.module.SendReceipAddress;
import com.laogeli.order.api.vo.DoorsVo;
import com.laogeli.order.api.vo.SendReceipVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wang
 * @Date 2021-07-01 14:06
 **/
@Mapper
public interface SendReceipMapper extends CrudMapper<SendReceipAddress> {


    /**
     * 通过客户id 查询寄单地址
     *
     * @param ids
     * @return List<SendReceipVo>
     */
    List<SendReceipVo> getListByIds(String[] ids);
}
