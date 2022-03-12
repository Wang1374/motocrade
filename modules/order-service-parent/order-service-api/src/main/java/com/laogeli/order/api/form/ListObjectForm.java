package com.laogeli.order.api.form;

import com.laogeli.order.api.module.InsuranceDetail;
import com.laogeli.order.api.module.InsuranceRecord;
import lombok.Data;

import java.util.List;

/**
 * 接收前台传递的对象(数组)
 *
 * @author BeiFang
 * @Date 2020-07-21
 **/
@Data
public class ListObjectForm {

    /**
     * 保险记录数组
     */
    private List<InsuranceRecord> insuranceRecordList;

    /**
     * 保险详细信息数组
     */
    private List<InsuranceDetail> insuranceDetails;

}
