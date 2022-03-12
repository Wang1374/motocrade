package com.laogeli.user.api.feign.fallback;

import com.laogeli.common.core.model.Log;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.vo.AttachmentVo;
import com.laogeli.common.core.vo.LogVo;
import com.laogeli.common.core.vo.UserVo;
import com.laogeli.user.api.dto.UserDto;
import com.laogeli.user.api.dto.UserInfoDto;
import com.laogeli.user.api.feign.UserServiceClient;
import com.laogeli.user.api.module.Menu;
import com.laogeli.user.api.module.MessageInfo;
import com.laogeli.user.api.module.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 日志断路器实现
 *
 * @author yangyu
 * @date 2019/12/23 23:39
 */
@Slf4j
@Component
public class UserServiceClientFallbackImpl implements UserServiceClient {

    private Throwable throwable;

    /**
     * 根据用户名查询用户信息
     *
     * @param identifier identifier
     * @return ResponseBean
     */
    @Override
    public ResponseBean<UserVo> findUserByIdentifier(String identifier) {
        log.error("feign 查询用户信息失败:{}, {}, {}", identifier, throwable);
        return null;
    }

    /**
     * 根据用户名查询用户信息
     *
     * @param identifier   identifier
     * @param identityType identityType
     * @return ResponseBean
     */
    @Override
    public ResponseBean<UserVo> findUserByIdentifier(String identifier, Integer identityType) {
        log.error("feign 查询用户信息失败:{}, {}, {}, {}", identityType, identifier, throwable);
        return null;
    }

    /**
     * 查询当前登录的用户信息
     *
     * @return ResponseBean
     */
    @Override
    public ResponseBean<UserInfoDto> info() {
        log.error("feign 查询用户信息失败:{},{}", throwable);
        return null;
    }

    /**
     * 根据用户ID批量查询用户信息
     *
     * @param ids ids
     * @return ResponseBean
     */
    @Override
    public ResponseBean<List<UserVo>> findUserById(@RequestBody Long[] ids) {
        log.error("调用{}异常:{},{}", "findById", ids, throwable);
        return null;
    }

    /**
     * 查询用户数量
     *
     * @param userVo userVo
     * @return ResponseBean
     */
    @Override
    public ResponseBean<Integer> findUserCount(UserVo userVo) {
        log.error("调用{}异常:{},{}", "findUserCount", userVo, throwable);
        return new ResponseBean<>(0);
    }

    /**
     * 根据附件ID删除附件
     *
     * @param id id
     * @return ResponseBean
     */
    @Override
    public ResponseBean<Boolean> deleteAttachment(Long id) {
        log.error("调用{}异常:{},{}", "delete", id, throwable);
        return new ResponseBean<>(Boolean.FALSE);
    }

    /**
     * 根据附件ID批量查询附件信息
     *
     * @param ids ids
     * @return ResponseBean
     */
    @Override
    public ResponseBean<List<AttachmentVo>> findAttachmentById(Long[] ids) {
        log.error("调用{}异常:{},{}", "findById", ids, throwable);
        return new ResponseBean<>(new ArrayList<>());
    }

    /**
     * 保存日志
     *
     * @param logInfo logInfo
     * @return ResponseBean
     */
    @Override
    public ResponseBean<Boolean> saveLog(Log logInfo) {
        log.error("feign 插入日志失败,{}", throwable);
        return null;
    }

    /**
     * 根据订单编号获取日志信息
     *
     * @param orderNumber orderNumber
     * @return List<LogVo>
     */
    @Override
    public List<LogVo> getOrderLog(String orderNumber) {
        log.error("feign 根据订单编号获取日志信息失败,{}", throwable);
        return null;
    }

    /**
     * 根据角色查找菜单
     *
     * @param role       角色
     * @return ResponseBean
     */
    @Override
    public List<Menu> findMenuByRole(String role) {
        log.error("feign 获取角色菜单失败, {}, {}", throwable);
        return new ArrayList<>();
    }

    /**
     * 根据角色查找菜单
     *
     * @param role       角色
     * @return ResponseBean
     */
    @Override
    public List<Menu> findMenuByStaffRole(String role) {
        log.error("feign 获取角色菜单失败, {}, {}", throwable);
        return null;
    }

    /**
     * 查询所有菜单
     *
     * @return ResponseBean
     */
    @Override
    public List<Menu> findAllMenu() {
        log.error("feign 获取所有菜单失败, {}, {}", throwable);
        return new ArrayList<>();
    }

    /**
     * 注册用户
     *
     * @param userDto userDto
     * @return ResponseBean
     */
    @Override
    public ResponseBean<Boolean> registerUser(UserDto userDto) {
        log.error("feign 注册用户失败, {}, {}, {}", userDto.getIdentityType(), userDto.getIdentifier(), throwable);
        return null;
    }

    /**
     * 通过微信注册用户失败
     * @param userDto
     * @return
     */
    @Override
    public ResponseBean<Boolean> registerByWx(UserDto userDto) {
        log.error("feign 注册用户失败,{},{},{}",userDto.getIdentityType(), userDto.getIdentifier(), throwable);
        return null;
    }

    /**
     * 更新用户
     *
     * @param userDto userDto
     * @return ResponseBean
     */
    @Override
    public ResponseBean<Boolean> updateUser(UserDto userDto) {
        log.error("feign 更新用户失败, {}, {}, {}", userDto.getIdentityType(), userDto.getIdentifier(), throwable);
        return null;
    }

    @Override
    public ResponseBean<String> getPreviewUrl(String id) {
        log.error("feign 获取头像地址失败, {}, {}", throwable);
        return null;
    }

    @Override
    public ResponseBean<Boolean> insertMessage(MessageInfo messageInfo) {
        log.error("feign 新增消息失败, {}",throwable);
        return null;
    }

    @Override
    public ResponseBean<UserVo> findUserByPhoneNumber(String openId, int loginType) {
        System.out.println(openId);
        System.out.println(loginType);
        log.error("feign 查询用户信息失败",throwable);
        return null;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
