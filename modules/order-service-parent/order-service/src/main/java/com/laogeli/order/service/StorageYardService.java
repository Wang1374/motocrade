package com.laogeli.order.service;

import com.laogeli.common.core.service.CrudService;
import com.laogeli.order.api.module.StorageYard;
import com.laogeli.order.mapper.StorageYardMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 堆场信息service
 *
 * @author yangyu
 * @date 2020-06-15
 */
@Slf4j
@AllArgsConstructor
@Service
public class StorageYardService extends CrudService<StorageYardMapper, StorageYard> {

    /**
     * 新增堆场
     */
    public Integer insertStorageYard(StorageYard storageYard) {
        Integer num = 0;
        StorageYard obj = dao.get(storageYard);
        if (obj == null) {
            num = dao.insert(storageYard);
        }
        return num;
    }

    /**
     * 修改堆场
     */
    public Integer updateStorageYard(StorageYard storageYard) {
        Integer num = 0;
        StorageYard obj = dao.get(storageYard);
        if (obj != null) {
            // 判断 是否修改的当前数据 如果id 与 堆场名称相同 为当前数据
            if (obj.getId().equals(storageYard.getId()) && obj.getName().equals(storageYard.getName())) {
                num = dao.update(storageYard);
            } else {
                num = 0;
            }
        } else {
            num = dao.update(storageYard);
        }
        return num;
    }
}
