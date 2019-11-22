package com.ai.dream.Filter;

import com.ai.dream.core.entity.RedisConsts;
import com.ai.dream.core.entity.ResultVO;
import com.ai.dream.tools.CookieUtils;
import com.ai.dream.utils.JsonTools;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;


@Component
public class AuthFilter extends ZuulFilter {

    Logger log = LoggerFactory.getLogger(AuthFilter.class);
    private static final String PRE_TYPE = "pre";

    @Autowired
    private JedisCluster jedisCluster;

    //排除过滤的 uri 地址
    private static final String LOGIN_URI = "/user/checkLogin";
    private static final String REGISTER_URI = "/user/saveUser";

    //无权限时的提示语
    private static final String INVALID_TOKEN = "invalid token";
    private static final String INVALID_USERID = "invalid userId";

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return PRE_DECORATION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        log.info("uri:{}", request.getRequestURI());
        //注册和登录接口不拦截，其他接口都要拦截校验 token
        if (LOGIN_URI.equals(request.getRequestURI()) ||
                REGISTER_URI.equals(request.getRequestURI())) {
            return false;
        }
        return true;
    }

    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        try{
            //先从 cookie 中取 token，cookie 中取失败再从 header 中取，两重校验
            //通过工具类从 Cookie 中取出 token
            String tokenCookie = CookieUtils.getCookieValue(request, "token");
            if (tokenCookie == null || StringUtils.isEmpty(tokenCookie)) {
                readTokenFromHeader(requestContext, request);
            } else {
                verifyToken(requestContext, request, tokenCookie);
            }
        }catch(Exception e){
            log.error("失败原因: ",e);

        }
        return null;
    }

    /**
     * 从 header 中读取 token 并校验
     */
    private void readTokenFromHeader(RequestContext requestContext, HttpServletRequest request) throws Exception{
        //从 header 中读取
        String headerToken = request.getHeader("token");
        if (StringUtils.isEmpty(headerToken)) {
            setUnauthorizedResponse(requestContext, INVALID_TOKEN);
        } else {
            verifyToken(requestContext, request, headerToken);
        }
    }

    /**
     * 从Redis中校验token
     */
    private void verifyToken(RequestContext requestContext, HttpServletRequest request, String token) throws Exception{
        //需要从cookie或header 中取出 userId 来校验 token 的有效性，因为每个用户对应一个token，在Redis中是以 TOKEN_userId 为键的
        String userIdCookie = CookieUtils.getCookieValue(request, "userId");
        if (userIdCookie == null || StringUtils.isEmpty(userIdCookie)) {
            //从header中取userId
            String userId = request.getHeader("userId");
            if (StringUtils.isEmpty(userId)) {
                setUnauthorizedResponse(requestContext, INVALID_USERID);
            } else {
                String redisToken = jedisCluster.get("TOKEN_"+userId);
                if (StringUtils.isEmpty(redisToken) || !redisToken.equals(token)) {
                    setUnauthorizedResponse(requestContext, INVALID_TOKEN);
                }
            }
        } else {
            String redisToken = jedisCluster.get("TOKEN_"+userIdCookie);
            if (StringUtils.isEmpty(redisToken) || !redisToken.equals(token)) {
                setUnauthorizedResponse(requestContext, INVALID_TOKEN);
            }
        }
    }


    /**
     * 设置 401 无权限状态
     */
    private void setUnauthorizedResponse(RequestContext requestContext, String msg) throws IOException {
        requestContext.setSendZuulResponse(false);
        requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());

        ResultVO vo = new ResultVO();
        vo.setCode(401);
        vo.setMsg(msg);
        String result = JsonTools.object2Json(vo);

        requestContext.setResponseBody(result);
    }
}