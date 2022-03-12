package com.laogeli.distribute.service;

import com.laogeli.common.core.service.CrudService;
import com.laogeli.distribute.api.module.ClientDoors;
import com.laogeli.distribute.mapper.ClientDoorsMapper;
import org.springframework.stereotype.Service;

/**
 * 客户端门点service
 *
 * @author beifang
 * @date 2020-12-11
 */
@Service
public class ClientDoorsService extends CrudService<ClientDoorsMapper, ClientDoors> {

    /**
     * 新增门点
     */
    public Integer insertDoors(ClientDoors clientDoors) {
        Integer num = 0;
        ClientDoors obj = dao.get(clientDoors);
        if (obj == null) {
            num = dao.insert(clientDoors);
        }
        return num;
    }

    /**
     * 修改门点
     */
    public Integer updateDoors(ClientDoors clientDoors) {
        ClientDoors obj = dao.get(clientDoors);
        // 有值 判断 是否修改的当前数据
        if (obj != null) {
            // 如果id 与 公司名相同
            if (obj.getId().equals(clientDoors.getId()) && obj.getDoorAs().equals(clientDoors.getDoorAs())) {
                // 修改
                dao.update(clientDoors);
                return 1;
            } else {
                return 0;
            }
        } else {
            // 修改
            dao.update(clientDoors);
            return 1;
        }
    }
}
