package com.laogeli.user.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.enums.LoginType;
import com.laogeli.common.core.exceptions.CommonException;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.properties.SysProperties;
import com.laogeli.common.core.service.CrudService;
import com.laogeli.common.core.utils.AesUtil;
import com.laogeli.common.core.utils.DateUtils;
import com.laogeli.common.core.utils.IdGen;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.common.core.vo.CompanyVo;
import com.laogeli.common.core.vo.UserVo;
import com.laogeli.common.security.constant.SecurityConstant;
import com.laogeli.common.security.wx.WxUser;
import com.laogeli.order.api.feign.OrderServiceClient;
import com.laogeli.order.api.module.Driver;
import com.laogeli.user.api.constant.MenuConstant;
import com.laogeli.user.api.constant.RoleConstant;
import com.laogeli.user.api.dto.UserDto;
import com.laogeli.user.api.dto.UserInfoDto;
import com.laogeli.user.api.enums.IdentityType;
import com.laogeli.user.api.module.*;
import com.laogeli.user.mapper.*;
import com.laogeli.user.utils.UserUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 用户service实现
 *
 * @author wang
 * @date 2019-12-31
 */
@AllArgsConstructor
@Slf4j
@Service
public class UserService extends CrudService<UserMapper, User> {

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    private final UserRoleMapper userRoleMapper;

    private final RoleMapper roleMapper;

    private final MenuService menuService;

    private final RedisTemplate redisTemplate;

    private final AttachmentService attachmentService;

    private final SysProperties sysProperties;

    private final UserAuthsService userAuthsService;

    private final UserStaffRoleService userStaffRoleService;

    private final StaffRoleMapper staffRoleMapper;

    private final UserStaffRoleMapper userStaffRoleMapper;

    private final CompanyService companyService;

    private final StaffRoleMenuService staffRoleMenuService;

    private final OrderServiceClient orderServiceClient;

    private final CompanyMapper companyMapper;


    /**
     * 查询分页
     *
     * @param page page
     * @param user user
     * @return PageInfo
     */
    public PageInfo<User> findUserPage(PageInfo<User> page, User user) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return new PageInfo<User>(dao.findList(user));
    }

    /**
     * 根据openId获取用户信息
     *
     * @param phoneNumber
     * @return
     */
    public UserVo findUserByPhoneNumber(String phoneNumber) {

        UserAuths userAuths = new UserAuths();
        userAuths.setIdentifier(phoneNumber);
        userAuths = userAuthsService.getByIdentifier(userAuths);
        if (userAuths == null)
            return null;
        // 查询用户信息
        User user = new User();
        user.setId(userAuths.getUserId());
        user = this.get(user);
        if (user == null)
            return null;
        // 查询用户角色。 判断是员工还是企业，若果是员工查询员工角色，是企业查询系统角色
        if ("-1".equals(user.getEmployeeId())) {
            List<Role> roles = this.getUserRoles(user);
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
            BeanUtils.copyProperties(userAuths, userVo);
            userVo.setRoleList(UserUtils.rolesToVo(roles));
            return userVo;
        } else {
            List<StaffRole> staffRoles = this.getStaffRoles(user);
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
            BeanUtils.copyProperties(userAuths, userVo);
            userVo.setRoleList(UserUtils.staffRolesToVo(staffRoles));
            return userVo;
        }
    }

    /**
     * 根据用户唯一标识获取用户详细信息
     *
     * @param identityType identityType
     * @param identifier   identifier
     * @return UserVo
     */
    @Cacheable(value = "user#" + CommonConstant.CACHE_EXPIRE, key = "#identifier")
    public UserVo findUserByIdentifier(Integer identityType, String identifier) {
        UserAuths userAuths = new UserAuths();
        userAuths.setIdentifier(identifier);
        if (identityType != null)
            userAuths.setIdentityType(IdentityType.match(identityType).getValue());
        userAuths = userAuthsService.getByIdentifier(userAuths);
        if (userAuths == null)
            return null;
        // 查询用户信息
        User user = new User();
        user.setId(userAuths.getUserId());
        user = this.get(user);
        if (user == null)
            return null;
        // 查询用户角色。 判断是员工还是企业，若果是员工查询员工角色，是企业查询系统角色
        if ("-1".equals(user.getEmployeeId())) {
            List<Role> roles = this.getUserRoles(user);
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
            BeanUtils.copyProperties(userAuths, userVo);
            userVo.setRoleList(UserUtils.rolesToVo(roles));
            return userVo;
        } else {
            List<StaffRole> staffRoles = this.getStaffRoles(user);
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
            BeanUtils.copyProperties(userAuths, userVo);
            userVo.setRoleList(UserUtils.staffRolesToVo(staffRoles));
            return userVo;
        }
    }

    /**
     * 根据用户唯一标识获取用户详细信息
     *
     * @param identifier identifier
     * @return UserVo
     */
    public UserVo findUserByIdentifier(String identifier) {
        return this.findUserByIdentifier(null, identifier);
    }

    /**
     * 查询账号是否存在
     *
     * @param identityType identityType
     * @param identifier   identifier
     * @return boolean
     */
    public boolean checkIdentifierIsExist(Integer identityType, String identifier) {
        UserAuths userAuths = new UserAuths();
        userAuths.setIdentifier(identifier);
        userAuths.setIdentityType(identityType);
        return userAuthsService.getByIdentifier(userAuths) != null;
    }

    /**
     * 保存验证码
     *
     * @param random    random
     * @param imageCode imageCode
     */
    public void saveImageCode(String random, String imageCode) {
        redisTemplate.opsForValue().set(CommonConstant.DEFAULT_CODE_KEY + LoginType.PWD.getType() + "@" + random, imageCode, CommonConstant.DEFAULT_IMAGE_EXPIRE, TimeUnit.SECONDS);
    }

    /**
     * 获取用户信息，包括头像、角色、权限信息
     *
     * @param userVo userVo
     * @return User
     */
    public UserInfoDto findUserInfo(UserVo userVo) {
        // 返回结果
        UserInfoDto userInfoDto = new UserInfoDto();
        String identifier = userVo.getIdentifier();
        // 根据唯一标识查询账号信息
        UserAuths userAuths = new UserAuths();
        if (userVo.getIdentityType() != null)
            userAuths.setIdentityType(userVo.getIdentityType());
        userAuths.setIdentifier(userVo.getIdentifier());
        userAuths = userAuthsService.getByIdentifier(userAuths);
        if (userAuths == null)
            throw new CommonException("账号" + identifier + "不存在.");
        // 根据用户id查询用户详细信息
        User user = new User();
        user.setId(userAuths.getUserId());
        user = this.get(user);
        if (user == null)
            throw new CommonException("查询用户信息失败.");
        // 查询用户的角色信息
        if ("-1".equals(user.getEmployeeId())) {
            List<Role> roles = this.getUserRoles(user);
            // 根据角色查询权限
            List<String> permissions = this.getUserPermissions(user, identifier, roles);
            userInfoDto.setRoles(roles.stream().map(Role::getRoleCode).toArray(String[]::new));
            userInfoDto.setPermissions(permissions.toArray(new String[0]));
        } else {
            List<StaffRole> staffRoles = this.getStaffRoles(user);
            // 根据角色查询权限
            List<String> permissions = this.getUserStaffPermissions(user, identifier, staffRoles);
            userInfoDto.setRoles(staffRoles.stream().map(StaffRole::getRoleCode).toArray(String[]::new));
            userInfoDto.setPermissions(permissions.toArray(new String[0]));
        }
        UserUtils.toUserInfoDto(userInfoDto, user, userAuths);
        if ("4".equals(userInfoDto.getIdentityType().toString())) {
            //查询所有的公司信息
            List<CompanyVo> allCompany = companyMapper.findAllCompany();
            userInfoDto.setCompanys(allCompany);
        }
        // 头像信息 和 照片墙
        this.initUserAvatar(userInfoDto, user);
        return userInfoDto;
    }

    /**
     * 组装用户数据，包括账号、角色
     *
     * @param tempUser      tempUser
     * @param userAuths     userAuths
     * @param userRoles     userRoles
     * @param finalRoleList finalRoleList
     * @return UserDto
     */
    public UserDto getUserDtoByUserAndUserAuths(User tempUser, List<UserAuths> userAuths, List<UserRole> userRoles, List<Role> finalRoleList) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(tempUser, userDto);
        // 设置账号信息
        if (CollectionUtils.isNotEmpty(userAuths)) {
            userAuths.stream().filter(tempUserAuths -> tempUserAuths.getUserId().equals(tempUser.getId()))
                    .findFirst().ifPresent(tempUserAuths -> userDto.setIdentifier(tempUserAuths.getIdentifier()));
        }
        // 设置角色信息
        if (CollectionUtils.isNotEmpty(userRoles)) {
            List<Role> userRoleList = new ArrayList<>();
            userRoles.stream()
                    // 过滤
                    .filter(tempUserRole -> tempUser.getId().equals(tempUserRole.getUserId()))
                    .forEach(tempUserRole -> finalRoleList.stream()
                            .filter(role -> role.getId().equals(tempUserRole.getRoleId()))
                            .forEach(userRoleList::add));
            userDto.setRoleList(userRoleList);
        }
        return userDto;
    }

    /**
     * 查询用户权限数组
     *
     * @param user       user
     * @param identifier identifier
     * @return List
     */
    public List<String> getUserPermissions(User user, String identifier) {
        return this.getUserPermissions(user, identifier, this.getUserRoles(user));
    }

    /**
     * 根据指定角色集合，查询用户权限数组
     *
     * @param user       user
     * @param identifier identifier
     * @param roles      roles
     * @return List
     */
    public List<String> getUserPermissions(User user, String identifier, List<Role> roles) {
        // 用户权限
        List<String> permissions = new ArrayList<>();
        List<Menu> menuList = new ArrayList<>();
        // 管理员
        if (UserUtils.isAdmin(identifier)) {
            Menu menu = new Menu();
            menu.setApplicationCode(user.getApplicationCode());
            menu.setMenuType(MenuConstant.MENU_TYPE_PERMISSION);
            menuList = menuService.findAllList(menu);
        } else {
            for (Role role : roles) {
                // 根据角色查找菜单
                List<Menu> roleMenuList = menuService.findMenuByRole(role.getRoleCode());
                if (CollectionUtils.isNotEmpty(roleMenuList))
                    menuList.addAll(roleMenuList);
            }
        }
        if (CollectionUtils.isNotEmpty(menuList)) {
            permissions = menuList.stream()
                    // 获取权限菜单
                    .filter(menu -> MenuConstant.MENU_TYPE_PERMISSION.equals(menu.getMenuType()))
                    // 获取权限
                    .map(Menu::getPermission).collect(Collectors.toList());

        }
        return permissions;
    }

    /**
     * 根据指定角色集合，查询用户权限数组
     *
     * @param user       user
     * @param identifier identifier
     * @param staffRoles staffRoles
     * @return List
     */
    public List<String> getUserStaffPermissions(User user, String identifier, List<StaffRole> staffRoles) {
        // 用户权限
        List<String> permissions = new ArrayList<>();
        List<Menu> menuList = new ArrayList<>();
        // 管理员
        if (UserUtils.isAdmin(identifier)) {
            Menu menu = new Menu();
            menu.setApplicationCode(user.getApplicationCode());
            menu.setMenuType(MenuConstant.MENU_TYPE_PERMISSION);
            menuList = menuService.findAllList(menu);
        } else {
            for (StaffRole staffRole : staffRoles) {
                // 根据角色查找菜单
                List<Menu> roleMenuList = menuService.findMenuByStaffRole(staffRole.getRoleCode());
                if (CollectionUtils.isNotEmpty(roleMenuList))
                    menuList.addAll(roleMenuList);
            }
        }
        if (CollectionUtils.isNotEmpty(menuList)) {
            permissions = menuList.stream()
                    // 获取权限菜单
                    .filter(menu -> MenuConstant.MENU_TYPE_PERMISSION.equals(menu.getMenuType()))
                    // 获取权限
                    .map(Menu::getPermission).collect(Collectors.toList());

        }
        return permissions;
    }

    /**
     * 获取用户的角色
     *
     * @param user user
     * @return List
     */
    private List<Role> getUserRoles(User user) {
        List<Role> roles = Lists.newArrayList();
        List<UserRole> userRoles = userRoleMapper.getByUserId(user.getId());
        if (CollectionUtils.isNotEmpty(userRoles)) {
            Role role = new Role();
            //从对象列表中提取getRoleId并排重
            role.setIds(userRoles.stream().map(UserRole::getRoleId).distinct().toArray(String[]::new));
            roles = roleMapper.findListById(role);
        }
        return roles;
    }

    /**
     * 获取员工用户的角色
     *
     * @param user user
     * @return List
     */
    private List<StaffRole> getStaffRoles(User user) {
        List<StaffRole> staffRoles = Lists.newArrayList();
        List<UserStaffRole> userStaffRoles = userStaffRoleMapper.getByUserId(user.getId());
        if (CollectionUtils.isNotEmpty(userStaffRoles)) {
            StaffRole staffRole = new StaffRole();
            //从对象列表中提取getRoleId并排重
            staffRole.setIds(userStaffRoles.stream().map(UserStaffRole::getRoleId).distinct().toArray(String[]::new));
            staffRoles = staffRoleMapper.findListById(staffRole);
        }
        return staffRoles;
    }

    /**
     * 批量查询用户的角色
     *
     * @param users users
     * @return List
     */
    public List<Role> getUsersRoles(List<User> users) {
        // 流处理获取用户ID集合，根据用户ID批量查找角色
        List<UserRole> userRoles = userRoleMapper.getByUserIds(users.stream().map(User::getId).collect(Collectors.toList()));
        List<Role> roleList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(userRoles)) {
            Role role = new Role();
            // 获取角色ID集合，转成字节数组
            role.setIds(userRoles.stream().map(UserRole::getRoleId).distinct().toArray(String[]::new));
            // 批量查找角色
            roleList = roleMapper.findListById(role);
        }
        return roleList;
    }

    /**
     * 更新用户
     *
     * @param userDto userDto
     * @return int
     */
    @Transactional
    @CacheEvict(value = "user", key = "#userDto.identifier")
    public boolean updateUser(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        user.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
        // 更新用户信息
        super.update(user);
        // 如果是企业管理员，进入系统角色表，如果是员工账户进入，企业角色
        if ("-1".equals(userDto.getEmployeeId())) {
            // 更新用户角色关系
            UserRole sysUserRole = new UserRole();
            sysUserRole.setUserId(user.getId());
            // 删除原有的角色信息
            userRoleMapper.delete(sysUserRole);
            if (CollectionUtils.isNotEmpty(userDto.getRoles())) {
                userDto.getRoles().forEach(roleId -> {
                    UserRole role = new UserRole();
                    role.setId(IdGen.snowflakeId());
                    role.setUserId(user.getId());
                    role.setRoleId(roleId);
                    // 保存角色信息
                    userRoleMapper.insert(role);
                });
            }
        } else {
            // 更新用户角色关系
            UserStaffRole userStaffRole = new UserStaffRole();
            userStaffRole.setUserId(user.getId());
            // 删除原有的角色信息
            userStaffRoleService.delete(userStaffRole);
            if (CollectionUtils.isNotEmpty(userDto.getStaffRole())) {
                userDto.getStaffRole().forEach(roleId -> {
                    UserStaffRole role = new UserStaffRole();
                    role.setId(IdGen.snowflakeId());
                    role.setUserId(user.getId());
                    role.setRoleId(roleId);
                    // 保存角色信息
                    userStaffRoleService.insert(role);
                });
            }
        }
        return Boolean.TRUE;
    }

    /**
     * 更新用户基本信息
     *
     * @param user user
     * @return int
     */
//    @Override
    @Transactional
    @CacheEvict(value = "user", key = "#user.id")
    public int update(User user, String companyId, Integer identityType) {
        //更新用户信息
        if (identityType == 4) {
            user.setCorporateIdentify(companyId);
        }
        int success = dao.update(user);
        // 根据用户+手机号  添加到司机列表，默认是外部司机，绑定到九辅公司下
        System.out.println(companyId);

        //如果是微信用户  并且已经绑定了公司
        if (identityType == 4 && StringUtils.isNotBlank(companyId)) {
            Driver driver = new Driver();
            driver.setDriverName(user.getName());
            driver.setDriverPhone(user.getIdentifier());
            driver.setBelongCompaniesId(companyId);
            driver.setDriverBelongState("2");
            ResponseBean<Integer> integerResponseBean = orderServiceClient.addWxDriver(driver);
            System.out.println(integerResponseBean);

        }
        return success;
    }

    /**
     * 更新密码
     *
     * @param userDto userDto
     * @return int
     */
    @Transactional
    @CacheEvict(value = "user", key = "#userDto.identifier")
    public int updatePassword(UserDto userDto) {
        if (StringUtils.isBlank(userDto.getNewPassword()))
            throw new CommonException("新密码不能为空.");
        if (StringUtils.isBlank(userDto.getIdentifier()))
            throw new CommonException("用户名不能为空.");
        UserAuths userAuths = new UserAuths();
        userAuths.setIdentifier(userDto.getIdentifier());
        userAuths = userAuthsService.getByIdentifier(userAuths);
        if (userAuths == null)
            throw new CommonException("账号不存在.");
        if (!encoder.matches(userDto.getOldPassword(), userAuths.getCredential()) && userDto.getOldPassword() != "") {
            throw new CommonException("旧密码不匹配");
        } else {
            // 旧密码一致，修改密码
            userAuths.setCredential(encoder.encode(userDto.getNewPassword()));
            return userAuthsService.update(userAuths);
        }
    }

    /**
     * 更新头像
     *
     * @param userDto userDto
     * @return int
     */
    @Transactional
    @CacheEvict(value = "user", key = "#userDto.identifier")
    public int updateAvatar(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user = this.get(user);
        if (user == null)
            throw new CommonException("用户不存在.");
        // 先删除旧头像
        if (user.getAvatarId() != null) {
            Attachment attachment = new Attachment();
            attachment.setId(user.getAvatarId());
            attachment = attachmentService.get(attachment);
            if (attachment != null)
                attachmentService.delete(attachment);
        }
        user.setAvatarId(userDto.getAvatarId());
        return super.update(user);
    }

    /**
     * 删除用户
     *
     * @param user user
     * @return int
     */
    @Transactional
    @Override
    @CacheEvict(value = "user", key = "#user.id")
    public int delete(User user) {
        // 删除用户角色关系
        if ("-1".equals(user.getEmployeeId())) {
            userRoleMapper.deleteByUserId(user.getId());
        } else {
            userStaffRoleMapper.deleteByUserId(user.getId());
        }
        // 删除用户授权信息
        UserAuths userAuths = new UserAuths();
        userAuths.setUserId(user.getId());
        userAuthsService.deleteByUserId(userAuths);
        return super.delete(user);
    }

    /**
     * 批量删除用户
     *
     * @param ids ids
     * @return int
     */
    @Transactional
    @Override
    @CacheEvict(value = "user", allEntries = true)
    public int deleteAll(String[] ids) {
        String currentUser = SysUtil.getUser(), applicationCode = SysUtil.getSysCode();
        for (String id : ids) {
            // 删除用户角色关系
            userRoleMapper.deleteByUserId(id);
            // 删除用户授权信息
            UserAuths userAuths = new UserAuths();
            userAuths.setNewRecord(false);
            userAuths.setUserId(id);
            userAuths.setCommonValue(currentUser, applicationCode);
            userAuthsService.deleteByUserId(userAuths);
        }
        return super.deleteAll(ids);
    }

    /**
     * 查询用户数量
     *
     * @param userVo userVo
     * @return int
     */
    public Integer userCount(UserVo userVo) {
        return this.dao.userCount(userVo);
    }

    /**
     * 初始化用户头像信息
     *
     * @param userInfoDto userInfoDto
     * @param user        user
     */
    private void initUserAvatar(UserInfoDto userInfoDto, User user) {
        try {
            // 附件id不为空，获取对应的预览地址，否则获取配置默认头像地址
            if (user.getAvatarId() != null) {
                Attachment attachment = new Attachment();
                attachment.setId(user.getAvatarId());
                userInfoDto.setAvatarUrl(attachmentService.getPreviewUrl(attachment));
            } else {
                userInfoDto.setAvatarUrl(sysProperties.getDefaultAvatar());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 重置密码
     *
     * @param userDto userDto
     * @return int
     */
    @Transactional
    @CacheEvict(value = "user", key = "#userDto.identifier")
    public boolean resetPassword(UserDto userDto) {
        UserAuths userAuths = new UserAuths();
        userAuths.setIdentifier(userDto.getIdentifier());
        userAuths = userAuthsService.getByIdentifier(userAuths);
        if (userAuths == null)
            throw new CommonException("账号不存在.");
        // 重置密码为123456
        // userAuths.setCredential(encoder.encode(CommonConstant.DEFAULT_PASSWORD));
        userAuths.setCredential(encoder.encode(userDto.getCredential()));
        return userAuthsService.update(userAuths) > 0;
    }

    /**
     * 注册，注意要清除缓存
     *
     * @param userDto userDto
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "user", key = "#userDto.identifier")
    public boolean register(UserDto userDto) {
        boolean success = false;
        if (userDto.getIdentityType() == null)
            userDto.setIdentityType(IdentityType.PASSWORD.getValue());
        // 解密
        String password = this.decryptCredential(userDto.getCredential(), userDto.getIdentityType());
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        // 1.保存用户基本信息
        user.setCommonValue(userDto.getIdentifier(), SysUtil.getSysCode());
        user.setName(userDto.getCompanyName());
        user.setPhone(userDto.getIdentifier());
        user.setStatus(CommonConstant.DEL_FLAG_NORMAL);
        user.setCorporateIdentify(IdGen.snowflakeId());
        user.setEmployeeId("-1");
        if (this.insert(user) > 0) {
            // 2.保存账号信息
            UserAuths userAuths = new UserAuths();
            userAuths.setCommonValue(userDto.getIdentifier(), user.getApplicationCode());
            userAuths.setUserId(user.getId());
            userAuths.setUserId(user.getId());
            userAuths.setIdentifier(userDto.getIdentifier());
            // 设置登录类型
            if (userDto.getIdentityType() != null)
                userAuths.setIdentityType(userDto.getIdentityType());
            // 设置密码
            userAuths.setCredential(encoder.encode(password));
            userAuthsService.insert(userAuths);
            // 3.保存企业公司信息
            Company company = new Company();
            company.setId(user.getCorporateIdentify());
            company.setAccountType(userDto.getAccountType());
            company.setCompanyName(userDto.getCompanyName());
            company.setCreator(userDto.getIdentifier());
            Date currentDate = DateUtils.asDate(LocalDateTime.now());
            company.setCreateDate(currentDate);
            company.setCommonValue(userDto.getIdentifier(), user.getApplicationCode());
            int count = companyService.insertCompany(company);
            if (count == 0) {
                throw new CommonException("公司名已存在！");
            }
            // 4.根据公司类型 分配默认角色
            this.defaultRole(user, userDto.getIdentifier(), userDto.getAccountType());
            // 5.新增默认费用参数
            int feeState = orderServiceClient.batchCostsSet(user.getCorporateIdentify());
            // 6.新增默认订单编号
            int orderState = orderServiceClient.addOrderNumber(user.getCorporateIdentify());
            // 7.如果是货代工厂 将添加至平台客户中。如果是车队，将平台添加至车队客户中。
            int addState = 0;
            if (userDto.getAccountType() == 2) {
                addState = orderServiceClient.addPlatformCustomers("792071937215041536", userDto.getCompanyName(), "锦线网络科技（上海）有限公司");
            } else if (userDto.getAccountType() == 1) {
                addState = orderServiceClient.addPlatformCustomers(user.getCorporateIdentify(), "锦线网络科技（上海）有限公司", userDto.getCompanyName());
            } else {
                addState = 1;
            }
            // 8.新建公司默认角色
            if (feeState == 0 || orderState == 0 || addState == 0) {
                // 手动开启事务回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new CommonException("网络异常请稍后重试！");
            } else {
                success = this.defaultStaffRole(user.getCorporateIdentify(), userDto.getIdentifier(), userDto.getAccountType());
            }
        }
        return success;
    }

    /**
     * 通过微信登录注册
     *
     * @param userDto
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean registerByWx(UserDto userDto) {
        boolean success = false;
        if (userDto.getIdentifier() == null)
            userDto.setIdentityType(IdentityType.WE_CHAT.getValue());
        User user = new User();
        BeanUtils.copyProperties(userDto, user);


        // 1 保存用户基本信息
        user.setCommonValue(userDto.getIdentifier(), SysUtil.getSysCode());
        user.setName(userDto.getIdentifier());
        user.setPhone(userDto.getIdentifier());
        user.setAvatarWx(userDto.getWxUser().getAvatarUrl());
        user.setStatus(CommonConstant.DEL_FLAG_NORMAL);
        user.setCorporateIdentify(IdGen.snowflakeId());
        user.setEmployeeId("-1");
        if (this.insert(user) > 0) {
            //保存账号信息
            // 2.保存账号信息
            UserAuths userAuths = new UserAuths();
            userAuths.setCommonValue(userDto.getIdentifier(), user.getApplicationCode());
            userAuths.setUserId(user.getId());
            userAuths.setIdentifier(userDto.getIdentifier());
            // 设置登录类型
            if (userDto.getIdentityType() != null)
                userAuths.setIdentityType(userDto.getIdentityType());
            // 设置密码
            userAuths.setCredential(encoder.encode("123456"));
            userAuthsService.insert(userAuths);

            //2.1  保存wx用户信息
            WxUser wxUser = new WxUser();
            BeanUtils.copyProperties(userDto.getWxUser(), wxUser);
            wxUser.setOpenId(userDto.getWxUser().getOpenId());
            wxUser.setIsDelete(0);
            wxUser.setCreateTime(new Date());
            wxUser.setUpdateTime(new Date());
            wxUser.setMemberId(user.getId());
            dao.insertWechat(wxUser);

            // 3.分配第三方临时用户角色
            // 查询默认角色
            Role role = new Role();
            role.setIsDefault(RoleConstant.WX_ROLW);
            Stream<Role> roleStream = roleMapper.findList(role).stream();
            if (Optional.ofNullable(roleStream).isPresent()) {
                Role defaultRole = roleStream.findFirst().orElse(null);
                if (defaultRole != null) {
                    UserRole userRole = new UserRole();
                    userRole.setCommonValue(userDto.getIdentifier(), user.getApplicationCode());
                    userRole.setUserId(user.getId());
                    userRole.setRoleId(defaultRole.getId());
                    // 保存用户角色关系
                    return userRoleMapper.insert(userRole) > 0;
                }
            }
        }
        return success;
    }

    /**
     * 解密密码
     *
     * @param encoded encoded
     * @return String
     */
    private String decryptCredential(String encoded, Integer identityType) {
        // 返回默认密码
        if (StringUtils.isBlank(encoded))
            return CommonConstant.DEFAULT_PASSWORD;
        // 微信、手机号注册不需要解密
        if (IdentityType.PHONE_NUMBER.getValue().equals(identityType))
            return encoded;
        // 解密密码
        try {
            encoded = AesUtil.decryptAES(encoded, sysProperties.getKey()).trim();
            log.info("密码解密结果：{}", encoded);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException("解密失败: " + e.getMessage());
        }
        return encoded;
    }

    /**
     * 新建用户
     *
     * @param userDto userDto
     * @return int
     */
    @Transactional
    public int createUser(UserDto userDto) {
        int update;
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        // 1.保存用户基本信息
        if ("-1".equals(userDto.getCorporateIdentify())) {
            user.setCorporateIdentify(IdGen.snowflakeId());
        }
        user.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
        user.setStatus(CommonConstant.DEL_FLAG_NORMAL);
        user.setEmployeeId(userDto.getEmployeeId());
        user.setAvatarId("866689233144909824");
        if ((update = this.insert(user)) > 0) {
            // 2.保存账号信息
            UserAuths userAuths = new UserAuths();
            userAuths.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
            userAuths.setUserId(user.getId());
            userAuths.setIdentifier(userDto.getIdentifier());
            // 设置登录类型
            if (userDto.getIdentityType() == null)
                userAuths.setIdentityType(IdentityType.PASSWORD.getValue());
            // 默认密码为123456
            if (StringUtils.isBlank(userDto.getCredential()))
                userDto.setCredential(CommonConstant.DEFAULT_PASSWORD);
            userAuths.setCredential(encoder.encode(userDto.getCredential()));
            update = userAuthsService.insert(userAuths);
        }
        // 判断是添加admin 添加企业用户，还是企业用户添加员工
        if ("-1".equals(userDto.getEmployeeId())) {
            // 3.保存企业公司信息
            Company company = new Company();
            company.setId(user.getCorporateIdentify());
            company.setAccountType(userDto.getAccountType());
            company.setCompanyName(userDto.getName());
            company.setCreator(SysUtil.getUser());
            Date currentDate = DateUtils.asDate(LocalDateTime.now());
            company.setCreateDate(currentDate);
            company.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
            int count = companyService.insertCompany(company);
            if (count == 0) {
                throw new CommonException("公司名已存在！");
            }
            // 4.根据公司类型 分配默认角色
            if (CollectionUtils.isNotEmpty(userDto.getRoles())) {
                userDto.getRoles().forEach(roleId -> {
                    UserRole userRole = new UserRole();
                    userRole.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
                    userRole.setUserId(user.getId());
                    userRole.setRoleId(roleId);
                    // 保存角色信息
                    userRoleMapper.insert(userRole);
                });
            } else {
                // 5.根据公司类型 分配默认角色,此次默认添加平台角色
                this.defaultRole(user, userDto.getIdentifier(), 0);
            }
            // 6.新增默认费用参数
            int feeState = orderServiceClient.batchCostsSet(user.getCorporateIdentify());
            // 7.新增默认订单编号
            int orderState = orderServiceClient.addOrderNumber(user.getCorporateIdentify());
            // 8.如果是货代，存入平台客户中 如果是车队，添加客户平台jxwl平台
            int addState = 0;
            if (userDto.getAccountType() == 2) {
                addState = orderServiceClient.addPlatformCustomers("792071937215041536", userDto.getCompanyName(), "锦线网络科技（上海）有限公司");
            } else if (userDto.getAccountType() == 1) {
                addState = orderServiceClient.addPlatformCustomers(user.getCorporateIdentify(), "锦线网络科技（上海）有限公司", userDto.getCompanyName());
            } else {
                addState = 1;
            }
            // 9.新建公司默认角色
            if (feeState == 0 || orderState == 0 || addState == 0) {
                // 手动开启事务回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new CommonException("网络异常请稍后重试！");
            } else {
                this.defaultStaffRole(user.getCorporateIdentify(), userDto.getIdentifier(), userDto.getAccountType());
            }
        } else {
            if (CollectionUtils.isNotEmpty(userDto.getStaffRole())) {
                userDto.getStaffRole().forEach(roleId -> {
                    UserStaffRole userStaffRole = new UserStaffRole();
                    userStaffRole.setCommonValue(userDto.getIdentifier(), user.getApplicationCode());
                    userStaffRole.setUserId(user.getId());
                    userStaffRole.setRoleId(roleId);
                    // 3.保存员工角色信息
                    userStaffRoleService.insert(userStaffRole);
                });
            }
        }
        return update;
    }

    /**
     * 分配默认角色
     *
     * @param user       user
     * @param identifier identifier
     * @return boolean
     */
    @Transactional
    public boolean defaultRole(User user, String identifier, int accountType) {
        Role role = new Role();
        if (accountType == 0) {
            role.setIsDefault(RoleConstant.PLATFORM_ROLE);
        } else if (accountType == 1) {
            role.setIsDefault(RoleConstant.IS_DEFAULT_ROLE);
        } else if (accountType == 2) {
            role.setIsDefault(RoleConstant.CUSTOMER_ROLE);
        }
        // 查询默认角色
        Stream<Role> roleStream = roleMapper.findList(role).stream();
        if (Optional.ofNullable(roleStream).isPresent()) {
            Role defaultRole = roleStream.findFirst().orElse(null);
            if (defaultRole != null) {
                UserRole userRole = new UserRole();
                userRole.setCommonValue(identifier, user.getApplicationCode());
                userRole.setUserId(user.getId());
                userRole.setRoleId(defaultRole.getId());
                // 保存用户角色关系
                return userRoleMapper.insert(userRole) > 0;
            }
        }
        return false;
    }

    /**
     * 新建公司默认角色
     *
     * @param corporateIdentify
     * @return boolean
     */
    @Transactional
    public boolean defaultStaffRole(String corporateIdentify, String identifier, Integer accountType) {
        List<StaffRole> staffRoleList = new ArrayList<>();
        // 添加员工角色
        Arrays.asList(SecurityConstant.STAFF_ROLE).stream().forEach(roleName -> {
                    String uid = IdGen.snowflakeId();
                    StaffRole staffRole = new StaffRole();
                    staffRole.setId(uid);
                    staffRole.setRoleName(roleName);
                    staffRole.setRoleCode(SecurityConstant.STAFF_ROLE_PREFIX + IdGen.getCard());
                    if ("业务".equals(roleName) || "操作".equals(roleName)) {
                        staffRole.setDataScope("2");
                    } else {
                        staffRole.setDataScope("1");
                    }
                    staffRole.setCorporateIdentify(corporateIdentify);
                    staffRole.setCreator(identifier);
                    staffRole.setApplicationCode("EXAM");

                    staffRoleList.add(staffRole);
                    // 需要根据公司类型分配默认的角色与权限
                    staffRoleMenuService.saveStaffRoleMenus(uid, SecurityConstant.getMenus(roleName, accountType));
                }
        );
        return staffRoleMapper.insertForeach(staffRoleList) > 0;
    }

    /**
     * 根据用户id批量查询UserVo
     *
     * @param userVo userVo
     * @return List
     */
    public List<UserVo> findUserVoListById(UserVo userVo) {
        List<UserVo> userVos = Lists.newArrayList();
        User user = new User();
        user.setIds(userVo.getIds());
        Stream<User> userStream = this.findListById(user).stream();
        if (Optional.ofNullable(userStream).isPresent()) {
            userVos = userStream.map(tempUser -> {
                UserVo tempUserVo = new UserVo();
                BeanUtils.copyProperties(tempUser, tempUserVo);
                return tempUserVo;
            }).collect(Collectors.toList());
        }
        return userVos;
    }

    /**
     * 导入用户
     *
     * @param userInfoDtos userInfoDtos
     * @return boolean
     */
    @Transactional
    @CacheEvict(value = "user", allEntries = true)
    public boolean importUsers(List<UserInfoDto> userInfoDtos) {
        String currentUser = SysUtil.getUser(), applicationCode = SysUtil.getSysCode();
        Date currentDate = DateUtils.asDate(LocalDateTime.now());
        for (UserInfoDto userInfoDto : userInfoDtos) {
            User user = new User();
            BeanUtils.copyProperties(userInfoDto, user);
            user.setModifier(currentUser);
            user.setModifyDate(currentDate);
            if (this.update(user) < 1) {
                user.setCommonValue(currentUser, applicationCode);
                user.setStatus(CommonConstant.STATUS_NORMAL);
                this.insert(user);
            }
            // 先删除用户授权信息
            UserAuths userAuths = new UserAuths();
            userAuths.setIdentifier(userInfoDto.getIdentifier());
            // 默认密码
            if (StringUtils.isBlank(userInfoDto.getCredential())) {
                userInfoDto.setCredential(encodeCredential(CommonConstant.DEFAULT_PASSWORD));
            }
            userAuths.setCredential(userInfoDto.getCredential());
            userAuths.setModifier(currentUser);
            userAuths.setModifyDate(currentDate);
            userAuthsService.deleteByIdentifier(userAuths);
            // 重新insert
            userAuths.setCommonValue(currentUser, applicationCode);
            userAuths.setUserId(user.getId());
            userAuths.setIdentityType(userInfoDto.getIdentityType());
            userAuthsService.insert(userAuths);
        }
        return true;
    }

    /**
     * 加密
     *
     * @param credential 明文
     * @return 密文
     */
    public String encodeCredential(String credential) {
        return encoder.encode(credential);
    }


    /**
     * 获取企业所有账户
     *
     * @param user
     * @return List<UserDto>
     */
    public List<UserDto> getAllCorporateAccount(User user) {
        return dao.getAllCorporateAccount(user);
    }

}
