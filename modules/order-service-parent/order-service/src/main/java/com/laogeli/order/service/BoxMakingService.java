package com.laogeli.order.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.laogeli.common.core.service.CrudService;
import com.laogeli.order.annotation.DataScope;
import com.laogeli.order.api.module.BoxMaking;
import com.laogeli.order.api.module.MakingChest;
import com.laogeli.order.mapper.BoxMakingMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 做箱列表信息 service
 *
 * @author wang
 * @date 2020-08-18
 */
@Slf4j
@AllArgsConstructor
@Service
public class BoxMakingService extends CrudService<BoxMakingMapper, BoxMaking> {

    /**
     * 查询做箱列表分页
     *
     * @param page   page
     * @param entity entity
     * @return PageInfo
     */
    @DataScope(userAlias = "a")
    public PageInfo<BoxMaking> findBoxMakingPage(PageInfo<BoxMaking> page, BoxMaking entity) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return new PageInfo<BoxMaking>(dao.findList(entity));
    }


    private final BoxMakingMapper boxMakingMapper;

    /**
     * 分页查询客户端做箱列表
     *
     * @param page
     * @param boxMaking
     * @return
     */
    public PageInfo<BoxMaking> findClientBoxList(PageInfo<MakingChest> page, BoxMaking boxMaking) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<BoxMaking> boxMakingList = boxMakingMapper.findClientBoxList(boxMaking);
        PageInfo<BoxMaking> pageInfo = new PageInfo<>(boxMakingList);
        return pageInfo;
    }
}
