package com.wgh.springcloud.commons.util;

import java.security.MessageDigest;

import com.github.pagehelper.util.StringUtil;

/**
 * sha1 加密算法
 *
 * @author wangguanghui
 */
public class SHA1 {

    public String getSha1(String str) {
        if (StringUtil.isEmpty(str)) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            char[] buf = new char[md.length * 2];
            int k = 0;
            for (int i = 0; i < md.length; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }
}
