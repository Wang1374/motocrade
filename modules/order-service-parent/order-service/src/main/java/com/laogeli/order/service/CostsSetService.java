package com.laogeli.order.service;

import com.laogeli.common.core.service.CrudService;
import com.laogeli.order.api.module.CostsSet;
import com.laogeli.order.mapper.CostsSetMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 费用设置信息service
 *
 * @author yangyu
 * @date 2020-06-15
 */
@Slf4j
@AllArgsConstructor
@Service
public class CostsSetService extends CrudService<CostsSetMapper, CostsSet> {

    /**
     * 新增费用
     */
    public Integer insertCostsSet(CostsSet costsSet) {
        Integer num = 0;
        CostsSet obj = dao.get(costsSet);
        if (obj == null) {
            num = dao.insert(costsSet);
        }
        return num;
    }

    /**
     * 修改费用
     */
    public Integer updateCostsSet(CostsSet costsSet) {
        Integer num = 0;
        CostsSet obj = dao.get(costsSet);
        if (obj != null) {
            // 如果id 与 费用名相同
            if (obj.getId().equals(costsSet.getId()) && obj.getFeeName().equals(costsSet.getFeeName())) {
                num = dao.update(costsSet);
            } else {
                num = 0;
            }
        } else {
            num = dao.update(costsSet);
        }
        return num;
    }
}
