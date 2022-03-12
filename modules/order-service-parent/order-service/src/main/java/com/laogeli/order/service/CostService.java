package com.laogeli.order.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.laogeli.common.core.service.CrudService;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.order.api.module.Cost;
import com.laogeli.order.api.vo.ClientCostVo;
import com.laogeli.order.api.vo.CostVo;
import com.laogeli.order.api.vo.ProfitVo;
import com.laogeli.order.mapper.CostMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 费用信息 service
 *
 * @author yangyu
 * @date 2020-08-27
 */
@Slf4j
@AllArgsConstructor
@Service
public class CostService extends CrudService<CostMapper, Cost> {

    private final CostMapper costMapper;

    /**
     * 查询列表
     *
     * @param cost
     * @return List
     */
    public List<Cost> getList(Cost cost) {
        return dao.getList(cost);
    }

    /**
     * 查询分页
     *
     * @param page   page
     * @param entity entity
     * @return PageInfo
     */
    public PageInfo<CostVo> findPage(PageInfo<CostVo> page, CostVo entity) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return new PageInfo<CostVo>(dao.findList(entity));
    }

    /**
     * 通过做箱id批量删除费用信息
     *
     * @param ids
     * @return int
     */
    @Transactional
    public int delAllCost(String[] ids) {
        return dao.delAllCost(ids);
    }

    /**
     * 先删除 再新增
     *
     * @param costList
     * @return int
     */
    @Transactional
    public int deleteOrAdd(List<Cost> costList) {
        int num = dao.deleteCost(costList.get(0).getMcId(), costList.get(0).getCostTypes(), costList.get(0).getVehicleCost());
        // 收费项目不为空就新增
        if (costList.get(0).getPayItems() != null) {
            num = this.addCostList(costList);
        }
        return num;
    }

    /**
     * 批量添加费用信息
     *
     * @param costList
     * @return int
     */
    @Transactional
    public int addCostList(List<Cost> costList) {
        for (Cost cost : costList) {
            cost.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
        }
        // 批量添加费用信息
        return dao.addCostList(costList);
    }

    /**
     * 批量修改
     *
     * @param costList
     * @return int
     */
    public int batchUpdate(List<Cost> costList) {
        return dao.batchUpdate(costList);
    }


    /**
     * 查询分页,获取收益
     *
     * @param page   page
     * @param entity entity
     * @return PageInfo
     */
    public PageInfo<ProfitVo> findProfitPage(PageInfo<Cost> page, ProfitVo entity) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return new PageInfo<ProfitVo>(dao.getProfitList(entity));
    }


    /**
     * 查询平台分页,获取收益
     *
     * @param page   page
     * @param entity entity
     * @return PageInfo
     */
    public PageInfo<ClientCostVo> findPageClient(PageInfo<Cost> page, ClientCostVo entity) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return new PageInfo<ClientCostVo>(dao.findClientPayCost(entity));
    }


    /**
     * 根据往来单位查询费用
     *
     * @param page
     * @param entiey
     * @return
     */
    @Transactional
    public PageInfo<ProfitVo> getRealisticAndShouldProfit(PageInfo<Cost> page, ProfitVo entiey) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        //查询所有的
        List<ProfitVo> profitPageList = costMapper.getBtypeProfitList(entiey);

        //根据业务毛利结果，过滤往来单位
        List<String> btypeIdArray = profitPageList.stream().map(ProfitVo::getBtypeId).collect(Collectors.toList());
        if (btypeIdArray.size() > 0) {
            //查实收
            List<ProfitVo> profitRealisticPage = costMapper.getRealisticBtypeProfitList(entiey.getBelongCompaniesId(), btypeIdArray);
            for (ProfitVo profitVo :
                    profitPageList) {
                for (ProfitVo costRealisticVo :
                        profitRealisticPage) {
                    if (profitVo.getBtype().equals(costRealisticVo.getBtype())) {
                        profitVo.setRealisticAchieve(costRealisticVo.getRealisticAchieve());
                        profitVo.setRealisticPay(costRealisticVo.getRealisticPay());
                        profitVo.setRealisticExtraAchieve(costRealisticVo.getRealisticExtraPay());
                        profitVo.setRealisticExtraPay(costRealisticVo.getRealisticExtraPay());
                    }
                    if (profitVo.getRealisticAchieve() == null || profitVo.getRealisticPay() == null || profitVo.getRealisticExtraPay() == null
                            || profitVo.getRealisticExtraAchieve() == null) {
                        profitVo.setRealisticAchieve(new BigDecimal(0));
                        profitVo.setRealisticPay(new BigDecimal(0));
                        profitVo.setRealisticExtraAchieve(new BigDecimal(0));
                        profitVo.setRealisticExtraPay(new BigDecimal(0));
                    }
                }
            }
        }
        return new PageInfo<>(profitPageList);
    }

    /**
     * 查询平台费用明细列表分页
     *
     * @param page   page
     * @param entity entity
     * @return PageInfo
     */
    public PageInfo<CostVo> platformCostListPage(PageInfo<CostVo> page, CostVo entity) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return new PageInfo<CostVo>(dao.platformCostListPage(entity));
    }
}
