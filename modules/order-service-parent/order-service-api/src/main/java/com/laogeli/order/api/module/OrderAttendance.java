package com.laogeli.order.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 员工出勤
 *
 * @author beifang
 * @Date 2021-09-26 16:15
 **/
@Data
public class OrderAttendance extends BaseEntity<OrderAttendance> {

  /**
   * 司机id
   */
  private String driverId;

  /**
   * 箱子id
   */
  private String mcId;

  /**
   * 年
   */
  private String years;

  /**
   * 月
   */
  private String months;

  /**
   * 日
   */
  private String days;

  /**
   * 内容
   */
  private String content;
}
