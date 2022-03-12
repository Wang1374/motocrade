package com.laogeli.auth.security;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.laogeli.auth.api.module.WxSession;
import com.laogeli.auth.model.CustomUserDetails;
import com.laogeli.auth.service.WxSessionService;
import com.laogeli.auth.utils.AESUtil;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.enums.LoginType;
import com.laogeli.common.core.exceptions.CommonException;
import com.laogeli.common.core.exceptions.ServiceException;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.properties.SysProperties;
import com.laogeli.common.core.utils.DateUtils;
import com.laogeli.common.core.utils.ResponseUtil;
import com.laogeli.common.core.vo.RoleVo;
import com.laogeli.common.core.vo.UserVo;
import com.laogeli.common.security.core.CustomUserDetailsService;
import com.laogeli.common.security.core.GrantedAuthorityImpl;
import com.laogeli.common.security.mobile.MobileUser;
import com.laogeli.common.security.wx.WxUser;
import com.laogeli.user.api.constant.MenuConstant;
import com.laogeli.user.api.dto.UserDto;
import com.laogeli.user.api.enums.IdentityType;
import com.laogeli.user.api.feign.UserServiceClient;
import com.laogeli.user.api.module.Menu;
import lombok.AllArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 从数据库获取用户信息
 *
 * @author yangyu
 * @date 2019-12-31
 */
@AllArgsConstructor
@Service("userDetailsService")
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final UserServiceClient userServiceClient;

    private final SysProperties sysProperties;

    private final WxSessionService wxService;

    private static final String GET_USER_INFO_FAIL = "get user information failed: ";

    /**
     * 加载用户信息
     *
     * @param username username
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        long start = System.currentTimeMillis();
        ResponseBean<UserVo> userVoResponseBean = userServiceClient.findUserByIdentifier(username);
        if (!ResponseUtil.isSuccess(userVoResponseBean))
            throw new ServiceException(GET_USER_INFO_FAIL + userVoResponseBean.getMsg());
        UserVo userVo = userVoResponseBean.getData();
        if (userVo == null)
            throw new UsernameNotFoundException("user does not exist");
        return new CustomUserDetails(username, userVo.getCredential(), CommonConstant.STATUS_NORMAL.equals(userVo.getStatus()), getAuthority(userVo), start, LoginType.PWD);
    }

    /**
     * 加载用户信息
     *
     * @param username 用户名
     * @return UserDetails
     * @throws UsernameNotFoundException
     * @author yangyu
     * @date 2019-12-31
     */
    @Override
    public UserDetails loadUserByIdentifier(String username) throws UsernameNotFoundException {
        long start = System.currentTimeMillis();
        ResponseBean<UserVo> userVoResponseBean = userServiceClient.findUserByIdentifier(username);
        if (!ResponseUtil.isSuccess(userVoResponseBean))
            throw new ServiceException(GET_USER_INFO_FAIL + userVoResponseBean.getMsg());
        UserVo userVo = userVoResponseBean.getData();
        if (userVo == null)
            throw new UsernameNotFoundException("user does not exist");
        return new CustomUserDetails(username, userVo.getCredential(), CommonConstant.STATUS_NORMAL.equals(userVo.getStatus()), getAuthority(userVo), start, LoginType.PWD);
    }

    /**
     * 根据手机号查询
     *
     * @param social     social
     * @param mobileUser mobileUser
     * @return UserDetails
     * @author yangyu
     * @date 2019-12-31
     */
    @Override
    public UserDetails loadUserBySocial(String social, MobileUser mobileUser) throws UsernameNotFoundException {
        long start = System.currentTimeMillis();
        ResponseBean<UserVo> userVoResponseBean = userServiceClient.findUserByIdentifier(social, IdentityType.PHONE_NUMBER.getValue());
        if (!ResponseUtil.isSuccess(userVoResponseBean))
            throw new ServiceException(GET_USER_INFO_FAIL + userVoResponseBean.getMsg());
        UserVo userVo = userVoResponseBean.getData();
        if (userVo == null) {
            throw new UsernameNotFoundException("用户名不存在.");
        } else {
            // TODO 记录登录时间，IP等信息
        }
        return new CustomUserDetails(userVo.getIdentifier(), userVo.getCredential(), CommonConstant.STATUS_NORMAL.equals(userVo.getStatus()), getAuthority(userVo), start, LoginType.SMS);
    }


    @Override
    public UserDetails loadUserByWxCode(String code, WxUser wxUser) throws UsernameNotFoundException {
        long start = System.currentTimeMillis();
        // 根据code获取openId和sessionKey
        WxSession wxSession = wxService.code2Session(code);
        if (wxSession == null)
            throw new CommonException("获取openId失败.");
        wxUser.setOpenId(wxSession.getOpenId());
        //解密微信用户信息
        String decrypt = AESUtil.wxDecrypt(wxUser.getEncryptedData(), wxSession.getSessionKey(), wxUser.getIv());
        System.err.println(decrypt);
//        TODO　　
        JSONObject parse = JSONObject.parseObject(decrypt);
        String wxPhoneNumber = parse.getString("phoneNumber");

//        String wxPhoneNumber = "13255288043";

        System.out.println("手机号："+wxPhoneNumber);
        // 获取用户信息  根据用户手机号
        ResponseBean<UserVo> userVoResponseBean = userServiceClient.findUserByPhoneNumber(wxPhoneNumber, IdentityType.WE_CHAT.getValue());
        if (!ResponseUtil.isSuccess(userVoResponseBean))
            throw new ServiceException(GET_USER_INFO_FAIL + userVoResponseBean.getMsg());
        UserVo userVo = userVoResponseBean.getData();
        // 为空说明是第一次登录，需要将用户信息增加到数据库里
        if (userVo == null) {
            UserDto userDto = new UserDto();
            // 用户的基本信息
            if (wxUser != null)
                userDto.setWxUser(wxUser);
            userDto.setIdentifier(wxPhoneNumber);
            userDto.setCredential(wxPhoneNumber);
            userDto.setIdentityType(IdentityType.WE_CHAT.getValue());
            userDto.setLoginTime(DateUtils.asDate(LocalDateTime.now()));
            // 注册账号
            ResponseBean<Boolean> response = userServiceClient.registerByWx(userDto);
            if (response == null || !response.getData())
                throw new CommonException("自动注册用户失败.");
            // 重新获取用户信息
            userVo =  userServiceClient.findUserByPhoneNumber(wxPhoneNumber, IdentityType.WE_CHAT.getValue()).getData();
        } else {
            // TODO 更新sessionKey，记录登录时间，IP等信息
        }
        return new CustomUserDetails(userVo.getIdentifier(), userVo.getCredential(), CommonConstant.STATUS_NORMAL.equals(userVo.getStatus()), getAuthority(userVo), start, LoginType.WECHAT);
    }





    /**
     * 获取用户权限
     *
     * @param userVo userVo
     * @return Set
     * @author yangyu
     * @date 2019-12-31
     */
    private Set<GrantedAuthority> getAuthority(UserVo userVo) {
        // 权限集合
        Set<GrantedAuthority> authorities = new HashSet<>();
        // 根据角色查找菜单权限
        List<Menu> menus = Lists.newArrayList();
        // 判断是否是管理员，是则查找所有菜单权限
        if (userVo.getIdentifier().equals(sysProperties.getAdminUser())) {
            // 查找所有菜单权限，因为角色一般是一个，这里只会执行一次
            menus = userServiceClient.findAllMenu();
        } else {
            // 根据角色查询菜单权限
            List<RoleVo> roleList = userVo.getRoleList();
            if (CollectionUtils.isNotEmpty(roleList)) {
                for (RoleVo role : roleList) {
                    // 根据角色查找菜单权限
                    // 这里 如果是员工，根据员工角色code 去查询权限，而不是继续查询系统权限了
                    if ("-1".equals(userVo.getEmployeeId())) {
                        List<Menu> roleMenus = userServiceClient.findMenuByRole(role.getRoleCode());
                        if (CollectionUtils.isNotEmpty(roleMenus))
                            menus.addAll(roleMenus);
                    } else {
                        List<Menu> roleMenus = userServiceClient.findMenuByStaffRole(role.getRoleCode());
                        if (CollectionUtils.isNotEmpty(roleMenus))
                            menus.addAll(roleMenus);
                    }
                    // 权限如果前缀是ROLE_，security就会认为这是个角色信息，而不是权限，例如ROLE_ADMIN就是ADMIN角色，MENU:ADD就是MENU:ADD权限
                    authorities.add(new GrantedAuthorityImpl(role.getRoleCode()));
                }
            }
        }
        if (CollectionUtils.isNotEmpty(menus)) {
            // 菜单权限
            List<GrantedAuthority> authorityList = menus.stream()
                    .filter(menu -> MenuConstant.MENU_TYPE_PERMISSION.equals(menu.getMenuType()))
                    .map(menu -> new GrantedAuthorityImpl(menu.getPermission())).collect(Collectors.toList());
            authorities.addAll(authorityList);
        }
        return authorities;
    }
}
