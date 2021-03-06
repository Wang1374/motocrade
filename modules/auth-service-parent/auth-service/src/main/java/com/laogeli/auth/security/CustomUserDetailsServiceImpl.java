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
 * ??????????????????????????????
 *
 * @author wang
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
     * ??????????????????
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
     * ??????????????????
     *
     * @param username ?????????
     * @return UserDetails
     * @throws UsernameNotFoundException
     * @author wang
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
     * ?????????????????????
     *
     * @param social     social
     * @param mobileUser mobileUser
     * @return UserDetails
     * @author wang
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
            throw new UsernameNotFoundException("??????????????????.");
        } else {
            // TODO ?????????????????????IP?????????
        }
        return new CustomUserDetails(userVo.getIdentifier(), userVo.getCredential(), CommonConstant.STATUS_NORMAL.equals(userVo.getStatus()), getAuthority(userVo), start, LoginType.SMS);
    }


    @Override
    public UserDetails loadUserByWxCode(String code, WxUser wxUser) throws UsernameNotFoundException {
        long start = System.currentTimeMillis();
        // ??????code??????openId???sessionKey
        WxSession wxSession = wxService.code2Session(code);
        if (wxSession == null)
            throw new CommonException("??????openId??????.");
        wxUser.setOpenId(wxSession.getOpenId());
        //????????????????????????
        String decrypt = AESUtil.wxDecrypt(wxUser.getEncryptedData(), wxSession.getSessionKey(), wxUser.getIv());
        System.err.println(decrypt);
//        TODO??????
        JSONObject parse = JSONObject.parseObject(decrypt);
        String wxPhoneNumber = parse.getString("phoneNumber");

//        String wxPhoneNumber = "13255288043";

        System.out.println("????????????"+wxPhoneNumber);
        // ??????????????????  ?????????????????????
        ResponseBean<UserVo> userVoResponseBean = userServiceClient.findUserByPhoneNumber(wxPhoneNumber, IdentityType.WE_CHAT.getValue());
        if (!ResponseUtil.isSuccess(userVoResponseBean))
            throw new ServiceException(GET_USER_INFO_FAIL + userVoResponseBean.getMsg());
        UserVo userVo = userVoResponseBean.getData();
        // ???????????????????????????????????????????????????????????????????????????
        if (userVo == null) {
            UserDto userDto = new UserDto();
            // ?????????????????????
            if (wxUser != null)
                userDto.setWxUser(wxUser);
            userDto.setIdentifier(wxPhoneNumber);
            userDto.setCredential(wxPhoneNumber);
            userDto.setIdentityType(IdentityType.WE_CHAT.getValue());
            userDto.setLoginTime(DateUtils.asDate(LocalDateTime.now()));
            // ????????????
            ResponseBean<Boolean> response = userServiceClient.registerByWx(userDto);
            if (response == null || !response.getData())
                throw new CommonException("????????????????????????.");
            // ????????????????????????
            userVo =  userServiceClient.findUserByPhoneNumber(wxPhoneNumber, IdentityType.WE_CHAT.getValue()).getData();
        } else {
            // TODO ??????sessionKey????????????????????????IP?????????
        }
        return new CustomUserDetails(userVo.getIdentifier(), userVo.getCredential(), CommonConstant.STATUS_NORMAL.equals(userVo.getStatus()), getAuthority(userVo), start, LoginType.WECHAT);
    }





    /**
     * ??????????????????
     *
     * @param userVo userVo
     * @return Set
     * @author wang
     * @date 2019-12-31
     */
    private Set<GrantedAuthority> getAuthority(UserVo userVo) {
        // ????????????
        Set<GrantedAuthority> authorities = new HashSet<>();
        // ??????????????????????????????
        List<Menu> menus = Lists.newArrayList();
        // ?????????????????????????????????????????????????????????
        if (userVo.getIdentifier().equals(sysProperties.getAdminUser())) {
            // ?????????????????????????????????????????????????????????????????????????????????
            menus = userServiceClient.findAllMenu();
        } else {
            // ??????????????????????????????
            List<RoleVo> roleList = userVo.getRoleList();
            if (CollectionUtils.isNotEmpty(roleList)) {
                for (RoleVo role : roleList) {
                    // ??????????????????????????????
                    // ?????? ????????????????????????????????????code ??????????????????????????????????????????????????????
                    if ("-1".equals(userVo.getEmployeeId())) {
                        List<Menu> roleMenus = userServiceClient.findMenuByRole(role.getRoleCode());
                        if (CollectionUtils.isNotEmpty(roleMenus))
                            menus.addAll(roleMenus);
                    } else {
                        List<Menu> roleMenus = userServiceClient.findMenuByStaffRole(role.getRoleCode());
                        if (CollectionUtils.isNotEmpty(roleMenus))
                            menus.addAll(roleMenus);
                    }
                    // ?????????????????????ROLE_???security????????????????????????????????????????????????????????????ROLE_ADMIN??????ADMIN?????????MENU:ADD??????MENU:ADD??????
                    authorities.add(new GrantedAuthorityImpl(role.getRoleCode()));
                }
            }
        }
        if (CollectionUtils.isNotEmpty(menus)) {
            // ????????????
            List<GrantedAuthority> authorityList = menus.stream()
                    .filter(menu -> MenuConstant.MENU_TYPE_PERMISSION.equals(menu.getMenuType()))
                    .map(menu -> new GrantedAuthorityImpl(menu.getPermission())).collect(Collectors.toList());
            authorities.addAll(authorityList);
        }
        return authorities;
    }
}
