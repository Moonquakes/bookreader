package com.wechat.BookReader.runner;

import com.wechat.BookReader.dao.BookRepo;
import com.wechat.BookReader.entity.Book;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @Author: Disclover
 * @Date: 2019/5/18 13:21
 */

@Component
@Order(value = 1)
public class BookRunner implements ApplicationRunner {
    @Autowired
    private BookRepo bookRepo;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String separator = System.getProperty("file.separator");
        String path = "src"
                + separator + "main"
                + separator + "java"
                + separator + "com"
                + separator + "wechat"
                + separator + "BookReader"
                + separator + "book.xlsx";
        File file = new File(path);
        Workbook workbook = null;
        try {
            InputStream inputStream = new FileInputStream(file);
            workbook = new XSSFWorkbook(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Sheet sheet = workbook.getSheetAt(0);
        int rowNum = sheet.getLastRowNum();
        for (int i = 1; i <= rowNum; i++) {
            Row row = sheet.getRow(i);
            String[] temp = new String[7];
            for (int index = 0; index < 7; index++) {
                try {
                    row.getCell(index).setCellType(CellType.STRING);
                    temp[index] = row.getCell(index).getStringCellValue();
                } catch (Exception e) {
                    temp[index] = "";
                }
            }
            int id = Integer.valueOf(temp[0]);
            String content = temp[1];
            String name = temp[2];
            String postUrl = temp[3];
            double star = Double.valueOf(temp[4]);
            String summary = temp[5];
            String writer = temp[6];
            Book book = new Book(id,name,writer,summary,content,postUrl,star);
            bookRepo.save(book);
        }
    }
}
