package com.laogeli.common.security.core;

import com.laogeli.common.security.mobile.MobileUser;
import com.laogeli.common.security.wx.WxUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 查询用户信息接口
 *
 * @author yangyu
 * @date 2020-7-31
 */
public interface CustomUserDetailsService extends UserDetailsService {

    /**
     * 根据用户名查询
     *
     * @param username   username
     * @return UserDetails
     * @author yangyu
     * @date 2019-12-31
     */
    UserDetails loadUserByIdentifier(String username) throws UsernameNotFoundException;

    /**
     * 根据手机号
     *
     * @param social     social
     * @param mobileUser mobileUser
     * @return UserDetails
     * @author yangyu
     * @date 2019-12-31
     */
    UserDetails loadUserBySocial(String social, MobileUser mobileUser) throws UsernameNotFoundException;


    /**
    *
    * @Desecription: 根据微信openId查询
    * @Param: code
    * @Return: wxUser
    * @Author: yangyu
    * @Date: 2021/8/25 21:30
    */
    UserDetails loadUserByWxCode(String code, WxUser wxUser) throws UsernameNotFoundException;
}
