package com.laogeli.user.api.feign;

import com.laogeli.common.core.constant.ServiceConstant;
import com.laogeli.common.core.model.Log;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.vo.AttachmentVo;
import com.laogeli.common.core.vo.LogVo;
import com.laogeli.common.core.vo.UserVo;
import com.laogeli.common.feign.config.CustomFeignConfig;
import com.laogeli.common.security.wx.WxUser;
import com.laogeli.user.api.dto.UserDto;
import com.laogeli.user.api.dto.UserInfoDto;
import com.laogeli.user.api.feign.factory.UserServiceClientFallbackFactory;
import com.laogeli.user.api.module.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户服务
 *
 * @author yangyu
 * @date 2019-12-31
 */
@FeignClient(value = ServiceConstant.USER_SERVICE, configuration = CustomFeignConfig.class, fallbackFactory = UserServiceClientFallbackFactory.class)
public interface UserServiceClient {

    /**
     * 根据用户名获取用户详细信息
     *
     * @param identifier identifier
     * @return ResponseBean
     * @author yangyu
     * @date 2019-12-31
     */
    @GetMapping("/v1/user/findUserByIdentifier/{identifier}")
    ResponseBean<UserVo> findUserByIdentifier(@PathVariable("identifier") String identifier);



    /**
     * 根据用户名和登录类型获取用户详细信息
     *
     * @param identifier   identifier
     * @param identityType identityType
     * @return ResponseBean
     * @author yangyu
     * @date 2019-12-31
     */
    @GetMapping("/v1/user/findUserByIdentifier/{identifier}")
    ResponseBean<UserVo> findUserByIdentifier(@PathVariable("identifier") String identifier, @RequestParam(value = "identityType", required = false) Integer identityType);

    /**
     * 根据phoneNumber获取wx用户信息
     * @param phoneNumber
     * @return
     */
    @GetMapping("/v1/user/findUserByPhoneNumber/{phoneNumber}")
    ResponseBean<UserVo> findUserByPhoneNumber(@PathVariable("phoneNumber")String phoneNumber,@RequestParam(value = "identityType", required = false) int loginType);

    /**
     * 获取当前用户的信息
     *
     * @return ResponseBean
     * @author yangyu
     * @date 2019-12-31
     */
    @GetMapping("/v1/user/info")
    ResponseBean<UserInfoDto> info();

    /**
     * 根据用户id获取用户
     *
     * @param ids ids
     * @return UserVo
     * @author yangyu
     * @date 2019-12-31
     */
    @RequestMapping(value = "/v1/user/findById", method = RequestMethod.POST)
    ResponseBean<List<UserVo>> findUserById(@RequestBody Long[] ids);

    /**
     * 查询用户数量
     *
     * @param userVo userVo
     * @return ResponseBean
     * @author yangyu
     * @date 2019-12-31
     */
    @RequestMapping(value = "/v1/user/userCount", method = RequestMethod.POST)
    ResponseBean<Integer> findUserCount(@RequestBody UserVo userVo);

    /**
     * 根据ID删除附件
     *
     * @param id id
     * @return ResponseBean
     * @author yangyu
     * @date 2019-12-31
     */
    @DeleteMapping("/v1/attachment/{id}")
    ResponseBean<Boolean> deleteAttachment(@PathVariable(value = "id") Long id);

    /**
     * 根据附件id获取附件
     *
     * @param ids ids
     * @return ResponseBean
     * @author yangyu
     * @date 2019-12-31
     */
    @RequestMapping(value = "/v1/attachment/findById", method = RequestMethod.POST)
    ResponseBean<List<AttachmentVo>> findAttachmentById(@RequestBody Long[] ids);

    /**
     * 保存日志
     *
     * @param log log
     * @return ResponseBean
     * @author yangyu
     * @date 2019-12-31
     */
    @PostMapping("/v1/log")
    ResponseBean<Boolean> saveLog(@RequestBody Log log);

    /**
     * 根据订单编号获取日志信息
     *
     * @param orderNumber orderNumber
     * @return List<LogVo>
     * @author yangyu
     * @date 2020-07-07
     */
    @GetMapping("/v1/log/getOrderLog/{orderNumber}")
    List<LogVo> getOrderLog(@PathVariable("orderNumber") String orderNumber);

    /**
     * 根据角色查找菜单
     *
     * @param role       角色
     * @return ResponseBean
     * @author yangyu
     * @date 2019-12-31
     */
    @GetMapping("/v1/menu/findMenuByRole/{role}")
    List<Menu> findMenuByRole(@PathVariable("role") String role);

    /**
     * 根据角色查找菜单
     *
     * @param role       角色
     * @return ResponseBean
     * @author yangyu
     * @date 2019-12-31
     */
    @GetMapping("/v1/menu/findMenuByStaffRole/{role}")
    List<Menu> findMenuByStaffRole(@PathVariable("role") String role);

    /**
     * 查询所有菜单
     *
     * @return ResponseBean
     * @author yangyu
     * @date 2019-12-31
     */
    @GetMapping("/v1/menu/findAllMenu")
    List<Menu> findAllMenu();

    /**
     * 注册用户
     *
     * @param userDto userDto
     * @return ResponseBean
     * @author yangyu
     * @date 2019-12-31
     */
    @PostMapping("/v1/user/register")
    ResponseBean<Boolean> registerUser(@RequestBody UserDto userDto);

    /**
     * 通过微信注册
     * @param userDto
     * @return
     */
    @PostMapping("/v1/user/registerByWx")
    ResponseBean<Boolean> registerByWx(@RequestBody UserDto userDto);

    /**
     * 更新用户
     *
     * @param userDto userDto
     * @return ResponseBean
     * @author yangyu
     * @date 2019-12-31
     */
    @PutMapping("/v1/user")
    ResponseBean<Boolean> updateUser(UserDto userDto);

    /**
     * 根据用户id获取用户头像
     *
     * @param id
     * @return User
     * @author yangyu
     * @date 2020-02-24
     */
    @GetMapping("/v1/attachment/{id}/preview")
    ResponseBean<String> getPreviewUrl(@PathVariable("id") String id);


    @PostMapping("/v1/message/insert")
    ResponseBean<Boolean> insertMessage(@RequestBody MessageInfo messageInfo);



}
