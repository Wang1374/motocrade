package com.laogeli.distribute.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.laogeli.common.core.service.CrudService;
import com.laogeli.common.core.utils.IdGen;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.distribute.api.module.LclCenter;
import com.laogeli.distribute.api.module.HairBody;
import com.laogeli.distribute.api.module.MakingChestCenter;
import com.laogeli.distribute.mapper.MakingChestCenterMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 客户端做箱信息 service
 *
 * @author yangyu
 * @date 2020-12-16
 */
@Slf4j
@AllArgsConstructor
@Service
public class MakingChestCenterService extends CrudService<MakingChestCenterMapper, MakingChestCenter> {

    /**
     * 添加做箱
     *
     * @param makingChestCenterList
     * @param placeId
     * @return
     */
    @Transactional
    public int placeMaking(List<MakingChestCenter> makingChestCenterList, String placeId) {
        //遍历做箱
        for (MakingChestCenter makingChestCenter : makingChestCenterList
        ) {
            makingChestCenter.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());

            makingChestCenter.setPlaceOrderId(placeId);
            //保存件毛体数据
            packageLclCenter(makingChestCenter.getLclList(), makingChestCenter.getId());

        }
        //批量添加做箱计划
        return dao.addMakingChestCenter(makingChestCenterList);
    }

    //添加件毛体
    public int packageLclCenter(List<LclCenter> lclCenterList, String mcId) {
        if (lclCenterList.size() > 0) {
            for (LclCenter lclCenter : lclCenterList
            ) {
                lclCenter.setId(IdGen.snowflakeId());
                lclCenter.setMcId(mcId);
                // 转成json 字符串 存入数据库
                String json = JSON.toJSONString(lclCenter.getHairBodyList());
                lclCenter.setHairBody(json);
            }
            return dao.addLclCenter(lclCenterList);
        }
        return 0;


    }

    /**
     * 获取门点 与 件毛体列表
     *
     * @param mcId mcId
     * @return List
     */
    public List<LclCenter> getLclCenterList(String[] mcId) {
        // 通过id数组 批量查询，将查询的结果集 中的json字符串 转数组对象。
        List<LclCenter> LclList = dao.getLclCenterList(mcId);
        for (LclCenter lclCenter : LclList) {
            List<HairBody> hairBodyList = JSONArray.parseArray(lclCenter.getHairBody(), HairBody.class);
            lclCenter.setHairBodyList(hairBodyList);
        }
        return LclList;
    }


    /**
     * 批量删除门点与件毛体信息
     *
     * @param ids
     * @return int
     */
    @Transactional
    public int delDoorAndLcl(String[] ids) {
        // 通过做箱id 批量删除门点与件毛体
        return dao.delAllLcl(ids);

    }

    /**
     * 根据下单id删除做箱信息
     * @param placeOrderId
     * @return
     */
    @Transactional
    public int delMcByPlaceorderId(String placeOrderId){
        return dao.delMcByPlaceorderId(placeOrderId);
    }


}
