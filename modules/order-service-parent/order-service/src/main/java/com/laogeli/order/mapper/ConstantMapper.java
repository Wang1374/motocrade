package com.laogeli.order.mapper;

import com.laogeli.order.api.module.ShipsName;
import com.laogeli.order.api.vo.BoxPileVo;
import com.laogeli.order.api.vo.DockVo;
import com.laogeli.order.api.vo.ShipCompanyVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 常量信息mapper
 *
 * @author wang
 * @date 2020-07-02
 */
@Mapper
public interface ConstantMapper {

    /**
     * 获取船名
     *
     * @return List<String>
     */
    List<String> getAllShipsName();

    /**
     * 获取船公司列表
     *
     * @return List<ShipCompanyVo>
     */
    List<ShipCompanyVo> getAllShipCompany();

    /**
     * 获取码头列表
     *
     * @return List<DockVo>
     */
    List<DockVo> getAllDock();

    /**
     * 获取码头列表
     *
     * @return List<BoxPileVo>
     */
    List<BoxPileVo> getAllBoxPile();


    /**
     * 添加船名
     *
     * @param shipsName
     * @return
     */
    int addShipName(ShipsName shipsName);

    /**
     * 根据船名查询是否存在
     * @param shipsName
     * @return
     */
    ShipsName getShipName(ShipsName shipsName);
}
