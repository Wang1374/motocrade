package com.laogeli.order.util;

import com.laogeli.common.core.utils.DateUtils;
import com.laogeli.order.api.vo.CostVo;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class PackExcel {
    public static void getExcel1(HSSFWorkbook workbook, List<CostVo> costVoList) {
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet();

        // 锁定sheet
        // sheet.protectSheet("jxwl");

        // 设置表格列宽度
        sheet.setColumnWidth(0, 5 * 256);
        sheet.setColumnWidth(1, 20 * 256);
        sheet.setColumnWidth(2, 10 * 256);
        sheet.setColumnWidth(3, 21 * 256);
        sheet.setColumnWidth(4, 23 * 256);
        sheet.setColumnWidth(5, 15 * 256);
        sheet.setColumnWidth(6, 13 * 256);
        sheet.setColumnWidth(7, 9 * 256);
        sheet.setColumnWidth(8, 15 * 256);
        sheet.setColumnWidth(9, 15 * 256);
        sheet.setColumnWidth(10, 11 * 256);
        sheet.setColumnWidth(11, 15 * 256);

        sheet.setColumnWidth(12, 30 * 256);
        sheet.setColumnWidth(13, 20 * 256);

        sheet.setColumnWidth(14, 30 * 256);
        sheet.setColumnWidth(15, 12 * 256);
        sheet.setColumnWidth(16, 11 * 256);
        sheet.setColumnWidth(17, 11 * 256);
        sheet.setColumnWidth(18, 20 * 256);
        sheet.setColumnWidth(19,20 * 256);
        sheet.setColumnWidth(20,20*256);

        // 第一行
        Row row0 = sheet.createRow(0);
        // 设置行高
        row0.setHeightInPoints(95);

        Cell cellA1 = row0.createCell(0);
        cellA1.setCellValue("多票费用明细");
        // cellA1.setCellStyle(cellStyle0);
        cellA1.setCellStyle(PoiUtil.getCellStyle0(workbook));
        // 合并单元格
        CellRangeAddress cra0 = new CellRangeAddress(0, 0, 0, 20);
        sheet.addMergedRegion(cra0);
        // 使用RegionUtil类为合并后的单元格添加边框
        PoiUtil.setRegionBorder(1, cra0, sheet);

        // 第二行
        Row row1 = sheet.createRow(1);
        row1.setHeightInPoints(25);

        Cell cellA2 = row1.createCell(0);
        cellA2.setCellValue("总计");
        cellA2.setCellStyle(PoiUtil.getCellStyle1(workbook));
        CellRangeAddress cra1 = new CellRangeAddress(1, 2, 0, 2);
        sheet.addMergedRegion(cra1);
        PoiUtil.setRegionBorder(1, cra1, sheet);

        // 生成”应收、应付、其他应收、其他应付“样式
        HSSFCellStyle cellStyle2 = workbook.createCellStyle();
        HSSFFont fontStyle2 = workbook.createFont();
        fontStyle2.setFontName("宋体");
        fontStyle2.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        fontStyle2.setFontHeightInPoints((short) 11);
        fontStyle2.setBold(true);
        cellStyle2.setFont(fontStyle2);
        cellStyle2.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
        cellStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFPalette customPalette3 = workbook.getCustomPalette();
        customPalette3.setColorAtIndex(IndexedColors.LIGHT_TURQUOISE.getIndex(), (byte) 221, (byte) 235, (byte) 247);
        cellStyle2.setAlignment(HorizontalAlignment.CENTER);
        cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);

        // 第二行 应收 标题
        Cell cellD2 = row1.createCell(3);
        cellD2.setCellValue("应收");
        cellD2.setCellStyle(cellStyle2);
        CellRangeAddress cra2 = new CellRangeAddress(1, 1, 3, 5);
        sheet.addMergedRegion(cra2);
        PoiUtil.setRegionBorder(1, cra2, sheet);

        // 第二行 应付 标题
        Cell cellG2 = row1.createCell(6);
        cellG2.setCellValue("应付");
        cellG2.setCellStyle(cellStyle2);
        CellRangeAddress cra3 = new CellRangeAddress(1, 1, 6, 9);
        sheet.addMergedRegion(cra3);
        PoiUtil.setRegionBorder(1, cra3, sheet);

        // 第二行 其他应收 标题
        Cell cellJ2 = row1.createCell(10);
        cellJ2.setCellValue("其他应收");
        cellJ2.setCellStyle(cellStyle2);
        CellRangeAddress cra4 = new CellRangeAddress(1, 1, 10, 12);
        sheet.addMergedRegion(cra4);
        PoiUtil.setRegionBorder(1, cra4, sheet);

        // 第二行 其他应付 标题
        Cell cellN2 = row1.createCell(13);
        cellN2.setCellValue("其他应付");
        cellN2.setCellStyle(cellStyle2);
        CellRangeAddress cra5 = new CellRangeAddress(1, 1, 13, 20);
        sheet.addMergedRegion(cra5);
        PoiUtil.setRegionBorder(1, cra5, sheet);

        // 第三行
        Row row2 = sheet.createRow(2);
        row2.setHeightInPoints(30);

        // 第三行 样式
        HSSFCellStyle cellStyle3 = workbook.createCellStyle();
        HSSFFont fontStyle3 = workbook.createFont();
        fontStyle3.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        fontStyle3.setFontHeightInPoints((short) 11);
        cellStyle3.setFont(fontStyle3);
        cellStyle3.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle3.setAlignment(HorizontalAlignment.CENTER);

        Cell cellD3 = row2.createCell(3);
        cellD3.setCellValue(costVoList.get(0).getAchieve().doubleValue());
        cellD3.setCellStyle(cellStyle3);
        CellRangeAddress cra6 = new CellRangeAddress(2, 2, 3, 5);
        sheet.addMergedRegion(cra6);
        PoiUtil.setRegionBorder(1, cra6, sheet);

        Cell cellG3 = row2.createCell(6);
        cellG3.setCellValue(costVoList.get(0).getPay().doubleValue());
        cellG3.setCellStyle(cellStyle3);
        CellRangeAddress cra7 = new CellRangeAddress(2, 2, 6, 9);
        sheet.addMergedRegion(cra7);
        PoiUtil.setRegionBorder(1, cra7, sheet);

        Cell cellJ3 = row2.createCell(10);
        cellJ3.setCellValue(costVoList.get(0).getExtraAchieve().doubleValue());
        cellJ3.setCellStyle(cellStyle3);
        CellRangeAddress cra8 = new CellRangeAddress(2, 2, 10, 12);
        sheet.addMergedRegion(cra8);
        PoiUtil.setRegionBorder(1, cra8, sheet);

        Cell cellN3 = row2.createCell(13);
        cellN3.setCellValue(costVoList.get(0).getExtraPay().doubleValue());
        cellN3.setCellStyle(cellStyle3);
        CellRangeAddress cra9 = new CellRangeAddress(2, 2, 13, 20);
        sheet.addMergedRegion(cra9);
        PoiUtil.setRegionBorder(1, cra9, sheet);

        // 第四行
        Row row3 = sheet.createRow(3);
        row3.setHeightInPoints(25);

        // 第四行样式
        HSSFCellStyle cellStyle4 = workbook.createCellStyle();
        HSSFFont fontStyle4 = workbook.createFont();
        fontStyle4.setFontName("楷体");
        fontStyle4.setColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
        fontStyle4.setFontHeightInPoints((short) 17);
        fontStyle4.setBold(true);
        cellStyle4.setFont(fontStyle4);
        cellStyle4.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle4.setAlignment(HorizontalAlignment.CENTER);

        Cell cellA4 = row3.createCell(0);
        cellA4.setCellValue("明细");
        cellA4.setCellStyle(cellStyle4);
        CellRangeAddress cra10 = new CellRangeAddress(3, 3, 0, 20);
        sheet.addMergedRegion(cra10);
        PoiUtil.setRegionBorder(1, cra10, sheet);

        // 第五行
        Row row4 = sheet.createRow(4);
        row4.setHeightInPoints(35);

        // 第五行样式
        HSSFCellStyle cellStyle5 = workbook.createCellStyle();
        HSSFFont fontStyle5 = workbook.createFont();
        fontStyle5.setFontName("宋体");
        fontStyle5.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        fontStyle5.setBold(true);
        fontStyle5.setFontHeightInPoints((short) 11);
        cellStyle5.setFont(fontStyle5);
        cellStyle5.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
        cellStyle5.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle5.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle5.setAlignment(HorizontalAlignment.CENTER);
        cellStyle5.setBorderBottom(BorderStyle.THIN);
        cellStyle5.setBorderLeft(BorderStyle.THIN);
        cellStyle5.setBorderTop(BorderStyle.THIN);
        cellStyle5.setBorderRight(BorderStyle.THIN);

        Cell cellA5 = row4.createCell(0);
        cellA5.setCellValue("序号");
        cellA5.setCellStyle(cellStyle5);

        Cell cellB5 = row4.createCell(1);
        cellB5.setCellValue("订单编号");
        cellB5.setCellStyle(cellStyle5);

        Cell cellC5 = row4.createCell(2);
        cellC5.setCellValue("业务类型");
        cellC5.setCellStyle(cellStyle5);

        Cell cellD5 = row4.createCell(3);
        cellD5.setCellValue("船名航次");
        cellD5.setCellStyle(cellStyle5);

        Cell cellE5 = row4.createCell(4);
        cellE5.setCellValue("提单号");
        cellE5.setCellStyle(cellStyle5);

        Cell cellF5 = row4.createCell(5);
        cellF5.setCellValue("提/还箱点");
        cellF5.setCellStyle(cellStyle5);

        Cell cellG5 = row4.createCell(6);
        cellG5.setCellValue("停靠码头");
        cellG5.setCellStyle(cellStyle5);

        Cell cellH5 = row4.createCell(7);
        cellH5.setCellValue("箱型");
        cellH5.setCellStyle(cellStyle5);

        Cell cellI5 = row4.createCell(8);
        cellI5.setCellValue("箱号");
        cellI5.setCellStyle(cellStyle5);

        Cell cellJ5 = row4.createCell(9);
        cellJ5.setCellValue("做箱时间");
        cellJ5.setCellStyle(cellStyle5);

        Cell cellK5 = row4.createCell(10);
        cellK5.setCellValue("车辆");
        cellK5.setCellStyle(cellStyle5);

        Cell cellL5 = row4.createCell(11);
        cellL5.setCellValue("门点");
        cellL5.setCellStyle(cellStyle5);

        //具体地址
        Cell cellM5m = row4.createCell(12);
        cellM5m.setCellValue("具体地址");
        cellM5m.setCellStyle(cellStyle5);

        Cell cellM5n = row4.createCell(13);
        cellM5n.setCellValue("联系方式");
        cellM5n.setCellStyle(cellStyle5);


        Cell cellM5 = row4.createCell(14);
        cellM5.setCellValue("往来单位");
        cellM5.setCellStyle(cellStyle5);

        Cell cellN5 = row4.createCell(15);
        cellN5.setCellValue("费用名称");
        cellN5.setCellStyle(cellStyle5);

        Cell cellO5 = row4.createCell(16);
        cellO5.setCellValue("属性");
        cellO5.setCellStyle(cellStyle5);

        Cell cellP5 = row4.createCell(17);
        cellP5.setCellValue("金额");
        cellP5.setCellStyle(cellStyle5);

        Cell cellQ5 = row4.createCell(18);
        cellQ5.setCellValue("备注");
        cellQ5.setCellStyle(cellStyle5);

        Cell cellR5 = row4.createCell(19);
        cellR5.setCellValue("客户编号");
        cellR5.setCellStyle(cellStyle5);

        Cell cellR6 = row4.createCell(20);
        cellR6.setCellValue("付款人");
        cellR6.setCellStyle(cellStyle5);

        // 第六行之后样式
        HSSFCellStyle cellStyle6 = workbook.createCellStyle();
        HSSFFont fontStyle6 = workbook.createFont();
        fontStyle6.setFontName("宋体");
        fontStyle6.setFontHeightInPoints((short) 11);
        cellStyle6.setFont(fontStyle6);
        cellStyle6.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle6.setAlignment(HorizontalAlignment.CENTER);
        cellStyle6.setBorderBottom(BorderStyle.THIN);
        cellStyle6.setBorderLeft(BorderStyle.THIN);
        cellStyle6.setBorderRight(BorderStyle.THIN);
        // 自动换行
        cellStyle6.setWrapText(true);

        // 根据订单编号、箱号、往来单位排序
        costVoList = costVoList.stream().sorted(Comparator.comparing(CostVo::getOrderNumber)
                .thenComparing(Comparator.comparing(CostVo::getCaseNumber).reversed().thenComparing(Comparator.comparing(CostVo::getBtype).reversed())))
                .collect(Collectors.toList());

        HSSFCellStyle style;
        HSSFCellStyle style1 = PoiUtil.getCellStyle(workbook, IndexedColors.TAN.getIndex());
        HSSFCellStyle style2 = PoiUtil.getCellStyle(workbook, IndexedColors.PALE_BLUE.getIndex());

        // 从excel的第六行开始
        int index = 5;
        int count = 0;
        for (int i = 0; i < costVoList.size(); i++) {
            CostVo costVo = costVoList.get(i);

            // 如果这一次与上一次不同 设置红色，后面都为红色  再次不同时，转为蓝色
            if (i != 0) {
                if (!costVo.getOrderNumber().equals(costVoList.get(i - 1).getOrderNumber())) {
                    count++;
                }
            }
            if (count % 2 == 0) {
                style = style1;
            } else {
                style = style2;
            }

            Row row = sheet.createRow(i + index);
            row.setHeightInPoints(30);

            Cell cell0 = row.createCell(0);
            cell0.setCellValue(i + 1);
            cell0.setCellStyle(style);

            Cell cell1 = row.createCell(1);
            cell1.setCellValue(costVo.getOrderNumber());
            cell1.setCellStyle(style);

            Cell cell2 = row.createCell(2);
            cell2.setCellValue(costVo.getOrderType() == 1 ? "海运出口" : "海运进口");
            cell2.setCellStyle(style);

            Cell cell3 = row.createCell(3);
            cell3.setCellValue(costVo.getVesselAndVoyage());
            cell3.setCellStyle(style);

            Cell cell4 = row.createCell(4);
            cell4.setCellValue(costVo.getBlNos());
            cell4.setCellStyle(style);

            Cell cell5 = row.createCell(5);
            cell5.setCellValue(costVo.getPrPoint());
            cell5.setCellStyle(cellStyle6);

            Cell cell6 = row.createCell(6);
            cell6.setCellValue(costVo.getDock());
            cell6.setCellStyle(cellStyle6);

            Cell cell7 = row.createCell(7);
            cell7.setCellValue(costVo.getBoxPile());
            cell7.setCellStyle(cellStyle6);

            Cell cell8 = row.createCell(8);
            cell8.setCellValue(costVo.getCaseNumber());
            cell8.setCellStyle(cellStyle6);

            Cell cell9 = row.createCell(9);
            cell9.setCellValue(DateUtils.dateToString(costVo.getPackingTime()));
            cell9.setCellStyle(cellStyle6);

            Cell cell10 = row.createCell(10);
            cell10.setCellValue(costVo.getVehicle());
            cell10.setCellStyle(cellStyle6);

            Cell cell11 = row.createCell(11);
            cell11.setCellValue(costVo.getDoor());
            cell11.setCellStyle(cellStyle6);

            //具体地址
            Cell cell12m = row.createCell(12);
            cell12m.setCellValue(costVo.getAddress());
            cell12m.setCellStyle(cellStyle6);

            Cell cell12n = row.createCell(13);
            cell12n.setCellValue(costVo.getContactNumber());
            cell12n.setCellStyle(cellStyle6);


            Cell cell12 = row.createCell(14);
            cell12.setCellValue(costVo.getBtype());
            cell12.setCellStyle(cellStyle6);

            Cell cell13 = row.createCell(15);
            cell13.setCellValue(costVo.getPayItems());
            cell13.setCellStyle(cellStyle6);

            Cell cell14 = row.createCell(16);
            cell14.setCellValue(costVo.getCostTypes() == 1 ? "应收费用" : (costVo.getCostTypes() == 2 ? "应付费用" : (costVo.getCostTypes() == 3 ? "其他应收费用" : "其他应付费用")));
            cell14.setCellStyle(cellStyle6);

            Cell cell15 = row.createCell(17);
            cell15.setCellValue(costVo.getTotalPrice().doubleValue());
            cell15.setCellStyle(cellStyle6);

            Cell cell16 = row.createCell(18);
            cell16.setCellValue(costVo.getRemark());
            cell16.setCellStyle(cellStyle6);


            Cell cell17 = row.createCell(19);
            cell17.setCellValue(costVo.getCustomerNum());
            cell17.setCellStyle(cellStyle6);

            Cell cell18 = row.createCell(20);
            cell18.setCellValue(costVo.getPayer());
            cell18.setCellStyle(cellStyle6);
        }

        // 打印页面设置
        HSSFPrintSetup printSetup = sheet.getPrintSetup();
        // 打印方向，true：横向，false：纵向(默认)
        printSetup.setLandscape(true);
        // A4纸
        printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // 将整个工作表打印在一页
        sheet.setAutobreaks(true);
        // 上边距
        sheet.setMargin(HSSFSheet.TopMargin, (double) 0.2);
        // 下边距
        sheet.setMargin(HSSFSheet.BottomMargin, (double) 0.2);
        // 左边距
        sheet.setMargin(HSSFSheet.LeftMargin, (double) 0.2);
        // 右边距
        sheet.setMargin(HSSFSheet.RightMargin, (double) 0.2);
    }



    public static void getExcel3(HSSFWorkbook workbook) {
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet();

        // 锁定sheet
        // sheet.protectSheet("jxwl");

        // 设置表格列宽度
        sheet.setColumnWidth(0, 20 * 256);
        sheet.setColumnWidth(1, 20 * 256);
        sheet.setColumnWidth(2, 10 * 256);
        sheet.setColumnWidth(3, 21 * 256);
        sheet.setColumnWidth(4, 23 * 256);
        sheet.setColumnWidth(5,23*256);
        sheet.setColumnWidth(6, 15 * 256);


        // 第一行
        Row row0 = sheet.createRow(0);
        row0.setHeightInPoints(20);

        // 第一行样式
        HSSFCellStyle cellStyle5 = workbook.createCellStyle();
        HSSFFont fontStyle5 = workbook.createFont();
        fontStyle5.setFontName("宋体");
        fontStyle5.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        fontStyle5.setBold(true);
        fontStyle5.setFontHeightInPoints((short) 11);
        cellStyle5.setFont(fontStyle5);
        cellStyle5.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle5.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle5.setAlignment(HorizontalAlignment.CENTER);
        cellStyle5.setBorderBottom(BorderStyle.THIN);
        cellStyle5.setBorderLeft(BorderStyle.THIN);
        cellStyle5.setBorderTop(BorderStyle.THIN);
        cellStyle5.setBorderRight(BorderStyle.THIN);

        Cell cellA5 = row0.createCell(0);
        cellA5.setCellValue("发票号");
        cellA5.setCellStyle(cellStyle5);
        cellA5.setCellType(CellType.STRING);

        Cell cellB5 = row0.createCell(1);
        cellB5.setCellValue("日期");
        cellB5.setCellStyle(cellStyle5);

        Cell cellC5 = row0.createCell(2);
        cellC5.setCellValue("时间");
        cellC5.setCellStyle(cellStyle5);

        Cell cellD5 = row0.createCell(3);
        cellD5.setCellValue("车牌号");
        cellD5.setCellStyle(cellStyle5);

        Cell cellE5 = row0.createCell(4);
        cellE5.setCellValue("单价");
        cellE5.setCellStyle(cellStyle5);

        Cell cellF5n= row0.createCell(5);
        cellF5n.setCellValue("数量");
        cellF5n.setCellStyle(cellStyle5);

        Cell cellF5 = row0.createCell(6);
        cellF5.setCellValue("金额");
        cellF5.setCellStyle(cellStyle5);


        // 第六行之后样式
        HSSFCellStyle cellStyle6 = workbook.createCellStyle();
        HSSFFont fontStyle6 = workbook.createFont();
        fontStyle6.setFontName("宋体");
        fontStyle6.setFontHeightInPoints((short) 11);
        cellStyle6.setFont(fontStyle6);
        cellStyle6.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle6.setAlignment(HorizontalAlignment.CENTER);
        cellStyle6.setBorderBottom(BorderStyle.THIN);
        cellStyle6.setBorderLeft(BorderStyle.THIN);
        cellStyle6.setBorderRight(BorderStyle.THIN);
        // 自动换行
        cellStyle6.setWrapText(true);


        // 打印页面设置
        HSSFPrintSetup printSetup = sheet.getPrintSetup();
        // 打印方向，true：横向，false：纵向(默认)
        printSetup.setLandscape(true);
        // A4纸
        printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // 将整个工作表打印在一页
        sheet.setAutobreaks(true);
        // 上边距
        sheet.setMargin(HSSFSheet.TopMargin, (double) 0.2);
        // 下边距
        sheet.setMargin(HSSFSheet.BottomMargin, (double) 0.2);
        // 左边距
        sheet.setMargin(HSSFSheet.LeftMargin, (double) 0.2);
        // 右边距
        sheet.setMargin(HSSFSheet.RightMargin, (double) 0.2);
    }


    public static void getExcel2(HSSFWorkbook workbook, List<CostVo> costVoList) {

        HSSFSheet sheet = workbook.createSheet();

        sheet.setColumnWidth(0, 5 * 256);
        sheet.setColumnWidth(1, 20 * 256);
        sheet.setColumnWidth(2, 10 * 256);
        sheet.setColumnWidth(3, 21 * 256);
        sheet.setColumnWidth(4, 23 * 256);
        sheet.setColumnWidth(5, 15 * 256);
        sheet.setColumnWidth(6, 13 * 256);
        sheet.setColumnWidth(7, 9 * 256);
        sheet.setColumnWidth(8, 15 * 256);
        sheet.setColumnWidth(9, 15 * 256);
        sheet.setColumnWidth(10, 11 * 256);
        sheet.setColumnWidth(11, 15 * 256);
        sheet.setColumnWidth(12, 30 * 256);
        sheet.setColumnWidth(14, 30 * 256);
        sheet.setColumnWidth(13, 20 * 256);
        sheet.setColumnWidth(15, 13 * 256);
        sheet.setColumnWidth(17, 11 * 256);
        sheet.setColumnWidth(19, 13 * 256);
        sheet.setColumnWidth(21, 35 * 256);
        sheet.setColumnWidth(24,20*256);

        Row row0 = sheet.createRow(0);
        row0.setHeightInPoints(95);

        Cell cellA1 = row0.createCell(0);
        cellA1.setCellValue("多票费用明细");
        cellA1.setCellStyle(PoiUtil.getCellStyle0(workbook));
        CellRangeAddress cra0 = new CellRangeAddress(0, 0, 0, 24);
        sheet.addMergedRegion(cra0);
        PoiUtil.setRegionBorder(1, cra0, sheet);

        Row row1 = sheet.createRow(1);
        row1.setHeightInPoints(25);

        Cell cellA2 = row1.createCell(0);
        cellA2.setCellValue("总计");
        cellA2.setCellStyle(PoiUtil.getCellStyle1(workbook));
        CellRangeAddress cra1 = new CellRangeAddress(1, 2, 0, 2);
        sheet.addMergedRegion(cra1);
        PoiUtil.setRegionBorder(1, cra1, sheet);

        // 生成”应收、应付、其他应收、其他应付“样式
        HSSFCellStyle cellStyle2 = workbook.createCellStyle();
        HSSFFont fontStyle2 = workbook.createFont();
        fontStyle2.setFontName("宋体");
        fontStyle2.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        fontStyle2.setFontHeightInPoints((short) 11);
        fontStyle2.setBold(true);
        cellStyle2.setFont(fontStyle2);
        cellStyle2.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
        cellStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFPalette customPalette3 = workbook.getCustomPalette();
        customPalette3.setColorAtIndex(IndexedColors.LIGHT_TURQUOISE.getIndex(), (byte) 221, (byte) 235, (byte) 247);
        cellStyle2.setAlignment(HorizontalAlignment.CENTER);
        cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);

        // 第二行 应收 标题
        Cell cellD2 = row1.createCell(3);
        cellD2.setCellValue("应收");
        cellD2.setCellStyle(cellStyle2);
        CellRangeAddress cra2 = new CellRangeAddress(1, 1, 3, 7);
        sheet.addMergedRegion(cra2);
        PoiUtil.setRegionBorder(1, cra2, sheet);

        // 第二行 应付 标题
        Cell cellG2 = row1.createCell(8);
        cellG2.setCellValue("应付");
        cellG2.setCellStyle(cellStyle2);
        CellRangeAddress cra3 = new CellRangeAddress(1, 1, 8, 12);
        sheet.addMergedRegion(cra3);
        PoiUtil.setRegionBorder(1, cra3, sheet);

        // 第二行 其他应收 标题
        Cell cellJ2 = row1.createCell(13);
        cellJ2.setCellValue("其他应收");
        cellJ2.setCellStyle(cellStyle2);
        CellRangeAddress cra4 = new CellRangeAddress(1, 1, 13, 16);
        sheet.addMergedRegion(cra4);
        PoiUtil.setRegionBorder(1, cra4, sheet);

        // 第二行 其他应付 标题
        Cell cellN2 = row1.createCell(17);
        cellN2.setCellValue("其他应付");
        cellN2.setCellStyle(cellStyle2);
        CellRangeAddress cra5 = new CellRangeAddress(1, 1, 17, 24);
        sheet.addMergedRegion(cra5);
        PoiUtil.setRegionBorder(1, cra5, sheet);

        // 第三行
        Row row2 = sheet.createRow(2);
        row2.setHeightInPoints(30);

        // 第三行 样式
        HSSFCellStyle cellStyle3 = workbook.createCellStyle();
        HSSFFont fontStyle3 = workbook.createFont();
        fontStyle3.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        fontStyle3.setFontHeightInPoints((short) 11);
        cellStyle3.setFont(fontStyle3);
        cellStyle3.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle3.setAlignment(HorizontalAlignment.CENTER);

        Cell cellD3 = row2.createCell(3);
        cellD3.setCellValue(costVoList.get(0).getAchieve().doubleValue());
        cellD3.setCellStyle(cellStyle3);
        CellRangeAddress cra6 = new CellRangeAddress(2, 2, 3, 7);
        sheet.addMergedRegion(cra6);
        PoiUtil.setRegionBorder(1, cra6, sheet);

        Cell cellG3 = row2.createCell(8);
        cellG3.setCellValue(costVoList.get(0).getPay().doubleValue());
        cellG3.setCellStyle(cellStyle3);
        CellRangeAddress cra7 = new CellRangeAddress(2, 2, 8, 12);
        sheet.addMergedRegion(cra7);
        PoiUtil.setRegionBorder(1, cra7, sheet);

        Cell cellJ3 = row2.createCell(13);
        cellJ3.setCellValue(costVoList.get(0).getExtraAchieve().doubleValue());
        cellJ3.setCellStyle(cellStyle3);
        CellRangeAddress cra8 = new CellRangeAddress(2, 2, 13, 16);
        sheet.addMergedRegion(cra8);
        PoiUtil.setRegionBorder(1, cra8, sheet);

        Cell cellN3 = row2.createCell(17);
        cellN3.setCellValue(costVoList.get(0).getExtraPay().doubleValue());
        cellN3.setCellStyle(cellStyle3);
        CellRangeAddress cra9 = new CellRangeAddress(2, 2, 17, 24);
        sheet.addMergedRegion(cra9);
        PoiUtil.setRegionBorder(1, cra9, sheet);

        // 第四行
        Row row3 = sheet.createRow(3);
        row3.setHeightInPoints(25);

        // 第四行样式
        HSSFCellStyle cellStyle4 = workbook.createCellStyle();
        HSSFFont fontStyle4 = workbook.createFont();
        fontStyle4.setFontName("楷体");
        fontStyle4.setColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
        fontStyle4.setFontHeightInPoints((short) 16);
        fontStyle4.setBold(true);
        cellStyle4.setFont(fontStyle4);
        cellStyle4.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle4.setAlignment(HorizontalAlignment.CENTER);

        Cell cellA4 = row3.createCell(0);
        cellA4.setCellValue("明细");
        cellA4.setCellStyle(cellStyle4);
        CellRangeAddress cra10 = new CellRangeAddress(3, 3, 0, 24);
        sheet.addMergedRegion(cra10);
        PoiUtil.setRegionBorder(1, cra10, sheet);

        // 第五行
        Row row4 = sheet.createRow(4);
        row4.setHeightInPoints(35);

        // 第五行样式
        HSSFCellStyle cellStyle5 = workbook.createCellStyle();
        HSSFFont fontStyle5 = workbook.createFont();
        fontStyle5.setFontName("宋体");
        fontStyle5.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        fontStyle5.setBold(true);
        fontStyle5.setFontHeightInPoints((short) 11);
        cellStyle5.setFont(fontStyle5);
        cellStyle5.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
        cellStyle5.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle5.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle5.setAlignment(HorizontalAlignment.CENTER);
        cellStyle5.setBorderBottom(BorderStyle.THIN);
        cellStyle5.setBorderLeft(BorderStyle.THIN);
        cellStyle5.setBorderTop(BorderStyle.THIN);
        cellStyle5.setBorderRight(BorderStyle.THIN);

        Cell cellA5 = row4.createCell(0);
        cellA5.setCellValue("序号");
        cellA5.setCellStyle(cellStyle5);

        Cell cellB5 = row4.createCell(1);
        cellB5.setCellValue("订单编号");
        cellB5.setCellStyle(cellStyle5);

        Cell cellC5 = row4.createCell(2);
        cellC5.setCellValue("业务类型");
        cellC5.setCellStyle(cellStyle5);

        Cell cellD5 = row4.createCell(3);
        cellD5.setCellValue("船名航次");
        cellD5.setCellStyle(cellStyle5);

        Cell cellE5 = row4.createCell(4);
        cellE5.setCellValue("提单号");
        cellE5.setCellStyle(cellStyle5);

        Cell cellF5 = row4.createCell(5);
        cellF5.setCellValue("提/还箱点");
        cellF5.setCellStyle(cellStyle5);

        Cell cellG5 = row4.createCell(6);
        cellG5.setCellValue("停靠码头");
        cellG5.setCellStyle(cellStyle5);

        Cell cellH5 = row4.createCell(7);
        cellH5.setCellValue("箱型");
        cellH5.setCellStyle(cellStyle5);

        Cell cellI5 = row4.createCell(8);
        cellI5.setCellValue("箱号");
        cellI5.setCellStyle(cellStyle5);

        Cell cellJ5 = row4.createCell(9);
        cellJ5.setCellValue("做箱时间");
        cellJ5.setCellStyle(cellStyle5);

        Cell cellK5 = row4.createCell(10);
        cellK5.setCellValue("车辆");
        cellK5.setCellStyle(cellStyle5);

        Cell cellL5 = row4.createCell(11);
        cellL5.setCellValue("门点");
        cellL5.setCellStyle(cellStyle5);

        //TODO　地址
        //门点地址和联系方式
        Cell cellM5m = row4.createCell(12);
        cellM5m.setCellValue("具体地址");
        cellM5m.setCellStyle(cellStyle5);

        Cell cellM5n = row4.createCell(13);
        cellM5n.setCellValue("联系方式");
        cellM5n.setCellStyle(cellStyle5);


        ///////

        Cell cellM5 = row4.createCell(14);//12
        cellM5.setCellValue("往来单位");
        cellM5.setCellStyle(cellStyle5);

        Cell cellN5 = row4.createCell(15);//13
        cellN5.setCellValue("应收");
        cellN5.setCellStyle(cellStyle5);

        Cell cellO5 = row4.createCell(16);//14
        cellO5.setCellValue("应收费用");
        cellO5.setCellStyle(cellStyle5);

        Cell cellP5 = row4.createCell(17);
        cellP5.setCellValue("应付");
        cellP5.setCellStyle(cellStyle5);

        Cell cellQ5 = row4.createCell(18);
        cellQ5.setCellValue("应付费用");
        cellQ5.setCellStyle(cellStyle5);

        Cell cellR5 = row4.createCell(19);
        cellR5.setCellValue("其他应收");
        cellR5.setCellStyle(cellStyle5);

        Cell cellS5 = row4.createCell(20);
        cellS5.setCellValue("其他应收费用");
        cellS5.setCellStyle(cellStyle5);

        Cell cellT5 = row4.createCell(21);
        cellT5.setCellValue("其他应付");
        cellT5.setCellStyle(cellStyle5);

        Cell cellU5 = row4.createCell(22);
        cellU5.setCellValue("其他应付费用");
        cellU5.setCellStyle(cellStyle5);

        Cell cellV5 = row4.createCell(23);
        cellV5.setCellValue("客户编号");
        cellV5.setCellStyle(cellStyle5);

        Cell cellV6 = row4.createCell(24);
        cellV6.setCellValue("付款人");
        cellV6.setCellStyle(cellStyle5);

        // 第六行之后样式
        HSSFCellStyle cellStyle6 = workbook.createCellStyle();
        HSSFFont fontStyle6 = workbook.createFont();
        fontStyle6.setFontName("宋体");
        fontStyle6.setFontHeightInPoints((short) 11);
        cellStyle6.setFont(fontStyle6);
        cellStyle6.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle6.setAlignment(HorizontalAlignment.CENTER);
        cellStyle6.setBorderBottom(BorderStyle.THIN);
        cellStyle6.setBorderLeft(BorderStyle.THIN);
        cellStyle6.setBorderRight(BorderStyle.THIN);
        // 自动换行
        cellStyle6.setWrapText(true);

        // 根据订单编号、箱号、往来单位排序
        costVoList = costVoList.stream().sorted(Comparator.comparing(CostVo::getOrderNumber)
                .thenComparing(Comparator.comparing(CostVo::getCaseNumber).reversed().thenComparing(Comparator.comparing(CostVo::getBtype).reversed())))
                .collect(Collectors.toList());

        //存储最大列宽
        Map<Integer, Integer> maxWidth = new HashMap<>();
        // 初始化14/16/18/20标题的列宽
        for (int i = 0; i <= 3; i++) {
            if (i == 0) {
                maxWidth.put(16, row4.getCell(16).getStringCellValue().getBytes().length * 256 + 200);
            } else if (i == 1) {
                maxWidth.put(18, row4.getCell(18).getStringCellValue().getBytes().length * 256 + 200);
            } else if (i == 2) {
                maxWidth.put(20, row4.getCell(20).getStringCellValue().getBytes().length * 256 + 200);
            } else {
                maxWidth.put(22, row4.getCell(22).getStringCellValue().getBytes().length * 256 + 200);
                maxWidth.put(23, row4.getCell(23).getStringCellValue().getBytes().length * 256 + 200);
            }
        }

        // 从excel的第六行开始
        int index = 5;
        // 获取分组
        List<Map.Entry<Integer, CostVo>> costVoMap = getCostVoMap(costVoList);
        for (int i = 0; i < costVoMap.size(); i++) {
            CostVo costVo = costVoMap.get(i).getValue();

            Row row = sheet.createRow(i + index);
            //row.setHeightInPoints(30);

            Cell cell0 = row.createCell(0);
            cell0.setCellValue(i + 1);
            cell0.setCellStyle(cellStyle6);

            Cell cell1 = row.createCell(1);
            cell1.setCellValue(costVo.getOrderNumber());
            cell1.setCellStyle(cellStyle6);

            Cell cell2 = row.createCell(2);
            cell2.setCellValue(costVo.getOrderType() == 1 ? "海运出口" : "海运进口");
            cell2.setCellStyle(cellStyle6);

            Cell cell3 = row.createCell(3);
            cell3.setCellValue(costVo.getVesselAndVoyage());
            cell3.setCellStyle(cellStyle6);

            Cell cell4 = row.createCell(4);
            cell4.setCellValue(costVo.getBlNos());
            cell4.setCellStyle(cellStyle6);

            Cell cell5 = row.createCell(5);
            cell5.setCellValue(costVo.getPrPoint());
            cell5.setCellStyle(cellStyle6);

            Cell cell6 = row.createCell(6);
            cell6.setCellValue(costVo.getDock());
            cell6.setCellStyle(cellStyle6);

            Cell cell7 = row.createCell(7);
            cell7.setCellValue(costVo.getBoxPile());
            cell7.setCellStyle(cellStyle6);

            Cell cell8 = row.createCell(8);
            cell8.setCellValue(costVo.getCaseNumber());
            cell8.setCellStyle(cellStyle6);

            Cell cell9 = row.createCell(9);
            cell9.setCellValue(DateUtils.dateToString(costVo.getPackingTime()));
            cell9.setCellStyle(cellStyle6);

            Cell cell10 = row.createCell(10);
            cell10.setCellValue(costVo.getVehicle());
            cell10.setCellStyle(cellStyle6);

            Cell cell11 = row.createCell(11);
            cell11.setCellValue(costVo.getDoor());
            cell11.setCellStyle(cellStyle6);

            //地址和联系方式
            Cell cell12m = row.createCell(12);
            cell12m.setCellValue(costVo.getAddress());
            cell12m.setCellStyle(cellStyle6);

            Cell cell12n = row.createCell(13);
            cell12n.setCellValue(costVo.getContactNumber());
            cell12n.setCellStyle(cellStyle6);

            //////

            Cell cell12 = row.createCell(14);
            cell12.setCellValue(costVo.getBtype());
            cell12.setCellStyle(cellStyle6);

            Cell cell13 = row.createCell(15);
            if (costVo.getYsTotalPrice() != null) {
                cell13.setCellValue(costVo.getYsTotalPrice().doubleValue());
                cell13.setCellStyle(cellStyle6);
            }

            Cell cell14 = row.createCell(16);
            cell14.setCellValue(costVo.getYsPayItems());
            cell14.setCellStyle(cellStyle6);

            Cell cell15 = row.createCell(17);
            if (costVo.getYfTotalPrice() != null) {
                cell15.setCellValue(costVo.getYfTotalPrice().doubleValue());
                cell15.setCellStyle(cellStyle6);
            }

            Cell cell16 = row.createCell(18);
            cell16.setCellValue(costVo.getYfPayItems());
            cell16.setCellStyle(cellStyle6);

            Cell cell17 = row.createCell(19);
            if (costVo.getQtYsTotalPrice() != null) {
                cell17.setCellValue(costVo.getQtYsTotalPrice().doubleValue());
                cell17.setCellStyle(cellStyle6);
            }

            Cell cell18 = row.createCell(20);
            cell18.setCellValue(costVo.getQtYsPayItems());
            cell18.setCellStyle(cellStyle6);

            Cell cell19 = row.createCell(21);
            if (costVo.getQtYfTotalPrice() != null) {
                cell19.setCellValue(costVo.getQtYfTotalPrice().doubleValue());
                cell19.setCellStyle(cellStyle6);
            }

            Cell cell20 = row.createCell(22);
            cell20.setCellValue(costVo.getQtYfPayItems());
            cell20.setCellStyle(cellStyle6);

            Cell cell21 = row.createCell(23);
            cell21.setCellValue(costVo.getCustomerNum());
            cell21.setCellStyle(cellStyle6);



            Cell cell24 = row.createCell(24);
            cell24.setCellValue(costVo.getPayer());
            cell24.setCellStyle(cellStyle6);

            int cell14Length = cell14.getStringCellValue().getBytes().length * 256 + 200;
            //这里把宽度最大限制到15000
            if (cell14Length > 15000) {
                cell14Length = 15000;
            }
            maxWidth.put(16, Math.max(cell14Length, maxWidth.get(16)));

            int cell16Length = cell16.getStringCellValue().getBytes().length * 256 + 200;
            if (cell16Length > 15000) {
                cell16Length = 15000;
            }
            maxWidth.put(18, Math.max(cell16Length, maxWidth.get(18)));

            int cell18Length = cell18.getStringCellValue().getBytes().length * 256 + 200;
            if (cell18Length > 15000) {
                cell18Length = 15000;
            }
            maxWidth.put(20, Math.max(cell18Length, maxWidth.get(20)));

            int cell20Length = cell20.getStringCellValue().getBytes().length * 256 + 200;
            if (cell20Length > 15000) {
                cell20Length = 15000;
            }
            maxWidth.put(22, Math.max(cell20Length, maxWidth.get(22)));

            int cell21Length = cell21.getStringCellValue().getBytes().length * 256 + 200;
            if (cell21Length > 15000) {
                cell21Length = 15000;
            }
            maxWidth.put(23, Math.max(cell21Length, maxWidth.get(23)));
        }

        // 列宽自适应
        for (Integer key : maxWidth.keySet()) {
            sheet.setColumnWidth(key, maxWidth.get(key));
        }

        // 打印页面设置
        HSSFPrintSetup printSetup = sheet.getPrintSetup();
        // 打印方向，true：横向，false：纵向(默认)
        printSetup.setLandscape(true);
        // A4纸
        printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // 将整个工作表打印在一页
        sheet.setAutobreaks(true);
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
     * 组装数据
     *
     * @param costVoList
     */
    public static List<Map.Entry<Integer, CostVo>> getCostVoMap(List<CostVo> costVoList) {


//        costVoList.stream().collect(
//                Collectors.groupingBy(t -> t.getCaseNumber(),
//                        Collectors.groupingBy(t1 -> t1.getBtype(),
//                                Collectors.groupingBy(t2 -> t2.getCostTypes())
//                        )
//                )
//        );

        Map<Integer, CostVo> costVoMap = new HashMap<>();

        Map<String/*箱号*/, Map<String/*往来单位*/, Map<Integer/*费用类型*/, List<CostVo>>>> groupMap = costVoList.stream().collect(Collectors.groupingBy(t ->
                t.getCaseNumber()+"_"+t.getPackingTime()+"_"+t.getBlNoStr(), Collectors.groupingBy(t1 -> t1.getBtype(), Collectors.groupingBy(t2 -> t2.getCostTypes()))));

        Map<String, CostVo> contCostVoMap = new HashMap<>();
        int count = 0;
        // 遍历出箱型key
        for (String key : groupMap.keySet()) {
            // 通过箱型key 得到往来单位map
            Map<String, Map<Integer, List<CostVo>>> btypeList = groupMap.get(key);
            // 遍历出往来单位key
            for (String key2 : btypeList.keySet()) {
                // 通过往来单位key 得到费用类型map
                Map<Integer, List<CostVo>> costTypesList = btypeList.get(key2);
                BigDecimal ysTotalPrice = new BigDecimal(0);
                String ysPayItems = "";
                BigDecimal yfTotalPrice = new BigDecimal(0);
                String yfPayItems = "";
                BigDecimal qtYsTotalPrice = new BigDecimal(0);
                String qtYsPayItems = "";
                BigDecimal qtYfTotalPrice = new BigDecimal(0);
                String qtYfPayItems = "";
                // 遍历费用类型key
                for (Integer key3 : costTypesList.keySet()) {
                    // 通过费用类型key 将每个费用数据的一组数据 放入费用map
                    contCostVoMap.put(key, costTypesList.get(key3).get(0));
                    // 通过费用类型key 获取费用数据map
                    List<CostVo> costVoList1 = costTypesList.get(key3);
                    // 遍历出每一条费用
                    for (int i = 0; i < costVoList1.size(); i++) {
                        if (key3 == 1) {// 应收
                            ysTotalPrice = ysTotalPrice.add(costVoList1.get(i).getTotalPrice());
                            ysPayItems += costVoList1.get(i).getPayItems() + "￥" + costVoList1.get(i).getTotalPrice() + "  ";
                        } else if (key3 == 2) {
                            yfTotalPrice = yfTotalPrice.add(costVoList1.get(i).getTotalPrice());
                            yfPayItems += costVoList1.get(i).getPayItems() + "￥" + costVoList1.get(i).getTotalPrice() + "  ";
                        } else if (key3 == 3) {
                            qtYsTotalPrice = qtYsTotalPrice.add(costVoList1.get(i).getTotalPrice());
                            qtYsPayItems += costVoList1.get(i).getPayItems() + "￥" + costVoList1.get(i).getTotalPrice() + "  ";
                        } else {
                            qtYfTotalPrice = qtYfTotalPrice.add(costVoList1.get(i).getTotalPrice());
                            qtYfPayItems += costVoList1.get(i).getPayItems() + "￥" + costVoList1.get(i).getTotalPrice() + "  ";
                        }
                    }
                }
                CostVo obj = contCostVoMap.get(key);
                obj.setYsTotalPrice(ysTotalPrice);
                obj.setYsPayItems(ysPayItems);
                obj.setYfTotalPrice(yfTotalPrice);
                obj.setYfPayItems(yfPayItems);
                obj.setQtYsTotalPrice(qtYsTotalPrice);
                obj.setQtYsPayItems(qtYsPayItems);
                obj.setQtYfTotalPrice(qtYfTotalPrice);
                obj.setQtYfPayItems(qtYfPayItems);
                costVoMap.put(count, obj);
                count++;
            }
        }
        // 安装订单编号排序
        List<Map.Entry<Integer, CostVo>> list = costVoMap.entrySet().stream()
                .sorted((entry1, entry2) -> entry1.getValue().getOrderNumber().compareTo(entry2.getValue().getOrderNumber()))
                .collect(Collectors.toList());
        return list;
    }
}
