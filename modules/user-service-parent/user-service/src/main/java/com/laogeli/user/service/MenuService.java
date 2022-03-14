package com.laogeli.user.service;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.properties.SysProperties;
import com.laogeli.common.core.service.CrudService;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.common.security.core.GrantedAuthorityImpl;
import com.laogeli.common.security.utils.SecurityUtil;
import com.laogeli.user.api.module.Menu;
import com.laogeli.user.api.module.Role;
import com.laogeli.user.api.vo.MetaVo;
import com.laogeli.user.api.vo.RouterVo;
import com.laogeli.user.mapper.MenuMapper;
import com.laogeli.user.api.unit.TreeSelect;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单service
 *
 * @author wang
 * @date 2019-12-31
 */
@AllArgsConstructor
@Service
public class MenuService extends CrudService<MenuMapper, Menu> {

    private final MenuMapper menuMapper;

    private final SysProperties sysProperties;

    /**
     * 根据角色查找菜单
     *
     * @param role 角色标识
     * @return List
     */
    @Cacheable(value = "menu#" + CommonConstant.CACHE_EXPIRE, key = "#role")
    public List<Menu> findMenuByRole(String role) {
        return menuMapper.findByRole(role);
    }

    /**
     * 根据角色查找菜单
     *
     * @param role 角色标识
     * @return List
     */
    @Cacheable(value = "menu#" + CommonConstant.CACHE_EXPIRE, key = "#role")
    public List<Menu> findMenuByStaffRole(String role) {
        return menuMapper.findByStaffRole(role);
    }

    /**
     * 批量查询菜单
     *
     * @return List
     */
    public List<Menu> finMenuByRoleList(String employeeId) {
        List<Menu> menus = Lists.newArrayList();
        String identifier = SysUtil.getUser();
        // 超级管理员
        if (identifier.equals(sysProperties.getAdminUser())) {
            // 获取角色的菜单
            Menu menu = new Menu();
            menus = this.findAllList(menu);
        } else {
            List<Role> roleList = SecurityUtil.getCurrentAuthentication().getAuthorities().stream()
                    // 按角色过滤
                    .filter(authority -> authority.getAuthority() != null && authority.getAuthority().startsWith("role_"))
                    .map(authority -> {
                        Role role = new Role();
                        role.setRoleCode(authority.getAuthority());
                        return role;
                    }).collect(Collectors.toList());
            // 获取每个角色菜单
            // 判断是否是员工
            if ("-1".equals(employeeId)) {
                for (Role role : roleList) {
                    List<Menu> roleMenus = this.findMenuByRole(role.getRoleCode());
                    if (CollectionUtils.isNotEmpty(roleMenus))
                        menus.addAll(roleMenus);
                }
            } else {
                for (Role role : roleList) {
                    List<Menu> roleMenus = this.findMenuByStaffRole(role.getRoleCode());
                    if (CollectionUtils.isNotEmpty(roleMenus))
                        menus.addAll(roleMenus);
                }
            }
            // 去重
            if (CollectionUtils.isNotEmpty(menus)) {
                menus = menus.stream().distinct().collect(Collectors.toList());
            }
            // 排序
            menus = CollUtil.sort(menus, Comparator.comparingInt(Menu::getSort));
        }
        return getChildPerms(menus, "-1");
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<Menu> getChildPerms(List<Menu> list, String parentId) {
        List<Menu> returnList = new ArrayList<Menu>();
        for (Iterator<Menu> iterator = list.iterator(); iterator.hasNext(); ) {
            Menu t = (Menu) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (parentId.equals(t.getParentId())) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<Menu> list, Menu t) {
        // 得到子节点列表
        List<Menu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (Menu tChild : childList) {
            if (hasChild(list, tChild)) {
                // 判断是否有子节点
                Iterator<Menu> it = childList.iterator();
                while (it.hasNext()) {
                    Menu n = (Menu) it.next();
                    recursionFn(list, n);
                }
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<Menu> getChildList(List<Menu> list, Menu t) {
        List<Menu> tlist = new ArrayList<Menu>();
        Iterator<Menu> it = list.iterator();
        while (it.hasNext()) {
            Menu n = (Menu) it.next();
            if (t.getId().equals(n.getParentId())) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<Menu> list, Menu t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    public List<RouterVo> buildMenus(List<Menu> menus) {
        List<RouterVo> routers = new LinkedList<>();
        for (Menu menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(StringUtils.capitalize(menu.getPath()));
            router.setPath(getRouterPath(menu));
            router.setComponent(StringUtils.isEmpty(menu.getComponent()) ? "Layout" : menu.getComponent());
            router.setMeta(new MetaVo(menu.getName(), menu.getIcon()));
            List<Menu> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && cMenus.size() > 0 && "M".equals(menu.getMenuType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(Menu menu) {
        String routerPath = menu.getPath();
        // 非外链并且是一级目录
        if ("-1".equals(menu.getParentId()) && "1".equals(menu.getIsFrame())) {
            routerPath = "/" + menu.getPath();
        }
        return routerPath;
    }

    /**
     * 查询全部菜单
     *
     * @param menu menu
     * @return List
     */
    @Override
    public List<Menu> findAllList(Menu menu) {
        return menuMapper.findAllList(menu);
    }

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    public List<String> selectMenuListByRoleId(String roleId) {
        return menuMapper.selectMenuListByRoleId(roleId);
    }

    /**
     * 根据员工角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    public List<String> selectMenuListByStaffRoleId(String roleId) {
        return menuMapper.selectMenuListByStaffRoleId(roleId);
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */
    public List<TreeSelect> buildMenuTreeSelect(List<Menu> menus) {
        List<Menu> menuTrees = buildMenuTree(menus);
        return menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    public List<Menu> buildMenuTree(List<Menu> menus) {
        List<Menu> returnList = new ArrayList<Menu>();
        for (Iterator<Menu> iterator = menus.iterator(); iterator.hasNext(); ) {
            Menu t = (Menu) iterator.next();
            // 根据传入的某个父节点ID,遍历该父节点的所有子节点
            if ("-1".equals(t.getParentId())) {
                recursionFn(menus, t);
                returnList.add(t);
            }
        }
        if (returnList.isEmpty()) {
            returnList = menus;
        }
        return returnList;
    }

    /**
     * 新增菜单
     *
     * @param menu menu
     * @return int
     */
    @Transactional
    @Override
    @CacheEvict(value = {"menu", "user"}, allEntries = true)
    public int insert(Menu menu) {
        return super.insert(menu);
    }

    /**
     * 更新菜单
     *
     * @param menu menu
     * @return int
     */
    @Transactional
    @Override
    @CacheEvict(value = {"menu", "user"}, allEntries = true)
    public int update(Menu menu) {
        return super.update(menu);
    }

    /**
     * 删除菜单
     *
     * @param menu menu
     * @return int
     * @author tangyi
     * @date 2018/8/27 16:22
     */
    @Override
    @Transactional
    @CacheEvict(value = {"menu", "user"}, allEntries = true)
    public int delete(Menu menu) {
        // 删除当前菜单
        super.delete(menu);
        // 删除父节点为当前节点的菜单
        Menu parentMenu = new Menu();
        parentMenu.setParentId(menu.getId());
        parentMenu.setNewRecord(false);
        parentMenu.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
        parentMenu.setDelFlag(CommonConstant.DEL_FLAG_DEL);
        return super.update(parentMenu);
    }
}
