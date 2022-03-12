package com.laogeli.user.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.laogeli.common.core.vo.CompanyVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 登录信息
 *
 * @author yangyu
 * @date 2019-12-31
 */
@Data
public class UserInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String id;

    /**
     * 授权类型，1：用户名密码，2：手机号，3：邮箱，4：微信，5：QQ
     */
    private Integer identityType;

    /**
     * 唯一标识，如用户名、手机号
     */
    private String identifier;

    /**
     * 密码
     */
    @JsonIgnore
    private String credential;

    /**
     * 姓名
     */
    private String name;


    /**
     * 电话号码
     */
    private String phone;

    /**
     * 头像对应的附件id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String avatarId;

    /**
     * 微信头像url
     */
    private String avatarWx;

    /**
     * 头像地址
     */
    private String avatarUrl;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 生日
     */
    private Date born;

    /**
     * 描述
     */
    private String userDesc;

    /**
     * 状态，0-启用，1-禁用
     */
    private Integer status;

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
     * 最近登parentUserId录时间
     */
    private Date loginTime;

    /**
     * 角色信息
     */
    private String[] roles;

    /**
     * 权限信息
     */
    private String[] permissions;

    /**
     * 系统编号
     */
    private String applicationCode;

    /**
     * 绑定车牌
     */
    protected String vehicles;


    /**
     * 企业信息
     */
    private List<CompanyVo> companys;
}
