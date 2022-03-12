package com.laogeli.user.controller;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.IdGen;
import com.laogeli.common.core.utils.PageUtil;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.common.log.annotation.Log;
import com.laogeli.common.security.constant.SecurityConstant;
import com.laogeli.user.api.dto.MenuDto;
import com.laogeli.user.api.dto.UserDto;
import com.laogeli.user.api.module.*;
import com.laogeli.user.mapper.StaffRoleMenuMapper;
import com.laogeli.user.service.*;
import com.laogeli.user.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 员工信息权限设置
 *
 * @author yangyu
 * @date 2020-06-03
 */
@Slf4j
@AllArgsConstructor
@Api("员工信息管理")
@RestController
@RequestMapping(value = "/v1/authority")
public class AuthorityController {

    private final UserService userService;

    private final UserAuthsService userAuthsService;

    private final StaffRoleService staffRoleService;

    private final UserStaffRoleService userStaffRoleService;

    private final MenuService menuService;

    private final StaffRoleMenuService staffRoleMenuService;

    /**
     * 获取分页数据
     *
     * @param pageNum           pageNum
     * @param pageSize          pageSize
     * @param sort              sort
     * @param order             order
     * @param corporateIdentify corporateIdentify
     * @return PageInfo
     */
    @GetMapping("staffList")
    @ApiOperation(value = "获取员工列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "corporateIdentify", value = "企业标识id", dataType = "String")
    })
    public PageInfo<UserDto> staffList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                       @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                       @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                       @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                       @RequestParam(value = "corporateIdentify", required = false, defaultValue = "") String corporateIdentify) {
        PageInfo<UserDto> userDtoPageInfo = new PageInfo<>();
        List<UserDto> userDtos = Lists.newArrayList();
        User user = new User();
        user.setEmployeeId(corporateIdentify);
        PageInfo<User> page = userService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), user);
        List<User> users = page.getList();
        if (CollectionUtils.isNotEmpty(users)) {
            // 批量查询账户
            List<UserAuths> userAuths = userAuthsService.getListByUsers(users);
            // 查询员工角色关联关系
            List<UserStaffRole> userStaffRoles = userStaffRoleService.getByUserIds(users.stream().map(User::getId).collect(Collectors.toList()));
            // 批量查找角色
            List<StaffRole> staffRoleList = userStaffRoleService.getStaffRoles(users);
            // 组装数据
            users.forEach(tempUser -> userDtos.add(userStaffRoleService.getUserDtoByUserAndUserAuths(tempUser, userAuths, userStaffRoles, staffRoleList)));
        }
        PageUtil.copyProperties(page, userDtoPageInfo);
        userDtoPageInfo.setList(userDtos);
        return userDtoPageInfo;
    }

    /**
     * 根据企业标识ID，获取当前企业员工角色列表
     *
     * @param corporateIdentify corporateIdentify
     * @return ResponseBean
     */
    @GetMapping("allRoles")
    @ApiOperation(value = "根据企业标识ID，获取当前企业员工角色列表")
    @ApiImplicitParam(name = "staffRole", value = "角色信息", dataType = "StaffRole")
    public ResponseBean<List<StaffRole>> allRoles(@RequestParam(value = "corporateIdentify") String corporateIdentify) {
        StaffRole staffRole = new StaffRole();
        staffRole.setCorporateIdentify(corporateIdentify);
        staffRole.setApplicationCode(SysUtil.getSysCode());
        return new ResponseBean<>(staffRoleService.findAllList(staffRole));
    }

    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("treeselect/{roleCode}")
    @ApiOperation(value = "获取菜单下拉树列表")
    public MenuDto treeselect(@PathVariable String roleCode) {
        // 查询查出当前 企业角色的菜单树结构
        List<Menu> menus = menuService.findMenuByRole(roleCode);
        MenuDto menuDto = new MenuDto();
        menuDto.setMenus(menuService.buildMenuTreeSelect(menus));
        return menuDto;
    }

    /**
     * 创建员工角色
     *
     * @param staffRole staffRole
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "创建员工角色", notes = "创建员工角色")
    @ApiImplicitParam(name = "StaffRole", value = "员工角色实体StaffRole", required = true, dataType = "StaffRole")
    @Log(value = "新增员工角色", type = 0)
    public ResponseBean<Boolean> staffRole(@RequestBody StaffRole staffRole) {
        // 生成唯一角色标识
        staffRole.setRoleCode(SecurityConstant.STAFF_ROLE_PREFIX + IdGen.getCard());
        staffRole.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
        boolean success = staffRoleService.insert(staffRole) > 0;
        if (success)
            return new ResponseBean<>(staffRoleMenuService.saveStaffRoleMenus(staffRole.getId(), staffRole.getMenuIds()) > 0);
        return new ResponseBean<>(-1 > 0);
    }

    /**
     * 根据企业标识id，查出所有员工角色
     */
    @GetMapping("getStaffRole/{corporateIdentify}")
    @ApiOperation(value = "根据企业标识id，查出所有员工角色")
    public  List<StaffRole> getStaffRole(@PathVariable String corporateIdentify) {
        StaffRole staffRole = new StaffRole();
        staffRole.setCorporateIdentify(corporateIdentify);
        return staffRoleService.findList(staffRole);
    }

    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "roleMenuTreeselect")
    @ApiOperation(value = "加载对应角色菜单列表树")
    public MenuDto roleMenuTreeselect(@RequestParam(value = "roleCode") String roleCode, @RequestParam("roleId") String roleId) {
        // 查询企业角色的全部菜单
        List<Menu> menus = menuService.findMenuByRole(roleCode);
        MenuDto menuDto = new MenuDto();
        // 选中需要改
        menuDto.setCheckedKeys(menuService.selectMenuListByStaffRoleId(roleId));
        menuDto.setMenus(menuService.buildMenuTreeSelect(menus));
        return menuDto;
    }

    /**
     * 修改员工角色菜单信息
     *
     * @param staffRole staffRole
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新员工角色菜单信息", notes = "根据员工角色id更新角色菜单信息")
    @ApiImplicitParam(name = "role", value = "角色实体role", required = true, dataType = "RoleVo")
    @Log(value = "修改员工角色菜单", type = 0)
    public ResponseBean<Boolean> updateRole(@RequestBody @Valid StaffRole staffRole) {
        staffRole.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
        boolean success = staffRoleService.update(staffRole) > 0;
        if (success)
            return new ResponseBean<>(staffRoleMenuService.saveStaffRoleMenus(staffRole.getId(), staffRole.getMenuIds()) > 0);
        return new ResponseBean<>(-1 > 0);
    }
}
