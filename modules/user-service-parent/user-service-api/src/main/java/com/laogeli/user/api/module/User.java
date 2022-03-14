package com.laogeli.user.api.module;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.laogeli.common.core.persistence.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

/**
 * 用户基本信息
 *
 * @author wang
 * @date 2019-12-31
 */
@Data
public class User extends BaseEntity<User> {

    /**
     * 姓名
     */
    private String name;

    /**
     * 电话
     */
    @Pattern(regexp = "^\\d{11}$", message = "请输入11位手机号")
    private String phone;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 出生日期
     */
    private Date born;

    /**
     * 描述
     */
    private String userDesc;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 头像id
     */
    private String avatarId;

    /**
     * 微信头像url
     */
    private String avatarWx;

    /**
     * 最近登录时间
     */
    private Date loginTime;

    /**
     * 企业标识ID （-1：系统内部账户 null: 员工账户 其他：企业账户）
     */
    @ApiModelProperty(value = "企业标识")
    private String corporateIdentify;

    /**
     * 员工标识 （-1：企业账户 其他：员工账户)
     */
    @ApiModelProperty(value = "员工标识")
    private String employeeId;

    /**
     * 绑定车牌
     */
    protected String vehicles;

    /**
     * 角色
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<String> role;

    /**
     * 角色列表
     */
    private List<Role> roleList;

    /**
     * 唯一标识，如用户名、手机号
     */
    @ApiModelProperty(value = "账号唯一标识，如用户名、手机号")
    private String identifier;


}
