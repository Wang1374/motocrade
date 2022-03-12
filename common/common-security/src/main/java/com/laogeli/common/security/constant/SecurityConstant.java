package com.laogeli.common.security.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统常量
 *
 * @author yangyu
 * @date 2019-12-31
 */
public class SecurityConstant {

    /**
     * 超级管理员角色
     */
    public static final String ROLE_ADMIN = "role_admin";

    /**
     * 基础角色
     */
    public static final String BASE_ROLE = "role_user";

    /**
     * 正常状态
     */
    public static final String NORMAL = "0";

    /**
     * 异常状态
     */
    public static final String ABNORMAL = "1";

    /**
     * 手机登录URL
     */
    public static final String MOBILE_TOKEN_URL = "/mobile/token";

    /**
     * 微信登录URL
     */
    public static final String WX_TOKEN_URL = "/wx/token";

    /**
     * 默认系统编号
     */
    public static final String SYS_CODE = "EXAM";

    /**
     * JSON 资源
     */
    public static final String CONTENT_TYPE = "application/json; charset=utf-8";

    /**
     * 员工角色前缀
     */
    public static final String STAFF_ROLE_PREFIX = "role_";

    /**
     * 员工角色
     */
    public static final String[] STAFF_ROLE = {"总经理", "财务", "操作", "业务"};

    /**
     * 获取对应的员工角色权限
     */
    public static final List<String> getMenus(String roleName, Integer accountType) {
        List<String> strList = new ArrayList<>();
        if ("总经理".equals(roleName)) {
            if (accountType == 0) {
                // 接单
                strList.add("571381763521770451");
                strList.add("571381763521770485");
                strList.add("571381763521770486");
                // 做箱
                strList.add("571381763521796542");
                strList.add("571381763521797321");
                strList.add("571381763521797322");
                strList.add("571381763521797323");
                strList.add("571381763521797324");
                // 费用
                strList.add("571381763521798171");
                strList.add("571381763521798185");
                strList.add("571381763521798186");
                strList.add("571381763521798187");
                strList.add("571381763521798188");
                strList.add("571381763521798189");
                strList.add("571381763521798190");
                strList.add("571381763521798191");
                strList.add("571381763521798194");
                // 业务毛利
                strList.add("571381763521798201");
                // 往来报表
                strList.add("571381763521798212");
                // 基础信息
                strList.add("571381763521798574");
                strList.add("571381763521798575");
                strList.add("571381763521798576");
                strList.add("571381763521798578");
                strList.add("571381763521798579");
                // 供应商
                strList.add("717394974064381952");
                strList.add("717394974064381953");
                strList.add("717394974064381955");
                strList.add("717394974064381956");
                // 往来单位
                strList.add("717395573661110272");
                strList.add("717395573661110273");
                strList.add("717395573661110275");
                strList.add("717395573661110276");
                // 门点管理
                strList.add("717395878742200320");
                strList.add("717395878742200321");
                strList.add("717395878742200323");
                strList.add("717395878742200324");
                // 查看堆场
                strList.add("717396308624805888");
                strList.add("717396308624805892");
            } else if (accountType == 1) {
                // 订单管理
                strList.add("571381763521770542");
                strList.add("571381763521770543");
                strList.add("571381763521770611");
                strList.add("571381763521770613");
                strList.add("571381763521770614");
                strList.add("571381763521770615");
                // 出口订单
                strList.add("571381763521771271");
                strList.add("571381763521772112");
                strList.add("571381763521772113");
                strList.add("571381763521772114");
                strList.add("571381763521772115");
                strList.add("571381763521772116");
                strList.add("571381763521772117");
                // 进口订单
                strList.add("571381763521772357");
                strList.add("571381763521773322");
                strList.add("571381763521773323");
                strList.add("571381763521773324");
                strList.add("571381763521773325");
                strList.add("571381763521773326");
                strList.add("571381763521773327");
                // 做箱
                strList.add("571381763521796542");
                strList.add("571381763521797321");
                strList.add("571381763521797322");
                strList.add("571381763521797323");
                strList.add("571381763521797324");
                // 费用
                strList.add("571381763521798171");
                strList.add("571381763521798185");
                strList.add("571381763521798186");
                strList.add("571381763521798187");
                strList.add("571381763521798188");
                strList.add("571381763521798189");
                strList.add("571381763521798190");
                strList.add("571381763521798191");
                strList.add("571381763521798194");
                // 业务毛利
                strList.add("571381763521798201");
                // 往来报表
                strList.add("571381763521798212");
                // 车辆管理
                strList.add("571381763521798215");
                strList.add("571381763521798223");
                strList.add("571381763521798231");
                strList.add("571381763521798233");
                strList.add("571381763521798234");
                // 司机列表
                strList.add("571381763521798258");
                strList.add("571381763521798312");
                strList.add("571381763521798314");
                strList.add("571381763521798315");
                // 油费记录
                strList.add("571381763521798351");
                strList.add("571381763521798362");
                strList.add("571381763521798371");
                strList.add("571381763521798382");
                strList.add("571381763521798391");
                strList.add("571381763521798511");
                strList.add("571381763521798521");
                // 基础信息
                strList.add("571381763521798574");
                strList.add("571381763521798575");
                strList.add("571381763521798576");
                strList.add("571381763521798578");
                strList.add("571381763521798579");
                // 供应商
                strList.add("717394974064381952");
                strList.add("717394974064381953");
                strList.add("717394974064381955");
                strList.add("717394974064381956");
                // 往来单位
                strList.add("717395573661110272");
                strList.add("717395573661110273");
                strList.add("717395573661110275");
                strList.add("717395573661110276");
                // 门点管理
                strList.add("717395878742200320");
                strList.add("717395878742200321");
                strList.add("717395878742200323");
                strList.add("717395878742200324");
                // 查看堆场
                strList.add("717396308624805888");
                strList.add("717396308624805892");
            } else if (accountType == 2) {
                // 下单中心
                strList.add("571381763521770424");
                strList.add("571381763521770431");
                strList.add("571381763521770432");
                strList.add("571381763521770433");
                // 费用
                strList.add("571381763521798171");
                strList.add("571381763521798185");
                strList.add("571381763521798186");
                // 基础信息
                strList.add("571381763521798574");
                strList.add("571381763521798575");
                strList.add("571381763521798576");
                strList.add("571381763521798578");
                strList.add("571381763521798579");
                // 供应商
                strList.add("717394974064381952");
                strList.add("717394974064381953");
                strList.add("717394974064381955");
                strList.add("717394974064381956");
                // 往来单位
                strList.add("717395573661110272");
                strList.add("717395573661110273");
                strList.add("717395573661110275");
                strList.add("717395573661110276");
                // 门点管理
                strList.add("717395878742200320");
                strList.add("717395878742200321");
                strList.add("717395878742200323");
                strList.add("717395878742200324");
                // 查看堆场
                strList.add("717396308624805888");
                strList.add("717396308624805892");
            }
        } else if ("财务".equals(roleName)) {
            /*if (accountType == 0) {
            } else if (accountType == 1) {
            } else if (accountType == 2) {
                // 费用
                strList.add("571381763521798171");
                strList.add("571381763521798185");
                strList.add("571381763521798186");
                // 基础信息
                strList.add("571381763521798574");
                strList.add("571381763521798575");
                strList.add("571381763521798579");
                // 供应商
                strList.add("717394974064381956");
                // 往来单位
                strList.add("717395573661110276");
                // 门点管理
                strList.add("717395878742200320");
                strList.add("717395878742200324");
                // 查看堆场
                strList.add("717396308624805888");
                strList.add("717396308624805892");
            }*/
        } else if ("操作".equals(roleName)) {
            /*if (accountType == 0) {
            } else if (accountType == 1) {
            } else if (accountType == 2) {
            }*/
        } else if ("业务".equals(roleName)) {
            /*if (accountType == 0) {
            } else if (accountType == 1) {
            } else if (accountType == 2) {
            }*/
        }
        return strList;
    }
}
