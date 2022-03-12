package com.laogeli.distribute.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.distribute.api.vo.MineOrderVo;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;
/**
 *
 * 我的订单mapper
 * @author beifang
 * @date 2020-12-21
 */
@Mapper
public interface MineOrderMapper extends CrudMapper<MineOrderVo> {

//    /**
//     * 分页查询我的订单数据
//     * @param mineOrderVo
//     * @return
//     */
//    List<MineOrderVo> findList(MineOrderVo mineOrderVo);
}
