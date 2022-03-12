package com.laogeli.order.api.module;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 费用设置信息
 *
 * @author yangyu
 * @date 2020-06-16
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CostsSet extends BaseEntity<CostsSet> {

    /**
     * 费用名称
     */
    private String feeName;

    /**
     * 费用代码
     */
    private String feeCode;

    /**
     * 币种
     */
    private String currency;

}
