package com.wgh.springcloud.commons.util;

/**
 * judge excel version
 *
 * @author wangguanghui
 */
public class IsExcelVersion {

    private final static String EXCEL_2003_REGEX = "^.+\\.(?i)(xls)$";
    private final static String EXCEL_2007_REGEX = "^.+\\.(?i)(xlsx)$";

    public static boolean isExcel2003(String filePath) {
        return filePath.matches(EXCEL_2003_REGEX);
    }

    public static boolean isExcel2007(String filePath) {
        return filePath.matches(EXCEL_2007_REGEX);
    }

}
