package com.laogeli.order.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.enums.KeyPrefix;
import com.laogeli.common.core.enums.OrderNumberPrefix;
import com.laogeli.common.core.service.CrudService;
import com.laogeli.common.core.utils.DateUtils;
import com.laogeli.common.core.utils.IdGen;
import com.laogeli.common.core.utils.StringUtils;
import com.laogeli.order.annotation.DataScope;
import com.laogeli.order.api.module.BusinessOrder;
import com.laogeli.order.api.module.MakingChest;
import com.laogeli.order.api.module.OrderNumber;
import com.laogeli.order.api.vo.OrderVo;
import com.laogeli.order.mapper.BusinessOrderMapper;
import com.laogeli.order.mapper.MakingChestMapper;
import io.goeasy.GoEasy;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 业务订单 service
 *
 * @author wang
 * @date 2020-06-18
 */
@Slf4j
@AllArgsConstructor
@Service
public class BusinessOrderService extends CrudService<BusinessOrderMapper, BusinessOrder> {

    private final MakingChestService makingChestService;

    private final MakingChestMapper makingChestMapper;

    private final OrderNumberService orderNumberService;

    private final RedisTemplate redisTemplate;


    /**
     * 查询订单分页
     *
     * @param page   page
     * @param entity entity
     * @return PageInfo
     */
    @DataScope(userAlias = "a")
    public PageInfo<BusinessOrder> findBusinessOrderPage(PageInfo<BusinessOrder> page, BusinessOrder entity) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return new PageInfo<BusinessOrder>(dao.findList(entity));
    }

    /**
     * 查询平台订单列表分页
     *
     * @param page   page
     * @param entity entity
     * @return PageInfo
     */
    public PageInfo<BusinessOrder> findPlatformOrderPage(PageInfo<BusinessOrder> page, BusinessOrder entity) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return new PageInfo<BusinessOrder>(dao.findPlatformOrderList(entity));
    }


    /**
     * 查询客户端订单列表分页
     *
     * @param page   page
     * @param entity entity
     * @return PageInfo
     */
    public PageInfo<BusinessOrder> findPlatformClientPage(PageInfo<BusinessOrder> page, BusinessOrder entity) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return new PageInfo<BusinessOrder>(dao.findPlatformClientList(entity));
    }

    /**
     * 新增订单
     *
     * @param businessOrder businessOrder
     */
    @Transactional
    public int insertBusinessOrder(BusinessOrder businessOrder) {
        String newOrderNumber = this.getNewOrderNumber(businessOrder.getBelongCompaniesId(), businessOrder.getOrderType());
        businessOrder.setOrderNumber(newOrderNumber);
        return dao.addBusinessOrder(businessOrder);
    }

    /**
     * 新增客户订单
     * @param businessOrder
     * @return
     */
    @Transactional
    public int insertClientBusinessOrder(BusinessOrder businessOrder){
        // TODO 有问题
        businessOrder.setBelongCompaniesId("795754454723989505");
        businessOrder.setForeignBelongCompaniesId("738701429283360769");
        businessOrder.setCustomerName("锦线供应链管理（常州）有限公司");
        businessOrder.setCustomerId("739864183402795008");
        // 操作员和业务员
        businessOrder.setOperator("九辅国际物流（上海）有限公司");
        businessOrder.setSalesman("九辅国际物流（上海）有限公司");
        String newOrderNumber = this.getNewOrderNumber(businessOrder.getBelongCompaniesId(), businessOrder.getOrderType());
        businessOrder.setOrderNumber(newOrderNumber);
        businessOrder.setDispatchType(1);

        return dao.addBusinessOrder(businessOrder);
    }

    /**
     * 获取订单编号
     *
     * @param belongCompaniesId,orderType
     */
    @Transactional
    public String getNewOrderNumber(String belongCompaniesId, int orderType) {
        // 根据企业标识查询 设置的订单编号格式，先去redis中查询，若没有 查询数据库，查询完后 放入redis 设置凌晨过期。
        String orderNumberKey = CommonConstant.ORDER_NUMBER_PREFIX + belongCompaniesId;
        OrderNumber orderNumber = new OrderNumber();
        if (!redisTemplate.hasKey(orderNumberKey)) {
            orderNumber.setBelongCompaniesId(belongCompaniesId);
            orderNumber = orderNumberService.get(orderNumber);
            // 设置到第二天早上00：00：01过期
            Long expireTime = DateUtils.getExpireTime();
            redisTemplate.opsForValue().set(orderNumberKey, orderNumber, expireTime * 1000, TimeUnit.MILLISECONDS);
        } else {
            Object obj = redisTemplate.opsForValue().get(orderNumberKey);
            BeanUtils.copyProperties(obj, orderNumber);
        }
        // 根据业务类型 获取前缀
        String prefix = OrderNumberPrefix.getPrefix(orderType);
        // 获取当前时间
        String currentTime = DateUtils.getDate();
        // 当前自增数
        Long num;
        if ("1".equals(orderNumber.getBusinessRule())) { // 业务叠加
            // 根据业务类型，获取在redis中key 的前缀
            String keyPrefix = KeyPrefix.getPrefix(orderType);
            String allAotuKey = keyPrefix + belongCompaniesId;
            if (!redisTemplate.hasKey(allAotuKey)) {
                // 获取数据库中订单编号 自增数
                Long ordNum = getOrdNum(belongCompaniesId, orderNumber.getDateRule(), orderType, 1);
                num = redisTemplate.opsForValue().increment(allAotuKey, ordNum + 1);
                // 设置过期时间，判断是 按天还是 按月
                setExpirationTime(orderNumber.getDateRule(), allAotuKey);
            } else {
                num = redisTemplate.opsForValue().increment(allAotuKey);
            }
        } else { // 全部叠加
            String allAotuKey = CommonConstant.ALL_AOTU_PREFIX + belongCompaniesId;
            if (!redisTemplate.hasKey(allAotuKey)) {
                // 没有去数据库查最大的一条订单编号 并插入到redis,并设置从多少开始自增
                Long ordNum = getOrdNum(belongCompaniesId, orderNumber.getDateRule(), orderType, 2);
                num = redisTemplate.opsForValue().increment(allAotuKey, ordNum + 1);
                setExpirationTime(orderNumber.getDateRule(), allAotuKey);
            } else {
                num = redisTemplate.opsForValue().increment(allAotuKey);
            }
        }
        // 和前缀、时间、自增数进行组合
        StringBuffer sn = new StringBuffer();
        sn.append(orderNumber.getPrefix());
        sn.append(prefix);
        sn.append(currentTime);
        sn.append(addZero(String.valueOf(num), orderNumber.getSeriesNumber()));
        return sn.toString();
    }

    /**
     * 获取订单编号
     *
     * @param belongCompaniesId,dateRule,orderType,businessRule
     */
    public Long getOrdNum(String belongCompaniesId, String dateRule, int orderType, int businessRule) {
        if (businessRule == 1) { // 业务叠加
            if ("1".equals(dateRule)) { // 按日叠加 查询 当天 最大的编号
                String number = dao.getOrderNumberByTypeDay(belongCompaniesId, orderType);
                return cutNumber(number);
            } else { // 按月叠加 查询 当月 最大的编号
                String number = dao.getOrderNumberByTypeMonth(belongCompaniesId, orderType);
                return cutNumber(number);
            }
        } else { // 全部叠加
            if ("1".equals(dateRule)) { // 按日叠加
                String number = dao.getOrderNumberByTypeDay(belongCompaniesId, 0);
                return cutNumber(number);
            } else { // 按月叠加
                String number = dao.getOrderNumberByTypeMonth(belongCompaniesId, 0);
                return cutNumber(number);
            }
        }
    }

    /**
     * 截取订单编号自增部分
     *
     * @param number
     */
    public Long cutNumber(String number) {
        if (number == null) {
            return 0L;
        } else {
            String str = StringUtils.substring(number);
            String ordStr = number.substring(number.indexOf(str) + 8);
            Long ordNum = Long.parseLong(ordStr.replaceAll("^(0+)", ""));
            return ordNum;
        }
    }

    /**
     * 设置过期时间，判断是 按天还是 按月
     *
     * @param str,key
     */
    public void setExpirationTime(String str, String key) {
        if ("1".equals(str)) {
            Long expireTime = DateUtils.getExpireTime();
            redisTemplate.expire(key, expireTime * 1000, TimeUnit.MILLISECONDS);
        } else {
            Long expireTime = DateUtils.getSecondsApart();
            redisTemplate.expire(key, expireTime * 1000, TimeUnit.MILLISECONDS);
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

    /**
     * 获取做箱信息和订单数据
     *
     * @param orderVo
     * @return
     */
    public List<OrderVo> getOrderExportData(List<OrderVo> orderVo) {
        //查找做箱信息
        List<MakingChest> makingChest = makingChestMapper.getMakingChestByIds(orderVo.get(0).getOrderIdArray());
        List<OrderVo> list = new ArrayList<>();
        if (makingChest.size() != 0) {
            for (int i = 0; i < makingChest.size(); i++) {
                OrderVo obj = new OrderVo();
                for (int j = 0; j < orderVo.size(); j++) {
                    if (makingChest.get(i).getBusinessOrderId().equals(orderVo.get(j).getId())) {
                        BeanUtils.copyProperties(orderVo.get(j), obj);
                        obj.setBoxPile(makingChest.get(i).getBoxPile());
                        obj.setCaseNumber(makingChest.get(i).getCaseNumber());
                        obj.setPackingTime(makingChest.get(i).getPackingTime());
                        obj.setDoor(makingChest.get(i).getDoor());
                        obj.setSuitcasePoint(makingChest.get(i).getSuitcasePoint());
                        obj.setReturnPoint(makingChest.get(i).getReturnPoint());
                        list.add(obj);
                    }
                }
            }
            return list;
        } else {
            return orderVo;
        }
    }


    /**
     * 新增 接单订单
     *
     * @param jsonObject
     */
    @Transactional
    public int insertOrderReceiving(JSONObject jsonObject) {
        String orderId = IdGen.snowflakeId();
        Date data = DateUtils.stampToDate(jsonObject.getString("operatorTime"));
        BusinessOrder businessOrder = new BusinessOrder();
        businessOrder.setId(orderId);
//        businessOrder.setBelongCompaniesId("792071937215041536");
        businessOrder.setBelongCompaniesId(jsonObject.getString("belongCompaniesId"));
        businessOrder.setPlaceOrderNumber(jsonObject.getString("placeOrderNumber"));
        // 有问题
//        businessOrder.setCustomerId("795754455581455747");
//        businessOrder.setCustomerName("锦线网络科技（上海）有限公司");
        businessOrder.setCustomerId("739864183402795008");
        businessOrder.setCustomerName("锦线供应链管理（常州）有限公司");
        businessOrder.setCustomerNum(jsonObject.getString("customerNum"));
        businessOrder.setContacts(jsonObject.getString("operator"));
        businessOrder.setOrderType(Integer.parseInt(jsonObject.getString("orderType")));
        businessOrder.setVesselAndVoyage(jsonObject.getString("vesselAndVoyage"));
        businessOrder.setBlNos(jsonObject.getString("blNos"));
        businessOrder.setBoxQuantity(jsonObject.getString("boxQuantity"));
        businessOrder.setPortOfLoading(jsonObject.getString("portOfLoading"));
        businessOrder.setPortOfDischarge(jsonObject.getString("portOfDischarge"));
        businessOrder.setPlaceOfDelivery(jsonObject.getString("placeOfDelivery"));
        businessOrder.setDock(jsonObject.getString("dock"));
        businessOrder.setOrderTime(data);
        businessOrder.setAppendixUrl(jsonObject.getString("appendixUrl"));
        // 订单状态1
        businessOrder.setBusinessState(1);
        businessOrder.setDispatchType(1);
        businessOrder.setCompanyName(jsonObject.getString("companyName"));
        businessOrder.setSingleUser(jsonObject.getString("singleUser"));
        businessOrder.setSinglePhone(jsonObject.getString("singlePhone"));
        businessOrder.setPlaceOrderNumber(jsonObject.getString("placeOrderNumber"));
        businessOrder.setCreator(jsonObject.getString("creator"));
        businessOrder.setCreateDate(data);
        businessOrder.setForeignBelongCompaniesId("738701429283360769");
        businessOrder.setApplicationCode("EXAM");
        businessOrder.setPlaceOrderRemark(jsonObject.getString("remark"));
        businessOrder.setAppendixName(jsonObject.getString("appendixName"));

        // 操作员和业务员
        businessOrder.setOperator("九辅国际物流（上海）有限公司");
        businessOrder.setSalesman("九辅国际物流（上海）有限公司");

        // 先插入做箱/件毛体
        List<MakingChest> makingChestList = JSONArray.parseArray(jsonObject.getString("makingChestCenterList"), MakingChest.class);
        if (makingChestList.size() > 0) {
            makingChestService.addOrderMcl(makingChestList, businessOrder.getId(), businessOrder.getCreator());
        }

        // 后插入订单，防止订单编号重复获取，redis重复自增，中间空白
        String newOrderNumber = this.getNewOrderNumber(businessOrder.getBelongCompaniesId(), businessOrder.getOrderType());
        businessOrder.setOrderNumber(newOrderNumber);

//        // 发送socket消息给客户端（以公司id为信道），推送提示消息，让其刷新列表
//        GoEasy goEasy = new GoEasy("https://rest-hangzhou.goeasy.io", "BC-577d22cac6404d699b1cde65380e0f31");
//        goEasy.publish(jsonObject.getString("belongCompaniesId"), "002");

        // 消费失败 会存入死讯，在死讯中插入死讯消息到数据库，并发送消息推送给 管理员...

        return dao.addBusinessOrder(businessOrder);
    }

    /**
     * 根据id 修改订单状态
     *
     * @return
     */
    public Integer updateById(String id, int businessState, String reason) {
        return dao.updateById(id, businessState, reason);
    }

    /**
     * 根据id修改操作记录
     */
    public Integer updateExcception(BusinessOrder businessOrder){
        return dao.updateExceptionById(businessOrder);
    }

    /**
     * 修改订单状态
     */
    public Integer updateBusinessState(BusinessOrder businessOrder){return  dao.updateBusinessState(businessOrder);}
}
