package com.laogeli.order.controller;

import com.github.pagehelper.PageInfo;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.exceptions.CommonException;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.PageUtil;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.order.api.module.CarMaintain;
import com.laogeli.order.mapper.CarMaintainMapper;
import com.laogeli.order.service.CarMaintainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 车辆维修记录管理
 *
 * @author BeiFang
 * @Date 2020-07-19
 **/
@Slf4j
@AllArgsConstructor
@Api("车辆保养维修记录管理")
@RestController
@RequestMapping("/v1/maintain")
public class CarMaintainController {

    private final CarMaintainService carMaintainService;

    private final CarMaintainMapper carMaintainMapper;

    /* *
     *  新增保养记录
     * @Param [repairList]
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增保养记录", notes = "新增保养记录信息")
    @ApiImplicitParam(name = "arMaintainList", value = "车辆保养记录数组", required = true, dataType = "List<CarMaintain>")
    public ResponseBean<Boolean> addMaintain(@RequestBody CarMaintain carMaintain) {

        carMaintain.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());

        return new ResponseBean<>(carMaintainMapper.addListObj(carMaintain) > 0);
    }

    /**
     * 修改保养维修记录
     *
     * @param carMaintain 保养维修记录实体
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改保养维修记录", notes = "修改保养维修记录")
    @ApiImplicitParam(name = "repairList", value = "车辆保养记录数组", required = true, dataType = "CarMaintain")
    public ResponseBean<Boolean> editMatintain(@RequestBody CarMaintain carMaintain) {
        try {
            carMaintain.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
            return new ResponseBean<>(carMaintainMapper.update(carMaintain) > 0);
        } catch (Exception e) {
            log.error("更新保养维修记录失败", e);
            throw new CommonException("更新保养维修记录失败");
        }
    }

    /**
     * 删除保养维修记录
     *
     * @param id 保养维修记录id
     * @return
     */
    @RequestMapping("/{id}")
    @ApiOperation(value = "删除保养维修记录", notes = "删除保养维修记录")
    @ApiImplicitParam(name = "id", value = "车辆保养记录id", required = true, dataType = "id")
    public ResponseBean<Boolean> deleteMaintain(@PathVariable String id) {
        CarMaintain carMaintain = new CarMaintain();
        carMaintain.setId(id);
        return new ResponseBean<>(carMaintainMapper.delete(carMaintain) > 0);
    }

    /**
     * 分页查询保养维修记录
     *
     * @param pageName          分页页码
     * @param pageSize          分页大小
     * @param sort              排序字段
     * @param order             排序方向
     * @param corporateIdentify 所属公司标识
     * @param searchParam       查询参数
     * @return
     */
    @RequestMapping("/getMaintain")
    @ApiOperation(value = "查询保养维修记录", notes = "分页查询保养维修记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "corporateIdentify", value = "企业标识id", dataType = "String")

    })
    public PageInfo<CarMaintain> findPage(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageName,
                                          @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                          @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.SORT) String sort,
                                          @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.ORDER) String order,
                                          @RequestParam(value = "corporateIdentify") String corporateIdentify,
                                          @RequestParam(value = "searchParam", required = false, defaultValue = "") String searchParam,
                                          @RequestParam(value = "vehicle", required = false, defaultValue = "") String vehicle,
                                          @RequestParam(value = "beginTime", required = false, defaultValue = "") String beginTime,
                                          @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime,
                                          @RequestParam(value = "mainProject",required = false,defaultValue = "")String mainProject) {
        CarMaintain carMaintain = new CarMaintain();
        carMaintain.setSearchParam(searchParam);
        carMaintain.setBelongCompaniesId(corporateIdentify);
        carMaintain.setVehicle(vehicle);
        carMaintain.setBeginTime(beginTime);
        carMaintain.setEndTime(endTime);
        carMaintain.setMainProject(mainProject);
        PageInfo<CarMaintain> pageInfo = carMaintainService.findPage(PageUtil.pageInfo(pageName, pageSize, sort, order), carMaintain);
        return pageInfo;
    }

}
