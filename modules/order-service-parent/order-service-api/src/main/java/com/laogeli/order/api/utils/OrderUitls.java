package com.laogeli.order.api.utils;

import java.util.LinkedHashMap;

/**
 * 订单工具类
 *
 * @author BeiFang
 * @Date 2020-07-31
 **/
public class OrderUitls {

    /*
     * 获取导出订单的标题map
     * @Param
     * @return
     */
    public static LinkedHashMap<String, String> getOrderMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("number", "序号");
        map.put("orderNumber", "业务编号");
        map.put("orderType", "业务类型");
        map.put("customerName", "客户名称");
        map.put("customerNum", "客户编号");
        map.put("contacts", "联系人");
        map.put("blNo", "提单号");
        map.put("vesselAndVoyage", "船名航次");
        map.put("dock", "停靠码头");
        map.put("orderTime", "接单日期");
        map.put("boxPile", "箱型");
        map.put("caseNumber", "箱号");
        map.put("packingTime", "做箱日期");
        map.put("door", "做箱门点");
        map.put("suitcasePoint", "提箱点");
        map.put("returnPoint", "还箱点");
        return map;
    }

    /**
     * 获取Oil属性的map
     *
     * @return LinkedHashMap
     * @author beifang
     * @date 2021-07-19
     */
    public static LinkedHashMap<String, String> getOilMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>(5);
        map.put("invoiceNum", "发票号");
        map.put("oilDate", "日期");
        map.put("oilTime","时间");
        map.put("vehicle", "车牌号");
        map.put("oilPrice", "单价");
        map.put("oilCapacity","数量");
        map.put("oilTotalPrice", "金额");
        return map;
    }
}
