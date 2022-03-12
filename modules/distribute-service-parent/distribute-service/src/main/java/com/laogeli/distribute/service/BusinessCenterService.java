package com.laogeli.distribute.service;


import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.service.CrudService;
import com.laogeli.common.core.utils.DateUtils;
import com.laogeli.common.core.utils.StringUtils;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.distribute.annotation.DataScope;
import com.laogeli.distribute.api.module.BusinessCenter;
import com.laogeli.distribute.api.vo.BusinessCenterVo;
import com.laogeli.distribute.config.ProducerMessage;
import com.laogeli.distribute.mapper.BusinessCenterMapper;
import com.laogeli.user.api.feign.UserServiceClient;
import com.laogeli.user.api.module.MessageInfo;
import io.goeasy.GoEasy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 下单接单 service
 *
 * @author yangyu
 * @date 2020-11-11
 */
@Slf4j
@AllArgsConstructor
@Service
public class BusinessCenterService extends CrudService<BusinessCenterMapper, BusinessCenter> {

    private final BusinessCenterMapper businessCenterMapper;

    private final RedisTemplate redisTemplate;

    private final MakingChestCenterService makingChestCenterService;


    private final ProducerMessage producerMessage;
    /**
     * 新增下单
     *
     * @param businessCenter
     * @return
     */
    public int insertBusiness(@NotNull BusinessCenter businessCenter) {
//        businessCenter.setPlaceOrderNumber(getOrderCenterNumber());
//        businessCenter.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
//        //做箱计划添加
//        if (businessCenter.getMakingChestCenterList().size() != 0) {
//            makingChestCenterService.placeMaking(businessCenter.getMakingChestCenterList(), businessCenter.getId());
//        }
//        return businessCenterMapper.insert(businessCenter);

        // 使用mq发送消息  防止分布式事务带来的问题
        // 防止发送消息失败，将发送消息存入本地。
        businessCenter.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
        businessCenter.setCreator(SysUtil.getUser());
        businessCenter.setOperatorTime(new Date());
        businessCenter.setMsgType("clientOrder");
        producerMessage.sendMsg(JSON.toJSONString(businessCenter));

        return 1;
    }


    private void m1() {
        //        // 发送消息给平台，刷新并弹出消息
//        GoEasy goEasy = new GoEasy("https://rest-hangzhou.goeasy.io", "BC-577d22cac6404d699b1cde65380e0f31");
//        goEasy.publish("792071937215041536", businessCenter.getCompanyName() + " 来新订单了");
//        //将消息保存到sys_message表
//        MessageInfo messageInfo = new MessageInfo();
//        messageInfo.setCommonValue(SysUtil.getUser(),SysUtil.getSysCode());
//        messageInfo.setBelongCompaniesId("792071937215041536");
//        messageInfo.setRouterAddress("Orderlist");
//        messageInfo.setReadStatus(0);
//        messageInfo.setNumberParams(businessCenter.getPlaceOrderNumber());
//        messageInfo.setSendUser(businessCenter.getCompanyName());
//        messageInfo.setMessageTitle("有新订单，下单编号为"+businessCenter.getPlaceOrderNumber());
//        messageInfo.setMessageContext("提单号: "+businessCenter.getBlNos()+"; 箱型箱量: "+businessCenter.getBoxQuantity());
//        userServiceClient.insertMessage(messageInfo);
    }

    /**
     * 编辑下单
     *
     * @param businessCenter
     * @return
     */
    @Transactional
    public int editPlaceOrderInfo(BusinessCenter businessCenter) {

        //1根据ids数组，批量删除门点，件毛体
        if (businessCenter.getMcIds().length > 0) {
            makingChestCenterService.delDoorAndLcl(businessCenter.getMcIds());
        }

        //2根据下单id删除做箱
        makingChestCenterService.delMcByPlaceorderId(businessCenter.getId());

        //3 新增做箱和件毛体
        //做箱计划添加
        if (businessCenter.getMakingChestCenterList().size() != 0) {
            makingChestCenterService.placeMaking(businessCenter.getMakingChestCenterList(), businessCenter.getId());
        }
        //4根据id修改订单数据
        return businessCenterMapper.editPlaceOrderById(businessCenter);

    }

    /**
     * 通过id 查询订单状态
     *
     * @param id
     * @return int
     */
    public Integer getOrderStatus(String id) {
        return businessCenterMapper.getOrderStatus(id);
    }

    /**
     * 根据下单id 更改 下单表 状态
     *
     * @param businessCenter
     * @return int
     */
    @Transactional
    public int updateById(BusinessCenter businessCenter) {
        return businessCenterMapper.updateById(businessCenter);
    }

    /**
     * 根据订单id 更改订单表状态
     *
     * @param id,businessState
     * @return int
     */
    @Transactional
    public int updateByOrderId(String id, int businessState) {
        return businessCenterMapper.updateByOrderId(id, businessState);
    }


    /**
     * 根据下单编号 修改派单状态
     *
     * @param placeOrderNumber,orderStatus
     * @return int
     */
    @Transactional
    public int updateByNumber(String placeOrderNumber, int orderStatus) {
        return businessCenterMapper.updateByNumber(placeOrderNumber, orderStatus);
    }

    /**
     * 根据订单表id修改做箱状态 为0 取消做箱
     *
     * @param orderId
     * @return
     */
    @Transactional
    public int updateMakingStatus(String orderId) {
        return businessCenterMapper.cancelMaking(orderId);
    }

    /**
     * 派单列表分页查询
     *
     * @param page   page
     * @param entity entity
     * @return PageInfo
     */
    @DataScope(userAlias = "a")
    public PageInfo<BusinessCenterVo> findDispatchPage(PageInfo<BusinessCenterVo> page, BusinessCenterVo entity) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return new PageInfo<BusinessCenterVo>(dao.findDispatchList(entity));
    }

    @Transactional
    public String getOrderCenterNumber() {
        //订单编号key
        String centerNumberKey = CommonConstant.ORDER_CENTER_NUMBER;
        long num;
        //查询缓存，如果没有值，即查询数据库
        if (!redisTemplate.hasKey(centerNumberKey)) {
            //查询数据库订单编号最大的那一条
            long ordNum = getOrdNum();
            num = redisTemplate.opsForValue().increment(centerNumberKey, ordNum + 1);
            //设置过期时间
            Long expireTime = DateUtils.getExpireTime();
            redisTemplate.expire(centerNumberKey, expireTime * 1000, TimeUnit.MILLISECONDS);
        } else {
            num = redisTemplate.opsForValue().increment(centerNumberKey);
        }
        //获取当前时间
        String currentTime = DateUtils.getDateAndMS1();
        StringBuffer sn = new StringBuffer();
        sn.append(currentTime);
        sn.append(addZero(String.valueOf(num), 5));
        return sn.toString();
    }

    /**
     * 获取订单编号
     *
     * @param
     */
    public Long getOrdNum() {
        //获取最大的那一条
        String number = businessCenterMapper.getMostBig();
        return cutNumber(number);
    }

    /**
     * 截取订单编号自增部分
     *
     * @param number JFSE2020111794245
     */
    public Long cutNumber(String number) {
        if (number == null) {
            return 0L;
        } else {
            String ordStr = number.substring(12);
            Long ordNum = Long.parseLong(ordStr.replaceAll("^(0+)", ""));
            return ordNum;
        }
    }

    /**
     * 自动补零
     *
     * @param numStr,maxNum
     */
    public String addZero(String numStr, Integer maxNum) {
        int addNum = maxNum - numStr.length();
        StringBuffer rStr = new StringBuffer();
        for (int i = 0; i < addNum; i++) {
            rStr.append("0");
        }
        rStr.append(numStr);
        return rStr.toString();
    }
}
