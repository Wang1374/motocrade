package com.laogeli.order.api.vo;

import lombok.Data;

import java.util.List;

/**
 * 客户返回对象
 *
 * @author yangyu
 * @date 2020-07-01
 */
@Data
public class ClienteleVo {

    /**
     * 所属公司标识
     */
    private String belongCompaniesId;

    /**
     * key
     */
    private String id;

    /**
     * 公司抬头value
     */
    private String value;

    /**
     * 公司抬头label
     */
    private String label;

    /**
     * 业务人员
     */
    private String salesman;

    /**
     * 合作伙伴： 1 固定  2 非固定
     */
    private int partner;

    /**
     * 付款人
     */
    private String payer;

    /**
     * 性质 ： 1 客户， 2 供应商, 3 往来单位
     */
    private String type;

    /**
     * 联系方式
     */
    private List<ContactWayVo> contactWayVoList;

    /**
     * 门点
     */
    private List<DoorsVo> doorsVoList;

    /**
     *
     */
    private List<SendReceipVo> ReceipVoList;
}
