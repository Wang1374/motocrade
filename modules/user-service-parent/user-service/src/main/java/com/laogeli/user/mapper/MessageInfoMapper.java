package com.laogeli.user.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.user.api.module.MessageInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.*;
/**
 * @author wang
 * @Date 2021-02-02 9:59
 **/
@Mapper
public interface MessageInfoMapper extends CrudMapper<MessageInfo> {

    /**
     * 根据公司id查询消息列表
     * @param messageInfo
     * @return
     */
   // List<MessageInfo> getMessageList(MessageInfo messageInfo);

    /**
     * 更新阅读状态
     * @param ids
     * @return
     */
    int updateReadStatus(@Param("ids") String[] ids);

    /**
     * 标记为全部已读
     * @param belongCompaniesId
     * @return
     */
    int updateAllRead(String belongCompaniesId);



    /**
     * 根据id删除消息
     * @param ids
     * @return
     */
    int deleteMessageByIds(@Param("ids") String[] ids);

    /**
     * 根据公司id查询未读消息数量
     * @param belongCompaniesId
     * @return
     */
    Integer getMessageNotRead(@Param("belongCompaniesId") String belongCompaniesId);
}
