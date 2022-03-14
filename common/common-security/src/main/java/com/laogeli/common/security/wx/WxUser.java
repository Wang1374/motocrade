package com.laogeli.common.security.wx;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wang
 * @Date 2021-08-25 9:36
 **/
@Data
public class WxUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;


    private String memberId;

    private String appId;

    private String openId;

    private String unionId;

    private String nickName;

    private String avatarUrl;

    private String gender;

    private String country;

    private String province;

    private String iv;
    private String encryptedData;

    private String city;

    private Date createTime;

    private Date updateTime;

    private Integer version;

    private Integer isDelete;

    private Integer sex;

    private String userDesc;

    private String language;

}
