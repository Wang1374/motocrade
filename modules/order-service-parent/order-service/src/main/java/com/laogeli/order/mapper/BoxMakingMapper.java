package com.laogeli.order.mapper;

import com.github.pagehelper.PageInfo;
import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.order.api.module.BoxMaking;
import com.laogeli.order.api.module.MakingChest;
import org.apache.ibatis.annotations.Mapper;

import java.util.*;

/**
 * 做箱列表信息 mapper
 *
 * @author yangyu
 * @date 2020-08-18
 */
@Mapper
public interface BoxMakingMapper extends CrudMapper<BoxMaking> {

    List<BoxMaking> findClientBoxList(BoxMaking boxMaking);
}
