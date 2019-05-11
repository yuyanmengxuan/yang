package com.wei.controller;

import com.csvreader.CsvWriter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
@RequestMapping("/export")
@Controller
public class Export {

    public void write(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 创建CSV写对象
            List<String> ls = new ArrayList<String>();
            for (int i = 0; i < 2; i++) {
               String s="我是"+i;
                ls.add(s);
            }
            //写入临时文件
            File tempFile = File.createTempFile("vehicle", ".csv");
            CsvWriter csvWriter = new CsvWriter(tempFile.getCanonicalPath(), ',', Charset.forName("UTF-8"));
            // 写表头
            long s = System.currentTimeMillis();
            System.err.println();
            String[] headers = {"姓名"};
            csvWriter.writeRecord(headers);
            for (String stu : ls) {
                csvWriter.write(stu);

                csvWriter.endRecord();
            }
            csvWriter.close();
            long e = System.currentTimeMillis();

            System.err.println(e - s);
            ;

            /**
             * 写入csv结束，写出流
             */
            java.io.OutputStream out = response.getOutputStream();
            byte[] b = new byte[1024];
            java.io.File fileLoad = new java.io.File(tempFile.getCanonicalPath());
            response.reset();
            response.setContentType("application/csv");
            response.setHeader("content-disposition", "attachment; filename=vehicleModel.csv");
            long fileLength = fileLoad.length();
            String length1 = String.valueOf(fileLength);
            response.setHeader("Content_Length", length1);
            java.io.FileInputStream in = new java.io.FileInputStream(fileLoad);
            int n;
            while ((n = in.read(b)) != -1) {
                out.write(b, 0, n); //每次写入out1024字节
            }
            in.close();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}