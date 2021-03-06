package com.laogeli.user.controller;

import com.github.pagehelper.PageInfo;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.PageUtil;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.common.core.web.BaseController;
import com.laogeli.common.log.annotation.Log;
import com.laogeli.common.security.constant.SecurityConstant;
import com.laogeli.user.api.module.Role;
import com.laogeli.user.service.RoleMenuService;
import com.laogeli.user.service.RoleService;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 角色controller
 *
 * @author wang
 * @date 2019-12-31
 */
@Slf4j
@AllArgsConstructor
@Api("角色信息管理")
@RestController
@RequestMapping("/v1/role")
public class RoleController extends BaseController {

    private final RoleService roleService;

    private final RoleMenuService roleMenuService;

    /**
     * 根据id获取角色
     *
     * @param id id
     * @return RoleVo
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取角色信息", notes = "根据角色id获取角色详细信息")
    @ApiImplicitParam(name = "id", value = "角色ID", required = true, dataType = "String", paramType = "path")
    public Role role(@PathVariable String id) {
        try {
            return roleService.get(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new Role();
    }

    /**
     * 角色分页查询
     *
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @param sort     sort
     * @param order    order
     * @param role     role
     * @return PageInfo
     */
    @GetMapping("roleList")
    @ApiOperation(value = "获取角色列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "role", value = "角色信息", dataType = "RoleVo")
    })
    public PageInfo<Role> roleList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                   @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                   @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                   @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                   Role role) {
        return roleService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), role);
    }

    /**
     * 查询所有角色
     *
     * @param role role
     * @return ResponseBean
     */
    @GetMapping("allRoles")
    @ApiOperation(value = "获取全部角色列表")
    @ApiImplicitParam(name = "role", value = "角色信息", dataType = "RoleVo")
    public ResponseBean<List<Role>> allRoles(Role role) {
        role.setApplicationCode(SysUtil.getSysCode());
        return new ResponseBean<>(roleService.findAllList(role));
    }

    /**
     * 修改角色菜单信息
     *
     * @param role role
     * @return ResponseBean
     */
    @PutMapping
    @PreAuthorize("hasAuthority('sys:role:edit') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "更新角色菜单信息", notes = "根据角色id更新角色菜单信息")
    @ApiImplicitParam(name = "role", value = "角色实体role", required = true, dataType = "RoleVo")
    @Log(value = "修改角色菜单", type = 0)
    public ResponseBean<Boolean> updateRole(@RequestBody @Valid Role role) {
        String msg;
        Role rl = roleService.findByRoleCode(role);
        if (rl != null && !rl.getId().equals(role.getId())){
            msg = role.getRoleCode() + ",角色权限标识已存在";
            return new ResponseBean<>(-1 > 0, msg);
        }
        role.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
        boolean success = roleService.update(role) > 0;
        if (success)
            return new ResponseBean<>(roleMenuService.saveRoleMenus(role.getId(), role.getMenuIds()) > 0);
        return new ResponseBean<>(-1 > 0);
    }

    /**
     * 创建角色
     *
     * @param role role
     * @return ResponseBean
     */
    @PostMapping
    @PreAuthorize("hasAuthority('sys:role:add') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "创建角色", notes = "创建角色")
    @ApiImplicitParam(name = "role", value = "角色实体role", required = true, dataType = "Role")
    @Log(value = "新增角色", type = 0)
    public ResponseBean<Boolean> role(@RequestBody @Valid Role role) {
        String msg;
        Role rl = roleService.findByRoleCode(role);
        if (rl != null && !rl.getId().equals(role.getId())){
            msg = role.getRoleCode() + ",角色权限字符已存在";
            return new ResponseBean<>(-1 > 0, msg);
        }
        role.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
        boolean success = roleService.insert(role) > 0;
        if (success)
            return new ResponseBean<>(roleMenuService.saveRoleMenus(role.getId(), role.getMenuIds()) > 0);
        return new ResponseBean<>(-1 > 0);
    }

    /**
     * 根据id删除角色
     *
     * @param id id
     * @return RoleVo
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:role:del') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "删除角色", notes = "根据ID删除角色")
    @ApiImplicitParam(name = "id", value = "角色ID", required = true, paramType = "path")
    @Log(value = "删除角色", type = 0)
    public ResponseBean<Boolean> deleteRole(@PathVariable String id) {
        Role role = new Role();
        role.setId(id);
        role.setNewRecord(false);
        role.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
        return new ResponseBean<>(roleService.delete(role) > 0);
    }

    /**
     * 批量删除
     *
     * @param role role
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @PreAuthorize("hasAuthority('sys:role:del') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "批量删除角色", notes = "根据角色id批量删除角色")
    @ApiImplicitParam(name = "role", value = "角色信息", dataType = "RoleVo")
    @Log(value = "批量删除角色", type = 0)
    public ResponseBean<Boolean> deleteAllRoles(@RequestBody Role role) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(role.getIdString()))
                success = roleService.deleteAll(role.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除角色失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
