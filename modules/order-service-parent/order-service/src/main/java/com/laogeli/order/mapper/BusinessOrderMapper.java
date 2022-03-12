package com.laogeli.order.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.common.log.annotation.Log;
import com.laogeli.order.api.module.BusinessOrder;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务订单mapper
 *
 * @author yangyu
 * @date 2020-06-18
 */
@Mapper
public interface BusinessOrderMapper extends CrudMapper<BusinessOrder> {

    /**
     * 查询 当天 最大一条订单编号
     *
     * @param belongCompaniesId belongCompaniesId
     * @return String
     */
    String getOrderNumberByTypeDay(@Param("belongCompaniesId") String belongCompaniesId, @Param("orderType") int orderType);

    /**
     * 查询 当月 最大一条订单编号
     *
     * @param belongCompaniesId belongCompaniesId
     * @return String
     */
    String getOrderNumberByTypeMonth(@Param("belongCompaniesId") String belongCompaniesId, @Param("orderType") int orderType);

    /**
     * 新增订单
     *
     * @param businessOrder businessOrder
     * @return int
     */
    int addBusinessOrder(BusinessOrder businessOrder);

    /**
     * 查询平台订单列表分页
     *
     * @param entity entity
     * @return List
     */
    List<BusinessOrder> findPlatformOrderList(BusinessOrder entity);

    /**
     * 查询客户端订单列表分页
     * @param entity
     * @return
     */
    List<BusinessOrder> findPlatformClientList(BusinessOrder entity);

    /**
     * 根据订单编号查找订单数据
     * @param orderNumber
     * @return
     */
    BusinessOrder getDataByOrderNumber(String orderNumber);

    /**
     * 根据id查询当前已读/未读状态
     *
     * @param id
     * @param companyId
     * @return
     */
    BusinessOrder getAlreadyOrder(@Param("id") String id, @Param("companyId") String companyId);

    /**
     * 添加当前公司id到company_ids 为已读
     *
     * @param
     * @return
     */
    Integer updateCompanyIdsById(@Param("id") String id, @Param("companyId") String companyId);

    /**
     * 根据id 修改订单状态
     *
     * @param id,businessState
     * @return
     */
    Integer updateById(@Param("id") String id, @Param("businessState") int businessState, @Param("reason") String reason);

    /**
     * 根据id修改操作记录
     * @param businessOrder
     * @return
     */
    Integer updateExceptionById(BusinessOrder businessOrder);
    /**
     * 修改订单状态
     *
     */
    Integer updateBusinessState(BusinessOrder businessOrder);
}
