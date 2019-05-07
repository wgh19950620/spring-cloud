package com.wgh.springcloud.client;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;

import com.wgh.springcloud.commons.domain.User;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringcloudClientApplicationTests {

    private final String KEY = "zhangsan";

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private JedisCluster jedisCluster;

    @Test
    public void contextLoads() {
    }

    @Test
    public void redisTest() {
        Long aLong = jedisCluster.incrBy(KEY, 1);
        Long incr = jedisCluster.incr(KEY);
        System.out.println(incr);
        System.out.println(aLong);
    }

    @Test
    public void excel() throws Exception {
        List<User> users = new ArrayList<>();
        //Excel文件
        XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(ResourceUtils.getFile("classpath:excel/用户信息.xlsx")));
        //Excel工作表
        XSSFSheet sheet = wb.getSheetAt(0);

        int totalRows = sheet.getPhysicalNumberOfRows();
        int totalCells = 0;
        // 得到Excel的列数(前提是有行数)
        if (totalRows > 1 && sheet.getRow(0) != null) {
            totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }

        for (int r = 1; r < totalRows; r++) {

            XSSFRow row = sheet.getRow(r);

            for (int c = 0; c < totalCells; c++) {
                XSSFCell cell = row.getCell(c);
                System.out.println("cell: " + cell);

                System.out.println("cellTypeEnum: " + cell.getCellTypeEnum());
                System.out.println("type: " + cell.getCellType());
                System.out.println("CellType.NUMERIC.getCode(): " + CellType.NUMERIC.getCode());
                System.out.println("CellType.NUMERIC: " + CellType.NUMERIC);
            }

            Integer id = Integer.valueOf(row.getCell(0).getStringCellValue());
            String name = row.getCell(1).getStringCellValue();
            Integer age = Integer.valueOf(row.getCell(2).getStringCellValue());
            String identity = row.getCell(3).getStringCellValue();

            users.add(new User(id, name, age, identity));
        }

        System.out.println("共有" + users.size() + "条数据：");
        users.forEach(System.out::println);
    }

}
