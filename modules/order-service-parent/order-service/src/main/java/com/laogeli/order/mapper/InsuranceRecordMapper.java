package com.laogeli.order.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.order.api.module.InsuranceDetail;
import com.laogeli.order.api.module.InsuranceRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * 保险记录mapper
 *
 * @author wang
 * @date 2020-07-20
 **/
@Mapper
public interface InsuranceRecordMapper extends CrudMapper<T> {

    /* *
     * 新增保险记录
     * @Param [carOutList]
     * @return int
     * @Date: 2020-07-17 15:59
     */

    int addListObj(List<InsuranceRecord> insuranceList);

    /**
     * 新增保险详情记录
     *
     * @param insuranceList
     * @return
     */
    int addListDetailObj(List<InsuranceDetail> insuranceList);

    /**
     * 根据车辆id查询所有保养记录
     *
     * @param insuranceRecord
     * @return
     */
    List<InsuranceRecord> findList(InsuranceRecord insuranceRecord);

    /**
     * 根据保险详情id查询所有保险详情
     *
     * @param insuranceDetail
     * @return
     */
    List<InsuranceDetail> findListDetail(InsuranceDetail insuranceDetail);

    /*
     * 根据保险详情id删除保险详情记录
     * @Param
     * @return
     */
    int deleteDetailById(@Param("insuranceDetailId") String insuranceDetailId);

    /*
     * 根据保险详情id删除保养记录
     * @Param
     * @return
     */
    int deleteRecordById(@Param("insuranceDetailId") String insuranceDetailId);

    /*
     * 根据保养记录id批量删除保养记录
     * @Param
     * @return
     */
    int deleteAll(List<String> ids);


    /*
     * 根据维修详情id删除维修详情信息
     * @Param insuranceDetail
     * @return int
     */
    int deleteByDetailId(InsuranceDetail insuranceDetail);

}
