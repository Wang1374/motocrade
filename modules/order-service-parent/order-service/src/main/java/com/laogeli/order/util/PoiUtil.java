package com.laogeli.order.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class PoiUtil {

    /**
     * 设置合并单元格后表格边框
     *
     * @param border 边框宽度
     * @param region 合并区域
     * @param sheet  HSSFSheet
     */
    public static void setRegionBorder(int border, CellRangeAddress region, HSSFSheet sheet) {
        // 下边框
        RegionUtil.setBorderBottom(border, region, sheet);
        // 左边框
        RegionUtil.setBorderLeft(border, region, sheet);
        // 右边框
        RegionUtil.setBorderRight(border, region, sheet);
        // 上边框
        RegionUtil.setBorderTop(border, region, sheet);
    }

    /**
     * 自适应宽度(中文支持)
     *
     * @param sheet
     * @param size
     */
    public static void setSizeColumn(Sheet sheet, int size) {
        for (int columnNum = 0; columnNum < size; columnNum++) {
            int columnWidth = sheet.getColumnWidth(columnNum) / 256;
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                Row currentRow;
                //当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }

                if (currentRow.getCell(columnNum) != null) {
                    Cell currentCell = currentRow.getCell(columnNum);
                    if (currentCell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            sheet.setColumnWidth(columnNum, columnWidth * 256);
        }
    }

    /**
     * 得到不同颜色的style样式
     *
     * @param workbook
     * @param color
     * @return
     */
    public static HSSFCellStyle getCellStyle(HSSFWorkbook workbook, Short color) {
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        HSSFFont fontStyle = workbook.createFont();
        cellStyle.setFillForegroundColor(color);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        fontStyle.setFontHeightInPoints((short) 11);
        fontStyle.setFontName("宋体");
        cellStyle.setFont(fontStyle);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        // 自动换行
        cellStyle.setWrapText(true);
        return cellStyle;
    }

    /**
     * 生成首行标题样式
     *
     * @return
     */
    public static HSSFCellStyle getCellStyle0(HSSFWorkbook workbook) {
        // 生成首行标题样式
        HSSFCellStyle cellStyle0 = workbook.createCellStyle();
        // 生成首行标题字体
        HSSFFont fontStyle0 = workbook.createFont();
        fontStyle0.setFontName("宋体");
        // 字体颜色 黑
        fontStyle0.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // 字体大小
        fontStyle0.setFontHeightInPoints((short) 24);
        // 粗体显示
        fontStyle0.setBold(true);
        // 把字体应用到当前的样式
        cellStyle0.setFont(fontStyle0);
        // 背景颜色
        cellStyle0.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        //填充单元格
        cellStyle0.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //自定义RGB
        HSSFPalette customPalette1 = workbook.getCustomPalette();
        customPalette1.setColorAtIndex(IndexedColors.AQUA.getIndex(), (byte) 169, (byte) 208, (byte) 142);
        // 垂直居中
        cellStyle0.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle0.setAlignment(HorizontalAlignment.CENTER);
        return cellStyle0;
    }

    /**
     * 生成”总计“样式
     *
     * @return
     */
    public static HSSFCellStyle getCellStyle1(HSSFWorkbook workbook) {
        HSSFCellStyle cellStyle1 = workbook.createCellStyle();
        HSSFFont fontStyle1 = workbook.createFont();
        fontStyle1.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        fontStyle1.setFontName("宋体");
        fontStyle1.setBold(true);
        fontStyle1.setFontHeightInPoints((short) 11);
        cellStyle1.setFont(fontStyle1);
        cellStyle1.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
        cellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFPalette customPalette2 = workbook.getCustomPalette();
        customPalette2.setColorAtIndex(IndexedColors.LAVENDER.getIndex(), (byte) 252, (byte) 228, (byte) 214);
        cellStyle1.setAlignment(HorizontalAlignment.CENTER);
        cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle1;
    }
}
