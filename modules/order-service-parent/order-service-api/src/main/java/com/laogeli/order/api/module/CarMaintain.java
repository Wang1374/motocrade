package com.laogeli.order.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

/**
 * 保养维修记录
 *
 * @author BeiFang
 * @Date 2020-10-19
 **/
@Data
@ToString(callSuper = true)
public class CarMaintain extends BaseEntity<CarMaintain> {
    /**
     * 保养记录id
     */
    private String id;

    /**
     * 所属公司id
     */
    private String belongCompaniesId;

    /**
     * 车牌号
     */
    private String vehicle;

    /**
     * 保养日期
     */
    private Date mainDate;

    /**
     * 公里数
     */
    private String kilometre;

    /**
     * 保养项目
     */
    private String mainProject;

    /**
     * 保养金额
     */
    private BigDecimal mainPrice;

    /**
     * 发票号
     */
    private String invoiceNum;

    /**
     * 备注
     */
    private String remark;

    /**
     * 总金额
     */

    private BigDecimal mainPriceSum;


    /**
     * 付款状态，true 付款，false 未付
     */
    private Boolean payStatus;

    /**
     * 付款说明
     */
    private String payStatement;

    /**
     * 附件url
     */
    private String ticketUrl;

    /**
     * 附件名字
     */
    private String ticketName;


}
