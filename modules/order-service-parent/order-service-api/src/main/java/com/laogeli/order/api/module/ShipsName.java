package com.laogeli.order.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wang
 * @Date 2021-02-10 10:59
 **/
@Data
public class ShipsName implements Serializable {

    /**
     * id
     */
    private int id;

    /**
     * 船名
     */
    private String shipsName;
}
