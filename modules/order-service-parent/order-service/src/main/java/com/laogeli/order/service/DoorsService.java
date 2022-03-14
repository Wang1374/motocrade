package com.laogeli.order.service;

import com.alibaba.fastjson.JSONObject;
import com.laogeli.common.core.service.CrudService;
import com.laogeli.common.core.utils.IdGen;
import com.laogeli.order.api.module.Doors;
import com.laogeli.order.api.vo.DoorsVo;
import com.laogeli.order.mapper.ClienteleMapper;
import com.laogeli.order.mapper.DoorsMapper;
import io.goeasy.GoEasy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 门店信息service
 *
 * @author wang
 * @date 2020-06-15
 */
@Slf4j
@AllArgsConstructor
@Service
public class DoorsService extends CrudService<DoorsMapper, Doors> {

    private final DoorsMapper doorsMapper;

    private final ClienteleMapper clienteleMapper;

    /**
     * 通过客户 查询门点
     *
     * @param ids
     * @return List<DoorsVo>
     */
    public List<DoorsVo> getListByIds(String[] ids) {
        return dao.getListByIds(ids);
    }

    /**
     * 新增门点
     */
    public Integer insertDoors(Doors doors) {
        Integer num = 0;
        Doors obj = dao.get(doors);
        if (obj == null) {

            num = dao.insert(doors);
        }
        return num;
    }

    /**
     * 修改门点
     */
    public Integer updateDoors(Doors doors) {
        Doors obj = dao.get(doors);
        // 有值 判断 是否修改的当前数据
        if (obj != null) {
            // 如果id 与 公司名相同
            if (obj.getId().equals(doors.getId()) && obj.getDoorAs().equals(doors.getDoorAs())) {
                // 修改
                dao.update(doors);
                return 1;
            } else {
                return 0;
            }
        } else {
            // 修改
            dao.update(doors);
            return 1;
        }
    }


    /**
     * 根据消息  新增门点
     *
     * @param jsonObject
     * @param customerId
     * @return
     */
    @Transactional
    public int insertCustomerDoor(JSONObject jsonObject, String customerId) {
        String orderId = IdGen.snowflakeId();
        Doors doors = new Doors();

        doors.setId(orderId);
        doors.setBelongCompaniesId("792071937215041536");
        doors.setCustomerId(customerId);
        doors.setCustomerName(jsonObject.getString("creator"));
        doors.setDoorAs(jsonObject.getString("doorAs"));
        doors.setDoorFullName(jsonObject.getString("doorFullName"));
        doors.setContacts(jsonObject.getString("contacts"));
        doors.setContactNumber(jsonObject.getString("contactNumber"));
        doors.setProvince(jsonObject.getString("provice"));
        doors.setCity(jsonObject.getString("city"));
        doors.setArea(jsonObject.getString("area"));
        doors.setAddress(jsonObject.getString("address"));
        doors.setRemark(jsonObject.getString("remark"));
        doors.setCreator(jsonObject.getString("creator"));
        doors.setCreateDate(new Date());
        doors.setModifier(jsonObject.getString("creator"));
        doors.setModifyDate(new Date());
        doors.setApplicationCode("EXAM");
        //发送消息 给平台
        // 发送socket消息给客户端（以公司id为信道），推送提示消息，让其刷新列表
        GoEasy goEasy = new GoEasy("https://rest-hangzhou.goeasy.io", "BC-577d22cac6404d699b1cde65380e0f31");
        goEasy.publish("792071937215041536", "updateDoorCache");
        return dao.insert(doors);
    }

    @Transactional
    public int updateCustomerDoor(JSONObject jsonObject) {
        //根据客户名和平台公司id  792071937215041536   查询客户id
        String customerName = jsonObject.getString("creator");
        String id = clienteleMapper.findIdByCompanyIdAndName("792071937215041536", customerName);
        //根据平台id，客户id，和修改前门点简称查找门点id
        Doors doors = new Doors();
        doors.setBelongCompaniesId("792071937215041536");
        doors.setCustomerId(id);
        doors.setDoorAs(jsonObject.getString("originalDoorAs"));

        Doors result = dao.get(doors);
        if (null != result) {
            String doorId = result.getId();
            //根据门点id修改门点信息
            doors = new Doors();

            doors.setId(doorId);
            doors.setBelongCompaniesId("792071937215041536");
            doors.setCustomerName(jsonObject.getString("creator"));
            doors.setDoorAs(jsonObject.getString("doorAs"));
            doors.setDoorFullName(jsonObject.getString("doorFullName"));
            doors.setContacts(jsonObject.getString("contacts"));
            doors.setContactNumber(jsonObject.getString("contactNumber"));
            doors.setProvince(jsonObject.getString("provice"));
            doors.setCity(jsonObject.getString("city"));
            doors.setArea(jsonObject.getString("area"));
            doors.setAddress(jsonObject.getString("address"));
            doors.setRemark(jsonObject.getString("remark"));
            doors.setCreator(jsonObject.getString("creator"));
            doors.setCreateDate(new Date());
            doors.setModifier(jsonObject.getString("creator"));
            doors.setModifyDate(new Date());
            doors.setApplicationCode("EXAM");
            //发送消息 给平台
            //删除缓存，重新拉取并获取
            // 发送socket消息给客户端（以公司id为信道），推送提示消息，让其刷新列表
            GoEasy goEasy = new GoEasy("https://rest-hangzhou.goeasy.io", "BC-577d22cac6404d699b1cde65380e0f31");
            goEasy.publish("792071937215041536", "updateDoorCache");
            return doorsMapper.updateCustomerDoor(doors);
        } else {
            return 0;
        }
    }
}
