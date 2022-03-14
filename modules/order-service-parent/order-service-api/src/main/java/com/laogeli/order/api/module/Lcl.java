package com.laogeli.order.api.module;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

/**
 * 门点 件毛体 信息
 *
 * @author wang
 * @date 2020-07-13
 **/
@Data
public class Lcl {

    /**
     * id
     */
    private String id;

    /**
     * 做箱id
     */
    private String mcId;

    /**
     * 门点简称
     */
    private String door;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系电话
     */
    private String contactNumber;

    /**
     * 地址
     */
    private String address;

    /**
     * 备注
     */
    private String remark;

    /**
     * 件毛体
     */
    private List<HairBody> hairBodyList;

    /**
     * 件毛体
     */
    @JsonIgnore  //返回时排除掉这个字段
    private String hairBody;
}
