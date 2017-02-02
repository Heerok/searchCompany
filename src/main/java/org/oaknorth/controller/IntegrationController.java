package org.oaknorth.controller;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.codec.binary.Base64;
import org.oaknorth.service.BatchImportService;
import org.oaknorth.view.Batch;
import org.oaknorth.view.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Created by Heerok on 31-01-2017.
 */
@RestController
public class IntegrationController {

    @Value("${oaknorth.filelocation}")
    private String filedir;

    @Autowired
    private BatchImportService batchImportService;

    @RequestMapping(value ="/upload", method = RequestMethod.POST)
    public Batch importData(@RequestParam("file") MultipartFile file){
        Path dirpath = Paths.get(filedir);
        Batch batch = new Batch();
        try{
            if (dirpath.toFile().exists()) {
                if (!dirpath.toFile().isDirectory()) {
                    System.out.println("{} is not a dir, cant continue");
                }
            } else {
                dirpath.toFile().mkdirs();
            }
            if (!file.isEmpty()) {
                String filename;
                filename = csvToXLSX(file.getInputStream(), dirpath.resolve(UUID.randomUUID().toString() + ".xls").toString());
                batch.setFilename(filename);
                batchImportService.importFile(batch,filename);
            }
            return batch;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String csvToXLSX(InputStream filename, String xlsfileName) {
        try {
            XSSFWorkbook workBook = new XSSFWorkbook();
            XSSFSheet sheet = workBook.createSheet("sheet1");
            String currentLine=null;
            int RowNum=0;
            BufferedReader br = new BufferedReader(new InputStreamReader(filename));
            while ((currentLine = br.readLine()) != null) {
                String str[] = currentLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                XSSFRow currentRow=sheet.createRow(RowNum);
                RowNum++;
                for(int i=0;i<str.length;i++){
                    currentRow.createCell(i).setCellValue(str[i]);
                }
            }

            FileOutputStream fileOutputStream =  new FileOutputStream(xlsfileName);
            workBook.write(fileOutputStream);
            fileOutputStream.close();
            return xlsfileName;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
