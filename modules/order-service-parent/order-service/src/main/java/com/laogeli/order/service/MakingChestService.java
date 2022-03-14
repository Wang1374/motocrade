package com.laogeli.order.service;

import com.laogeli.common.core.service.CrudService;
import com.laogeli.common.core.utils.DateUtils;
import com.laogeli.common.core.utils.IdGen;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.order.api.module.HairBody;
import com.laogeli.order.api.module.Lcl;
import com.laogeli.order.api.module.MakingChest;
import com.laogeli.order.mapper.MakingChestMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 做箱信息 service
 *
 * @author wang
 * @date 2020-07-09
 */
@Slf4j
@AllArgsConstructor
@Service
public class MakingChestService extends CrudService<MakingChestMapper, MakingChest> {

    private final CostService costService;

    private final MakingChestMapper makingChestMapper;

    /**
     * 获取门点 与 件毛体列表
     *
     * @param mcId mcId
     * @return List
     */
    public List<Lcl> getLclList(String[] mcId) {
        // 通过id数组 批量查询，将查询的结果集 中的json字符串 转数组对象。
        List<Lcl> LclList = dao.getLclList(mcId);
        for (Lcl lcl : LclList) {
            JSONArray json = JSONArray.fromObject(lcl.getHairBody());
            List<HairBody> hairBodyList = (List<HairBody>) JSONArray.toCollection(json, HairBody.class);
            lcl.setHairBodyList(hairBodyList);
        }
        return LclList;
    }

    /**
     * 批量添加做箱信息 与 门点与件毛体信息
     *
     * @param makingChestList
     * @return int
     */
    @Transactional
    public int addMcAndLcl(List<MakingChest> makingChestList) {
        for (MakingChest makingChest : makingChestList) {
            makingChest.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
            this.packageLcl(makingChest.getLclList(), makingChest.getId());
        }
        // 批量添加做箱信息
        return dao.addMc(makingChestList);
    }

    /**
     * 批量删除门点与件毛体信息、费用信息、做箱信息
     *
     * @param ids
     * @return int
     */
    @Transactional
    public int delCostLclMc(String[] ids) {
        // 通过做箱id 批量删除门点与件毛体
        dao.delAllLcl(ids);
        // 通过做箱id 批量删除费用信息
        costService.delAllCost(ids);
        // 通过做箱id  批量删除做箱信息
        return dao.delAllMc(ids);
    }

    /**
     * 通过做箱id 批量修改做箱信息 与 门点与件毛体信息
     *
     * @param makingChestList
     * @return int
     */
    @Transactional
    public int updateLclMc(List<MakingChest> makingChestList, String[] ids) {
        // 通过做箱id 批量删除门点与件毛体
        dao.delAllLcl(ids);
        for (MakingChest makingChest : makingChestList) {
            this.packageLcl(makingChest.getLclList(), makingChest.getId());
            makingChest.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
        }
        // 通过做箱id批量修改做箱信息
        return dao.batchUpdateMc(makingChestList);
    }

    /**
     * 批量添加门点与件毛体信息
     *
     * @param lclList,mcId
     * @return int
     */
    @Transactional
    public int packageLcl(List<Lcl> lclList, String mcId) {
        if (lclList.size() > 0) {
            for (Lcl lcl : lclList) {
                lcl.setId(IdGen.snowflakeId());
                lcl.setMcId(mcId);
                // 转成json 字符串 存入数据库
                JSONArray array = JSONArray.fromObject(lcl.getHairBodyList());
                lcl.setHairBody(array.toString());
            }
            // 批量添加门点与件毛体
            return dao.addLcl(lclList);
        }
        return 0;
    }

    /**
     * 接单 批量添加做箱信息 与 门点与件毛体信息
     *
     * @param makingChestList
     * @return int
     */
    @Transactional
    public int addOrderMcl(List<MakingChest> makingChestList, String orderId, String creator) {
        for (MakingChest makingChest : makingChestList) {
            makingChest.setId(IdGen.snowflakeId());
            makingChest.setBusinessOrderId(orderId);
            makingChest.setCaseNumber(String.valueOf(makingChestList.indexOf(makingChest) + 1));
            makingChest.setTareWeight(0.0);
            makingChest.setTypeOfShipping(1);
            makingChest.setCreator(creator);
            makingChest.setCreateDate(DateUtils.asDate(LocalDateTime.now()));
            makingChest.setApplicationCode("EXAM");
            this.packageLcl(makingChest.getLclList(), makingChest.getId());
        }
        // 批量添加做箱信息
        return dao.addMc(makingChestList);
    }

    /**
     * 根据做箱id查询做箱状态
     *
     * @param id
     * @return
     */
    @Transactional
    public Integer getMakingStatus(String id) {
        return makingChestMapper.getMakingStatus(id);
    }

    /**
     * 通过做箱id修改做箱状态
     *
     * @return
     */
    @Transactional
    public Integer updateById(MakingChest makingChest) {
        return makingChestMapper.updateById(makingChest);
    }

    /**
     * 根据订单id查询正常做箱的数量
     *
     * @param orderId
     * @return
     */
    @Transactional
    public int getIsMakingStatus(String orderId) {
        return makingChestMapper.getIsMakingStatus(orderId);
    }

    /**
     * 根据下单编号修改订单状态
     *
     * @param placeOrderNubmer
     * @return
     */
    @Transactional
    public int updateOrderCenter(String placeOrderNubmer) {
        return makingChestMapper.updateOrderCenter(placeOrderNubmer);
    }

    /**
     * 根据订单表id修改做箱状态 为0 取消做箱
     *
     * @param orderId
     * @return
     */
    @Transactional
    public int updateMakingStatus(String orderId) {
        return makingChestMapper.cancelMaking(orderId);
    }

}
