package com.laogeli.order.Job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.constant.ZywApiConstant;
import com.laogeli.common.core.exceptions.CommonException;
import com.laogeli.common.core.utils.DateUtils;
import com.laogeli.order.api.vo.ZywOrder;
import com.laogeli.order.util.Sign;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.Integer.parseInt;

@Slf4j
@Component
@AllArgsConstructor
public class OrderOilJob extends QuartzJobBean {

    private final RedisTemplate redisTemplate;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //查找油网油费，添加到缓存
        String zywKey = CommonConstant.ZYW_KEY;
        if (redisTemplate.hasKey(zywKey)) {
            //删除key
            redisTemplate.randomKey();
        }
        List<ZywOrder> zywDataList;
        //查询找油网油费
        String startTime = "2019-01-01 00:00:00";
        String endTime = DateUtils.getDateAndMS();
        JSONObject jsonOilPrice = getMonthlyZywPrice(startTime, endTime, 1, 200);
        JSONObject jsonData = jsonOilPrice.getJSONObject("data");
        String total = jsonData.getString("total");
        JSONArray list = jsonData.getJSONArray("list");
        zywDataList = JSONObject.parseArray(list.toJSONString(), ZywOrder.class);

        //查询的条数
        int listSize = zywDataList.size();
        //页数
        int count = 2;
        //  38
        while (parseInt(total) > listSize) {
            total = String.valueOf(parseInt(total) - listSize);
            //查询下一页的值
            jsonOilPrice = getMonthlyZywPrice(startTime, endTime, count++, 200);
            JSONObject json = jsonOilPrice.getJSONObject("data");
            JSONArray jsonList = json.getJSONArray("list");
            List<ZywOrder> listCount = JSONObject.parseArray(jsonList.toJSONString(), ZywOrder.class);
            for (ZywOrder zy : listCount
            ) {
                zywDataList.add(zy);
            }
            //查询的条数
            listSize = listCount.size();
        }
        //放入缓存
        if (zywDataList.size() != 0) {
            redisTemplate.opsForValue().set(zywKey, JSON.toJSONString(zywDataList));
            Long expireTime = DateUtils.getExpireTime();
            redisTemplate.expire(zywKey, expireTime * 1000, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 查找油网油费
     *
     * @param startTime
     * @param endTime
     * @param page
     * @param rows
     * @return
     */
    public JSONObject getMonthlyZywPrice(String startTime, String endTime, Integer page, Integer rows) {
        try {
            HashMap<String, String> map = new HashMap();
            map.put("page", page.toString());
            map.put("rows", rows.toString());
            map.put("companyId", "1508");
//            map.put("companyId", "2304");
            map.put("startTime", startTime);
            map.put("endTime", endTime);
            String result = Sign.connectJava("order.orderList", ZywApiConstant.APP_ID, ZywApiConstant.APP_KEY, ZywApiConstant.ZYW_URL, map);
            JSONObject jsonObject = JSONObject.parseObject(result);
            if ("0000".equals(jsonObject.getString("code"))) {
                return jsonObject;
            } else {
                throw new CommonException("获取找油网数据失败，错误原因：" + jsonObject.get("msg").toString());
            }
        } catch (Exception e) {
            log.error("获取找油网数据失败，错误原因：", e);
            throw new CommonException("获取找油网数据失败，错误原因：" + e.getMessage());
        }
    }
}
