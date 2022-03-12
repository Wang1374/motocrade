package com.laogeli.user.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 公司信息
 *
 * @author yangyu
 * @date 2020-06-08
 */
@Data
public class Company extends BaseEntity<Company> {

    /**
     * 公司名
     */
    private String companyName;

    /**
     * 法人
     */
    private String legalPerson;

    /**
     * 银行卡信息
     */
    private String bankCard;

    /**
     * 统一社会信用代码
     */
    private String uscc;

    /**
     * 成立日期
     */
    private Date registerDate;

    /**
     * 营业期限
     */
    private Date termOfOperation;

    /**
     * 营业执照图片地址
     */
    private String url;

    /**
     * 公司地址
     */
    private String location;

    /**
     * 找油网客户id
     */
    private Integer zyCompanyId;

    /**
     * 公司类型 1：车队 2：货代/工厂
     */
    private Integer accountType;
}
