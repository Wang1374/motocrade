package com.laogeli.order.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.order.api.module.Cost;
import com.laogeli.order.api.vo.ClientCostVo;
import com.laogeli.order.api.vo.CostVo;
import com.laogeli.order.api.vo.ProfitVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 费用信息 mapper
 *
 * @author yangyu
 * @date 2020-08-27
 */
@Mapper
public interface CostMapper extends CrudMapper<Cost> {

    /**
     * 获取列表数据
     *
     * @param cost
     * @return List
     */
    List<Cost> getList(Cost cost);

    /**
     * 获取分页列表数据
     *
     * @param costVo
     * @return List
     */
    List<CostVo> findList(CostVo costVo);

    /**
     * 批量添加 费用信息
     *
     * @param costList
     * @return int
     */
    int addCostList(@Param("list") List<Cost> costList);

    /**
     * 通过做箱id批量删除费用信息
     *
     * @param ids ids
     * @return int
     */
    int delAllCost(String[] ids);

    /**
     * 通过做箱id 与 费用类型删除费用信息
     *
     * @param mcId,costTypes
     * @return int
     */
    int deleteCost(@Param("mcId") String mcId, @Param("costTypes") int costTypes, @Param("vehicleCost") String vehicleCost);

    /**
     * 批量修改
     *
     * @param costList
     * @return int
     */
    int batchUpdate(@Param("list") List<Cost> costList);

    /**
     * 分组查询获取业务列表收益数据
     *
     * @param entity entity
     * @return T
     */
    List<ProfitVo> getProfitList(ProfitVo entity);

    /**
     * 根据往来单位id分组查询获取业务列表收益数据
     *
     * @param entity entity
     * @return T
     */
    List<ProfitVo> getBtypeProfitList(ProfitVo entity);

    /**
     * 根据往来单位id分组查询获取业务列表收益数据
     * 实收，实付，额外实收，额外实付
     *
     * @return T
     */
    List<ProfitVo> getRealisticBtypeProfitList(@Param("belongCompaniesId") String belongCompaniesId,
                                               @Param("btypeIdArray") List<String> btypeIdArray);

    /**
     * 查询invoicUrl
     *
     * @param cost
     * @return
     */
    List<Cost> findInvoiceUrl(Cost cost);

    /**
     * 分组查询获取车辆列表收益数据
     *
     * @param entity entity
     * @return T
     */
    List<ProfitVo> getCarProfitList(ProfitVo entity);

    /**
     * 根据cost id修改代垫发票的url
     *
     * @param costId
     * @param imageUrl
     * @return
     */
    int updateInvoiceUrl(@Param("costId") String costId,
                         @Param("imageUrl") String imageUrl);


    /**
     * 分组查询获取车辆列表 每月收益数据
     *
     * @param entity entity
     * @return T
     */
    List<ProfitVo> getMonthlyCarProfit(ProfitVo entity);

    /**
     * 查询平台费用明细列表分页
     *
     * @param entity entity
     * @return List
     */
    List<CostVo> platformCostListPage(CostVo entity);

    /**
     * 查询平台费用应付费用列表分页
     *
     * @param entity entity
     * @return List
     */
    List<ClientCostVo> findClientPayCost(ClientCostVo entity);

    /**
     * 根据做箱id获取箱子毛利
     * @param mcId
     * @return
     */
    ProfitVo getMakingProfit(String mcId);

}
