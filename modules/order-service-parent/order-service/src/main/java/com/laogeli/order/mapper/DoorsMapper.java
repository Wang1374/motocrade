package com.laogeli.order.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.order.api.module.Doors;
import com.laogeli.order.api.vo.DoorsVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 门店信息mapper
 *
 * @author wang
 * @date 2020-06-15
 */
@Mapper
public interface DoorsMapper extends CrudMapper<Doors> {

    /**
     * 通过客户id 查询门点
     *
     * @param ids
     * @return List<DoorsVo>
     */
    List<DoorsVo> getListByIds(String[] ids);

    /*
     *根据客户id查询门点信息
     * @Param
     * @return
     */
    List<Doors> findListByCustomerId(String customerId);

    /**
     * 客户端修改门点
     * @param doors
     * @return
     */
    int updateCustomerDoor(Doors doors);

    /**
     * 根据客户id修改门点的客户id以及客户名称
     * @param doors
     * @return
     */
    int updateByClienteleId(Doors doors);

}
