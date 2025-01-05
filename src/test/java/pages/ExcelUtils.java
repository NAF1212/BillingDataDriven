package pages;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtils {

    /**
     * Reads data from an Excel file and returns it as a list of object arrays.
     *
     * @param filePath  The path to the Excel file.
     * @param sheetName The name of the sheet to read data from.
     * @return A list of object arrays, where each array represents a row of data.
     * @throws IOException If an error occurs during file handling.
     */
    public static List<Object[]> readExcelData(String filePath, String sheetName) throws IOException {
        List<Object[]> dataList = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet " + sheetName + " does not exist in the file.");
            }

            DataFormatter formatter = new DataFormatter();
            boolean isHeaderRow = true;

            for (Row row : sheet) {
                if (isHeaderRow) {
                    isHeaderRow = false;
                    continue; // Skip header row
                }

                String tripDetails = formatCell(row, 0, formatter);
                String billNumber = formatCell(row, 1, formatter);
                String term = formatCell(row, 2, formatter);
                String sequence = formatCell(row, 3, formatter);
                String shipperId = formatCell(row, 4, formatter);
                String consigneeId = formatCell(row, 5, formatter);
                String thirdParty = formatCell(row, 6, formatter);
                String checkboxName = formatCell(row, 7, formatter);
                String quantity = formatCell(row, 8, formatter);
                String pieces = formatCell(row, 9, formatter);
                String cuft = formatCell(row, 10, formatter);
                String description = formatCell(row, 11, formatter);
                String weight = formatCell(row, 12, formatter);

                // Skip rows with missing mandatory fields
                if (tripDetails.isEmpty() || billNumber.isEmpty() || term.isEmpty() ||
                    sequence.isEmpty() || shipperId.isEmpty() || consigneeId.isEmpty()) {
                    continue;
                }

                // Add the row data as an object array
                dataList.add(new Object[]{
                        tripDetails, billNumber, term, sequence, shipperId, consigneeId,
                        thirdParty, checkboxName, quantity, pieces, cuft, description, weight
                });
            }
        }
        return dataList;
    }

    /**
     * Formats a cell value as a string using the DataFormatter.
     *
     * @param row       The row containing the cell.
     * @param colIndex  The column index of the cell.
     * @param formatter The DataFormatter instance.
     * @return The formatted cell value as a string.
     */
    private static String formatCell(Row row, int colIndex, DataFormatter formatter) {
        Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        return formatter.formatCellValue(cell).trim();
    }

    /**
     * Reads a cell value as a boolean, supporting multiple formats (boolean, string).
     *
     * @param row      The row containing the cell.
     * @param colIndex The column index of the cell.
     * @return The cell value as a boolean.
     */
    public static boolean readCellAsBoolean(Row row, int colIndex) {
        Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        if (cell != null) {
            switch (cell.getCellType()) {
                case BOOLEAN:
                    return cell.getBooleanCellValue();
                case STRING:
                    String value = cell.getStringCellValue().toUpperCase().trim();
                    return "TRUE".equals(value) || "YES".equals(value) || "1".equals(value);
                case NUMERIC:
                    return cell.getNumericCellValue() == 1;
                default:
                    return false;
            }
        }
        return false;
    }
}
