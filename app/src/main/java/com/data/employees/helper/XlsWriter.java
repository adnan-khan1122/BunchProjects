package com.data.employees.helper;

import android.database.Cursor;
import android.util.Log;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class XlsWriter {

    private Cursor cursor;
    private File file;


    public XlsWriter(Cursor cursor, File file) {
        this.cursor = cursor;
        this.file = file;
    }

    public FileOutputStream writeNext() {
        FileOutputStream fos = null;
        HSSFWorkbook workbook = new HSSFWorkbook();

        //Adding Cell's Style
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setBorderBottom((short) 2);
        cellStyle.setBorderTop((short) 2);
        cellStyle.setBorderLeft((short) 2);
        cellStyle.setBorderRight((short) 2);
        HSSFSheet firstSheet = workbook.createSheet("Employee Sheet");
        firstSheet.setColumnWidth(1, (15 * 400));
        firstSheet.setColumnWidth(2, (15 * 400));

        //Table Header
        HSSFRow rowA = firstSheet.createRow(1);
        int numberOfColumns = cursor.getColumnCount();
        int i ;
        for(i = 0;i< numberOfColumns;i++){

            HSSFCell cell = rowA.createCell(1 + i);
            cell.setCellValue(new HSSFRichTextString(cursor.getColumnName(i)));
            cell.setCellStyle(cellStyle);
        }

        //Table data
        int j = 0;
        while (cursor.moveToNext()) {
            HSSFRow rowData = firstSheet.createRow(j + 2);
            HSSFCell cell1 = rowData.createCell(1);
            HSSFCell cell2 = rowData.createCell(2);

            //Which column you want to exprort
            cell1.setCellValue(new HSSFRichTextString(cursor.getString(0)));
            cell1.setCellStyle(cellStyle);

            cell2.setCellValue(new HSSFRichTextString(cursor.getString(1)));
            cell2.setCellStyle(cellStyle);
            j++;

        }

        cursor.close();

        try {

            fos = new FileOutputStream(file);
            workbook.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fos;


    }

}
