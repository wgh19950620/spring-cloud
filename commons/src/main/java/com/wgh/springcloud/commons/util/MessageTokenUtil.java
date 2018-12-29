package com.wgh.springcloud.commons.util;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import com.google.gson.Gson;
import com.wgh.springcloud.commons.domain.MessageMetaInfo;

/**
 * encode util
 *
 * @author wangguanghui
 * @version 1.0
 * @rule token=appId:hmac-sha1(appSecret,urlsafe-base64(meta)):urlsafe-base64(meta)
 */
public final class MessageTokenUtil {

    private static       Logger logger   = LoggerFactory.getLogger(MessageTokenUtil.class);
    private static final String MAC_NAME = "HmacSHA1";
    private static final String ENCODING = "UTF-8";

    /**
     * 密钥
     */
    private String appSecret;

    /**
     * 为调⽤⽅提供的元数据，格式为json字符串
     */
    private String meta;

    public MessageTokenUtil(String privateKey, MessageMetaInfo messageMetaInfo) {
        super();
        this.appSecret = privateKey;
        messageMetaInfo.setTime(System.currentTimeMillis() / 1000 + 3600);
        Gson gson = new Gson();
        this.meta = ObjectUtils.isEmpty(messageMetaInfo) ? null : gson.toJson(messageMetaInfo);
    }

    /**
     * 使用 HMAC-SHA1 签名方法对对encryptText进行签名
     *
     * @param encryptText 被签名的字符串
     * @param encryptKey  密钥
     * @return 返回被加密后的字符串
     * @throws Exception
     */
    public static String HmacSHA1Encrypt(String encryptKey, String encryptText) throws Exception {
        byte[] data = encryptKey.getBytes(ENCODING);
        // 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
        // 生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance(MAC_NAME);
        // 用给定密钥初始化 Mac 对象
        mac.init(secretKey);
        byte[] text = encryptText.getBytes(ENCODING);
        // 完成 Mac 操作
        byte[] digest = mac.doFinal(text);
        StringBuilder sBuilder = bytesToHexString(digest);
        return sBuilder.toString();
    }

    /**
     * 转换成Hex
     *
     * @param bytesArray
     */
    public static StringBuilder bytesToHexString(byte[] bytesArray) {
        if (bytesArray == null) {
            return null;
        }
        StringBuilder sBuilder = new StringBuilder();
        for (byte b : bytesArray) {
            String hv = String.format("%02x", b);
            sBuilder.append(hv);
        }
        return sBuilder;
    }

    /**
     * safeUrlBase64Encode加密
     *
     * @param data data
     */
    public static String safeUrlBase64Encode(String data) {
        return safeUrlBase64Encode(data.getBytes());
    }

    public static String safeUrlBase64Encode(byte[] data) {
        String encodeBase64 = new BASE64Encoder().encode(data);
        String safeBase64Str = encodeBase64.replace('+', '-');
        safeBase64Str = safeBase64Str.replace('/', '_');
        safeBase64Str = safeBase64Str.replaceAll("=", "");
        return safeBase64Str;
    }

    /**
     * safeUrlBase64Encode解密
     *
     * @param safeBase64Str safeBase64Str
     */
    public static byte[] safeUrlBase64Decode(final String safeBase64Str) throws IOException {
        String base64Str = safeBase64Str.replace('-', '+');
        base64Str = base64Str.replace('_', '/');
        int mod4 = base64Str.length() % 4;
        if (mod4 > 0) {
            base64Str = base64Str + "====".substring(mod4);
        }
        return new BASE64Decoder().decodeBuffer(base64Str);
    }

    /**
     * urlsafe_base64 加密
     *
     * @param data data
     */
    public static String encoded(String data) throws UnsupportedEncodingException {
        byte[] b = Base64.encodeBase64URLSafe(data.getBytes(ENCODING));
        return new String(b, ENCODING);
    }

    /**
     * urlsafe_base64 解密
     *
     * @param data data
     */
    public static String decode(String data) throws UnsupportedEncodingException {
        byte[] b = Base64.decodeBase64(data.getBytes(ENCODING));
        return new String(b, ENCODING);
    }

    /**
     * 获取加密token
     *
     * @return
     */
    public String getToken() {
        try {
            logger.info(String.format("getToken request param:%s", meta));
            String urlSafeBase64Str = safeUrlBase64Encode(meta);
            String hmacSha1Str = HmacSHA1Encrypt(appSecret, urlSafeBase64Str);
            String token = hmacSha1Str + ":" + urlSafeBase64Str;
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取本机ip地址，并自动区分Windows还是linux操作系统
     *
     * @return String
     */
    public static String getLocalIP() {
        String sIP = "";
        InetAddress ip = null;
        try {
            //如果是Windows操作系统
            if (isWindowsOS()) {
                ip = InetAddress.getLocalHost();
            } else {
                //如果是Linux操作系统
                boolean bFindIP = false;
                Enumeration<NetworkInterface> netInterfaces = NetworkInterface
                        .getNetworkInterfaces();
                while (netInterfaces.hasMoreElements()) {
                    if (bFindIP) {
                        break;
                    }
                    NetworkInterface ni = netInterfaces.nextElement();
                    //----------特定情况，可以考虑用ni.getName判断
                    //遍历所有ip
                    Enumeration<InetAddress> ips = ni.getInetAddresses();
                    while (ips.hasMoreElements()) {
                        ip = ips.nextElement();
                        if (ip.isSiteLocalAddress()
                                && !ip.isLoopbackAddress()   //127.开头的都是lookback地址
                                && !ip.getHostAddress().contains(":")) {
                            bFindIP = true;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("get ip error.", e);
        }

        if (null != ip) {
            sIP = ip.getHostAddress();
        }
        logger.debug("==========ip:" + sIP);

        return sIP;
    }

    /**
     * 判断系统
     *
     * @return String
     */
    private static boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        String windows = "windows";

        if (osName.toLowerCase().contains(windows)) {

            isWindowsOS = true;
        }
        return isWindowsOS;
    }

    public static void main(String[] args) {
        try {

            MessageMetaInfo metaInfo = new MessageMetaInfo();
            metaInfo.setAccount("478269652");
            metaInfo.setName("jock");
            metaInfo.setTime(System.currentTimeMillis() / 1000 + 3600);
            Gson gson = new Gson();
            String string = gson.toJsonTree(metaInfo).toString();
            System.out.println("json string: " + string);
            System.out.println("metaInfo.toString()" + metaInfo.toString());
            String str = ("478269652\njock\n" + System.currentTimeMillis() / 1000 + 3600);
            String privateKey = "a1d20b8aef4f400987e5a1ef20941b05";
            String hmacSha1 = ShaUtil.hmacSha1(privateKey, str);
            metaInfo.setHash(hmacSha1);
            MessageTokenUtil mtu = new MessageTokenUtil(privateKey, metaInfo);
            System.out.println(mtu.getToken() + "**" + (System.currentTimeMillis() / 1000 + 3600));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
