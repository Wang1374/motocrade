package com.laogeli.common.core.vo;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 用户Vo
 *
 * @author wang
 * @date 2019-12-31
 */
@Data
public class UserVo extends BaseEntity<UserVo> {

    /**
     * 授权类型，1：用户名密码，2：手机号
     */
    private Integer identityType;

    /**
     * 唯一标识，如用户名、手机号
     */
    private String identifier;

    /**
     * 密码凭证，跟授权类型有关，如密码、第三方系统的token等
     */
    private String credential;

    /**
     * 用户Id
     */
    private String userId;

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
    private String avatarId;

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
     * 详细描述
     */
    private String userDesc;

    /**
     * 状态，0-启用，1-禁用
     */
    private Integer status;

    /**
     * 最近登录时间
     */
    private Date loginTime;

    /**
     * 企业标识ID （-1：系统内部账户 null: 员工账户 其他：企业账户）
     */
    private String corporateIdentify;

    /**
     * 员工标识 （-1：企业账户 其他：员工账户)
     */
    private String employeeId;

    /**
     * 角色列表
     */
    private List<RoleVo> roleList;

    /*----------------------微信相关------------------------*/

    /**
     * 微信appId
     */
    private String appId;

    /**
     * openId
     */
    private String openId;



}

