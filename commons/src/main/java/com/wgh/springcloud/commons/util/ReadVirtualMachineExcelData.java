package com.wgh.springcloud.commons.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.wgh.springcloud.commons.domain.VmDiskRequest;
import com.wgh.springcloud.commons.domain.VmRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取云主机 excel 表相关信息
 *
 * @author wangguanghui
 */
public class ReadVirtualMachineExcelData {

    private final static String EXCEL_2003_REGEX = "^.+\\.(?i)(xls)$";
    private final static String EXCEL_2007_REGEX = "^.+\\.(?i)(xlsx)$";

    // 总行数
    private int totalRows = 0;

    // 总列数
    private int totalCells = 0;

    // 错误信息
    private String errorMsg;

    public ReadVirtualMachineExcelData() {
    }

    /**
     * 获取总行数
     *
     * @return
     */
    public int getTotalRows() {
        return totalRows;
    }

    /**
     * 获取总列数
     *
     * @return
     */
    public int getTotalCells() {
        return totalCells;
    }

    /**
     * 获取错误信息
     *
     * @return 错误信息
     */
    public String getErrorInfo() {
        return errorMsg;
    }

    /**
     * 读云主机 EXCEL 文件，获取信息集合
     *
     * @param mFile excel 文件
     * @return 云主机封装信息
     */
    public List<VmRequest> getVmExcelInfo(MultipartFile mFile) {
        String fileName = mFile.getOriginalFilename();
        // 获取文件名
        try {
            if (validateExcelData(fileName)) {
                // 验证文件名是否合格
                return null;
            }
            boolean isExcel2003 = true;
            // 根据文件名判断文件是2003版本还是2007版本
            if (isExcel2007(fileName)) {
                isExcel2003 = false;
            }
            return createVmExcel(mFile.getInputStream(), isExcel2003);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<VmDiskRequest> getVmDiskExcelInfo(MultipartFile mFile) {
        String fileName = mFile.getOriginalFilename();
        // 获取文件名
        try {
            if (validateExcelData(fileName)) {
                // 验证文件名是否合格
                return null;
            }
            boolean isExcel2003 = true;
            // 根据文件名判断文件是2003版本还是2007版本
            if (isExcel2007(fileName)) {
                isExcel2003 = false;
            }
            return createVmDiskExcel(mFile.getInputStream(), isExcel2003);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据excel里面的内容读取客户信息
     *
     * @param is          输入流
     * @param isExcel2003 excel是2003还是2007版本
     * @return
     * @throws IOException
     */
    public List<VmRequest> createVmExcel(InputStream is, boolean isExcel2003) {
        try {
            Workbook wb;
            if (isExcel2003) {
                // 当excel是2003时,创建excel2003
                wb = new HSSFWorkbook(is);
            } else {
                // 当excel是2007时,创建excel2007
                wb = new XSSFWorkbook(is);
            }
            return readVmExcelValue(wb);
            // 读取Excel里面客户的信息
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<VmDiskRequest> createVmDiskExcel(InputStream is, boolean isExcel2003) {
        try {
            Workbook wb;
            if (isExcel2003) {
                // 当excel是2003时,创建excel2003
                wb = new HSSFWorkbook(is);
            } else {
                // 当excel是2007时,创建excel2007
                wb = new XSSFWorkbook(is);
            }
            return readVmDiskExcelValue(wb);
            // 读取Excel里面客户的信息
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取云主机 Excel 里面信息
     *
     * @param wb workbook
     * @return excel 中主机信息
     */
    private List<VmRequest> readVmExcelValue(Workbook wb) {
        // 得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        // 得到Excel的行数
        this.totalRows = sheet.getPhysicalNumberOfRows();
        // 得到Excel的列数(前提是有行数)
        if (totalRows > 1 && sheet.getRow(0) != null) {
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        List<VmRequest> userList = new ArrayList<VmRequest>();
        // 循环Excel行数
        for (int r = 1; r < totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            VmRequest vm = new VmRequest();
            // 循环Excel的列
            for (int c = 0; c < this.totalCells; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {
                    if (c == 0) {
                        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            vm.setPlatformId(String.valueOf(cell.getNumericCellValue()));
                        } else {
                            vm.setPlatformId(cell.getStringCellValue());
                        }
                    } else if (c == 1) {
                        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            cell.setCellType(CellType.STRING);
                            vm.setCpu(Integer.valueOf(cell.getStringCellValue()));
                        } else {
                            vm.setCpu(Integer.valueOf(cell.getStringCellValue()));
                        }
                    } else if (c == 2) {
                        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            vm.setBizId(String.valueOf(cell.getNumericCellValue()));
                        } else {
                            vm.setBizId(cell.getStringCellValue());
                        }

                    } else if (c == 3) {
                        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            vm.setSubnet(String.valueOf(cell.getNumericCellValue()));
                        } else {
                            vm.setSubnet(cell.getStringCellValue());
                        }

                    } else if (c == 4) {
                        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            cell.setCellType(CellType.STRING);
                            vm.setMemory(Integer.valueOf(cell.getStringCellValue()));
                        } else {
                            vm.setMemory(Integer.valueOf(cell.getStringCellValue()));
                        }

                    } else if (c == 5) {
                        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            vm.setDiskSize1(String.valueOf(cell.getNumericCellValue()));
                        } else {
                            vm.setDiskSize1(cell.getStringCellValue());
                        }

                    } else if (c == 6) {
                        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            vm.setDiskType1(String.valueOf(cell.getNumericCellValue()));
                        } else {
                            vm.setDiskType1(cell.getStringCellValue());
                        }

                    } else if (c == 7) {
                        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            vm.setDiskSize2(String.valueOf(cell.getNumericCellValue()));
                        } else {
                            vm.setDiskSize2(cell.getStringCellValue());
                        }

                    } else if (c == 8) {
                        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            vm.setDiskType2(String.valueOf(cell.getNumericCellValue()));
                        } else {
                            vm.setDiskType2(cell.getStringCellValue());
                        }

                    } else if (c == 9) {
                        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            vm.setOs(String.valueOf(cell.getNumericCellValue()));
                        } else {
                            vm.setOs(cell.getStringCellValue());
                        }

                    } else if (c == 10) {
                        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            vm.setSysDiskSize(String.valueOf(cell.getNumericCellValue()));
                        } else {
                            vm.setSysDiskSize(cell.getStringCellValue());
                        }

                    } else if (c == 11) {
                        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            vm.setSysDiskType(String.valueOf(cell.getNumericCellValue()));
                        } else {
                            vm.setSysDiskType(cell.getStringCellValue());
                        }

                    }
                }
            }
            // 添加到list
            userList.add(vm);
        }
        return userList;
    }

    private List<VmDiskRequest> readVmDiskExcelValue(Workbook wb) {
        // 得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        // 得到Excel的行数
        this.totalRows = sheet.getPhysicalNumberOfRows();
        // 得到Excel的列数(前提是有行数)
        if (totalRows > 1 && sheet.getRow(0) != null) {
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        List<VmDiskRequest> userList = new ArrayList<VmDiskRequest>();
        // 循环Excel行数
        for (int r = 1; r < totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            VmDiskRequest vm = new VmDiskRequest();
            // 循环Excel的列
            for (int c = 0; c < this.totalCells; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {
                    if (c == 0) {
                        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            vm.setVmId(String.valueOf(cell.getNumericCellValue()));
                        } else {
                            vm.setVmId(cell.getStringCellValue());
                        }
                    } else if (c == 1) {

                        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            cell.setCellType(CellType.STRING);
                            vm.setIsOsDisk(Integer.parseInt(cell.getStringCellValue()));
                        } else {
                            vm.setIsOsDisk(Integer.valueOf(cell.getStringCellValue()));
                        }
                    } else if (c == 2) {
                        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            vm.setType(String.valueOf(cell.getNumericCellValue()));
                        } else {
                            vm.setType(cell.getStringCellValue());
                        }
                    } else if (c == 3) {
                        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            vm.setSize(String.valueOf(cell.getNumericCellValue()));
                        } else {
                            vm.setSize(cell.getStringCellValue());
                        }
                    } else if (c == 4) {
                        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            vm.setName(String.valueOf(cell.getNumericCellValue()));
                        } else {
                            vm.setName(cell.getStringCellValue());
                        }
                    }
                }
            }
            // 添加到list
            userList.add(vm);
        }
        return userList;
    }

    /**
     * 验证EXCEL文件
     *
     * @param filePath 文件名称
     * @return 校验结果
     */
    private boolean validateExcelData(String filePath) {
        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
            errorMsg = "文件名不是excel格式";
            return true;
        }
        return false;
    }

    /**
     * 判断是否为 excel2003 格式
     *
     * @param filePath 文件名称
     * @return 校验结果
     */
    private static boolean isExcel2003(String filePath) {
        return filePath.matches(EXCEL_2003_REGEX);
    }

    /**
     * 判断是否为 excel2007 格式
     *
     * @param filePath 文件名称
     * @return 校验结果
     */
    private static boolean isExcel2007(String filePath) {
        return filePath.matches(EXCEL_2007_REGEX);
    }

}
