package cn.chasers.wehappy.common.constant;

/**
 * 权限相关常量定义
 * @author lollipop
 */
public interface AuthConstant {

    /**
     * JWT存储权限前缀
     */
    String AUTHORITY_PREFIX = "ROLE_";

    /**
     * JWT存储权限属性
     */
    String AUTHORITY_CLAIM_NAME = "authorities";

    /**
     * 后台clientId
     */
    String ADMIN_CLIENT_ID = "admin";

    /**
     * 前台clientId
     */
    String PORTAL_CLIENT_ID = "portal";

    /**
     * 后台管理接口路径匹配
     */
    String ADMIN_URL_PATTERN = "/admin/**";

    /**
     * Redis缓存权限规则key
     */
    String RESOURCE_ROLES_MAP_KEY = "auth:resourceRolesMap";

    /**
     * 认证信息Http请求头
     */
    String JWT_TOKEN_HEADER = "Authorization";

    /**
     * 认证信息Http query param
     */
    String JWT_TOKEN = "token";

    /**
     * JWT令牌前缀
     */
    String JWT_TOKEN_PREFIX = "Bearer ";

    /**
     * JWT令牌前缀
     */
    String WS_JWT_TOKEN_PREFIX = "Bearer, ";

    /**
     * 用户信息Http请求头
     */
    String USER_TOKEN_HEADER = "user";

}
