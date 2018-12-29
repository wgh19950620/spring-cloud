package com.wgh.springcloud.commons.util;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.github.pagehelper.util.StringUtil;

/**
 * SHA 加密工具类
 *
 * @author wangguanghui
 */
public final class ShaUtil {
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    private static final String MD5_ALGORITHM = "MD5";

    private static final String ENCODING = "UTF-8";

    private ShaUtil() {
        super();
    }

    /**
     * MD5
     *
     * @param input 数据
     * @return 加密结果
     */
    public static String md5(String input) {
        if (null == input) {
            return null;
        }

        try {
            return getMd5Token(MD5_ALGORITHM, input);
        } catch (Exception ex) {
            return null;
        }
    }

    private static String getMd5Token(String type, String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(type);
        md.update(data.getBytes());
        byte[] digest = md.digest();
        return new String(Base64.encodeBase64(digest));
    }

    /**
     * HMAC SHA1
     *
     * @param secret 密钥
     * @param input  数据
     * @return 加密结果
     */
    public static String hmacSha1(String secret, String input) {

        if (StringUtil.isEmpty(input)) {
            return null;
        }

        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(ENCODING), HMAC_SHA1_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(secretKeySpec);
            byte[] rawHmac = mac.doFinal(input.getBytes());

            return new String(Base64.encodeBase64(rawHmac));
        } catch (Exception ex) {
            return null;
        }
    }

}
