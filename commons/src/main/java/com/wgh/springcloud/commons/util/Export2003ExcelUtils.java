package com.wgh.springcloud.commons.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.IndexedColors;

import javax.servlet.http.HttpServletResponse;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import com.wgh.springcloud.commons.domain.ExcelData;

/**
 * excel export util
 *
 * @author wangguanghui
 */
public final class Export2003ExcelUtils {

    private final static String INK_FONT     = "微软雅黑";
    private final static String ARIAL_FONT   = "Arial";
    private final static String DEFAULT_NAME = "Sheet1";

    /**
     * 设置响应头信息
     *
     * @param response  HttpServletResponse
     * @param fileName  文件默认名称
     * @param excelData 表格实体类
     * @throws Exception 抛出错误信息
     */
    public static void exportExcel(HttpServletResponse response, String fileName, ExcelData excelData) throws Exception {

        // 告诉浏览器用什么软件可以打开此文件
        response.setHeader("content-Type", "application/vnd.ms-excel");
        // 下载文件的默认名称
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
        exportExcel(excelData, response.getOutputStream());
    }

    /**
     * 设置 XSSFWorkbook 即xlxs结尾的excel表格
     *
     * @param excelData    表格实体类
     * @param outputStream OutputStream
     * @throws Exception 抛出错误信息
     */
    public static void exportExcel(ExcelData excelData, OutputStream outputStream) throws Exception {

        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();

        try {
            String sheetName = excelData.getName();

            if (null == sheetName) {
                sheetName = DEFAULT_NAME;
            }
            HSSFSheet hssfSheet = hssfWorkbook.createSheet(sheetName);
            writeExcel(hssfWorkbook, hssfSheet, excelData);

            hssfWorkbook.write(outputStream);
        } finally {
            hssfWorkbook.close();
        }
    }

    /**
     * 数据写入 excel
     *
     * @param hssfWorkbook HSSFWorkbook
     * @param sheet        HSSFSheet
     * @param data         表格实体类
     */
    private static void writeExcel(HSSFWorkbook hssfWorkbook, HSSFSheet sheet, ExcelData data) {

        int rowIndex;

        rowIndex = writeTitlesToExcel(hssfWorkbook, sheet, data.getTitles());
        writeRowsToExcel(hssfWorkbook, sheet, data.getRows(), rowIndex);

        Export2007ExcelUtils.autoSizeColumns(sheet, data.getTitles().size() + 1);

    }

    /**
     * 对title写入
     *
     * @param hssfWorkbook HSSFWorkbook
     * @param sheet        HSSFSheet
     * @param titles       标题数据
     * @return 行数
     */
    private static int writeTitlesToExcel(HSSFWorkbook hssfWorkbook, HSSFSheet sheet, List<String> titles) {
        int rowIndex = 0;
        int colIndex;

        HSSFFont titleFont = hssfWorkbook.createFont();
        titleFont.setFontName(INK_FONT);
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 10);
        titleFont.setColor(IndexedColors.BLACK.index);

        HSSFCellStyle hssfCellStyle = hssfWorkbook.createCellStyle();
        /**
         * 过时方法
         *
         * hssfWorkbook.setAlignment(XSSFCellStyle.ALIGN_CENTER)
         * hssfWorkbook.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER)
         * hssfWorkbook.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND)
         */
        hssfCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        hssfCellStyle.setFont(titleFont);
        HSSFRow titleRow = sheet.createRow(rowIndex);
        /**
         * 标题行高
         *
         * titleRow.setHeightInPoints(25)
         */
        colIndex = 0;

        for (String field : titles) {
            HSSFCell cell = titleRow.createCell(colIndex);
            cell.setCellValue(field);
            cell.setCellStyle(hssfCellStyle);
            colIndex++;
        }

        rowIndex++;
        return rowIndex;
    }

    /**
     * 写入数据
     *
     * @param hssfWorkbook HSSFWorkbook
     * @param sheet        HSSFSheet
     * @param rows         数据
     * @param rowIndex     行数
     * @return 行数
     */
    private static int writeRowsToExcel(HSSFWorkbook hssfWorkbook, HSSFSheet sheet, List<List<Object>> rows, int rowIndex) {
        int colIndex;

        HSSFFont dataFont = hssfWorkbook.createFont();
        dataFont.setFontName(ARIAL_FONT);
        dataFont.setFontHeightInPoints((short) 10);
        dataFont.setColor(IndexedColors.BLACK.index);

        HSSFCellStyle dataStyle = hssfWorkbook.createCellStyle();
        /**
         * 过时方法
         *
         * dataStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER)
         * dataStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER)
         */
        dataStyle.setFont(dataFont);
        for (List<Object> rowData : rows) {
            HSSFRow dataRow = sheet.createRow(rowIndex);
            /**
             * 数据行高
             *
             * dataRow.setHeightInPoints(25)
             */
            colIndex = 0;

            for (Object cellData : rowData) {
                HSSFCell cell = dataRow.createCell(colIndex);
                if (cellData != null) {
                    cell.setCellValue(cellData.toString());
                } else {
                    cell.setCellValue("");
                }

                cell.setCellStyle(dataStyle);
                colIndex++;
            }
            rowIndex++;
        }
        return rowIndex;
    }

}
