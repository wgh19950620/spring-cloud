package com.wgh.springcloud.commons.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.github.pagehelper.util.StringUtil;

/**
 * excel upload util
 *
 * @author wangguanghui
 */
public class ExcelImportUtil {

    private static final Log    log       = LogFactory.getLog(ExcelImportUtil.class);
    private final static String HSSF_FLAG = "HSSF";
    private final static String XSSF_FLAG = "XSSF";

    /**
     * excel文件导入
     * 该方法在传统方法加以修改，支持不同版本导入，最终返回一个list集合，集合中封装的是object数组，然后根据业务需要封装自己想要的pojo对象。
     * 该方法支持指定服务端文件路径上传和客户端上传，如果是服务端指定路径上传则file赋值null，反之，则filePaht赋值null
     * 前端页面代码以下4行
     * <form action="upload" enctype="multipart/form-data">
     * <input type="file" name="myFile" />
     * <input type="submit" value="Upload! " />
     * </form>
     * springMvc action 中使用以下代码，以下两行
     * MultipartHttpServletRequest mulRequest = (MultipartHttpServletRequest) request;
     * MultipartFile file = mulRequest.getFile("excel");
     *
     * @param file
     * @param filePath "d:/test.xls";
     * @return
     */
    public static List excelUpLoad(File file, String filePath) {
        List list = null;

        if (null == file && StringUtil.isEmpty(filePath)) {
            log.error("导入excel，发现无效的参数，无法执行!");
            return null;
        }

        File existFile = file;
        if (StringUtil.isNotEmpty(filePath)) {
            existFile = new File(filePath);
        }

        String filename = existFile.getName();
        if (StringUtil.isEmpty(filename)) {
            log.error("导入excel,文件不存在！");
        }
        try {
            InputStream input = new FileInputStream(existFile);
            Workbook workBook;
            //以下做法是为了区分不同版本，然后使用XSSF或HSSF
            String flag;
            try {
                workBook = new XSSFWorkbook(input);
                flag = XSSF_FLAG;
            } catch (Exception ex) {
                workBook = new HSSFWorkbook(new FileInputStream(existFile));
                flag = HSSF_FLAG;
            }

            //根据标记使用不同的方法来解析excel
            if (XSSF_FLAG.equals(flag)) {
                XSSFSheet sheet = (XSSFSheet) workBook.getSheetAt(0);
                if (sheet != null) {
                    list = new ArrayList();
                    for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                        XSSFRow row = sheet.getRow(i);
                        Object[] obj = new Object[row.getPhysicalNumberOfCells()];
                        for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                            XSSFCell cell = row.getCell(j);
                            String cellStr = cell.toString();
                            log.info("【" + cellStr + "】 ");
                            obj[j] = cellStr;
                        }
                        System.out.println();
                        list.add(obj);
                    }
                    log.info("当前excel总条数" + list.size());
                }
            } else if (HSSF_FLAG.equals(flag)) {
                HSSFSheet sheet = (HSSFSheet) workBook.getSheetAt(0);
                if (sheet != null) {
                    list = new ArrayList();
                    for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                        HSSFRow row = sheet.getRow(i);
                        Object[] obj = new Object[row.getPhysicalNumberOfCells()];
                        for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                            HSSFCell cell = row.getCell(j);
                            String cellStr = cell.toString();
                            log.info("【" + cellStr + "】 ");
                            obj[j] = cellStr;
                        }
                        System.out.println();
                        list.add(obj);
                    }
                    log.info("当前excel总条数" + list.size());
                }
            } else {
                log.error("导入excel,发生未知错误！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
