package com.laogeli.common.core.utils;

import com.laogeli.common.core.constant.ApiMsg;
import com.laogeli.common.core.model.ResponseBean;

/**
 * @author wang
 * @date 20201-01-08
 */
public class ResponseUtil {

    private ResponseUtil() {
    }

    /**
     * 是否成功
     *
     * @param responseBean responseBean
     * @return boolean
     */
    public static boolean isSuccess(ResponseBean<?> responseBean) {
        return responseBean != null && responseBean.getCode() == ApiMsg.KEY_SUCCESS;
    }
}

