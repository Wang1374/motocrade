package com.laogeli.user.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.user.api.module.UserAuths;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * UserAuthsMapper
 *
 * @author yangyu
 * @date 2019-12-31
 */
@Mapper
public interface UserAuthsMapper extends CrudMapper<UserAuths> {

    /**
     * 根据唯一标识查询
     *
     * @param userAuths userAuths
     * @return UserAuths
     */
    UserAuths getByIdentifier(UserAuths userAuths);

    /**
     * 根据用户id批量查询
     *
     * @param userIds userIds
     * @return List
     */
    List<UserAuths> getListByUserIds(@Param("userIds") String[] userIds);

    /**
     * 根据唯一标识删除
     *
     * @param userAuths userAuths
     * @return int
     */
    int deleteByIdentifier(UserAuths userAuths);


    /**
     * 根据用户ID删除
     *
     * @param userAuths userAuths
     * @return int
     * @author tangyi
     * @date 2019/07/04 11:43:50
     */
    int deleteByUserId(UserAuths userAuths);

    /**
     * 批量插入
     *
     * @param userAuths userAuths
     * @return int
     * @author tangyi
     * @date 2019-09-03 13:07
     */
    int insertBatch(List<UserAuths> userAuths);
}
