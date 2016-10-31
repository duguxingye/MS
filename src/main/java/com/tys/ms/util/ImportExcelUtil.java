package com.tys.ms.util;


/**
 * Created by Administrator on 2016/10/27.
 */
public class ImportExcelUtil {

//    public void importExcel(FileBucket fileBean) {
//
//        ByteArrayInputStream bis = new ByteArrayInputStream(fileBean.getFileData().getBytes());
//        Workbook workbook;
//        try {
//            if (fileBean.getFileData().getOriginalFilename().endsWith("xls")) {
//                workbook = new HSSFWorkbook(bis);
//            } else if (fileBean.getFileData().getOriginalFilename().endsWith("xlsx")) {
//                workbook = new XSSFWorkbook(bis);
//            } else {
//                throw new IllegalArgumentException("Received file does not have a standard excel extension.");
//            }
//
//            for (Row row : workbook.getSheet("1")) {
//                if (row.getRowNum() == 0) {
//                    Iterator<Cell> cellIterator = row.cellIterator();
//                    while (cellIterator.hasNext()) {
//                        Cell cell = cellIterator.next();
//                        //go from cell to cell and do create sql based on the content
//                    }
//                }
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
