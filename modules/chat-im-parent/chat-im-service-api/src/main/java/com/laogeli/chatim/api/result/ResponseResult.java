package com.laogeli.chatim.api.result;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

/**
 * description: 统一定义返回值对象
 *
 * @author HongPei
 * @date 2021/5/29 15:46
 */
@Builder
public class ResponseResult extends HashMap<String, Object> {

    private static final Integer ERROR_STATUS = -1;

    private static final Integer SUCCESS_STATUS = 200;

    private static final String SUCCESS_MSG = "ok";

    public ResponseResult() {
        super();
    }

    public ResponseResult(int code) {
        super();
        setStatus(code);
    }

    public ResponseResult(HttpStatus status) {
        super();
        setStatus(status.value());
        setMsg(status.getReasonPhrase());
    }

    public ResponseResult success() {
        put("msg", SUCCESS_MSG);
        put("status", SUCCESS_STATUS);
        return this;
    }

    public ResponseResult success(Integer chatType) {
        put("msg", SUCCESS_MSG);
        put("status", SUCCESS_STATUS);
        put("chatType", chatType);
        return this;
    }

    public ResponseResult error(String msg) {
        put("msg", msg);
        put("status", ERROR_STATUS);
        return this;
    }

    public ResponseResult error(String msg, Integer chatType) {
        put("msg", msg);
        put("status", ERROR_STATUS);
        put("chatType", chatType);
        return this;
    }

    public ResponseResult setData(String key, Object obj) {
        @SuppressWarnings("unchecked")
        HashMap<String, Object> data = (HashMap<String, Object>) get("data");
        if (data == null) {
            data = new HashMap<>();
            put("data", data);
        }
        data.put(key, obj);
        return this;
    }

    public ResponseResult setStatus(int status) {
        put("status", status);
        return this;
    }

    public ResponseResult setMsg(String msg) {
        put("msg", msg);
        return this;
    }

    public ResponseResult setValue(String key, Object val) {
        put(key, val);
        return this;
    }

}
