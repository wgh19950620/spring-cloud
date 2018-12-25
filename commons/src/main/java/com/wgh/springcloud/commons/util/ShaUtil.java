package com.wgh.springcloud.commons.util;

import org.apache.tomcat.util.codec.binary.Base64;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;

import com.github.pagehelper.util.StringUtil;

/**
 * SHA 加密工具类
 *
 * @author wangguanghui
 */
public final class ShaUtil {
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    private static final String MD5_ALGORITHM = "MD5";

    private static final String AES_ALGORITHM = "AES";

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

    /**
     * AES 加密
     *
     * @param encodeRules 加密规则
     * @param content     需加密字符串
     * @return 加密后的值
     */
    public static String aesEncode(String encodeRules, String content) {
        if(StringUtil.isEmpty(content)) {
            return null;
        }

        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
            // 生成一个128位随机源
            keyGenerator.init(128, new SecureRandom(encodeRules.getBytes()));
            SecretKey secretKey = keyGenerator.generateKey();
            new SecretKeySpec(secretKey.getEncoded(), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] byteAES = cipher.doFinal(content.getBytes(ENCODING));
            return new BASE64Encoder().encode(byteAES);
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static String aesDecode(String encodeRules, String content) {
        if(StringUtil.isEmpty(content)) {
            return null;
        }

        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
            // 生成一个128位随机源
            keyGenerator.init(128, new SecureRandom(encodeRules.getBytes()));
            SecretKey secretKey = keyGenerator.generateKey();
            SecretKey key = new SecretKeySpec(secretKey.getEncoded(), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] byteContent = new BASE64Decoder().decodeBuffer(content);
            return new String(cipher.doFinal(byteContent), ENCODING);
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        /*
         * 加密
         */
        System.out.println("使用AES对称加密，请输入加密的规则");
        String encodeRules = scanner.next();
        System.out.println("请输入要加密的内容:");
        String content = scanner.next();
        System.out.println("根据输入的规则" + encodeRules + "加密后的密文是:" + aesEncode(encodeRules, content));

        /*
         * 解密
         */
        System.out.println("使用AES对称解密，请输入加密的规则：(须与加密相同)");
        encodeRules = scanner.next();
        System.out.println("请输入要解密的内容（密文）:");
        content = scanner.next();
        System.out.println("根据输入的规则" + encodeRules + "解密后的明文是:" + aesDecode(encodeRules, content));
    }
}
