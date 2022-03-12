package com.laogeli.order.service;

import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.service.CrudService;
import com.laogeli.common.core.utils.DateUtils;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.order.api.module.OilCard;
import com.laogeli.order.api.vo.OilVoImport;
import com.laogeli.order.api.vo.ZywOrder;
import com.laogeli.order.mapper.OilCardMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 油卡service
 *
 * @author
 * @Date 2020-07-19
 **/
@Service
@Slf4j
@AllArgsConstructor
public class OilCardService extends CrudService<OilCardMapper, OilCard> {

    private final RedisTemplate redisTemplate;

    /**
     * 修改邮费信息
     *
     * @param oilCard
     * @return
     */
    public Integer updateOil(OilCard oilCard) {
        //通过id查询邮费记录是否存在
        OilCard result = dao.get(oilCard);
        if (result != null) {
            //判断数据库数据与当前数据的所属公司id是否一致
            if (result.getBelongCompaniesId().equals(oilCard.getBelongCompaniesId())) {
                //执行修改
                dao.update(oilCard);
                return 1;
            }
        }
        return 0;
    }

    /**
     * 查询找油网数据
     *
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @param zyCompanyId zyCompanyId
     * @param pageNum     当前页码
     * @param pageSize    每页条数
     * @return
     */
    public ResponseBean getZywOilPrice(String startTime,
                                       String endTime,
                                       Integer zyCompanyId,
                                       String pageNum,
                                       String pageSize,
                                       String truckLicenseNumber) {
        String zywKey = CommonConstant.ZYW_KEY;
        List<ZywOrder> zywDataList = new ArrayList<>();
        int total = 0;
        if (redisTemplate.hasKey(zywKey)) {
            Object obj = redisTemplate.opsForValue().get(zywKey);
            JSONArray jsonArray = JSONArray.fromObject(obj);
            zywDataList = JSONArray.toList(jsonArray, new ZywOrder(), new JsonConfig());
            //过滤时间范围内的zywDataList  和当前车辆
            zywDataList = zywDataList.stream().filter(s ->
                    DateUtils.getDate(s.getOrderTime()).getTime() >= DateUtils.getDate(startTime).getTime()
                            && DateUtils.getDate(s.getOrderTime()).getTime() <= DateUtils.getDate(endTime).getTime())
                    .collect(Collectors.toList());
            //过滤车牌号
            if (!truckLicenseNumber.equals("")) {
                zywDataList = zywDataList.stream().filter(s -> s.getTruckLicenseNumber().equals(truckLicenseNumber))
                        .collect(Collectors.toList());
            }
            //总条数
            total = zywDataList.size();
            //当前开始的条数
            int currentIndex = Integer.parseInt(pageSize) * (Integer.parseInt(pageNum) - 1);
            int endIndex = currentIndex + Integer.parseInt(pageSize);
            if (currentIndex < total) {
                if (endIndex > total) {
                    endIndex = total;
                }
                zywDataList = zywDataList.subList(currentIndex, endIndex);
            }
        }
        return new ResponseBean(zywDataList, total);
    }


    /**
     * 导入邮费记录
     * @param oilCardList
     * @return
     */
    public int importOils(List<OilVoImport> oilCardList,String belongCompaniesId )   {
        for (OilVoImport oilVoImport:oilCardList
             ) {
            oilVoImport.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
            oilVoImport.setBelongCompaniesId(belongCompaniesId);
        }
        return dao.addOil(oilCardList);
    }

}


