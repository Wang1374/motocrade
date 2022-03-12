package com.laogeli.user.api.module;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单
 *
 * @author yangyu
 * @date 2019-12-31
 */
@Data
public class Menu extends BaseEntity<Menu> {

    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空")
    private String name;

    /**
     * 父菜单ID
     */
    private String parentId;

    /**
     * 排序
     */
    protected Integer sort;

    /**
     * 后台url
     */
    private String url;

    /**
     * 路径
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 是否为外链（0是 1否）
     */
    private String isFrame;

    /**
     * 类型（M目录 C菜单 F按钮）
     */
    private String menuType;

    /**
     * 显示状态（0显示 1隐藏）
     */
    private String visible;

    /**
     * 菜单状态（0显示 1隐藏）
     */
    private String status;

    /**
     * 菜单权限标识
     */
    private String permission;

    /**
     * 图标
     */
    private String icon;

    /**
     * 类型
     */
    // private Integer type;

    /**
     * 备注
     */
    private String remark;

    /** 子菜单 */
    private List<Menu> children = new ArrayList<Menu>();

    public Menu() {}

    public Menu(Menu menu) {
        this.id = menu.getId();
        this.parentId = menu.getParentId();
        this.icon = menu.getIcon();
        this.name = menu.getName();
        this.url = menu.getUrl();
        // this.type = menu.getType();
        this.sort = menu.getSort();
        this.component = menu.getComponent();
        this.path = menu.getPath();
        this.remark = menu.getRemark();
    }
}
