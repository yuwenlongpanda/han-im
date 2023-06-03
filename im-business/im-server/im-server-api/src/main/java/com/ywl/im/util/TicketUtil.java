package com.ywl.im.util;

import com.ywl.im.enums.TicketTypeEnum;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;

/**
 * @author zhoumiao
 * @date 2022-05-24
 */
public class TicketUtil {
    private static final String SPLIT = ":";

    public static String encrypt(String sSrc, String sKey) {
        if (sKey == null) {
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            return null;
        }
        byte[] raw = sKey.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

        try {
            //"算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes(StandardCharsets.UTF_8));
            //此处使用BASE64做转码功能，同时能起到2次加密的作用。
            return new Base64().encodeToString(encrypted);
        } catch (Exception e) {
            return null;
        }
    }

    public static String decrypt(String sSrc, String sKey) {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                return null;
            }
            byte[] raw = sKey.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            //先用base64解密
            byte[] decode = new Base64().decode(sSrc);
            try {
                byte[] original = cipher.doFinal(decode);
                return new String(original, StandardCharsets.UTF_8);
            } catch (Exception e) {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    public static String generateSrcTicket(String client, String fromId, TicketTypeEnum ticketType, Long lastTimestamp) {
        StringJoiner stringJoiner = new StringJoiner(SPLIT);
        return stringJoiner.add(client).add(fromId).add(ticketType.getCode()).add(lastTimestamp.toString()).toString();
    }

    public static String ticket(String client, String fromId, TicketTypeEnum ticketType, Long lastTimestamp, String secret) {
        StringJoiner stringJoiner = new StringJoiner(SPLIT);
        String srcTicket = stringJoiner.add(client).add(fromId).add(ticketType.getCode()).add(lastTimestamp.toString()).toString();
        return encrypt(srcTicket, secret);
    }

    public static String[] parseSrcTicket(String srcTicket) {
        return srcTicket.split(SPLIT);
    }

    public static String getClient(String srcTicket) {
        return parseSrcTicket(srcTicket)[0];
    }

    public static String getFromId(String srcTicket) {
        return parseSrcTicket(srcTicket)[1];
    }

    public static String getTicketTypeCode(String srcTicket) {
        return parseSrcTicket(srcTicket)[2];
    }

    public static Long getLastTimestamp(String srcTicket) {
        return Long.parseLong(parseSrcTicket(srcTicket)[3]);
    }


}