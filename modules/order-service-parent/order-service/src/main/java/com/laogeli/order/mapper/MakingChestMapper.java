package com.laogeli.order.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.order.api.module.Lcl;
import com.laogeli.order.api.module.MakingChest;
import com.laogeli.order.api.vo.WxMakingChestVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 做箱信息mapper
 *
 * @author wang
 * @date 2020-07-09
 */
@Mapper
public interface MakingChestMapper extends CrudMapper<MakingChest> {

    /**
     * 获取门点 与 件毛体列表
     *
     * @param mcId mcId
     * @return List
     */
    List<Lcl> getLclList(String[] mcId);

    /**
     * 批量添加 做箱信息
     *
     * @param makingChestList
     * @return int
     */
    int addMc(List<MakingChest> makingChestList);

    /**
     * 通过做箱id批量删除 做箱信息
     *
     * @param ids
     * @return int
     */
    int delAllMc(String[] ids);

    /**
     * 通过做箱id 批量修改 做箱信息
     *
     * @param makingChestList
     * @return int
     */
    int batchUpdateMc(List<MakingChest> makingChestList);


    /**
     * 批量添加门点与件毛体
     *
     * @param lclList
     * @return int
     */
    int addLcl(List<Lcl> lclList);

    /**
     * 通过做箱id批量删除门点与件毛体
     *
     * @param ids
     * @return int
     */
    int delAllLcl(String[] ids);


    /**
     * 根据订单id查询做箱信息
     *
     * @param ids
     * @return
     */
    List<MakingChest> getMakingChestByIds(String[] ids);

    /**
     * 根据做箱id查询做箱状态
     *
     * @param id
     * @return
     */
    Integer getMakingStatus(String id);

    /**
     * 通过做箱id修改做箱状态
     *
     * @param makingChest
     * @return
     */
    Integer updateById(MakingChest makingChest);

    /**
     * 根据订单id查询正常做箱的数量
     *
     * @param orderId
     * @return
     */
    int getIsMakingStatus(String orderId);

    /**
     * 根据下单编号修改下单表订单状态
     *
     * @param placeOrderNumber
     * @return
     */
    int updateOrderCenter(String placeOrderNumber);

    /**
     * 根据订单id修改做箱状态为取消做箱
     *
     * @param orderId
     * @return
     */
    int cancelMaking(String orderId);

    /**
     * 根据做箱id 保存/修改箱货照片
     * @param makingChest
     * @return
     */
    int updatePictureById(MakingChest makingChest);

    int updateUrlById(@Param("id") String id,@Param("receiptUrl") String receiptUrl);

//    /**
//     * 根据做箱id获取做箱信息
//     * @param id
//     * @return
//     */
//    MakingChest findMakingChestById(String id);

    /**
     * 根据微信用户的手机号获取箱货信息
     * @param wxMakingChestVo
     * @return
     */

    List<WxMakingChestVo> getByPhoneNumber(WxMakingChestVo wxMakingChestVo);
}
