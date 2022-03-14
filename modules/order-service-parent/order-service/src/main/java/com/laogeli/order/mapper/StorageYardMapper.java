package com.laogeli.order.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.order.api.module.StorageYard;
import org.apache.ibatis.annotations.Mapper;

/**
 * 堆场信息mapper
 *
 * @author wang
 * @date 2020-06-15
 */
@Mapper
public interface StorageYardMapper extends CrudMapper<StorageYard> {
}
