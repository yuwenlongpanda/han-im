package com.ywl.im.result;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author zhou miao
 * @date 2022/06/02
 */
public enum ImApiResultEnum {
    //
    API_CURL_SUCCESS("SUCCESS", "api调用成功"),
    API_CURL_FAIL("API_CURL_FAIL", "api调用失败"),
    API_CURL_PARAM_ERROR("HEADER_PARAM_ERROR", "api请求头参数错误"),
    API_CURL_SYSTEM_CHECK_ERROR("SYSTEM_CHECK_ERROR", "系统校验错误"),
    API_CURL_SYSTEM_SYSTEM_NOT_EXIST("SYSTEM_NOT_EXIST", "系统不存在"),
    API_CURL_TICKET_INVALID("TICKET_INVALID", "ticket无效"),
    ;

    private String code;
    private String message;

    ImApiResultEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static void check(String code) {
        if (!Objects.equals(API_CURL_SUCCESS.getCode(), code)) {
            throw new RuntimeException(getByCode(code).getMessage());
        }
    }

    private static ImApiResultEnum getByCode(String code) {
        return Arrays.stream(ImApiResultEnum.values())
                .filter(re -> Objects.equals(code, re.getCode()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("code不存在：" + code));
    }

}
