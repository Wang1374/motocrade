package com.laogeli.order.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 车辆维修费用实体
 *
 * @author wang
 * @date 2020-10-26
 */
@Data
public class CarRepair extends BaseEntity<CarRepair> {
    /**
     * 维修记录id
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
     * 维修日期
     */
    private Date repairDate;

    /**
     * 维修项目
     */
    private String repairProject;

    /**
     * 维修金额
     */
    private BigDecimal repairPrice;

    /**
     * 发票号
     */
    private String invoiceNum;

    /**
     * 备注
     */
    private String remark;

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
     * 附件name
     */
    private String ticketName;
}
