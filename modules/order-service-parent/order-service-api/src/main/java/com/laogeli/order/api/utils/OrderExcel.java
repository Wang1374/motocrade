package com.laogeli.order.api.utils;

import com.laogeli.order.api.vo.OrderVo;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class OrderExcel {

    public static void getExcel(HSSFWorkbook workbook, List<OrderVo> OrderExportList) {
        HSSFSheet sheet = workbook.createSheet();
        // 设置表格列宽度
        sheet.setColumnWidth(0, 5 * 256);
        sheet.setColumnWidth(1, 20 * 256);
        sheet.setColumnWidth(2, 10 * 256);
        sheet.setColumnWidth(3, 21 * 256);
        sheet.setColumnWidth(4, 15 * 256);
        sheet.setColumnWidth(5, 15 * 256);
        sheet.setColumnWidth(6, 20 * 256);
        sheet.setColumnWidth(7, 20 * 256);
        sheet.setColumnWidth(8, 15 * 256);
        sheet.setColumnWidth(9, 20 * 256);
        sheet.setColumnWidth(10, 10 * 256);
        sheet.setColumnWidth(11, 15 * 256);
        sheet.setColumnWidth(12, 20 * 256);
        sheet.setColumnWidth(13, 11 * 256);
        sheet.setColumnWidth(14, 11 * 256);

        Row row1 = sheet.createRow(0);
        row1.setHeightInPoints(35);

        //样式
        HSSFCellStyle cellStyle1 = workbook.createCellStyle();
        HSSFFont fontStyle1 = workbook.createFont();
        fontStyle1.setFontName("宋体");
        fontStyle1.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        fontStyle1.setBold(true);
        fontStyle1.setFontHeightInPoints((short) 11);
        cellStyle1.setFont(fontStyle1);
        cellStyle1.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
        cellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle1.setAlignment(HorizontalAlignment.CENTER);
        cellStyle1.setBorderBottom(BorderStyle.THIN);
        cellStyle1.setBorderLeft(BorderStyle.THIN);
        cellStyle1.setBorderTop(BorderStyle.THIN);
        cellStyle1.setBorderRight(BorderStyle.THIN);

        Cell cellh1 = row1.createCell(0);
        cellh1.setCellValue("序号");
        cellh1.setCellStyle(cellStyle1);

        Cell cellh2 = row1.createCell(1);
        cellh2.setCellValue("业务编号");
        cellh2.setCellStyle(cellStyle1);

        Cell cellh3 = row1.createCell(2);
        cellh3.setCellValue("业务类型");
        cellh3.setCellStyle(cellStyle1);

        Cell cellh4 = row1.createCell(3);
        cellh4.setCellValue("客户名称");
        cellh4.setCellStyle(cellStyle1);

        Cell cellh5 = row1.createCell(4);
        cellh5.setCellValue("客户编号");
        cellh5.setCellStyle(cellStyle1);

        Cell cellh6 = row1.createCell(5);
        cellh6.setCellValue("联系人");
        cellh6.setCellStyle(cellStyle1);

        Cell cellh7 = row1.createCell(6);
        cellh7.setCellValue("提单号");
        cellh7.setCellStyle(cellStyle1);

        Cell cellh8 = row1.createCell(7);
        cellh8.setCellValue("船名航次");
        cellh8.setCellStyle(cellStyle1);

        Cell cellh9 = row1.createCell(8);
        cellh9.setCellValue("停靠码头");
        cellh9.setCellStyle(cellStyle1);

        Cell cellh10 = row1.createCell(9);
        cellh10.setCellValue("接单日期");
        cellh10.setCellStyle(cellStyle1);

        Cell cellh11 = row1.createCell(10);
        cellh11.setCellValue("箱型");
        cellh11.setCellStyle(cellStyle1);

        Cell cellh12 = row1.createCell(11);
        cellh12.setCellValue("箱号");
        cellh12.setCellStyle(cellStyle1);

        Cell cellh13 = row1.createCell(12);
        cellh13.setCellValue("做箱日期");
        cellh13.setCellStyle(cellStyle1);

        Cell cellh14 = row1.createCell(13);
        cellh14.setCellValue("做箱门点");
        cellh14.setCellStyle(cellStyle1);

        Cell cellh15 = row1.createCell(14);
        cellh15.setCellValue("提/还箱点");
        cellh15.setCellStyle(cellStyle1);

        // 第2行之后样式
        HSSFCellStyle cellStyle2 = workbook.createCellStyle();
        HSSFFont fontStyle2 = workbook.createFont();
        fontStyle2.setFontName("宋体");
        fontStyle2.setFontHeightInPoints((short) 11);
        cellStyle2.setFont(fontStyle2);
        cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle2.setAlignment(HorizontalAlignment.CENTER);
        cellStyle2.setBorderBottom(BorderStyle.THIN);
        cellStyle2.setBorderLeft(BorderStyle.THIN);
        cellStyle2.setBorderRight(BorderStyle.THIN);
        // 自动换行
        cellStyle2.setWrapText(true);

        // 根据订单编号排序
        OrderExportList = OrderExportList.stream().sorted(Comparator.comparing(OrderVo::getOrderNumber))
                .collect(Collectors.toList());

        int startRow = 1;
        int countIndex = 0;
        for (int i = 0; i < OrderExportList.size(); i++) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            OrderVo orderExport = OrderExportList.get(i);

            Row row = sheet.createRow(i + 1);
            row.setHeightInPoints(30);

            Cell cell1 = row.createCell(0);
            cell1.setCellValue(i + 1);
            cell1.setCellStyle(cellStyle2);

            Cell cell2 = row.createCell(1);
            cell2.setCellValue(orderExport.getOrderNumber());
            cell2.setCellStyle(cellStyle2);
            if (i > 0) {
                if (orderExport.getOrderNumber().equals(OrderExportList.get(i - 1).getOrderNumber())) {
                    countIndex++;
                } else if(countIndex!=0){
                    //合并单元格
                    mergeCell(startRow,countIndex,sheet);
                    startRow = startRow + countIndex + 1;
                    countIndex = 0;
                }else {
                    startRow = startRow+1;
                }
            }
            if(i==OrderExportList.size()-1 && countIndex>0){
                //合并单元格
                mergeCell(startRow,countIndex,sheet);
            }
            Cell cell3 = row.createCell(2);
            cell3.setCellValue(orderExport.getOrderType() == 1 ? "海运进口" : (orderExport.getOrderType() == 2 ? "海运出口" : ""));
            cell3.setCellStyle(cellStyle2);

            Cell cell4 = row.createCell(3);
            cell4.setCellValue(orderExport.getCustomerName());
            cell4.setCellStyle(cellStyle2);

            Cell cell5 = row.createCell(4);
            cell5.setCellValue(orderExport.getCustomerNum());
            cell5.setCellStyle(cellStyle2);

            Cell cell6 = row.createCell(5);
            cell6.setCellValue(orderExport.getContacts());
            cell6.setCellStyle(cellStyle2);

            Cell cell7 = row.createCell(6);
            cell7.setCellValue(orderExport.getBlNos());
            cell7.setCellStyle(cellStyle2);

            Cell cell8 = row.createCell(7);
            cell8.setCellValue(orderExport.getVesselAndVoyage());
            cell8.setCellStyle(cellStyle2);

            Cell cell9 = row.createCell(8);
            cell9.setCellValue(orderExport.getDock());
            cell9.setCellStyle(cellStyle2);

            Cell cell10 = row.createCell(9);
            cell10.setCellValue(orderExport.getOrderTime() == null ? "" : sdf.format(orderExport.getOrderTime()));
            cell10.setCellStyle(cellStyle2);

            Cell cell11 = row.createCell(10);
            cell11.setCellValue(orderExport.getBoxPile());
            cell11.setCellStyle(cellStyle2);

            Cell cell12 = row.createCell(11);
            cell12.setCellValue(orderExport.getCaseNumber());
            cell12.setCellStyle(cellStyle2);

            Cell cell13 = row.createCell(12);
            cell13.setCellValue(orderExport.getPackingTime() == null ? "" : sdf.format(orderExport.getPackingTime()));
            cell13.setCellStyle(cellStyle2);

            Cell cell14 = row.createCell(13);
            cell14.setCellValue(orderExport.getDoor());
            cell14.setCellStyle(cellStyle2);

            Cell cell15 = row.createCell(14);
            cell15.setCellValue(orderExport.getOrderType()==1 ?orderExport.getSuitcasePoint():(orderExport.getOrderType()==2?orderExport.getReturnPoint():""));
            cell15.setCellStyle(cellStyle2);

        }
        // 打印页面设置
        HSSFPrintSetup printSetup = sheet.getPrintSetup();
        // 打印方向，true：横向，false：纵向(默认)
        printSetup.setLandscape(true);
        // A4纸
        printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);

        printSetup.setFitWidth((short)1);
        printSetup.setScale((short)64);

        // 将整个工作表打印在一页
       // sheet.setAutobreaks(true);

        // 上边距
        sheet.setMargin(HSSFSheet.TopMargin, (double) 0.2);
        // 下边距
        sheet.setMargin(HSSFSheet.BottomMargin, (double) 0.2);
        // 左边距
        sheet.setMargin(HSSFSheet.LeftMargin, (double) 0.2);
        // 右边距
        sheet.setMargin(HSSFSheet.RightMargin, (double) 0.2);
    }

    /**
     * 合并单元格
     * @param startRow
     * @param countIndex
     * @param sheet
     */
    public static void mergeCell(Integer startRow, Integer countIndex,HSSFSheet sheet){
        CellRangeAddress cra1 = new CellRangeAddress(startRow, startRow + countIndex, 1, 1);
        sheet.addMergedRegion(cra1);
        CellRangeAddress cra2 = new CellRangeAddress(startRow, startRow + countIndex, 2, 2);
        sheet.addMergedRegion(cra2);

        CellRangeAddress cra3 = new CellRangeAddress(startRow, startRow + countIndex, 3, 3);
        sheet.addMergedRegion(cra3);

        CellRangeAddress cra4= new CellRangeAddress(startRow, startRow + countIndex, 4, 4);
        sheet.addMergedRegion(cra4);

        CellRangeAddress cra5 = new CellRangeAddress(startRow, startRow + countIndex, 5, 5);
        sheet.addMergedRegion(cra5);

        CellRangeAddress cra6 = new CellRangeAddress(startRow, startRow + countIndex, 6, 6);
        sheet.addMergedRegion(cra6);

        CellRangeAddress cra7 = new CellRangeAddress(startRow, startRow + countIndex, 7, 7);
        sheet.addMergedRegion(cra7);

        CellRangeAddress cra8 = new CellRangeAddress(startRow, startRow + countIndex, 8, 8);
        sheet.addMergedRegion(cra8);
        CellRangeAddress cra9 = new CellRangeAddress(startRow, startRow + countIndex, 9, 9);
        sheet.addMergedRegion(cra9);
    }
}
