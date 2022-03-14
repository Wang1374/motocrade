package com.laogeli.user.api.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.laogeli.common.security.wx.WxUser;
import com.laogeli.user.api.module.Role;
import com.laogeli.user.api.module.StaffRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * userDto
 *
 * @author wang
 * @date 2019-12-31
 */
@Data
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel(value = "UserDto", description = "用户返回结果")
public class UserDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String id;

    /**
     * 授权类型，1：用户名密码，2：手机号
     */
    @ApiModelProperty(value = "授权类型，1：用户名密码，2：手机号", dataType = "Integer", example = "1")
    private Integer identityType;

    /**
     * 唯一标识，如用户名、手机号
     */
    @ApiModelProperty(value = "账号唯一标识，如用户名、手机号")
    private String identifier;

    /**
     * 密码凭证，跟授权类型有关，如密码、第三方系统的token等
     */
    @ApiModelProperty(value = "密码，需要使用AES加密", example = "lBTqrKS0kZixOFXeZ0HRng==")
    private String credential;

    /**
     * 旧密码
     */
    @ApiModelProperty(value = "旧密码", hidden = true)
    private String oldPassword;

    /**
     * 新密码
     */
    @ApiModelProperty(value = "新密码", hidden = true)
    private String newPassword;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名", example = "git")
    private String name;

    /**
     * 电话
     */
    @ApiModelProperty(value = "电话", example = "15521089184")
    @Pattern(regexp = "^\\d{11}$", message = "请输入11位手机号")
    private String phone;

    /**
     * 头像id
     */
    @ApiModelProperty(value = "头像id", hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String avatarId;

    /**
     * 头像URL
     */
    @ApiModelProperty(value = "头像URL", hidden = true)
    private String avatarUrl;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱", example = "1633736729@qq.com")
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * (邮箱以及手机)验证码类型
     */
    @ApiModelProperty(value = "验证码类型")
    private String codeType;

    /**
     * (邮箱以及手机)验证码
     */
    @ApiModelProperty(value = "验证码")
    private String securityCode;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别，0：男，1：女", dataType = "Integer", example = "0")
    private Integer sex;

    /**
     * 出生日期
     */
    @ApiModelProperty(value = "出生日期", dataType = "Date")
    private Date born;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述", example = "git")
    private String userDesc;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态，0：启用，1：禁用", example = "0")
    private Integer status;

    /**
     * 最近登录时间
     */
    @ApiModelProperty(value = "最近登录时间", hidden = true)
    private Date loginTime;

    /**
     * 公司类型 0：平台 1：车队 2：货代/工厂
     */
    @ApiModelProperty(value = "公司类型")
    private Integer accountType;

    /**
     * 企业名
     */
    @ApiModelProperty(value = "企业名")
    private String CompanyName;

    /**
     * 企业标识ID （0：系统内部账户 null: 员工账户 其他：企业账户）
     */
    @ApiModelProperty(value = "企业标识")
    private String corporateIdentify;

    /**
     * 员工标识 （-1：企业账户 其他：员工账户)
     */
    @ApiModelProperty(value = "员工标识")
    private String employeeId;

    /**
     * 创建日期
     */
    protected Date createDate;

    /**
     * 绑定车牌
     */
    protected String vehicles;

    /**
     * 系统编号
     */
    @ApiModelProperty(value = "系统编号", example = "EXAM")
    private String applicationCode;

    /**
     * 角色
     */
    @ApiModelProperty(value = "角色", hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<String> roles = new ArrayList<>();

    /**
     * 角色列表
     */
    @ApiModelProperty(value = "角色列表", hidden = true)
    private List<Role> roleList = new ArrayList<>();

    /**
     * 员工角色
     */
    @ApiModelProperty(value = "员工角色", hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<String>  staffRole = new ArrayList<>();

    /**
     * 员工角色列表
     */
    @ApiModelProperty(value = "员工角色列表", hidden = true)
    private List<StaffRole> staffRoleList = new ArrayList<>();

    /**
     * 微信用户信息
     */
    private WxUser wxUser;

    /**
     * 公司id
     */
    private String companyId;

}
