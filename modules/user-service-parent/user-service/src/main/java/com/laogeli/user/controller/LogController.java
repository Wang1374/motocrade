package com.laogeli.user.controller;

import com.github.pagehelper.PageInfo;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.model.Log;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.PageUtil;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.common.core.vo.LogVo;
import com.laogeli.common.core.web.BaseController;
import com.laogeli.common.security.constant.SecurityConstant;
import com.laogeli.user.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 日志controller
 *
 * @author wang
 * @date 2019-12-31
 */
@Slf4j
@AllArgsConstructor
@Api("日志信息管理")
@RestController
@RequestMapping("/v1/log")
public class LogController extends BaseController {

    private final LogService logService;

    /**
     * 根据id获取日志
     *
     * @param id id
     * @return Log
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取日志信息", notes = "根据日志id获取日志详细信息")
    @ApiImplicitParam(name = "id", value = "日志ID", required = true, dataType = "String", paramType = "path")
    public Log log(@PathVariable String id) {
        try {
            return logService.get(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new Log();
    }

    /**
     * 日志分页查询
     *
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @param sort     sort
     * @param order    order
     * @param log      log
     * @return PageInfo
     */
    @GetMapping("logList")
    @ApiOperation(value = "获取日志列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "log", value = "日志信息", dataType = "Log")
    })
    public PageInfo<Log> userList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                  @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                  @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                  @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                  Log log) {
        return logService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), log);
    }

    /**
     * 新增日志
     *
     * @param log log
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增日志", notes = "新增日志")
    @ApiImplicitParam(name = "log", value = "日志实体Log", required = true, dataType = "Log")
    public ResponseBean<Boolean> addLog(@RequestBody @Valid Log log) {
        if (StringUtils.isBlank(log.getId()))
            log.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
        // 保存日志
        return new ResponseBean<>(logService.insert(log) > 0);
    }

    /**
     * 删除日志
     *
     * @param id id
     * @return ResponseBean
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('monitor:log:del') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "删除日志", notes = "根据ID删除日志")
    @ApiImplicitParam(name = "id", value = "日志ID", required = true, paramType = "path")
    public ResponseBean<Boolean> delete(@PathVariable String id) {
        Log log = new Log();
        log.setId(id);
        return new ResponseBean<>(logService.delete(log) > 0);
    }

    /**
     * 批量删除
     *
     * @param logInfo logInfo
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @PreAuthorize("hasAuthority('monitor:log:del') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "批量删除日志", notes = "根据日志ids批量删除日志")
    @ApiImplicitParam(name = "logInfo", value = "日志信息", dataType = "Log")
    public ResponseBean<Boolean> deleteAllAttachments(@RequestBody Log logInfo) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(logInfo.getIdString()))
                success = logService.deleteAll(logInfo.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除日志失败！", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 根据订单编号获取日志信息
     *
     * @param orderNumber orderNumber
     * @return List<Clientele>
     */
    @GetMapping("getOrderLog/{orderNumber}")
    @ApiOperation(value = "根据订单编号获取日志信息")
    @ApiImplicitParams({ @ApiImplicitParam(name = "orderNumber", value = "订单编号", dataType = "String") })
    public List<LogVo> getOrderLog(@PathVariable String orderNumber) {
        return logService.getOrderLog(orderNumber);
    }
}
