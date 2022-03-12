package com.laogeli.distribute.aspectj;

import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.persistence.BaseEntity;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.common.core.vo.RoleVo;
import com.laogeli.common.core.vo.UserVo;
import com.laogeli.distribute.annotation.DataScope;
import com.laogeli.user.api.feign.UserServiceClient;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 数据过滤处理
 *
 * @author yangyu
 */
@Aspect // 作用是把当前类标识为一个切面供容器读取
@Component
@AllArgsConstructor
public class DataScopeAspect {

    /**
     * 全部数据权限
     */
    public static final String DATA_SCOPE_ALL = "1";

    /**
     * 仅本人数据权限
     */
    public static final String DATA_SCOPE_SELF = "2";

    private final UserServiceClient userServiceClient;

    // 配置织入点
    @Pointcut("@annotation(com.laogeli.distribute.annotation.DataScope)")
    // Pointcut是植入Advice的触发条件。每个Pointcut的定义包括2部分，一是表达式，二是方法签名。
    // 方法签名必须是 public及void型。可以将Pointcut中的方法看作是一个被Advice引用的助记符，因为表达式不直观，
    // 因此我们可以通过方法签名的方式为 此表达式命名。因此Pointcut中的方法只需要方法签名，而不需要在方法体内编写实际代码。
    // @annotation：用于匹配当前执行方法持有指定注解的方法；
    public void dataScopePointCut() {
    }

    @Before("dataScopePointCut()")
    // @Before：标识一个前置增强方法，相当于BeforeAdvice的功能，相似功能的还有
    // @AfterThrowing：异常抛出增强，相当于ThrowsAdvice
    // @After: final增强，不管是抛出异常或者正常退出都会执行
    public void doBefore(JoinPoint point) throws Throwable {
        handleDataScope(point);
    }

    protected void handleDataScope(final JoinPoint joinPoint) {
        // 获得注解
        DataScope controllerDataScope = getAnnotationLog(joinPoint);
        if (controllerDataScope == null) {
            return;
        }
        String identifier = SysUtil.getUser();
        if (!"admin".equals(identifier)) {
            ResponseBean<UserVo> userVoResponseBean = userServiceClient.findUserByIdentifier(identifier);
            UserVo userVo = userVoResponseBean.getData();
            dataScopeFilter(joinPoint, userVo, controllerDataScope.userAlias());
        }
    }

    /**
     * 数据范围过滤
     *
     * @param joinPoint 切点
     * @param userVo    用户
     * @param userAlias 别名
     */
    public static void dataScopeFilter(JoinPoint joinPoint, UserVo userVo, String userAlias) {
        StringBuilder sqlString = new StringBuilder();
        for (RoleVo roleVo : userVo.getRoleList()) {
            String dataScope = roleVo.getDataScope();
            if (DATA_SCOPE_ALL.equals(dataScope)) {
                sqlString = new StringBuilder();
                break;
            } else if (DATA_SCOPE_SELF.equals(dataScope)) {
                if (StringUtils.isNotBlank(userAlias)) {
                    sqlString.append(com.laogeli.common.core.utils.StringUtils.format(" OR {}.creator = {} ", userAlias, "'"+userVo.getIdentifier()+"'"));
                } else {
                    // 数据权限为仅本人且没有userAlias别名不查询任何数据
                    sqlString.append(" OR 1=0 ");
                }
            }
        }

        if (StringUtils.isNotBlank(sqlString.toString())) {
            BaseEntity baseEntity = (BaseEntity) joinPoint.getArgs()[1];
            baseEntity.setDataScope(" AND " + sqlString.substring(4));
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private DataScope getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(DataScope.class);
        }
        return null;
    }
}
