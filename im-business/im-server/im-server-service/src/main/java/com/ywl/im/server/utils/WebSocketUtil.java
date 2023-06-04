package com.ywl.im.server.utils;

import com.ywl.im.server.enums.ConnectTypeEnum;
import com.ywl.im.server.models.HandshakeParam;
import io.netty.handler.codec.http.FullHttpRequest;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WebSocketUtil {

    //    private static final String NICKNAME_KEY = "nickname";
//    private static final String AVATAR_KEY = "avatar";
    private static final String TICKET_KEY = "ticket";
    private static final String CLIENT_KEY = "client";
    private static final String USER_KEY = "user";
    private static final String CONNECT_KEY = "connect";

    public static boolean isCommonConnect(Map<String, String> handshakeParamMap) {
        return containsAllKeys(handshakeParamMap, TICKET_KEY, CLIENT_KEY, USER_KEY)
                && Objects.equals(handshakeParamMap.get(CONNECT_KEY), ConnectTypeEnum.COMMON.getCode());
    }

    public static boolean isInternalConnect(Map<String, String> handshakeParamMap) {
        return Objects.equals(handshakeParamMap.get(CONNECT_KEY), ConnectTypeEnum.INTERNAL.getCode());
    }

    public static HandshakeParam getHandshakeParam(Map<String, String> handshakeParamMap) {
        HandshakeParam handshakeParam = new HandshakeParam();
        handshakeParamMap.forEach((k, v) -> {
            try {
                Field declaredField = HandshakeParam.class.getDeclaredField(k);
                declaredField.setAccessible(true);
                declaredField.set(handshakeParam, v);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // Handle exception
            }
        });
        return handshakeParam;
    }

    public static Map<String, String> parseHandshakeParams(FullHttpRequest fullHttpRequest) {
        String handshakeParams = fullHttpRequest.uri();
        try {
            String decodedParams = URLDecoder.decode(handshakeParams, "UTF-8");
            String[] uriArray = decodedParams.split("\\?");
            if (uriArray.length < 2) {
                return Collections.emptyMap();
            }
            String[] params = uriArray[1].split("&");
            Map<String, String> handshakeParamMap = new HashMap<>();
            for (String param : params) {
                int indexOf = param.indexOf("=");
                if (indexOf > 0) {
                    String key = param.substring(0, indexOf);
                    String value = param.substring(indexOf + 1);
                    handshakeParamMap.put(key, value);
                }
            }
            return handshakeParamMap;
        } catch (UnsupportedEncodingException e) {
            return Collections.emptyMap();
        }
    }

    private static boolean containsAllKeys(Map<String, String> map, String... keys) {
        for (String key : keys) {
            if (!map.containsKey(key)) {
                return false;
            }
        }
        return true;
    }

}
