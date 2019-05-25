package com.wgh.springcloud.commons.util;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide;

import javax.servlet.http.HttpServletResponse;

import java.awt.Color;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import com.wgh.springcloud.commons.domain.ExcelData;

/**
 * excel export util
 *
 * @author wangguanghui
 */
public class Export2007ExcelUtils {

    private final static String INK_FONT = "微软雅黑";

    private final static String ARIAL_FONT = "Arial";

    private final static String DEFAULT_NAME = "Sheet1";

    /**
     * 设置响应头信息
     *
     * @param response  HttpServletResponse
     * @param fileName  文件默认名称
     * @param excelData 表格实体类
     * @throws Exception 抛出错误信息
     */
    public static void exportExcel(HttpServletResponse response, String fileName, ExcelData excelData)
                    throws Exception {

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

        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();

        try {
            String sheetName = excelData.getName();

            if (null == sheetName) {
                sheetName = DEFAULT_NAME;
            }
            XSSFSheet xssfSheet = xssfWorkbook.createSheet(sheetName);
            writeExcel(xssfWorkbook, xssfSheet, excelData);

            xssfWorkbook.write(outputStream);
        } finally {
            xssfWorkbook.close();
        }
    }

    /**
     * 数据写入 excel
     *
     * @param xssfWorkbook XSSFWorkbook
     * @param sheet        Sheet
     * @param data         表格实体类
     */
    private static void writeExcel(XSSFWorkbook xssfWorkbook, Sheet sheet, ExcelData data) {

        int rowIndex = 0;

        rowIndex = writeTitlesToExcel(xssfWorkbook, sheet, data.getTitles());
        writeRowsToExcel(xssfWorkbook, sheet, data.getRows(), rowIndex);
        autoSizeColumns(sheet, data.getTitles().size() + 1);

    }

    /**
     * 对title写入
     *
     * @param xssfWorkbook XSSFWorkbook
     * @param sheet        Sheet
     * @param titles       标题数据
     * @return 行数
     */
    private static int writeTitlesToExcel(XSSFWorkbook xssfWorkbook, Sheet sheet, List<String> titles) {
        int rowIndex = 0;
        int colIndex;

        Font titleFont = xssfWorkbook.createFont();
        titleFont.setFontName(INK_FONT);
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 10);
        titleFont.setColor(IndexedColors.BLACK.index);

        XSSFCellStyle xssfCellStyle = xssfWorkbook.createCellStyle();
        /**
         * 过时方法
         *
         * xssfCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER)
         * xssfCellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER)
         * xssfCellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND)
         */
        xssfCellStyle.setFillForegroundColor(new XSSFColor(new Color(182, 184, 192)));
        xssfCellStyle.setFont(titleFont);
        /**
         * 设置边框
         *
         * setBorder(xssfCellStyle, BorderStyle.THIN, new XSSFColor(new Color(0, 0, 0)));
         */
        Row titleRow = sheet.createRow(rowIndex);
        /**
         * 标题行高
         *
         * titleRow.setHeightInPoints(25)
         */
        colIndex = 0;

        for (String field : titles) {
            Cell cell = titleRow.createCell(colIndex);
            cell.setCellValue(field);
            cell.setCellStyle(xssfCellStyle);
            colIndex++;
        }

        rowIndex++;
        return rowIndex;
    }

    /**
     * 写入数据
     *
     * @param xssfWorkbook XSSFWorkbook
     * @param sheet        Sheet
     * @param rows         数据
     * @param rowIndex     行数
     * @return 行数
     */
    private static int writeRowsToExcel(XSSFWorkbook xssfWorkbook, Sheet sheet, List<List<Object>> rows, int rowIndex) {
        int colIndex;

        Font dataFont = xssfWorkbook.createFont();
        dataFont.setFontName(ARIAL_FONT);
        dataFont.setFontHeightInPoints((short) 10);
        dataFont.setColor(IndexedColors.BLACK.index);

        XSSFCellStyle dataStyle = xssfWorkbook.createCellStyle();
        /**
         * 过时方法
         *
         * dataStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER)
         * dataStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER)
         */
        dataStyle.setFont(dataFont);
        /**
         * 设置边框
         *
         * setBorder(dataStyle, BorderStyle.THIN, new XSSFColor(new Color(0, 0, 0)));
         */
        for (List<Object> rowData : rows) {
            Row dataRow = sheet.createRow(rowIndex);
            /**
             * 数据行高
             *
             * dataRow.setHeightInPoints(25)
             */
            colIndex = 0;

            for (Object cellData : rowData) {
                Cell cell = dataRow.createCell(colIndex);
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

    /**
     * 设置列数
     *
     * @param sheet        Sheet
     * @param columnNumber 列数
     */
    public static void autoSizeColumns(Sheet sheet, int columnNumber) {

        for (int i = 0; i < columnNumber; i++) {
            int orgWidth = sheet.getColumnWidth(i);
            sheet.autoSizeColumn(i, true);
            int newWidth = (sheet.getColumnWidth(i) + 100);

            if (newWidth > orgWidth) {
                sheet.setColumnWidth(i, newWidth);
            } else {
                sheet.setColumnWidth(i, orgWidth);
            }
        }
    }

    /**
     * 设置边框样式与颜色
     *
     * @param style  样式
     * @param border 边框大小
     * @param color  颜色
     */
    private static void setBorder(XSSFCellStyle style, BorderStyle border, XSSFColor color) {
        style.setBorderTop(border);
        style.setBorderLeft(border);
        style.setBorderRight(border);
        style.setBorderBottom(border);
        style.setBorderColor(BorderSide.TOP, color);
        style.setBorderColor(BorderSide.LEFT, color);
        style.setBorderColor(BorderSide.RIGHT, color);
        style.setBorderColor(BorderSide.BOTTOM, color);
    }

    /**
     * 设置响应头信息
     *
     * @param response  HttpServletResponse
     * @param fileName  文件默认名称
     * @param excelData 表格实体类
     * @throws Exception 抛出错误信息
     */
    public static void exportExcel(HttpServletResponse response, String fileName, ExcelData excelData,
                    XSSFWorkbook xssfWorkbook) throws Exception {

        // 告诉浏览器用什么软件可以打开此文件
        response.setHeader("content-Type", "application/vnd.ms-excel");
        // 下载文件的默认名称
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
        exportExcel(excelData, xssfWorkbook);
    }

    /**
     * 设置 XSSFWorkbook 即xlxs结尾的excel表格
     *
     * @param excelData 表格实体类
     * @throws Exception 抛出错误信息
     */
    public static void exportExcel(ExcelData excelData, XSSFWorkbook xssfWorkbook) {

        if (xssfWorkbook == null) {
            xssfWorkbook = new XSSFWorkbook();
        }

        String sheetName = excelData.getName();

        if (null == sheetName) {
            sheetName = DEFAULT_NAME;
        }

        XSSFSheet xssfSheet = xssfWorkbook.createSheet(sheetName);
        writeExcel(xssfWorkbook, xssfSheet, excelData);

    }

}
