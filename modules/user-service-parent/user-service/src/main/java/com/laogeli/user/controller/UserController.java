package com.laogeli.user.controller;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.exceptions.CommonException;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.*;
import com.laogeli.common.core.vo.UserVo;
import com.laogeli.common.core.web.BaseController;
import com.laogeli.common.log.annotation.Log;
import com.laogeli.common.security.constant.SecurityConstant;
import com.laogeli.order.api.module.Vehicle;
import com.laogeli.user.api.dto.UserDto;
import com.laogeli.user.api.dto.UserInfoDto;
import com.laogeli.user.api.module.Role;
import com.laogeli.user.api.module.User;
import com.laogeli.user.api.module.UserAuths;
import com.laogeli.user.api.module.UserRole;
import com.laogeli.user.api.vo.EmployeeVo;
import com.laogeli.user.mapper.UserMapper;
import com.laogeli.user.service.UserAuthsService;
import com.laogeli.user.service.UserRoleService;
import com.laogeli.user.service.UserService;
import com.laogeli.user.utils.UserUtils;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 用户信息管理
 *
 * @author wang
 * @date 2019-12-31
 */
@Slf4j
@AllArgsConstructor
@Api("用户信息管理")
@RestController
@RequestMapping(value = "/v1/user")
public class UserController extends BaseController {

    private final UserService userService;

    private final UserMapper userMapper;

    private final UserRoleService userRoleService;

    private final UserAuthsService userAuthsService;

    private StringRedisTemplate stringRedisTemplate;

    /**
     * 根据id获取
     *
     * @param id id
     * @return ResponseBean
     */
    @GetMapping("getUserById/{id}")
    @ApiOperation(value = "获取用户信息", notes = "根据用户id获取用户详细信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "String", paramType = "path")
    public ResponseBean<User> getUserById(@PathVariable String id) {
        User user = new User();
        user.setId(id);

        return new ResponseBean<>(userService.get(user));
    }


    /**
     * 获取当前用户信息（角色、权限）
     *
     * @return 用户名
     */
    @GetMapping("info")
    @ApiOperation(value = "获取用户信息", notes = "获取当前登录用户详细信息")
    @ApiImplicitParam(name = "identityType", value = "账号类型", required = true, dataType = "String")
    public ResponseBean<UserInfoDto> userInfo(@RequestParam(required = false) String identityType, OAuth2Authentication authentication) {
        try {

            UserVo userVo = new UserVo();
            if (StringUtils.isNotEmpty(identityType))
                userVo.setIdentityType(Integer.valueOf(identityType));
            userVo.setIdentifier(authentication.getName());
            return new ResponseBean<>(userService.findUserInfo(userVo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException("获取当前登录用户详细信息");
        }
    }


    /**
     * 根据用户唯一标识获取用户详细信息
     *
     * @param identifier   identifier
     * @param identityType identityType
     * @return UserVo
     */
    @GetMapping("/findUserByIdentifier/{identifier}")
    @ApiOperation(value = "获取用户信息", notes = "根据用户name获取用户详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "identifier", value = "用户唯一标识", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "identityType", value = "用户授权类型", dataType = "Integer")
    })
    public ResponseBean<UserVo> findUserByIdentifier(@PathVariable String identifier, @RequestParam(required = false) Integer identityType) {
        System.out.println(identifier);
        return new ResponseBean<>(userService.findUserByIdentifier(identityType, identifier));
    }


    /**
     * 获取分页数据
     *
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @param sort     sort
     * @param order    order
     * @param userVo   userVo
     * @return PageInfo
     */
    @GetMapping("userList")
    @ApiOperation(value = "获取用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "userVo", value = "用户信息", dataType = "UserVo")
    })
    public PageInfo<UserDto> userList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                      @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                      @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                      @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                      @RequestParam(value = "name", required = false, defaultValue = "") String name,
                                      @RequestParam(value = "phone", required = false, defaultValue = "") String phone,
                                      @RequestParam(value = "status", required = false, defaultValue = "") Integer status,
                                      @RequestParam(value = "beginTime", required = false, defaultValue = "") String beginTime,
                                      @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime,
                                      UserVo userVo) {
        PageInfo<UserDto> userDtoPageInfo = new PageInfo<>();
        List<UserDto> userDtos = Lists.newArrayList();
        User user = new User();
        BeanUtils.copyProperties(userVo, user);
        user.setName(name);
        user.setPhone(phone);
        user.setStatus(status);
        user.setBeginTime(beginTime);
        user.setEndTime(endTime);
        PageInfo<User> page = userService.findUserPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), user);
        List<User> users = page.getList();
        if (CollectionUtils.isNotEmpty(users)) {
            // 批量查询账户
            List<UserAuths> userAuths = userAuthsService.getListByUsers(users);
            // 查询用户角色关联关系
            List<UserRole> userRoles = userRoleService.getByUserIds(users.stream().map(User::getId).collect(Collectors.toList()));
            // 批量查找角色
            List<Role> finalRoleList = userService.getUsersRoles(users);
            // 组装数据
            users.forEach(tempUser -> userDtos.add(userService.getUserDtoByUserAndUserAuths(tempUser, userAuths, userRoles, finalRoleList)));
        }
        PageUtil.copyProperties(page, userDtoPageInfo);
        userDtoPageInfo.setList(userDtos);
        return userDtoPageInfo;
    }

    /**
     * 创建用户
     *
     * @param userDto userDto
     * @return ResponseBean
     */
    @PostMapping
    // @PreAuthorize("hasAuthority('sys:user:add') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "创建用户", notes = "创建用户")
    @ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
    @Log(value = "新增用户", type = 0)
    public ResponseBean<Boolean> addUser(@RequestBody @Valid UserDto userDto) {
        return new ResponseBean<>(userService.createUser(userDto) > 0);
    }

    /**
     * 更新用户
     *
     * @param userDto userDto
     * @return ResponseBean
     */
    @PutMapping
    // @PreAuthorize("hasAuthority('sys:user:edit') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "更新用户信息", notes = "根据用户id更新用户的基本信息、角色信息")
    @ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
    @Log(value = "修改用户", type = 0)
    public ResponseBean<Boolean> updateUser(@RequestBody UserDto userDto) {
        try {
            return new ResponseBean<>(userService.updateUser(userDto));
        } catch (Exception e) {
            log.error("更新用户信息失败！", e);
            throw new CommonException("更新用户信息失败！");
        }
    }

    /**
     * 更新用户的基本信息
     *
     * @param userDto userDto
     * @return ResponseBean
     */
    @PutMapping("updateInfo")
    @ApiOperation(value = "更新用户基本信息", notes = "根据用户id更新用户的基本信息")
    @ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
    @Log(value = "更新用户基本信息", type = 0)
    public ResponseBean<Boolean> updateInfo(@RequestBody UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        user.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
        return new ResponseBean<>(userService.update(user,userDto.getCompanyId(),userDto.getIdentityType()) > 0);
    }

    /**
     * 状态修改
     */
    @PutMapping("/changeStatus")
    @PreAuthorize("hasAuthority('sys:user:edit') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "更新用户状态", notes = "根据用户id更新用户状态")
    @ApiImplicitParam(name = "User", value = "用户实体user", required = true, dataType = "User")
    @Log(value = "更新用户状态", type = 0)
    public ResponseBean<Boolean> changeStatus(@RequestBody UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        user.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
        return new ResponseBean<>(userService.update(user) > 0);
    }

    /**
     * 修改密码
     *
     * @param userDto userDto
     * @return ResponseBean
     */
    @PutMapping("updatePassword")
    @ApiOperation(value = "修改用户密码", notes = "修改用户密码")
    @ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
    @Log(value = "更新用户密码", type = 0)
    public ResponseBean<Boolean> updatePassword(@RequestBody UserDto userDto) {
        return new ResponseBean<>(userService.updatePassword(userDto) > 0);
    }

    /**
     * 更新头像
     *
     * @param userDto userDto
     * @return ResponseBean
     */
    @PutMapping("updateAvatar")
    @ApiOperation(value = "更新用户头像", notes = "根据用户id更新用户的头像信息")
    @ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
    @Log(value = "更新用户头像", type = 0)
    public ResponseBean<Boolean> updateAvatar(@RequestBody UserDto userDto) {
        return new ResponseBean<>(userService.updateAvatar(userDto) > 0);
    }

    /**
     * 删除用户
     *
     * @param id id
     * @return ResponseBean
     */
    @DeleteMapping("/{id}")
    // @PreAuthorize("hasAuthority('sys:user:del') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "删除用户", notes = "根据ID删除用户")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "path")
    @Log(value = "删除用户", type = 0)
    public ResponseBean<Boolean> deleteUser(@PathVariable String id) {
        try {
            User user = new User();
            user.setId(id);
            user = userService.get(user);
            user.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
            return new ResponseBean<>(userService.delete(user) > 0);
        } catch (Exception e) {
            log.error("删除用户信息失败！", e);
            throw new CommonException("删除用户信息失败！");
        }
    }

    /**
     * 导出
     *
     * @param userVo userVo
     */
    @PostMapping("export")
    @PreAuthorize("hasAuthority('sys:user:export') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "导出用户", notes = "根据用户id导出用户")
    @ApiImplicitParam(name = "userVo", value = "用户信息", required = true, dataType = "UserVo")
    @Log(value = "导出用户", type = 0)
    public void exportUser(@RequestBody UserVo userVo, HttpServletRequest request, HttpServletResponse response) {
        try {
            // 配置response
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, Servlets.getDownName(request, "用户信息" + DateUtils.localDateMillisToString(LocalDateTime.now()) + ".xlsx"));
            List<User> users;
            if (StringUtils.isNotEmpty(userVo.getIdString())) {
                User user = new User();
                user.setIds(Stream.of(userVo.getIdString().split(",")).filter(StringUtils::isNotBlank).distinct().toArray(String[]::new));
                users = userService.findListById(user);
            } else {
                // 导出本租户下的全部用户
                User user = new User();
                users = userService.findList(user);
            }
            if (CollectionUtils.isEmpty(users))
                throw new CommonException("无用户数据.");
            // 查询用户授权信息
            List<UserAuths> userAuths = userAuthsService.getListByUsers(users);
            // 组装数据，转成dto
            List<UserInfoDto> userInfoDtos = users.stream().map(tempUser -> {
                UserInfoDto userDto = new UserInfoDto();
                userAuths.stream()
                        .filter(userAuth -> userAuth.getUserId().equals(tempUser.getId()))
                        .findFirst()
                        .ifPresent(userAuth -> UserUtils.toUserInfoDto(userDto, tempUser, userAuth));
                return userDto;
            }).collect(Collectors.toList());
            ExcelToolUtil.exportExcel(request.getInputStream(), response.getOutputStream(), MapUtil.java2Map(userInfoDtos), UserUtils.getUserMap());
        } catch (Exception e) {
            log.error("导出用户数据失败！", e);
            throw new CommonException("导出用户数据失败！");
        }
    }

    /**
     * 导入数据
     *
     * @param file file
     * @return ResponseBean
     */
    @PostMapping("import")
//    @PreAuthorize("hasAuthority('sys:user:import') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "导入数据", notes = "导入数据")
    @Log(value = "导入用户", type = 0)
    public ResponseBean<Boolean> importUser(@ApiParam(value = "要上传的文件", required = true) MultipartFile file) {
        try {
            log.debug("开始导入用户数据");
            List<UserInfoDto> userInfoDtos = MapUtil.map2Java(UserInfoDto.class,
                    ExcelToolUtil.importExcel(file.getInputStream(), UserUtils.getUserMap()));
            if (CollectionUtils.isEmpty(userInfoDtos))
                throw new CommonException("无用户数据导入.");
            return new ResponseBean<>(userService.importUsers(userInfoDtos));
        } catch (Exception e) {
            log.error("导入用户数据失败！", e);
            throw new CommonException("导入用户数据失败！");
        }
    }

    /**
     * 批量删除
     *
     * @param user user
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @PreAuthorize("hasAuthority('sys:user:del') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "批量删除用户", notes = "根据用户id批量删除用户")
    @ApiImplicitParam(name = "user", value = "用户信息", dataType = "user")
    @Log(value = "批量删除用户", type = 0)
    public ResponseBean<Boolean> deleteAllUsers(@RequestBody User user) {
        try {
            boolean success = Boolean.FALSE;
            if (user.getIds().length > 0)
                success = userService.deleteAll(user.getIds()) > 0;
            return new ResponseBean<>(success);
        } catch (Exception e) {
            log.error("删除用户失败！", e);
            throw new CommonException("删除用户失败！");
        }
    }

    /**
     * 根据ID批量查询
     *
     * @param userVo userVo
     * @return ResponseBean
     */
    @PostMapping(value = "findById")
    @ApiOperation(value = "根据ID查询用户", notes = "根据ID查询用户")
    @ApiImplicitParam(name = "UserVo", value = "UserVo", required = true, paramType = "UserVo")
    public ResponseBean<List<UserVo>> findById(@RequestBody UserVo userVo) {
        return new ResponseBean<>(userService.findUserVoListById(userVo));
    }

    /**
     * 注册
     *
     * @param userDto userDto
     * @return ResponseBean
     * @author wang
     * @date 2019/01/10 22:35
     */
    @ApiOperation(value = "注册", notes = "注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "identifier", value = "手机号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "companyName", value = "公司名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "credential", value = "密码", dataType = "String", paramType = "query")
    })
    @PostMapping("register")
    @Log(value = "注册用户", type = 0)
    public ResponseBean<Boolean> register(@RequestBody @Valid UserDto userDto) {
        return new ResponseBean<>(userService.register(userDto));
    }


    /**
     * 通过微信注册
     *
     * @param userDto userDto
     * @return ResponseBean
     * @author wang
     * @date 2019/01/10 22:35
     */
    @ApiOperation(value = "注册", notes = "注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "identifier", value = "手机号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "companyName", value = "公司名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "credential", value = "密码", dataType = "String", paramType = "query")
    })
    @PostMapping("registerByWx")
    @Log(value = "注册用户", type = 0)
    public ResponseBean<Boolean> registerByWx(@RequestBody @Valid UserDto userDto) {
        System.out.println(userDto);
        return new ResponseBean<>(userService.registerByWx(userDto));
    }

    /**
     * 检查账号是否存在
     *
     * @param identityType identityType
     * @param identifier   identifier
     * @return ResponseBean
     * @author tangyi
     * @date 2019/04/23 15:35
     */
    @ApiOperation(value = "检查账号是否存在", notes = "检查账号是否存在")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "identityType", value = "用户唯一标识类型", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "identifier", value = "用户唯一标识", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("checkExist/{identifier}")
    public ResponseBean<Boolean> checkExist(@PathVariable("identifier") String identifier, @RequestParam Integer identityType) {
        return new ResponseBean<>(userService.checkIdentifierIsExist(identityType, identifier));
    }

    /**
     * 查询用户数量
     *
     * @param userVo userVo
     * @return ResponseBean
     */
    @PostMapping("userCount")
    public ResponseBean<Integer> userCount(UserVo userVo) {
        return new ResponseBean<>(userService.userCount(userVo));
    }

    /**
     * 重置密码
     *
     * @param userDto userDto
     * @return ResponseBean
     */
    @PutMapping("resetPassword")
    @ApiOperation(value = "重置密码", notes = "根据用户id重置密码")
    @ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
    @Log(value = "重置密码", type = 0)
    public ResponseBean<Boolean> resetPassword(@RequestBody UserDto userDto) {
        return new ResponseBean<>(userService.resetPassword(userDto));
    }

    /**
     * 获取手机验证码（code存在Redis上），并限制次数,半小时五次
     *
     * @param
     * @return ResponseBean
     * @author ynagyu
     */
    @ApiOperation(value = "获取验证码", notes = "获取验证码")
    @ApiImplicitParams({ @ApiImplicitParam(name = "phone", value = "手机号", dataType = "String", paramType = "query") })
    @GetMapping("getSmsCode")
    public ResponseBean<Boolean> getSmsCode(@RequestParam @NotBlank String phone) {
        //设置key 自动 +1
        long count = stringRedisTemplate.boundValueOps(CommonConstant.SMS_COUNT_PREFIX + phone).increment(1);
        if(count==1){
            //设置 30 分钟过期
            stringRedisTemplate.expire(CommonConstant.SMS_COUNT_PREFIX + phone, CommonConstant.SMS_COUNT_EXPIRE , TimeUnit.SECONDS);
        }
        if(count > 5){
            log.info("验证码发送频繁,超过了限定的次数");
            throw new CommonException("验证码发送频繁");
        }
        //String res = SendMsgUtil.sendMsg(phone);
        String res = "0;123456";
        String[] arr = res.split(";");
        String code = arr[1];
        String res_code = arr[0];

        if (StringUtils.isNotBlank(res_code)) {
            //发送成功，放入redis
            stringRedisTemplate.opsForValue().set(CommonConstant.SMS_CODE_PREFIX + phone, code, CommonConstant.SMS_EXPIRE, TimeUnit.SECONDS);
        }
        return new ResponseBean<>(true, "短信验证码成功");
    }

    /**
     * 效验手机验证码
     *
     * @param
     * @return ResponseBean
     * @author ynagyu
     */
    @ApiOperation(value = "效验手机验证码", notes = "效验手机验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "验证码", dataType = "String", paramType = "query")
    })
    @PostMapping("checkSmsCode")
    public ResponseBean<Boolean> checkSmsCode(@RequestParam @NotBlank String phone,
                                              @RequestParam @NotBlank String code,
                                              @RequestParam @NotBlank int type) {
        if (type == 2) {
            // 查询手机号是否注册
            UserAuths userAuths = new UserAuths();
            userAuths.setIdentifier(phone);
            UserAuths obj = userAuthsService.getByIdentifier(userAuths);
            if (obj != null) {
                throw new CommonException("该手机号已注册");
            }
        }
        if (!stringRedisTemplate.hasKey(CommonConstant.SMS_CODE_PREFIX + phone))
            throw new CommonException("手机验证码已过期，请重新获取");
        Object codeObj = stringRedisTemplate.opsForValue().get(CommonConstant.SMS_CODE_PREFIX + phone);
        if (codeObj == null)
            throw new CommonException("手机验证码已过期，请重新获取");
        String saveCode = codeObj.toString();
        if (!StrUtil.equals(saveCode, code)) {
            throw new CommonException("手机验证码错误");
        }
        return new ResponseBean<>(true);
    }

    /**
     * 获取企业所有账户
     *
     * @param corporateIdentify corporateIdentify
     * @return List<UserDto>
     */
    @GetMapping("getAllCorporateAccount/{corporateIdentify}")
    @ApiOperation(value = "获取企业所有账户")
    @ApiImplicitParams({ @ApiImplicitParam(name = "corporateIdentify", value = "企业标识id", dataType = "String") })
    public List<UserDto> getAllCorporateAccount(@PathVariable String corporateIdentify) {
        User user = new User();
        user.setEmployeeId(corporateIdentify);
        return userService.getAllCorporateAccount(user);
    }

    /**
     * 根据企业标识和车牌号获取受益人
     * @param licensePlateNumber
     * @return
     */
    @GetMapping("getSyrInfo")
    @ApiOperation(value = "获取车辆受益人")
    @ApiImplicitParams({@ApiImplicitParam(name = "corporateIdentify",value = "企业标识",defaultValue = "String"),
            @ApiImplicitParam(name = "licensePlateNumber",value = "车牌号",defaultValue = "String")})
    public List<String> getsyrInfo( @RequestParam(value = "licensePlateNumber")String licensePlateNumber){
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlateNumber(licensePlateNumber);

        return userMapper.findSyrInfo(vehicle);
    }


    /**
     * 根据企业标识获取公司所有员工
     * @return
     */
    @GetMapping("/getEmployee/{corporateIdentify}")
    @ApiOperation(value = "获取公司所有员工")
    @ApiImplicitParam(name = "corporateIdentify",value = "企业标识",defaultValue = "String")
    public List<EmployeeVo> getEmployeeVO(@PathVariable("corporateIdentify") String corporateIdentify){
        return  userMapper.getEmployeeVo(corporateIdentify);
    }

    /**
     * 根据openId获取微信用户信息
     * @param phoneNumber
     * @param loginType
     * @return
     */
    @GetMapping("/findUserByPhoneNumber/{phoneNumber}")
    @ApiOperation(value = "根据openId获取微信用户信息")
    public ResponseBean<UserVo> findUserByPhoneNumber(@PathVariable("phoneNumber")String phoneNumber,@RequestParam(required = false) Integer loginType){
        return new ResponseBean<>(userService.findUserByPhoneNumber(phoneNumber));
    }
}
