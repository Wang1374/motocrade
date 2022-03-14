package com.laogeli.order.api.module;

import lombok.Data;

import java.io.Serializable;

/**
 * 表头信息
 *
 * @author wang
 * @date 2020-08-31
 */
@Data
public class TableHeader implements Serializable {

    /**
     * 表头数据id
     */
    private Integer id;

    /**
     * 归属表id
     */
    private String tableId;

    /**
     * 表头字段
     */
    private String rowField;

    /**
     * 表头名字
     */
    private String rowName;

    /**
     * 表头排序
     */
    private String width;

    /**
     * 表头数据是否展示
     */
    private Boolean show1;

    /**
     * 用户id
     */
    private String userId;
}
