package com.laogeli.distribute.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.laogeli.common.core.service.CrudService;
import com.laogeli.distribute.annotation.DataScope;
import com.laogeli.distribute.api.vo.MineOrderVo;
import com.laogeli.distribute.mapper.MineOrderMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
/**
 *
 * 我的订单service
 * @author wang
 * @date 2020-12-21
 */
@Service
@AllArgsConstructor
public class MineOrderService extends CrudService<MineOrderMapper,MineOrderVo> {

    private final MineOrderMapper mineOrderMapper;

    /**
     * 分页查询订单数据
     * @param pageInfo
     * @param mineOrderVo
     * @return
     */
    @DataScope(userAlias = "a")
    public PageInfo<MineOrderVo> findMineOrder(PageInfo<MineOrderVo> pageInfo,MineOrderVo mineOrderVo){
        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        List<MineOrderVo> mineOrderVoList= mineOrderMapper.findList(mineOrderVo);
        return new PageInfo<>(mineOrderVoList);
    }
}
