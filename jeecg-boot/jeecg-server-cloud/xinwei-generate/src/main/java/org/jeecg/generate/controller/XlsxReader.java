package org.jeecg.generate.controller;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XlsxReader {

    /**
     * 读取XLSX文件并获取每列的值
     *
     * @param filePath   XLSX文件路径
     * @param sheetIndex 工作表索引（从0开始）
     * @return 包含每列值的列表，每个元素是一列的所有值
     * @throws IOException 如果文件操作出现错误
     */
    public static List<List<String>> readColumnsFromXlsx(String filePath, int sheetIndex) throws IOException {
        List<List<String>> columns = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            // 获取指定的工作表
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            if (sheet == null) {
                throw new IllegalArgumentException("指定索引的工作表不存在");
            }

            // 获取最大行数和最大列数
            int rowCount = sheet.getLastRowNum() + 1;
            int columnCount = getMaxColumnCount(sheet);

            // 初始化列列表
            for (int i = 0; i < columnCount; i++) {
                columns.add(new ArrayList<>());
            }

            // 遍历每一行，将单元格值添加到对应列
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    // 如果行不存在，为所有列添加空字符串
                    for (int colIndex = 0; colIndex < columnCount; colIndex++) {
                        columns.get(colIndex).add("");
                    }
                    continue;
                }

                // 遍历每行的单元格
                for (int colIndex = 0; colIndex < columnCount; colIndex++) {
                    Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    columns.get(colIndex).add(getCellValue(cell));
                }
            }
        }

        return columns;
    }

    /**
     * 获取工作表中最大的列数
     */
    private static int getMaxColumnCount(Sheet sheet) {
        int maxColumns = 0;
        int rowCount = sheet.getLastRowNum() + 1;

        for (int i = 0; i < rowCount; i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                int lastColumn = row.getLastCellNum();
                if (lastColumn > maxColumns) {
                    maxColumns = lastColumn;
                }
            }
        }

        return maxColumns;
    }

    /**
     * 获取单元格的值，根据单元格类型进行转换
     */
    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // 处理数字，避免科学计数法
                    double value = cell.getNumericCellValue();
                    if (value == (long) value) {
                        return String.valueOf((long) value);
                    } else {
                        return String.valueOf(value);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    // 使用示例
    public static void main(String[] args) {
        try {
            String filePath = "D:\\Desktop\\FDQ_TRACK.xlsx";
            int sheetIndex = 0; // 第一个工作表

            List<List<String>> columns = readColumnsFromXlsx(filePath, sheetIndex);

            // 打印每列的值
            for (int i = 0; i < columns.size(); i++) {
                System.out.println("第 " + (i + 1) + " 列的值:");
                for (String value : columns.get(i)) {
                    System.out.println(value);
                }
                System.out.println("---------------------");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
