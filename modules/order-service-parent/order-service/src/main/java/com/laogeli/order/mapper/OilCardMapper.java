package com.laogeli.order.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.order.api.module.MakingChest;
import com.laogeli.order.api.module.OilCard;
import com.laogeli.order.api.vo.OilVoImport;
import com.laogeli.order.api.vo.VehicleCostVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 油卡记录mapper
 *
 * @author BeiFang
 * @date 2020-07-19
 **/
@Mapper
public interface OilCardMapper extends CrudMapper<OilCard> {

    /**
     * 查询车辆邮费记录
     *
     * @param vehicleArray      车牌号数组
     * @param belongCompaniesId 所属公司id
     * @param beginTime         开始时间
     * @param endTime           结束时间
     * @return
     */
    List<VehicleCostVo> findOilCost(@Param("vehicleArray") Set<String> vehicleArray,
                                    @Param("belongCompaniesId") String belongCompaniesId,
                                    @Param("beginTime") String beginTime,
                                    @Param("endTime") String endTime);

    /**
     * 查询车辆邮费记录
     *
     * @param vehicleArray      车牌号数组
     * @param belongCompaniesId 所属公司id
     * @param year              年份
     * @return
     */
    List<VehicleCostVo> monthlyOilCost(@Param("vehicleArray") List<String> vehicleArray,
                                       @Param("belongCompaniesId") String belongCompaniesId,
                                       @Param("year") String year);

    /**
     * 批量添加 邮费记录
     *
     * @param oilVoImportList
     * @return int
     */
    int addOil(List<OilVoImport> oilVoImportList);
}
