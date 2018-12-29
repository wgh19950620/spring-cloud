package com.wgh.springcloud.commons.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author wangguanghui
 */
public final class DateUtil {

    /**
     * 根据格式获取当前格式化时间
     *
     * @param format 格式化方式，基础格式为yyyy-MM-dd HH:mm:ss
     * @return 当前时间
     */
    private static String getCurrentTimeByFormat(String format, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * 格式化时间
     *
     * @param format 格式化格式，基础格式为yyyy-MM-dd HH:mm:ss
     * @param time   1527320060036L
     * @return
     */
    private static String formatTime(String format, long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(new Date(time));
    }

    /**
     * String 类型转化为 Date 类型
     * @param strTime   String 类型时间
     * @return          Date 类型时间，转化后保存时分秒
     * @throws Exception
     */
    private static Date stringToDate(String strTime) throws Exception{
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }


    public static void main(String[] args) {
        try {

            Date nowTime = new Date(System.currentTimeMillis());
            SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String retStrFormatNowDate = sdFormatter.format(nowTime);

            Date date = stringToDate("2018-12-24 10:40:34");
            System.out.println(getCurrentTimeByFormat("yyyy-MM-dd HH:mm:ss", date));
            System.out.println(DateUtil.formatTime("yyyy-MM-dd HH:mm:ss", 1545617914738L));
            Calendar calendar = Calendar.getInstance();
            long now = calendar.getTimeInMillis();
            long time = 1545617914738L;

            System.out.println((now - time) <= 10 * 60 * 1000);
            System.out.println(DateUtil.formatTime("yyyy-MM-dd HH:mm:ss", 1545617914738L));

            System.out.println(retStrFormatNowDate);
            System.out.println(System.currentTimeMillis());
            System.out.println(now);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
