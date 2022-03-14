package com.laogeli.user.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.laogeli.common.core.persistence.TreeEntity;
import com.laogeli.user.api.module.Menu;
import com.laogeli.user.api.unit.TreeSelect;
import lombok.Data;

import java.util.List;

/**
 * 菜单dto
 *
 * @author wang
 * @date 2019-12-31
 */
@Data
public class MenuDto {

    /**
     * 选中菜单ID
     */
    private List<String> checkedKeys;

    /**
     * 菜单列表
     */
    protected List<TreeSelect> menus;
}
