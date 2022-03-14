package com.laogeli.distribute.controller;

import com.laogeli.distribute.api.module.LclCenter;
import com.laogeli.distribute.api.module.MakingChestCenter;
import com.laogeli.distribute.service.MakingChestCenterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 做箱管理
 *
 * @author wang
 * @date 2020-11-17
 */
@Slf4j
@AllArgsConstructor
@Api("做箱管理")
@RestController
@RequestMapping("/v1/makingChestCenter")
public class MakingChestCenterController {

    private final MakingChestCenterService makingChestCenterService;

    /**
     * 根据下单id获取做箱信息
     *
     * @param placeOrderId placeOrderId
     * @return List<MakingChestCenter>
     */
    @GetMapping("getMakingChestCenterList/{placeOrderId}")
    @ApiOperation(value = "根据下单id获取做箱信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "placeOrderId", value = "下单id", dataType = "String")})
    public List<MakingChestCenter> getMakingChestCenterList(@PathVariable String placeOrderId) {
        MakingChestCenter makingChest = new MakingChestCenter();
        makingChest.setPlaceOrderId(placeOrderId);
        List<MakingChestCenter> mccList = makingChestCenterService.findList(makingChest);
        if (mccList.size() > 0) {
            // 遍历出做箱id 存入数组，通过id数组 批量查询 门点 与 件毛体
            String idString = "";
            for (MakingChestCenter mcc : mccList) {
                idString += mcc.getId() + ",";
            }
            List<LclCenter> lclCenterList = makingChestCenterService.getLclCenterList((idString.substring(0, idString.length() - 1)).split(","));
            for (MakingChestCenter mc : mccList) {
                List<LclCenter> list = new ArrayList<>();
                for (LclCenter lclCenter : lclCenterList) {
                    if (lclCenter.getMcId().equals(mc.getId())) {
                        list.add(lclCenter);
                    }
                }
                mc.setLclList(list);
            }
        }
        return mccList;
    }
}
