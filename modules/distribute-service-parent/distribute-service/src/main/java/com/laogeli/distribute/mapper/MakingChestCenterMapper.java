package com.laogeli.distribute.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.distribute.api.module.LclCenter;
import com.laogeli.distribute.api.module.MakingChestCenter;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 客户端做箱信息mapper
 *
 * @author yangyu
 * @date 2020-12-16
 */
@Mapper
public interface MakingChestCenterMapper extends CrudMapper<MakingChestCenter> {

    /**
     * 批量添加门点与件毛体
     *
     * @return
     */
    int addLclCenter(List<LclCenter> lclCenterList);

    /**
     * 保存做箱
     *
     * @param makingChestCenterList
     * @return
     */
    int addMakingChestCenter(List<MakingChestCenter> makingChestCenterList);

    /**
     * 获取门点 与 件毛体列表
     *
     * @param mcId mcId
     * @return List
     */
    List<LclCenter> getLclCenterList(String[] mcId);

    /**
     * 通过做箱id批量删除门点与件毛体
     *
     * @param ids
     * @return int
     */
    int delAllLcl(String[] ids);

    /**
     * 根据下单编号批量删除做箱信息
     * @param id
     * @return
     */
    int delMcByPlaceorderId(String id);


}
