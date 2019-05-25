package com.wgh.springcloud.client.controller;

import com.wgh.springcloud.client.service.UserService;
import com.wgh.springcloud.commons.domain.CheckoutVirtualMachineResponse;
import com.wgh.springcloud.commons.domain.CtYunVirtualMachineVerifyDifference;
import com.wgh.springcloud.commons.domain.CtYunVmAndLocalVmDifferences;
import com.wgh.springcloud.commons.domain.ExcelData;
import com.wgh.springcloud.commons.domain.ResourceResponse;
import com.wgh.springcloud.commons.domain.User;
import com.wgh.springcloud.commons.domain.VirtualMachineDetailResponse;
import com.wgh.springcloud.commons.domain.VmDiskRequest;
import com.wgh.springcloud.commons.util.ExcelImportUtil;
import com.wgh.springcloud.commons.util.Export2003ExcelUtils;
import com.wgh.springcloud.commons.util.Export2007ExcelUtils;
import com.wgh.springcloud.commons.util.IsExcelVersion;
import com.wgh.springcloud.commons.util.ReadVirtualMachineExcelData;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * export excel controller
 *
 * @author wangguanghui
 */
@Controller
public class ExcelController {

    private final static String FILENAME = "用户信息.xlsx";

    private Logger logger = LoggerFactory.getLogger(ExcelController.class);

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

    /**
     * 导出Excel
     *
     * @return 流文件
     */
    @PostMapping("/upload/vmExcel")
    @ResponseBody
    public OutputStream uploadVmExcel(@RequestBody CheckoutVirtualMachineResponse condition,
                    HttpServletResponse response) throws Exception {

        ExcelData firstExcelData = getDifferenceVms(condition.getCtYunVmAndLocalVmDifferences());
        //        ExcelData secondExcelData = getBizIds(condition.getFailBizIds(), condition.getInvalidBizIds());
        String fileName = "天翼云主机与本地主机差异性.xlsx";

        return ExcelImportUtil.export2007Excel(response, fileName, firstExcelData);

    }

    private ExcelData getDifferenceVms(List<CtYunVmAndLocalVmDifferences> differences) {
        ExcelData excelData = new ExcelData();
        AtomicInteger count = new AtomicInteger();
        String sheet = "差异性数据";
        List<String> titles =
                        Arrays.asList("bizId", "资源池", "天翼云主机", "天翼云主机", "天翼云主机", "天翼云主机", "天翼云主机", "天翼云主机", "天翼云主机",
                                        "天翼云主机", "天翼云主机", "天翼云主机", "天翼云主机", "天翼云主机", "本地主机", "本地主机", "本地主机", "本地主机",
                                        "本地主机", "本地主机", "本地主机", "本地主机");
        List<List<Object>> rows = new ArrayList<>();
        List<Object> rowCell = new ArrayList<>();
        rowCell.add("");
        rowCell.add("");
        rowCell.add("真实ID");
        rowCell.add("主机名称");
        rowCell.add("实际状态");
        rowCell.add("系统类型");
        rowCell.add("资源池ID");
        rowCell.add("子网ID");
        rowCell.add("创建时间");
        rowCell.add("可用区ID");
        rowCell.add("可用区名称");
        rowCell.add("CPU");
        rowCell.add("内存");
        rowCell.add("是否冻结");
        rowCell.add("真实ID");
        rowCell.add("主机名称");
        rowCell.add("实际状态");
        rowCell.add("系统类型");
        rowCell.add("资源池ID");
        rowCell.add("可用区ID");
        rowCell.add("CPU");
        rowCell.add("内存");

        rows.add(rowCell);

        logger.info("差异性数据共：" + differences.size() + "条");
        differences.forEach(difference -> {

            count.addAndGet(1);
            logger.info("处理第：" + count + "条数据");
            if (count.get() == 126) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (difference.getCtYunVmDetails().size() != 0 && difference.getCtYunVmDetails().size() != 0) {

                int size = difference.getCtYunVmDetails().size() <= difference.getLocalVmDetails().size() ?
                                difference.getLocalVmDetails().size() :
                                difference.getCtYunVmDetails().size();
                for (int i = 0; i < size; i++) {
                    List<Object> row = new ArrayList<>();
                    row.add(difference.getBizId());
                    row.add(difference.getRegion());

                    if (difference.getCtYunVmDetails().size() > i) {
                        CtYunVirtualMachineVerifyDifference ctYunVms = difference.getCtYunVmDetails().get(i);
                        row.add(ctYunVms.getResVmId());
                        row.add(ctYunVms.getVmName());
                        row.add(ctYunVms.getVmStatus());
                        row.add(ctYunVms.getOsStyle());
                        row.add(ctYunVms.getRegionId());
                        row.add(ctYunVms.getVlanId());
                        row.add(ctYunVms.getCreateDate());
                        row.add(ctYunVms.getZoneId());
                        row.add(ctYunVms.getZoneName());
                        row.add(ctYunVms.getCpuNum());
                        row.add(ctYunVms.getMemSize());
                        row.add(ctYunVms.getIsFreeze());
                    } else {
                        row.add("");
                        row.add("");
                        row.add("");
                        row.add("");
                        row.add("");
                        row.add("");
                        row.add("");
                        row.add("");
                        row.add("");
                        row.add("");
                        row.add("");
                        row.add("");
                        row.add("");
                    }

                    if (difference.getLocalVmDetails().size() > i) {
                        VirtualMachineDetailResponse localMvs = difference.getLocalVmDetails().get(i);
                        row.add(localMvs.getExternalId());
                        row.add(localMvs.getName());
                        row.add(localMvs.getPowerState());
                        row.add(localMvs.getOsType());
                        row.add(localMvs.getRegion());
                        row.add(localMvs.getAvailabilityZone());
                        row.add(localMvs.getCpu());
                        row.add(localMvs.getMemory());
                    } else {
                        row.add("");
                        row.add("");
                        row.add("");
                        row.add("");
                        row.add("");
                        row.add("");
                        row.add("");
                        row.add("");
                    }


                    rows.add(row);
                }

            }
        });

        excelData.setName(sheet);
        excelData.setTitles(titles);
        excelData.setRows(rows);
        return excelData;
    }

    private ExcelData getBizIds(Set<String> failBizIds, Set<String> invailedBizIds) {
        ExcelData excelData = new ExcelData();
        String sheet = "无数据的bizIds";
        List<String> titles = Arrays.asList("查询失败的bizIds", "查询无效的bizIds");

        List<List<Object>> rows = new ArrayList<>();

        List<Object> row = new ArrayList<>();
        row.add(failBizIds);
        row.add(invailedBizIds);
        rows.add(row);

        excelData.setName(sheet);
        excelData.setTitles(titles);
        excelData.setRows(rows);

        return excelData;
    }
}
