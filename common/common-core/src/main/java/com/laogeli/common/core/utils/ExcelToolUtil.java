
package com.laogeli.common.core.utils;

import com.google.gson.internal.$Gson$Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * @author wang
 * @date 2019-12-31
 */
public class ExcelToolUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final DateTimeFormatter FORMATTER_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 根据数据和标题封装excel数据输出流
     *
     * @param input          输入流
     * @param out            响应输出流
     * @param list           待导出数据
     * @param keys2titlesMap 标题
     */
    public static void exportExcel(InputStream input, OutputStream out,
                                   List<Map<String, Object>> list, LinkedHashMap<String, String> keys2titlesMap) throws Exception {
        // 创建SXSSFWorkbook
        SXSSFWorkbook wb = new SXSSFWorkbook(100);

        String sheetName = "data";
        if (keys2titlesMap.keySet().iterator().next().equals("orderNumber")) {
            sheetName = (String) list.get(0).get(keys2titlesMap.keySet().iterator().next());
        }
        Sheet sh = wb.createSheet(sheetName);
        // 获取样式
        Map<String, CellStyle> styles = createStyles(wb);
        // 创建首行标题，设置高度
        // SXSSFRow row = sh.createRow(0);
        Row rowHeader = sh.createRow(0);
        rowHeader.setHeightInPoints(16);
        Iterator<String> keys = keys2titlesMap.keySet().iterator();
        int i = 0;
        List<String> keyList = new ArrayList<String>();
        while (keys.hasNext()) {
            // 遍历标题，设置标题样式
            String key = keys.next();
            keyList.add(key);
            String title = keys2titlesMap.get(key);
            Cell cellHeader = rowHeader.createCell(i++);
            cellHeader.setCellStyle(styles.get("header"));
            cellHeader.setCellValue(title);
        }
        // 设置列宽
        for (int t = 0; t < keyList.size(); t++) {
            int colWidth = sh.getColumnWidth(t) * 2;
            sh.setColumnWidth(t, colWidth < 3000 ? 3000 : colWidth);
            //sh.setColumnWidth(t, 256 * colWidth + 184);
//            if(colWidth<255*256){
//                sh.setColumnWidth(t, colWidth < 3000 ? 3000 : colWidth);
//            }else{
//                sh.setColumnWidth(t,6000 );
//            }
        }

        for (int rownum = 1; rownum <= list.size(); rownum++) {
            // 遍历数据，逐一插入excel
            Row row = sh.createRow(rownum);
            Map<String, Object> dataMap = list.get(rownum - 1);
            i = 0;
            for (String key : keyList) {

                // 根据标题，设置excel数据
                Cell cell = row.createCell(i++);
                cell.setCellStyle(styles.get("data2"));
                //过滤，字段
                if (dataMap.get(key) != null) {
                    if (dataMap.get(key).equals("1")) {
                        dataMap.put(key, "海运出口");
                    } else if (dataMap.get(key).equals("2")) {
                        dataMap.put(key, "海运进口");
                    }

                    // 根据值的类型，设置表格的数据格式
                    if (dataMap.get(key) instanceof String) {
                        cell.setCellValue(MapUtil.getString(dataMap, key));
                    } else if (dataMap.get(key) instanceof Date) {
                        Date date = MapUtil.getDate(dataMap, key);
                        cell.setCellValue(date == null ? "" : FORMATTER.format(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())));
                    } else if (dataMap.get(key) instanceof Number) {
                        cell.setCellValue(MapUtil.getDouble(dataMap, key));
                    } else if (dataMap.get(key) instanceof Boolean) {
                        Boolean value = MapUtil.getBooleanValue(dataMap, key);
                        cell.setCellValue(value == null ? Boolean.FALSE : value);
                    }
                } else if (key.contains(".")) {
                    // 标题的名称对应的可以是对象
                    String[] temp = StringUtils.split(key, ".");
                    Object obj = dataMap.get(temp[0]);
                    if (obj != null) {
                        // 映射list
                        if (temp[1].contains("#")) {
                            String listIndex = StringUtils.split(temp[1], "#")[1];
                            obj = ((List) obj).get(Integer.parseInt(listIndex));
                            // 取得对应的字段值
                            obj = ReflectionUtil.getFieldValue(obj, temp[2]);
                        } else {
                            // 映射对象，取得对应的字段值
                            obj = ReflectionUtil.getFieldValue(obj, temp[1]);
                        }
                        if (obj != null) {
                            cell.setCellValue(obj.toString());
                        } else {
                            cell.setCellValue("");
                        }
                    }
                }
            }
            if (rownum % 100 == 0) {
                ((SXSSFSheet) sh).flushRows();
            }
        }
        wb.write(out);
        out.close();
    }

    /**
     * 创建表格样式
     *
     * @param wb 工作薄对象
     * @return 样式列表
     */
    private static Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        // 设置标题样式
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        Font titleFont = wb.createFont();
        titleFont.setFontName("Arial");
        titleFont.setFontHeightInPoints((short) 100);
        style.setFont(titleFont);
        styles.put("title", style);


        style.setWrapText(true);//自动换行
        // 设置数据行的默认样式
        style = wb.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        Font dataFont = wb.createFont();
        dataFont.setFontName("Arial");
        dataFont.setFontHeightInPoints((short) 10);
        style.setFont(dataFont);
        styles.put("data", style);
        // 设置数据行的默认样式1，位置左对齐
        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        styles.put("data1", style);
        // 设置数据行的默认样式2，位置居中
        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        styles.put("data2", style);
        // 设置数据行的默认样式3，位置右对齐
        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        styles.put("data3", style);

        // 设置表头的样式
        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        //style.setAlignment();
        Font headerFont = wb.createFont();
        headerFont.setFontName("Arial");
        headerFont.setFontHeightInPoints((short) 15);

        headerFont.setColor(IndexedColors.BLACK.getIndex());
        style.setFont(headerFont);
        style.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        //style.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
        styles.put("header", style);
        return styles;
    }

    /**
     * 数据
     *
     * @param input          input
     * @param keys2titlesMap keys2titlesMap
     * @return List
     */
    public static List<Map<String, Object>> importExcel(InputStream input,
                                                        LinkedHashMap<String, String> keys2titlesMap) throws Exception {
        String[] keys = keys2titlesMap.keySet().toArray(new String[]{});
        return importExcel(input, keys);
    }

    /**
     * 导入数据
     *
     * @param input input
     * @param keys  keys
     * @return List
     */
    private static List<Map<String, Object>> importExcel(InputStream input, String[] keys) throws Exception {
        Workbook wb = WorkbookFactory.create(input);
        Sheet sheet = wb.getSheetAt(0);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int rownum = 1; rownum <= sheet.getLastRowNum(); rownum++) {
            // 从第二行开始解析数据
            Row row = sheet.getRow(rownum);
            if (row == null) {
                continue;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            for (int cellnum = 0; cellnum < row.getLastCellNum(); cellnum++) {
                // 从第1列开始将单元格中的值写入map
                Cell cell = row.getCell(cellnum);
                if (cell == null)
                    continue;
                int valType = cell.getCellType();
                if (valType == Cell.CELL_TYPE_STRING) {
                    map.put(keys[cellnum], cell.getStringCellValue());
                } else if (valType == Cell.CELL_TYPE_BOOLEAN) {
                    map.put(keys[cellnum], cell.getBooleanCellValue());
                } else if (valType == Cell.CELL_TYPE_NUMERIC) {
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // 用于转化为日期格式
                        Date d = cell.getDateCellValue();
                        map.put(keys[cellnum], FORMATTER_DATE.format(LocalDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault())));
                    } else {
                        map.put(keys[cellnum], numToStringFormat(String.valueOf(cell.getNumericCellValue())));
                    }
                }
            }
            list.add(map);
        }
        System.out.println(list);
        return list;
    }

    /**
     * 数据
     *
     * @param input          input
     * @param keys2titlesMap keys2titlesMap
     * @return List
     */
    public static List<Map<String, Object>> importExcelOil(InputStream input,
                                                           LinkedHashMap<String, String> keys2titlesMap) throws Exception {
        String[] keys = keys2titlesMap.keySet().toArray(new String[]{});
        return importOilExcel(input, keys);
    }


    /**
     * 导入数据
     *
     * @param input input
     * @param keys  keys
     * @return List
     */
    private static List<Map<String, Object>> importOilExcel(InputStream input, String[] keys) throws Exception {
        Workbook wb = WorkbookFactory.create(input);
        Sheet sheet = wb.getSheetAt(0);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int rownum = 1; rownum <= sheet.getLastRowNum(); rownum++) {
            SimpleDateFormat sdf = null;
            // 从第二行开始解析数据
            Row row = sheet.getRow(rownum);
            if (row == null) {
                continue;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            for (int cellnum = 0; cellnum < row.getLastCellNum(); cellnum++) {
                // 从第1列开始将单元格中的值写入map
                Cell cell = row.getCell(cellnum);

                short format = cell.getCellStyle().getDataFormat();
                if (cell == null)
                    continue;
                CellType cellTypeEnum = cell.getCellTypeEnum();
                if (cellTypeEnum == CellType.STRING) {
                    map.put(keys[cellnum], cell.getStringCellValue());
                } else if (cellTypeEnum == CellType.BOOLEAN) {
                    map.put(keys[cellnum], cell.getBooleanCellValue());

                } else if (format == 20 || format == 32||cellnum==2) {
                    sdf = new SimpleDateFormat("HH:mm");
                    String format1 = sdf.format(cell.getDateCellValue()); //日期
                    map.put(keys[cellnum], format1);
                } else if (format == 14 || format == 31 || format == 57 || format == 58 ) {
                    // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                    double value = cell.getNumericCellValue();
                    Date date = org.apache.poi.ss.usermodel.DateUtil
                            .getJavaDate(value);
                    String format1 = sdf.format(date);
                    map.put(keys[cellnum], format1);
                } else if (cellTypeEnum == CellType.NUMERIC && cellnum!=1 || cellnum==6) {
                    cell.setCellType(CellType.STRING);
                    map.put(keys[cellnum], cell.getStringCellValue());//数字
                } else if (cellnum==1) {
                    String ss = "";
                    ss =new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
                    ss=ss.toString();
                    map.put(keys[cellnum], ss);//数字
                }
            }
            list.add(map);


        }
        return list;
    }

    public static String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        // 判断数据的类型
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC: // 数字
                //short s = cell.getCellStyle().getDataFormat();
                if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
                    SimpleDateFormat sdf = null;
                    // 验证short值
                    if (cell.getCellStyle().getDataFormat() == 14) {
                        sdf = new SimpleDateFormat("yyyy/MM/dd");
                    } else if (cell.getCellStyle().getDataFormat() == 21) {
                        sdf = new SimpleDateFormat("HH:mm:ss");
                    } else if (cell.getCellStyle().getDataFormat() == 22) {
                        sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    } else {
                        throw new RuntimeException("日期格式错误!!!");
                    }
                    Date date = cell.getDateCellValue();
                    cellValue = sdf.format(date);
                } else if (cell.getCellStyle().getDataFormat() == 0) {//处理数值格式
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    cellValue = String.valueOf(cell.getRichStringCellValue().getString());
                }
                break;
            case Cell.CELL_TYPE_STRING: // 字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN: // Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA: // 公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_BLANK: // 空值
                cellValue = null;
                break;
            case Cell.CELL_TYPE_ERROR: // 故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }



    /**
     * 29      * 判断是否是“02-十一月-2006”格式的日期类型
     * 30
     */
    private static boolean checkDate(String str) {
        String[] dataArr = str.split("-");
        try {
            if (dataArr.length == 3) {
                int x = Integer.parseInt(dataArr[0]);
                String y = dataArr[1];
                int z = Integer.parseInt(dataArr[2]);
                if (x > 0 && x < 32 && z > 0 && z < 10000 && y.endsWith("月")) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * 判断这个数字是否是科学计数法 如果是 则转换成普通模式
     *
     * @param num num
     * @return String
     */
    private static String numToStringFormat(String num) {
        if (num.contains("E10")) {
            return new BigDecimal(num).toPlainString();
        } else if (num.contains(".0")) {
            return num.substring(0, num.length() - 2);
        } else
            return num;
    }
}


