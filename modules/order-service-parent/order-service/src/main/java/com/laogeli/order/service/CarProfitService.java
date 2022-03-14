package com.laogeli.order.service;

import com.alibaba.fastjson.JSONArray;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.exceptions.CommonException;
import com.laogeli.common.core.utils.DateUtils;
import com.laogeli.common.core.utils.JsonMapper;
import com.laogeli.order.api.vo.ProfitVo;
import com.laogeli.order.api.vo.ZywOrder;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.laogeli.common.core.service.CrudService;
import com.laogeli.order.api.module.*;
import com.laogeli.order.api.vo.VehicleCostVo;
import com.laogeli.order.mapper.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;


/**
 * 车辆利润分析业务层
 *
 * @author wang
 * @date 2020-10-10
 */
@Slf4j
@AllArgsConstructor
@Service
public class CarProfitService extends CrudService<CarProfitMapper, Cost> {

    private final RedisTemplate redisTemplate;

    private final CostMapper costMapper;

    private final CarExtraCostMapper carExtraCostMapper;

    private final CarRoadCostMapper carRoadCostMapper;

    private final OilCardMapper oilCardMapper;

    private final CarMaintainMapper carMaintainMapper;

    private final CarRepairMapper carRepairMapper;

    private final CarTyreMapper carTyreMapper;

    private final CarExtraCostService carExtraCostService;

    private final CarRoadCostService carRoadCostService;

    private static int corePoolSize = Runtime.getRuntime().availableProcessors() * 2;

    /**
     * 查询分页,获取收益
     *
     * @param page   page
     * @param entity entity
     * @return PageInfo
     */
    @Transactional
    public PageInfo<ProfitVo> findProfitPage(PageInfo<Cost> page, ProfitVo entity) {
        //如果没有传递开始时间，默认查询从当前年第一天到此刻的值
        if (entity.getBeginTime().equals("") || entity.getBeginTime() == null) {
            entity.setBeginTime(DateUtils.getCurrentYear());
        }
        if (entity.getEndTime().equals("") || entity.getEndTime() == null) {
            entity.setEndTime(DateUtils.getCurrentDate());
        }
        PageHelper.startPage(page.getPageNum(), page.getPageSize());

        //车牌号数组,按照“,"号进行拆分
        String[] vehicleArrays = entity.getVehicles().split(",");
        List<String> cars = Arrays.asList(vehicleArrays);

        entity.setCarArrays(cars);
        long l1 = System.currentTimeMillis();
        //查询业务毛利
        List<ProfitVo> businessList = costMapper.getCarProfitList(entity);
        long l2 = System.currentTimeMillis();

        /*
         * 有业务才会查出车票，若是没有业务毛利，其他费用是无法赋值的，因是根据车牌号进行赋值的。简单处理就是添加有没查询到业务毛利的车辆到 businessList*/

        //根据业务毛利结果，过滤车牌号
        List<String> vehicleArray = businessList.stream().map(ProfitVo::getVehicle).collect(Collectors.toList());
        //车牌号去重
        Set<String> vehicleSet = new HashSet<>();
        vehicleSet.addAll(vehicleArray);

        //查询车辆成本，邮费，保养维修，保险，其它费用
        //分组统计的车牌号和费用
        if (vehicleArray.size() > 0) {
            // 开启多线程执行并发任务
            long t1 = System.currentTimeMillis();
            getCostVoAsync(vehicleSet, entity, businessList);
            long t2 = System.currentTimeMillis();

            List<ZywOrder> zywDataList;
            //查缓存数据
            String zywDataKey = CommonConstant.ZYW_KEY;
            if (redisTemplate.hasKey(zywDataKey)) {
                //查缓存
                String jsonZywdata = String.valueOf(redisTemplate.opsForValue().get(zywDataKey));
                zywDataList = JSONArray.parseArray(jsonZywdata, ZywOrder.class);
                //过滤时间范围内的zywDataList
                zywDataList = zywDataList.stream().filter(s ->
                        DateUtils.getDate(s.getOrderTime()).getTime() >= DateUtils.getDate(entity.getBeginTime()).getTime()
                                && DateUtils.getDate(s.getOrderTime()).getTime() <= DateUtils.getDate(entity.getEndTime()).getTime())
                        .collect(Collectors.toList());
                for (ProfitVo profitVo : businessList
                ) {
                    for (int i = 0; i < zywDataList.size(); i++) {
                        if (profitVo.getVehicle().equals(zywDataList.get(i).getTruckLicenseNumber())) {
                            if (profitVo.getCarOilPrice() == null) {
                                profitVo.setCarOilPrice(zywDataList.get(i).getPaymentAmount());
                            } else {
                                profitVo.setCarOilPrice(profitVo.getCarOilPrice().add(zywDataList.get(i).getPaymentAmount()));
                            }
                        }
                    }
                }
            }
        }
        PageInfo<ProfitVo> pageInfo = new PageInfo<>(businessList);
        return pageInfo;
    }

    private void m1(Set<String> vehicleSet, ProfitVo entity, List<ProfitVo> businessList) {
        //            // 根据车牌号
        //1  查询其它费用
            List<VehicleCostVo> extraCost = carExtraCostMapper.findCost(vehicleSet, entity.getBelongCompaniesId(), entity.getBeginTime(), entity.getEndTime());

            //2查路费
           List<VehicleCostVo> carRoadCost = carRoadCostMapper.findRoadCost(vehicleSet, entity.getBelongCompaniesId(), entity.getBeginTime(), entity.getEndTime());


            //4 查系统邮费
            List<VehicleCostVo> oilCost = oilCardMapper.findOilCost(vehicleSet, entity.getBelongCompaniesId(), entity.getBeginTime(), entity.getEndTime());

            //5 查保养
            List<VehicleCostVo> maintainCost = carMaintainMapper.findMaintainCost(vehicleSet, entity.getBelongCompaniesId(), entity.getBeginTime(), entity.getEndTime());

            //6  查维修
            List<VehicleCostVo> repairCost = carRepairMapper.findRepairCost(vehicleSet, entity.getBelongCompaniesId(), entity.getBeginTime(), entity.getEndTime());

            //7 查轮胎费用
            List<VehicleCostVo> tyreCost = carTyreMapper.findTyreCost(vehicleSet, entity.getBelongCompaniesId(), entity.getBeginTime(), entity.getEndTime());
            long a1 = System.currentTimeMillis();

            for (ProfitVo profitVo : businessList
            ) {
                //系统油费
                for (VehicleCostVo vehicleCostVo : oilCost
                ) {
                    if (profitVo.getVehicle().equals(vehicleCostVo.getVehicle())) {
                        profitVo.setCarOilPrice(vehicleCostVo.getTotalPrice());
                    }
                }
                //其它费用
                for (VehicleCostVo vehicleCostVo : extraCost
                ) {
                    if (vehicleCostVo.getVehicle().equals(profitVo.getVehicle())) {
                        profitVo.setCarExtraPrice(vehicleCostVo.getTotalPrice());
                    }
                }
                //路费
                for (VehicleCostVo vehicleCostVo : carRoadCost
                ) {
                    if (vehicleCostVo.getVehicle().equals(profitVo.getVehicle())) {
                        profitVo.setCarRoadPrice(vehicleCostVo.getTotalPrice());
                    }
                }
                //保养费用
                for (VehicleCostVo vehicleCostVo : maintainCost
                ) {
                    if (vehicleCostVo.getVehicle().equals(profitVo.getVehicle())) {
                        profitVo.setCarMaintainPrice(vehicleCostVo.getTotalPrice());
                    }
                }
                //维修费用
                for (VehicleCostVo vehicleCostVo : repairCost
                ) {
                    if (vehicleCostVo.getVehicle().equals(profitVo.getVehicle())) {
                        profitVo.setCarRepairPrice(vehicleCostVo.getTotalPrice());
                    }
                }

                //轮胎费用
                for (VehicleCostVo vehicleCostVo : tyreCost
                ) {
                    if (vehicleCostVo.getVehicle().equals(profitVo.getVehicle())) {
                        profitVo.setCarTyrePrice(vehicleCostVo.getTotalPrice());
                    }
                }
            }
    }

    public void getCostVoAsync(Set<String> vehicleSet, ProfitVo entity, List<ProfitVo> businessList) {
        //核心线程数不能超过最大线程数
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
                1000,
                2L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());

        // 任务 1
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            List<VehicleCostVo> extraCost = carExtraCostMapper.findCost(vehicleSet, entity.getBelongCompaniesId(), entity.getBeginTime(), entity.getEndTime());
            for (ProfitVo profitVo : businessList
            ) {
                //其它费用
                for (VehicleCostVo vehicleCostVo : extraCost
                ) {
                    if (vehicleCostVo.getVehicle().equals(profitVo.getVehicle())) {
                        profitVo.setCarExtraPrice(vehicleCostVo.getTotalPrice());
                    }
                }
            }
        }, threadPoolExecutor).exceptionally((e) -> {
            e.printStackTrace();
            throw new CommonException("查询其它费用失败");
        });
        // 任务 2
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            List<VehicleCostVo> carRoadCost = carRoadCostMapper.findRoadCost(vehicleSet, entity.getBelongCompaniesId(), entity.getBeginTime(), entity.getEndTime());
            for (ProfitVo profitVo : businessList
            ) {
                //路费
                for (VehicleCostVo vehicleCostVo : carRoadCost
                ) {
                    if (vehicleCostVo.getVehicle().equals(profitVo.getVehicle())) {
                        profitVo.setCarRoadPrice(vehicleCostVo.getTotalPrice());
                    }
                }
            }
        }, threadPoolExecutor).exceptionally((e) -> {
            e.printStackTrace();
            throw new CommonException("查询路费失败");
        });
        // 任务 3
        CompletableFuture<Void> future3 = CompletableFuture.runAsync(() -> {
            List<VehicleCostVo> oilCost = oilCardMapper.findOilCost(vehicleSet, entity.getBelongCompaniesId(), entity.getBeginTime(), entity.getEndTime());
            for (ProfitVo profitVo : businessList
            ) {
                //系统油费
                for (VehicleCostVo vehicleCostVo : oilCost
                ) {
                    if (profitVo.getVehicle().equals(vehicleCostVo.getVehicle())) {
                        profitVo.setCarOilPrice(vehicleCostVo.getTotalPrice());
                    }
                }
            }
        }, threadPoolExecutor).exceptionally((e) -> {
            e.printStackTrace();
            throw new CommonException("查询系统油费失败");
        });

        // 任务 4
        CompletableFuture<Void> future4 = CompletableFuture.runAsync(() -> {
            List<VehicleCostVo> maintainCost = carMaintainMapper.findMaintainCost(vehicleSet, entity.getBelongCompaniesId(), entity.getBeginTime(), entity.getEndTime());
            for (ProfitVo profitVo : businessList
            ) {
                //保养费用
                for (VehicleCostVo vehicleCostVo : maintainCost
                ) {
                    if (vehicleCostVo.getVehicle().equals(profitVo.getVehicle())) {
                        profitVo.setCarMaintainPrice(vehicleCostVo.getTotalPrice());
                    }
                }
            }
        }, threadPoolExecutor).exceptionally((e) -> {
            e.printStackTrace();
            throw new CommonException("查询保养费用失败");
        });
        // 任务5
        CompletableFuture<Void> future5 = CompletableFuture.runAsync(() -> {
            List<VehicleCostVo> repairCost = carRepairMapper.findRepairCost(vehicleSet, entity.getBelongCompaniesId(), entity.getBeginTime(), entity.getEndTime());
            for (ProfitVo profitVo : businessList
            ) {
                //维修费用
                for (VehicleCostVo vehicleCostVo : repairCost
                ) {
                    if (vehicleCostVo.getVehicle().equals(profitVo.getVehicle())) {
                        profitVo.setCarRepairPrice(vehicleCostVo.getTotalPrice());
                    }
                }
            }
        }, threadPoolExecutor).exceptionally((e) -> {
            e.printStackTrace();
            throw new CommonException("查询维修费用失败");
        });

        // 任务 6
        CompletableFuture<Void> future6 = CompletableFuture.runAsync(() -> {
            List<VehicleCostVo> tyreCost = carTyreMapper.findTyreCost(vehicleSet, entity.getBelongCompaniesId(), entity.getBeginTime(), entity.getEndTime());
            for (ProfitVo profitVo : businessList
            ) {
                //轮胎费用
                for (VehicleCostVo vehicleCostVo : tyreCost
                ) {
                    if (vehicleCostVo.getVehicle().equals(profitVo.getVehicle())) {
                        profitVo.setCarTyrePrice(vehicleCostVo.getTotalPrice());
                    }
                }
            }
        }, threadPoolExecutor).exceptionally((e) -> {
            e.printStackTrace();
            throw new CommonException("查询轮胎费用失败");
        });

        CompletableFuture<Void> future = CompletableFuture.allOf(future1, future2, future3, future4, future5, future6);
        try {
            future.get();// 等待所有任务都完成
            threadPoolExecutor.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonException("查询费用失败");
        }
    }

    /**
     * 根据年份获取车辆平均每月利润
     *
     * @param entity entity
     * @return PageInfo
     */
    @Transactional
    public List<ProfitVo> findMonthlyProfit(ProfitVo entity) {

        //车牌号数组,按照“,"号进行拆分
        String[] vehicleArrays = entity.getVehicles().split(",");
        List<String> cars = Arrays.asList(vehicleArrays);
        entity.setCarArrays(cars);

        // 1.查业务毛利
        List<ProfitVo> businessList = costMapper.getMonthlyCarProfit(entity);

        // 根据业务毛利结果，过滤车牌号
        List<String> vehicleArray = businessList.stream().map(ProfitVo::getVehicle).collect(Collectors.toList());

        if (vehicleArray.size() > 0) {
            // 2.查其它费用
            List<VehicleCostVo> extraCostList = carExtraCostMapper.monthlyExtraCost(vehicleArray, entity.getBelongCompaniesId(), entity.getYear());

            // 3.查路费
            List<VehicleCostVo> carRoadCostList = carRoadCostMapper.monthlyRoadCost(vehicleArray, entity.getBelongCompaniesId(), entity.getYear());

            // 4.查系统邮费
            List<VehicleCostVo> oilCostList = oilCardMapper.monthlyOilCost(vehicleArray, entity.getBelongCompaniesId(), entity.getYear());

            //5 查保养
            List<VehicleCostVo> carMaintainCost = carMaintainMapper.monthlyMaintainCost(vehicleArray, entity.getBelongCompaniesId(), entity.getYear());

            // 6.查油网缓存数据
            Object object = redisTemplate.opsForValue().get(CommonConstant.ZYW_KEY);

            //7 查维修
            //carRepairMapper
            List<VehicleCostVo> carRepairCost = carRepairMapper.monthlyRepairCost(vehicleArray, entity.getBelongCompaniesId(), entity.getYear());

            // 所有成本相加
            for (ProfitVo profitVo : businessList) {
                BigDecimal jan = new BigDecimal(0);
                BigDecimal feb = new BigDecimal(0);
                BigDecimal mar = new BigDecimal(0);
                BigDecimal apr = new BigDecimal(0);
                BigDecimal may = new BigDecimal(0);
                BigDecimal june = new BigDecimal(0);
                BigDecimal july = new BigDecimal(0);
                BigDecimal aug = new BigDecimal(0);
                BigDecimal sept = new BigDecimal(0);
                BigDecimal oct = new BigDecimal(0);
                BigDecimal nov = new BigDecimal(0);
                BigDecimal dece = new BigDecimal(0);

//                // 其它费用
                for (VehicleCostVo vehicleCostVo : extraCostList) {
                    if (profitVo.getVehicle().equals(vehicleCostVo.getVehicle())) {
                        jan = jan.add(vehicleCostVo.getJan());
                        feb = feb.add(vehicleCostVo.getFeb());
                        mar = mar.add(vehicleCostVo.getMar());
                        apr = apr.add(vehicleCostVo.getApr());
                        may = may.add(vehicleCostVo.getMay());
                        june = june.add(vehicleCostVo.getJune());
                        july = july.add(vehicleCostVo.getJuly());
                        aug = aug.add(vehicleCostVo.getAug());
                        sept = sept.add(vehicleCostVo.getSept());
                        oct = oct.add(vehicleCostVo.getOct());
                        nov = nov.add(vehicleCostVo.getNov());
                        dece = dece.add(vehicleCostVo.getDece());
                    }
                }
//
//                // 路费
                for (VehicleCostVo vehicleCostVo : carRoadCostList) {
                    if (profitVo.getVehicle().equals(vehicleCostVo.getVehicle())) {
                        jan = jan.add(vehicleCostVo.getJan());
                        feb = feb.add(vehicleCostVo.getFeb());
                        mar = mar.add(vehicleCostVo.getMar());
                        apr = apr.add(vehicleCostVo.getApr());
                        may = may.add(vehicleCostVo.getMay());
                        june = june.add(vehicleCostVo.getJune());
                        july = july.add(vehicleCostVo.getJuly());
                        aug = aug.add(vehicleCostVo.getAug());
                        sept = sept.add(vehicleCostVo.getSept());
                        oct = oct.add(vehicleCostVo.getOct());
                        nov = nov.add(vehicleCostVo.getNov());
                        dece = dece.add(vehicleCostVo.getDece());
                    }
                }
                // 保养费用
                for (VehicleCostVo vehicleCostVo : carMaintainCost) {
                    if (profitVo.getVehicle().equals(vehicleCostVo.getVehicle())) {
                        jan = jan.add(vehicleCostVo.getJan());
                        feb = feb.add(vehicleCostVo.getFeb());
                        mar = mar.add(vehicleCostVo.getMar());
                        apr = apr.add(vehicleCostVo.getApr());
                        may = may.add(vehicleCostVo.getMay());
                        june = june.add(vehicleCostVo.getJune());
                        july = july.add(vehicleCostVo.getJuly());
                        aug = aug.add(vehicleCostVo.getAug());
                        sept = sept.add(vehicleCostVo.getSept());
                        oct = oct.add(vehicleCostVo.getOct());
                        nov = nov.add(vehicleCostVo.getNov());
                        dece = dece.add(vehicleCostVo.getDece());
                    }
                }

                // 维修费用
                for (VehicleCostVo vehicleCostVo : carRepairCost) {
                    if (profitVo.getVehicle().equals(vehicleCostVo.getVehicle())) {
                        jan = jan.add(vehicleCostVo.getJan());
                        feb = feb.add(vehicleCostVo.getFeb());
                        mar = mar.add(vehicleCostVo.getMar());
                        apr = apr.add(vehicleCostVo.getApr());
                        may = may.add(vehicleCostVo.getMay());
                        june = june.add(vehicleCostVo.getJune());
                        july = july.add(vehicleCostVo.getJuly());
                        aug = aug.add(vehicleCostVo.getAug());
                        sept = sept.add(vehicleCostVo.getSept());
                        oct = oct.add(vehicleCostVo.getOct());
                        nov = nov.add(vehicleCostVo.getNov());
                        dece = dece.add(vehicleCostVo.getDece());
                    }
                }

                // 系统油费
                for (VehicleCostVo vehicleCostVo : oilCostList) {
                    if (profitVo.getVehicle().equals(vehicleCostVo.getVehicle())) {
                        jan = jan.add(vehicleCostVo.getJan());
                        feb = feb.add(vehicleCostVo.getFeb());
                        mar = mar.add(vehicleCostVo.getMar());
                        apr = apr.add(vehicleCostVo.getApr());
                        may = may.add(vehicleCostVo.getMay());
                        june = june.add(vehicleCostVo.getJune());
                        july = july.add(vehicleCostVo.getJuly());
                        aug = aug.add(vehicleCostVo.getAug());
                        sept = sept.add(vehicleCostVo.getSept());
                        oct = oct.add(vehicleCostVo.getOct());
                        nov = nov.add(vehicleCostVo.getNov());
                        dece = dece.add(vehicleCostVo.getDece());
                    }
                }

                if (object != null) {
                    // 转为list对象
                    List<ZywOrder> zywOrderList = JsonMapper.getInstance().fromJson(object.toString(), JsonMapper.getInstance().createCollectionType(ArrayList.class, ZywOrder.class));
                    // 按传入的年份过滤, 再按车牌号与月份分组, 月份下的金额进行合并统计
                    Map<String/*车牌号*/, Map<Integer/*月份*/, Double/*金额*/>> sumCarMonth = zywOrderList.stream().filter(z -> DateUtils.getYear(z.getOrderTime()) == Integer.parseInt(entity.getYear())).collect(
                            Collectors.groupingBy(t -> t.getTruckLicenseNumber(),
                                    Collectors.groupingBy(t -> DateUtils.getMonth(t.getOrderTime()),
                                            Collectors.summingDouble(t -> t.getPaymentAmount().doubleValue()))));

                    // 找油网邮费
                    for (String vehicleKey : sumCarMonth.keySet()) {
                        // 车辆相同
                        if (profitVo.getVehicle().equals(vehicleKey)) {
                            Map<Integer/*月份*/, Double/*金额*/> monthMap = sumCarMonth.get(vehicleKey);
                            for (Integer monthKey : monthMap.keySet()) {
                                BigDecimal value = new BigDecimal(Double.toString(monthMap.get(monthKey)));
                                switch (monthKey) {
                                    case 1:
                                        jan = jan.add(value);
                                        break;
                                    case 2:
                                        feb = feb.add(value);
                                        break;
                                    case 3:
                                        mar = mar.add(value);
                                        break;
                                    case 4:
                                        apr = apr.add(value);
                                        break;
                                    case 5:
                                        may = may.add(value);
                                        break;
                                    case 6:
                                        june = june.add(value);
                                        break;
                                    case 7:
                                        july = july.add(value);
                                        break;
                                    case 8:
                                        aug = aug.add(value);
                                        break;
                                    case 9:
                                        sept = sept.add(value);
                                        break;
                                    case 10:
                                        oct = oct.add(value);
                                        break;
                                    case 11:
                                        nov = nov.add(value);
                                        break;
                                    case 12:
                                        dece = dece.add(value);
                                        break;
                                }
                            }
                        }
                    }
                }

                // 本车利润减去 所有成本
                profitVo.setJan(profitVo.getJan().subtract(jan));
                profitVo.setFeb(profitVo.getFeb().subtract(feb));
                profitVo.setMar(profitVo.getMar().subtract(mar));
                profitVo.setApr(profitVo.getApr().subtract(apr));
                profitVo.setMay(profitVo.getMay().subtract(may));
                profitVo.setJune(profitVo.getJune().subtract(june));
                profitVo.setJuly(profitVo.getJuly().subtract(july));
                profitVo.setAug(profitVo.getAug().subtract(aug));
                profitVo.setSept(profitVo.getSept().subtract(sept));
                profitVo.setOct(profitVo.getOct().subtract(oct));
                profitVo.setNov(profitVo.getNov().subtract(nov));
                profitVo.setDece(profitVo.getDece().subtract(dece));
            }
        }
        return businessList;
    }

}
