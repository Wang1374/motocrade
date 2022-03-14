package com.laogeli.order.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.order.api.module.Clientele;
import com.laogeli.order.api.vo.ClienteleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户信息mapper
 *
 * @author wang
 * @date 2020-06-09
 */
@Mapper
public interface ClienteleMapper extends CrudMapper<Clientele> {

    /**
     * 通过企业标识id 查询客户
     *
     * @param belongCompaniesId
     * @return List<ClienteleVo>
     */
    List<ClienteleVo> getListById(@Param("belongCompaniesId") String belongCompaniesId);

    /**
     * 查询客户id
     * @param belongCompaniesId
     * @param companyName
     * @return
     */
    String findIdByCompanyIdAndName(@Param("belongCompaniesId") String belongCompaniesId,
                             @Param("companyName")String companyName);


    /**
     * 根据companyName 修改往来单位
     * @param companyName
     * @param updateData
     * @return
     */
    int updateClientByName(@Param("companyName") String companyName,
                           @Param("updateData") String updateData);

}
