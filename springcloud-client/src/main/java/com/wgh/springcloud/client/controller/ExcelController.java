package com.wgh.springcloud.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;

import com.wgh.springcloud.client.service.UserService;
import com.wgh.springcloud.commons.domain.ExcelData;
import com.wgh.springcloud.commons.domain.User;
import com.wgh.springcloud.commons.util.Export2003ExcelUtils;
import com.wgh.springcloud.commons.util.Export2007ExcelUtils;
import com.wgh.springcloud.commons.util.IsExcelVersion;

/**
 * export excel controller
 *
 * @author wangguanghui
 */
@RestController
public class ExcelController {

    private final static String FILENAME = "用户信息.xlsx";

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/excel", method = RequestMethod.GET)
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
        if(IsExcelVersion.isExcel2007(FILENAME)) {
            Export2007ExcelUtils.exportExcel(response, FILENAME, data);
        } else if(IsExcelVersion.isExcel2003(FILENAME)) {
            Export2003ExcelUtils.exportExcel(response, FILENAME, data);
        }
    }
}
