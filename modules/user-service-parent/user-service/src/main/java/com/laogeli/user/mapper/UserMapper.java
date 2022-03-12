package com.laogeli.user.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.common.core.vo.UserVo;
import com.laogeli.common.security.wx.WxUser;
import com.laogeli.order.api.module.Vehicle;
import com.laogeli.user.api.dto.UserDto;
import com.laogeli.user.api.module.User;
import com.laogeli.user.api.vo.EmployeeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户mapper接口
 *
 * @author yangyu
 * @date 2019-12-31
 */
@Mapper
public interface UserMapper extends CrudMapper<User> {

    /**
     * 查询用户数量
     *
     * @param userVo userVo
     * @return Integer
     */
    Integer userCount(UserVo userVo);

    /**
     * 获取企业所有账户
     *
     * @param user
     * @return List<UserDto>
     */
    List<UserDto> getAllCorporateAccount(User user);

    /**
     * 获取受益人
     * @param vehicle
     * @return
     */
    List<String> findSyrInfo(Vehicle vehicle);

    /**
     * 根据企业标识获取公司所有员工
     * @param corporateIdentify
     * @return
     */
    List<EmployeeVo> getEmployeeVo(String corporateIdentify);

    /**
     * 根据openId获取用户信息
     * @param openId
     * @return
     */
//    UserVo findUserByOpenId(String openId);

    /**
     * 添加微信用户信息
     * @param wxUser
     * @return
     */
    int insertWechat(WxUser wxUser);
}
