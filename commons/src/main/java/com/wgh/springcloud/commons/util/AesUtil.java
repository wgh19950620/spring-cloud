package com.wgh.springcloud.commons.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.security.SecureRandom;
import java.util.Scanner;

import com.github.pagehelper.util.StringUtil;

/**
 * aes encode_decode methods
 *
 * @author wangguanghui
 */
public class AesUtil {

    private static final String AES_ALGORITHM = "AES";

    private static final String ENCODING = "UTF-8";

    /**
     * AES 加密
     *
     * @param encodeRules 加密规则
     * @param content     需加密字符串
     * @return 加密后的值
     */
    private static String aesEncode(String encodeRules, String content) {
        if (StringUtil.isEmpty(content)) {
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

    /**
     * AES 解密
     *
     * @param encodeRules 加密规则
     * @param content     需加密字符串
     * @return 解密后的值
     */
    private static String aesDecode(String encodeRules, String content) {
        if (StringUtil.isEmpty(content)) {
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
