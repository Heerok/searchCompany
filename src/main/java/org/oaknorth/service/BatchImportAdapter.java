package org.oaknorth.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;

/**
 * Created by Heerok on 31-01-2017.
 */
public class BatchImportAdapter {

    public List<String> collectRow(Row row, int numCols) {
        List<String> cells = new LinkedList<>();
        numCols = Math.max(numCols, row.getLastCellNum());
        for (int cn = 0; cn < numCols; cn++) {
            Cell cell = row.getCell(cn, Row.RETURN_BLANK_AS_NULL);
            if (cell == null) {
                cells.add(EMPTY_STRING);
                continue;
            }

            if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                switch (cell.getCachedFormulaResultType()) {
                    case Cell.CELL_TYPE_NUMERIC:
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        break;
                    case Cell.CELL_TYPE_STRING:
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                        cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
                        break;
                }
            }

            if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    cells.add(dateFormat.format(cell.getDateCellValue()));
                } else {

                    String s = String.valueOf(cell.getNumericCellValue());
                    int x = s.indexOf('E') > 0 ? s.indexOf('E') : 0;
                    if (x > 0) {
                        int y = s.indexOf('.');
                        Double d = Double.parseDouble(s.substring(x + 1)) - (x - y) + 1;
                        s = s.substring(0, y) + s.substring(y + 1, x) + "E" + d.toString();
                    }
                    if (s.indexOf('.') > 0) {
                        x = s.indexOf('.') > 0 ? s.indexOf('.') : 0;

                        if(x>0) {
                            String w = s.substring(x + 1, x + 2);
                            if (Integer.parseInt(w) > 0)
                                cells.add(s.substring(0, s.length()));
                            else
                                cells.add(s.substring(0, x));
                        }


                    }
                }
                continue;
            }
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    cells.add(cell.getBooleanCellValue() + "");
                    break;
                case Cell.CELL_TYPE_STRING:
                    cells.add(cell.getStringCellValue());
                    break;
            }
        }
        return cells;
    }

    public String prepareHeader(List<String> cells, List<String> columns) {
        //TODO hardocodes vulcan
        StringBuilder builder = new StringBuilder("insert into raw_vulcan_oneship_import").append("(");
        StringBuilder tail = new StringBuilder(") values (");

        for (int i = 0; i < cells.size(); i++) {
            builder.append('`').append(cells.get(i).trim().replace(" ", "_").toUpperCase()).append('`');
            tail.append("?");
            if (i < cells.size() - 1) {
                builder.append(",");
                tail.append(",");
            }
            columns.add(cells.get(i));
        }
        tail.append(")");
        builder.append(tail);
        return builder.toString();
    }


}
