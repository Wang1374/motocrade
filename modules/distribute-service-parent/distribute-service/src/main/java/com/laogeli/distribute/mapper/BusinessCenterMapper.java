package com.laogeli.distribute.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.distribute.api.module.BusinessCenter;
import com.laogeli.distribute.api.vo.BusinessCenterVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 下单接单mapper
 *
 * @author wang
 * @date 2020-11-17
 */
@Mapper
public interface BusinessCenterMapper extends CrudMapper<BusinessCenter> {

    /**
     * 获取最大的订单编号
     *
     * @return
     */
    String getMostBig();

    /**
     * 通过id 查询订单状态
     *
     * @param id
     * @return int
     */
    int getOrderStatus(String id);

    /**
     * 通过id 修改订单状态
     *
     * @param businessCenter
     * @return int
     */
    int updateById(BusinessCenter businessCenter);

    /**
     * 根据订单id 更改订单表状态
     *
     * @param id,businessState
     * @return int
     */
    int updateByOrderId(@Param("id") String id, @Param("businessState") int businessState);

    /**
     * 根据下单编号 修改派单状态
     *
     * @param placeOrderNumber,orderStatus
     * @return int
     */
    int updateByNumber(@Param("placeOrderNumber") String placeOrderNumber, @Param("orderStatus") int orderStatus);

    /**
     * 派单列表分页查询
     *
     * @param businessCenterVo
     * @return List
     */
    List<BusinessCenterVo> findDispatchList(BusinessCenterVo businessCenterVo);

    /**
     * 根据下单id修改下单数据
     *
     * @param businessCenter
     * @return
     */
    int editPlaceOrderById(BusinessCenter businessCenter);


    /**
     * 根据订单id修改做箱状态为取消做箱
     *
     * @param orderId
     * @return
     */
    int cancelMaking(String orderId);
}
