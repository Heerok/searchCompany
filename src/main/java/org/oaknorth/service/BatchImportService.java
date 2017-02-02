package org.oaknorth.service;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.oaknorth.view.Batch;
import org.oaknorth.view.BatchImportDataMapping;
import org.oaknorth.view.BatchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Heerok on 31-01-2017.
 */
@Service
public class BatchImportService extends BatchImportAdapter{

    @Autowired
    private IntegrationService integrationService;


    public Batch importFile(Batch batch, String filename){
        List<String> columns = new ArrayList<>();
        List<String[]> params = new ArrayList<>();
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(new FileInputStream(filename));
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
        Sheet sheet = workbook.getSheetAt(0);
        if (sheet.getRow(0) == null) {
            batch.setStatus("ERROR");
            batch.setComments("Header missing at first row!!!");
            return batch;
        }
        for (Row row : sheet) {
            List<String> cells = collectRow(row, columns.size());
            if (row.getRowNum() == 0) {
                prepareHeader(cells, columns);
            } else {
                params.add(cells.toArray(new String[columns.size()]));
            }
        }
        if (params.size() == 0) {
            batch.setStatus("ERROR");
            batch.setComments("Data not found!!!");
            return batch;
        }

        Map<String, String> fieldmap = BatchImportDataMapping.getFieldmap();
        for (String[] row : params) {
            HashMap<String, String> map = new HashMap<>();
            for (int i = 0; i < columns.size(); i++) {
                if (fieldmap.containsKey(columns.get(i)))
                    map.put(fieldmap.get(columns.get(i)), row[i]);
            }
            importBatch(map, batch);
        }
        return batch;
    }

    private void importBatch(HashMap<String, String> dataRow,Batch batch){
        if (StringUtils.isEmpty(dataRow.get("Company Name"))) {
            batch.addResult(new BatchResult(
                    dataRow.get("Company Name"),
                    "Company Name is empty"
            ));
            return;
        }
        BatchResult result = new BatchResult(dataRow.get("Company Name"),"OK");
        result.setCompanies(integrationService.searchCompany(dataRow.get("Company Name")));
        batch.addResult(result);
    }

}
