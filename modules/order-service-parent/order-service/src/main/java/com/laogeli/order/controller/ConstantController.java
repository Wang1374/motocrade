package com.laogeli.order.controller;

import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.exceptions.CommonException;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.DateUtils;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.order.api.module.ShipsName;
import com.laogeli.order.api.module.Vehicle;
import com.laogeli.order.api.vo.BoxPileVo;
import com.laogeli.order.api.vo.DockVo;
import com.laogeli.order.api.vo.ShipCompanyVo;
import com.laogeli.order.mapper.ConstantMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 常量信息管理
 *
 * @author yangyu
 * @date 2020-07-02
 */
@Slf4j
@AllArgsConstructor
@Api("常量信息管理")
@RestController
@RequestMapping("/v1/constant")
public class ConstantController {

    private final ConstantMapper constantMapper;
    private final RedisTemplate redisTemplate;
    private final RedissonClient redissonClient;

    /**
     * 获取船名数组
     *
     * @return List<String>
     */
    @GetMapping("allShipsName")
    @ApiOperation(value = "获取船名数组")
    public List<String> allShipsName() {
        // 放入缓存，过期时间月底。
        String shipsNameKey = CommonConstant.SHIPS_NAME_KEY;
        if (redisTemplate.hasKey(shipsNameKey)) {
            List<String> shipsNameList = redisTemplate.opsForList().range(shipsNameKey, 0, -1);
            return shipsNameList;
        } else {
            List<String> shipsNameList = constantMapper.getAllShipsName();
            redisTemplate.opsForList().rightPushAll(shipsNameKey, shipsNameList);
            Long expireTime = DateUtils.getSecondsApart();
            redisTemplate.expire(shipsNameKey, expireTime * 1000, TimeUnit.MILLISECONDS);
            return shipsNameList;
        }
    }

    /**
     * 获取船公司列表
     *
     * @return List<String>
     */
    @GetMapping("allShipCompany")
    @ApiOperation(value = "获取船公司列表")
    public List<ShipCompanyVo> allShipCompany() {
        // 放入缓存，过期时间月底。
        String shipCompanyKey = CommonConstant.SHIP_COMPANY_KEY;
        if (redisTemplate.hasKey(shipCompanyKey)) {
            List<ShipCompanyVo> shipCompanyVoList = redisTemplate.opsForList().range(shipCompanyKey, 0, -1);
            return shipCompanyVoList;
        } else {
            List<ShipCompanyVo> shipCompanyVoList = constantMapper.getAllShipCompany();
            redisTemplate.opsForList().rightPushAll(shipCompanyKey, shipCompanyVoList);
            Long expireTime = DateUtils.getSecondsApart();
            redisTemplate.expire(shipCompanyKey, expireTime * 1000, TimeUnit.MILLISECONDS);
            return shipCompanyVoList;
        }
    }

    /**
     * 获取码头列表
     *
     * @return List<String>
     */
    @GetMapping("dockList")
    @ApiOperation(value = "获取码头列表")
    public List<DockVo> dockList() {
        // 放入缓存，过期时间月底。
        String dockKey = CommonConstant.DOCK_KEY;
        if (redisTemplate.hasKey(dockKey)) {
            List<DockVo> dockVoList = redisTemplate.opsForList().range(dockKey, 0, -1);
            return dockVoList;
        } else {
            List<DockVo> dockVoList = constantMapper.getAllDock();
            redisTemplate.opsForList().rightPushAll(dockKey, dockVoList);
            Long expireTime = DateUtils.getSecondsApart();
            redisTemplate.expire(dockKey, expireTime * 1000, TimeUnit.MILLISECONDS);
            return dockVoList;
        }
    }

    /**
     * 获取箱型列表
     *
     * @return List<String>
     */
    @GetMapping("boxPileList")
    @ApiOperation(value = "获取箱型列表")
    public List<BoxPileVo> boxPileList() {
        // 放入缓存，过期时间月底。
        String boxPileKey = CommonConstant.BOX_PILE_KEY;
        if (redisTemplate.hasKey(boxPileKey)) {
            List<BoxPileVo> boxPileVoList = redisTemplate.opsForList().range(boxPileKey, 0, -1);
            return boxPileVoList;
        } else {
            List<BoxPileVo> boxPileVoList = constantMapper.getAllBoxPile();
            redisTemplate.opsForList().rightPushAll(boxPileKey, boxPileVoList);
            Long expireTime = DateUtils.getSecondsApart();
            redisTemplate.expire(boxPileKey, expireTime * 1000, TimeUnit.MILLISECONDS);
            return boxPileVoList;
        }
    }

    /**
     * 添加船名
     * @param shipsName
     * @return
     */
    @RequestMapping("/addShipName")
    @ApiOperation(value = "添加船名")
    public ResponseBean<Integer> addShipsName(@RequestBody ShipsName shipsName){
        RLock lock = redissonClient.getLock(shipsName.getShipsName());
        boolean getLock = false;
        try{
            Integer count = 0;
            if (getLock = lock.tryLock(0, 5, TimeUnit.SECONDS)) {
                //查询船名是否存在
                ShipsName resulet = constantMapper.getShipName(shipsName);
                if(resulet==null){
                    //执行新增
                    count = constantMapper.addShipName(shipsName);
                }
            }else {
                log.info("Redisson分布式锁没有获得锁:{},ThreadName:{}", shipsName.getShipsName(), Thread.currentThread().getName());
            }
            if (count == 0) {
                return new ResponseBean<>(count, "船名已经存在");
            } else {
                String shipsNameKey = CommonConstant.SHIPS_NAME_KEY;
                //判断有没有缓存
                if(redisTemplate.hasKey(shipsNameKey)){
                    //删除缓存
                    redisTemplate.delete(shipsNameKey);
                }
                return new ResponseBean<>(count, "新增成功");
            }
        }catch (Exception e){
            log.error("新增船名失败, Redisson 获取分布式锁异常, 异常信息:{}", e);
            throw new CommonException("新增船名失败");
        }finally {
            if (!getLock) {
                return new ResponseBean<>(-1, "请勿重复操作");
            }
            lock.unlock();
            log.info("Redisson分布式锁释放锁:{},ThreadName :{}",shipsName.getShipsName(), Thread.currentThread().getName());
        }


    }
}
