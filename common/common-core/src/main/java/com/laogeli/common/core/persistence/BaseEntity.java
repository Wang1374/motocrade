package com.laogeli.common.core.persistence;

import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.utils.DateUtils;
import com.laogeli.common.core.utils.IdGen;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

/**
 * Entity基类
 *
 * @author yangyu
 * @date 2019-12-31
 */
@Data
@NoArgsConstructor // 生成无参构造函数
public class BaseEntity<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String id;

    /**
     * 创建者唯一标识
     */
    protected String creator;

    /**
     * 创建者姓名
     */
    protected String name;

    /**
     * 创建者手机号
     */
    protected String phone;

    /**
     * 角色标识
     */
    protected ArrayList role_code;

    /**
     * 数据权限
     * */
    private String dataScope;

    /**
     * 创建日期
     */
    protected Date createDate;

    /**
     * 更新者
     */
    protected String modifier;

    /**
     * 更新日期
     */
    protected Date modifyDate;

    /**
     * 删除标记 0:正常，1-删除
     */
    protected Integer delFlag = CommonConstant.DEL_FLAG_NORMAL;

    /**
     * 系统编号
     */
    protected String applicationCode;

    /**
     * 是否为新记录
     */
    protected boolean isNewRecord;

    /**
     * 开始时间
     */
    protected String beginTime;

    /**
     * 结束时间
     */
    protected String endTime;

    /**
     * ID数组
     */
    protected String[] ids;

    /**
     * ID字符串，多个用逗号隔开
     */
    protected String idString;

    /**
     * 所属公司标识
     */
    private String belongCompaniesId;

    /**
     * 多条件搜索
     */
    protected String searchParam;

    public BaseEntity(String id) {
        this();
        this.id = id;
    }

    /**
     * 是否为新记录
     *
     * @return boolean
     */
    public boolean isNewRecord() {
        return this.isNewRecord || StringUtils.isBlank(this.getId());
    }

    /**
     * 设置基本属性
     *
     * @param userCode        用户编码
     * @param applicationCode 系统编号
     */
    public void setCommonValue(String userCode, String applicationCode) {
        Date currentDate = DateUtils.asDate(LocalDateTime.now());
        if (this.isNewRecord()) {
            this.setId(IdGen.snowflakeId());
            this.setNewRecord(true);
            this.creator = userCode;
            this.createDate = currentDate;
        }
        this.modifier = userCode;
        this.modifyDate = currentDate;
        this.delFlag = 0;
        this.applicationCode = applicationCode;
    }
}

