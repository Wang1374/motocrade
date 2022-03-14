package com.laogeli.common.core.utils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 分页查询工具类
 *
 * @author wang
 * @date 2019-12-31
 */
public class PageUtil {

    /**
     * 降序
     */
    private static final String DESC = "desc";

    /**
     * 升序
     */
    private static final String ASC = "asc";


    /*
     * 排序SQL
     * 加强多个字段排序
     * @param sort  排序字段
     * @param order 顺序
     * @author wang
     * @date 2019/9/25
     * @return java.lang.String
     */
    public static String orderBy(String sort, String order) {
        String[] sortArr = sort.split(",");
        String[] orderArr = order.split(",");
        String sortOrOrder = "";
        for (int i = 0; i < sortArr.length; i++) {
            if (DESC.equals(orderArr[i])) {
                sortOrOrder += sortArr[i] + " desc,";
            } else if (ASC.equals(orderArr[i])) {
                sortOrOrder += sortArr[i] + " asc,";
            }
        }
        if (sortOrOrder.length() > 0) {
            sortOrOrder = sortOrOrder.substring(0, sortOrOrder.length() - 1);
        }
        return sortOrOrder;
    }


    /**
     * 初始化pageInfo
     *
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @param sort     sort
     * @param order    order
     * @return PageInfo
     * @author tangyi
     * @date 2019/03/28 15:36
     */
    public static <T> PageInfo<T> pageInfo(String pageNum, String pageSize, String sort, String order) {
        PageInfo<T> page = new PageInfo<>();
        page.setPageNum(Integer.parseInt(pageNum));
        page.setPageSize(Integer.parseInt(pageSize));
        PageHelper.orderBy(orderBy(sort, order));
        return page;
    }


    /**
     * 初始化page 不排序
     *
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @return PageInfo
     * @author wang
     * @date 2020-01-09
     */
    public static <T> PageInfo<T> page(String pageNum, String pageSize) {
        PageInfo<T> page = new PageInfo<>();
        page.setPageNum(Integer.parseInt(pageNum));
        page.setPageSize(Integer.parseInt(pageSize));
        return page;
    }


    /**
     * 复制属性
     *
     * @param source source
     * @param target target
     * @author tangyi
     * @date 2019/07/03 22:26:18
     */
    public static void copyProperties(PageInfo<?> source, PageInfo<?> target) {
        target.setPageNum(source.getPageNum());
        target.setPageSize(source.getPageSize());
        target.setSize(source.getSize());
        target.setStartRow(source.getStartRow());
        target.setEndRow(source.getEndRow());
        target.setPages(source.getPages());
        target.setPrePage(source.getPrePage());
        target.setNextPage(source.getNextPage());
        target.setIsFirstPage(source.isIsFirstPage());
        target.setIsLastPage(source.isIsLastPage());
        target.setHasPreviousPage(source.isHasPreviousPage());
        target.setHasNextPage(source.isHasNextPage());
        target.setNavigatePages(source.getNavigatePages());
        target.setNavigatepageNums(source.getNavigatepageNums());
        target.setNavigateFirstPage(source.getNavigateFirstPage());
        target.setNavigateLastPage(source.getNavigateLastPage());
        target.setTotal(source.getTotal());
    }
}
