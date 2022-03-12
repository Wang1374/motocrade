package com.laogeli.user.controller;

import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.vo.UserVo;
import com.laogeli.common.core.web.BaseController;
import com.laogeli.user.api.dto.DashboardDto;
import com.laogeli.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台首页数据展示
 *
 * @author yangyu
 * @date 2019-12-31
 */
@AllArgsConstructor
@Api("后台首页数据展示")
@RestController
@RequestMapping("/v1/dashboard")
public class DashboardController extends BaseController {

    private final UserService userService;

    /**
     * 获取管控台首页数据
     *
     * @return ResponseBean
     * @author yangyu
     * @date 2020-1-1
     */
    @GetMapping
    @ApiOperation(value = "后台首页数据展示", notes = "后台首页数据展示")
    public ResponseBean<DashboardDto> dashboard() {
        DashboardDto dashboardDto = new DashboardDto();
        // 查询用户数量
        UserVo userVo = new UserVo();
        dashboardDto.setOnlineUserNumber(userService.userCount(userVo).toString());
        return new ResponseBean<>(dashboardDto);
    }
}
