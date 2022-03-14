package com.laogeli.common.core.constant;

/**
 * 公用常量
 *
 * @author wang
 * @date 2019-12-31
 */
public class CommonConstant {

  /**
   * 正常
   */
  public static final Integer STATUS_NORMAL = 0;

  /**
   * 异常
   */
  public static final String STATUS_EXCEPTION = "1";

  /**
   * 锁定
   */
  public static final String STATUS_LOCK = "9";

  /**
   * 页码
   */
  public static final String PAGE_NUM = "pageNum";

  /**
   * 分页大小
   */
  public static final String PAGE_SIZE = "pageSize";

  /**
   * 排序
   */
  public static final String SORT = "sort";

  /**
   * 排序方向
   */
  public static final String ORDER = "order";

  /**
   * 默认页数
   */
  public static final String PAGE_NUM_DEFAULT = "1";

  /**
   * 默认分页大小
   */
  public static final String PAGE_SIZE_DEFAULT = "10";

  /**
   * 默认排序
   */
  public static final String PAGE_SORT_DEFAULT = "create_date";

  /**
   * 默认排序方向
   */
  public static final String PAGE_ORDER_DEFAULT = " desc";

  /**
   * 正常状态
   */
  public static final Integer DEL_FLAG_NORMAL = 0;

  /**
   * 删除状态
   */
  public static final Integer DEL_FLAG_DEL = 1;

  /**
   * 路由配置在Redis中的key
   */
  public static final String ROUTE_KEY = "_ROUTE_KEY";

  /**
   * 默认生成图形验证码过期时间
   */
  public static final int DEFAULT_IMAGE_EXPIRE = 60 * 5;

  /**
   * 默认短信验证码过期时间
   */
  public static final int DEFAULT_SMS_EXPIRE = 60 * 15;

  /**
   * 短信验证码 失效时间 秒
   * */
  public static final int SMS_EXPIRE = 60 * 2;

  /**
   * 短信验证码条数 失效时间 秒
   * */
  public static final int SMS_COUNT_EXPIRE = 60 * 30;

  /**
   * 保存图形code的前缀
   */
  public static final String DEFAULT_CODE_KEY = "captcha:graph:";

  /**
   * 手机号验证码在Redis中的key前缀
   */
  public static final String SMS_CODE_PREFIX = "captcha:sms:";

  /**
   * 手机号验证码条数在Redis中的key前缀
   */
  public static final String SMS_COUNT_PREFIX = "captcha:count:sms:";

  /**
   * token请求头名称
   */
  public static final String REQ_HEADER = "Authorization";

  /**
   * token分割符
   */
  public static final String TOKEN_SPLIT = "Bearer ";

  /**
   * 默认密码
   */
  public static final String DEFAULT_PASSWORD = "123456";

  /**
   * 默认排序
   */
  public static final Integer DEFAULT_SORT = 30;

  /**
   * utf-8
   */
  public static final String UTF8 = "UTF-8";

  /**
   * 阿里
   */
  public static final String ALIYUN_SMS = "aliyun_sms";

  /**
   * 参数校验失败
   */
  public static final String IllEGAL_ARGUMENT = "参数校验失败！";

  /**
   * 验证码长度
   */
  public static final String CODE_SIZE = "6";

  /**
   * Bearer
   */
  public static final String AUTHORIZATION_BEARER = "Bearer ";

  /**
   * 密码类型
   */
  public static final String GRANT_TYPE_PASSWORD = "password";

  /**
   * 手机号类型
   */
  public static final String GRANT_TYPE_MOBILE = "mobile";

  /**
   * 微信类型
   */
  public static final String GRANT_TYPE_WX="wx";

  /**
   * 默认超时时间
   */
  public static final String CACHE_EXPIRE = "CACHE_EXPIRE";

  /**
   * 系统推送名
   */
  public static final String SYSTEM = "SYSTEM";

  /**
   * 保存企业 订单编号格式 的前缀
   */
  public static final String ORDER_NUMBER_PREFIX = "order:number:";

  /**
   * 保存下单/接单  订单编号key
   */
  public static final String ORDER_CENTER_NUMBER = "order:center:";

  /**
   * 全部叠加 自增数 的前缀
   */
  public static final String ALL_AOTU_PREFIX = "order:aotu:";

  /**
   * 海运出口 自增数 的前缀
   */
  public static final String SE_AOTU_PREFIX = "order:se:";

  /**
   * 海运进口 自增数 的前缀
   */
  public static final String SI_AOTU_PREFIX = "order:si:";

  /**
   * 空运出口 自增数 的前缀
   */
  public static final String AE_AOTU_PREFIX = "order:ae:";

  /**
   * 空运进口 自增数 的前缀
   */
  public static final String AI_AOTU_PREFIX = "order:ai:";

  /**
   * 船名缓存中的key
   */
  public static final String SHIPS_NAME_KEY = "order:ships_name";

  /**
   * 船公司缓存中的key
   */
  public static final String SHIP_COMPANY_KEY = "order:ship_company";

  /**
   * 码头缓存中的key
   */
  public static final String DOCK_KEY = "order:dock";

  /**
   * 门点缓存的key
   */
  public static final String DOOR_KEY = "order:door:";

  /**
   * 箱型缓存中的key
   */
  public static final String BOX_PILE_KEY = "order:box_pile";

  /**
   * 默认表头缓存中的key
   */
  public static final String TABLE_HEADER_DEFAULT_KEY = "order:table_header:default:";

  /**
   * 修改后归属自己的表头key
   */
  public static final String TABLE_HEADER_KEY = "order:table_header:";

  /**
   * 找油网利润数据缓存key
   */
  public static final String ZYW_KEY = "order:zyw_data";

  /**
   * messageId key
   */
  public static final String MQ_MSG_ID_KEY = "rabbitmq:msg_id:";

}

