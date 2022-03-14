package com.laogeli.order.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.order.api.module.CostsSet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 费用设置信息mapper
 *
 * @author wang
 * @date 2020-06-16
 */
@Mapper
public interface CostsSetMapper extends CrudMapper<CostsSet> {

    /*
     * 根据企业标识id查询费用设置
     * @Param belongCompaniesId
     * @return List<CostsSet>
     */
    List<CostsSet> getListQyId(String belongCompaniesId);

    /**
     * 批量添加费用参数
     *
     * @param costsSetList
     * @return int
     */
    int batchCostsSet(@Param("list") List<CostsSet> costsSetList);
}
