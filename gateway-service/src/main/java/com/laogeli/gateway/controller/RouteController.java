package com.laogeli.gateway.controller;

import com.github.pagehelper.PageInfo;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.exceptions.CommonException;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.PageUtil;
import com.laogeli.common.core.web.BaseController;
import com.laogeli.gateway.module.Route;
import com.laogeli.gateway.service.RouteService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 路由controller
 * TODO：增加security认证
 *
 * @author wang
 * @date 2019-12-31
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/route/v1/route")
public class RouteController extends BaseController {

    private final RouteService routeService;

    /**
     * 根据id获取路由
     *
     * @param id id
     * @return Route
     * @author wang
     * @date 2019-12-31
     */
    @GetMapping("/{id}")
    public Route get(@PathVariable String id) {
        try {
            Route route = new Route();
            route.setId(id);
            return routeService.get(route);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException(e.getMessage());
        }
    }

    /**
     * 路由分页查询
     *
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @param sort     sort
     * @param order    order
     * @param route    route
     * @return PageInfo
     * @author wang
     * @date 2019-12-31
     */
    @GetMapping("routeList")
    public PageInfo<Route> userList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                    @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                    @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                    @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                    Route route) {
        return routeService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), route);
    }

    /**
     * 修改路由
     *
     * @param route route
     * @return ResponseBean
     * @author wang
     * @date 2019-12-31
     */
    @PutMapping
    public ResponseBean<Boolean> updateRoute(@RequestBody @Valid Route route) {
        try {
            // 更新路由
            return new ResponseBean<>(routeService.update(route) > 0);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException(e.getMessage());
        }
    }

    /**
     * 创建路由
     *
     * @param route route
     * @return ResponseBean
     * @author wang
     * @date 2019-12-31
     */
    @PostMapping
    public ResponseBean<Boolean> add(@RequestBody @Valid Route route) {
        try {
            // 新增路由
            return new ResponseBean<>(routeService.insert(route) > 0);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException(e.getMessage());
        }
    }

    /**
     * 根据id删除路由
     *
     * @param id id
     * @return ResponseBean
     * @author wang
     * @date 2019-12-31
     */
    @DeleteMapping("/{id}")
    public ResponseBean<Boolean> delete(@PathVariable String id) {
        try {
            return new ResponseBean<>(routeService.delete(id) > 0);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException(e.getMessage());
        }
    }

    /**
     * 批量删除
     *
     * @param route route
     * @return ResponseBean
     * @author wang
     * @date 2019-12-31
     */
    @PostMapping("deleteAll")
    public ResponseBean<Boolean> deleteAll(@RequestBody Route route) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(route.getIdString()))
                success = routeService.deleteAll(route.getIdString().split(",")) > 0;
            return new ResponseBean<>(success);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException(e.getMessage());
        }
    }


    /**
     * 刷新路由
     *
     * @return ResponseBean
     * @author wang
     * @date 2019-12-31
     */
    @GetMapping("refresh")
    public ResponseBean<Boolean> refresh() {
        try {
            return new ResponseBean<>(routeService.refresh());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CommonException(e.getMessage());
        }
    }
}
