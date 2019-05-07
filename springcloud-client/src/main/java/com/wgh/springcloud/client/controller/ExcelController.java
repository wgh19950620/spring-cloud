package com.wgh.springcloud.client.controller;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

import com.wgh.springcloud.client.service.UserService;
import com.wgh.springcloud.commons.domain.ExcelData;
import com.wgh.springcloud.commons.domain.ResourceResponse;
import com.wgh.springcloud.commons.domain.User;
import com.wgh.springcloud.commons.domain.VmDiskRequest;
import com.wgh.springcloud.commons.util.Export2003ExcelUtils;
import com.wgh.springcloud.commons.util.Export2007ExcelUtils;
import com.wgh.springcloud.commons.util.IsExcelVersion;
import com.wgh.springcloud.commons.util.ReadVirtualMachineExcelData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * export excel controller
 *
 * @author wangguanghui
 */
@Controller
public class ExcelController {

    private final static String FILENAME = "用户信息.xlsx";

    @Autowired
    private UserService userService;

    @RequestMapping("/upload")
    public String index() {
        return "upload";
    }

    /*@RequestMapping(value="/upload")
    @ResponseBody
    public ModelAndView upload(){
        ModelAndView mode = new ModelAndView();
        mode.setViewName("upload");
        return mode;
    }*/

    /**
     * 导出excel
     *
     * @param response HttpServletResponse
     * @throws Exception exception
     */
    @RequestMapping(value = "/excel", method = RequestMethod.GET)
    @ResponseBody
    public void excel(HttpServletResponse response) throws Exception {
        ExcelData data = new ExcelData();
        data.setName("hello");
        List<String> titles = new ArrayList<>();
        titles.add("用户ID");
        titles.add("姓名");
        titles.add("年龄");
        titles.add("身份");
        data.setTitles(titles);

        List<List<Object>> rows = new ArrayList<>();
        List<User> userList = userService.findUser();

        userList.forEach(user -> {
            List<Object> row = new ArrayList<>();
            row.add(user.getId());
            row.add(user.getName());
            row.add(user.getAge());
            row.add(user.getIdentity());
            rows.add(row);
        });

        data.setRows(rows);
        data.setName("用户信息");

        /**
         * 生成本地文件
         *
         * File f = new File("c:/test.xlsx")
         * FileOutputStream out = new FileOutputStream(f)
         * Export2007ExcelUtils.exportExcel(data, out)
         * out.close()
         */
        if (IsExcelVersion.isExcel2007(FILENAME)) {
            Export2007ExcelUtils.exportExcel(response, FILENAME, data);
        } else if (IsExcelVersion.isExcel2003(FILENAME)) {
            Export2003ExcelUtils.exportExcel(response, FILENAME, data);
        }
    }

    @PostMapping("/upload/vmDisks/excel")
    @ResponseBody
    public ResourceResponse uploadVmDiskExcel(@RequestParam("file") MultipartFile file) {
        ResourceResponse response = new ResourceResponse();
        ReadVirtualMachineExcelData readExcel = new ReadVirtualMachineExcelData();

        List<VmDiskRequest> vmDisks = readExcel.getVmDiskExcelInfo(file);
        vmDisks.forEach(System.out::println);
        response.setData(vmDisks);
        return response;
    }

    /**
     * 导入excel 数据
     *
     * @param file 导入文件
     * @return 解析结果
     */
    @PostMapping("/upload/excel")
    @ResponseBody
    public ResourceResponse uploadExcel(@RequestParam("file") MultipartFile file) throws Exception {
        ResourceResponse response = new ResourceResponse();
        Workbook workbook = null;
        Sheet sheet = null;
        List<User> users = new ArrayList<>();

        String fileName = file.getOriginalFilename();

        if (fileName.isEmpty() || file.getSize() == 0) {

            response.saveError("上传失败");
            return response;
        }

        try {
            if (IsExcelVersion.isExcel2007(fileName)) {

                workbook = new XSSFWorkbook(file.getInputStream());
            } else if (IsExcelVersion.isExcel2003(fileName)) {

                workbook = new HSSFWorkbook(file.getInputStream());
            }

            if (null != workbook) {
                sheet = workbook.getSheetAt(0);
            }

            if (null == sheet) {
                response.saveError("Excel 表为空");
                return response;
            }

            System.out.println(sheet.getLastRowNum());

            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {

                Row row = sheet.getRow(i);

                if (null == row) {
                    response.saveError("Excel 无数据");
                    return response;
                }

                Integer id = Integer.valueOf(row.getCell(0).getStringCellValue());
                String name = row.getCell(1).getStringCellValue();
                Integer age = Integer.valueOf(row.getCell(2).getStringCellValue());
                String identity = row.getCell(3).getStringCellValue();

                users.add(new User(id, name, age, identity));
            }

            users.forEach(System.out::println);
            users.forEach(user -> userService.saveUser(user));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }
}
