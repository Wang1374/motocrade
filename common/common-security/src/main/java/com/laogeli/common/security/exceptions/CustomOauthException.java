package com.laogeli.common.security.exceptions;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.laogeli.common.security.serializer.CustomOauthExceptionSerializer;
import lombok.Data;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * 定制OAuth2Exception
 *
 * @author yangyu
 * @date 2019-12-31
 */
@Data
@JsonSerialize(using = CustomOauthExceptionSerializer.class)
public class CustomOauthException extends OAuth2Exception {

    /**
     * 错误码
     */
    private int code;

    public CustomOauthException(String msg, int code) {
        super(msg);
        this.code = code;
    }
}
