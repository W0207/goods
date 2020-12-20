package cn.edu.xmu.gateway.localfilter;

import cn.edu.xmu.gateway.util.*;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;

/**
 * @author Ming Qiu
 * @date Created in 2020/11/13 22:31
 **/
public class AuthFilter implements GatewayFilter, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    private String tokenName;

    public AuthFilter(Config config) {
        this.tokenName = config.getTokenName();
    }

    /**
     * gateway001 权限过滤器
     * 1. 检查JWT是否合法,以及是否过期，如果过期则需要在response的头里换发新JWT，如果不过期将旧的JWT在response的头中返回
     * 2. 判断用户的shopid是否与路径上的shopid一致（0可以不做这一检查）
     * 3. 在redis中判断用户是否有权限访问url,如果不在redis中需要通过dubbo接口load用户权限
     * 4. 需要以dubbo接口访问privilegeservice
     *
     * @param exchange
     * @param chain
     * @return
     * @author wwc
     * @date 2020/12/02 17:13
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        // 获取请求参数
        String token = request.getHeaders().getFirst(tokenName);
        RequestPath url = request.getPath();
        HttpMethod method = request.getMethod();
        // 判断token是否为空，无需token的url在配置文件中设置
        logger.debug("filter: token = " + token);
        if (StringUtil.isNullOrEmpty(token)) {
            return getErrorResponse(HttpStatus.UNAUTHORIZED, ResponseCode.AUTH_INVALID_JWT, response, "token为空");
        }
        // 判断token是否合法
        JwtHelper.UserAndDepart userAndDepart = new JwtHelper().verifyTokenAndGetClaims(token);
        if (userAndDepart == null) {
            // 若token解析不合法
            return getErrorResponse(HttpStatus.UNAUTHORIZED, ResponseCode.AUTH_INVALID_JWT, response, "token解析不合法");
        }
        Long userId = userAndDepart.getUserId();
        Long departId = userAndDepart.getDepartId();
        if (departId != -2) {
            return getErrorResponse(HttpStatus.UNAUTHORIZED, ResponseCode.AUTH_INVALID_JWT, response, "管理员无法访问普通用户URL");
        }
        // 若token合法
        // 获取redis工具
        RedisTemplate redisTemplate = GatewayUtil.redis;
        // 判断该token是否被ban
        logger.debug("判断token是否被ban");
        if (redisTemplate.hasKey(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.writeWith(Mono.empty());
        }
        // 检测完了则该token有效
        // 解析有效期
        Date expireTime = userAndDepart.getExpTime();
        String jwt = token;

        // 判断该token有效期是否还长
        Long sec = expireTime.getTime() - System.currentTimeMillis();
        if (sec < GatewayUtil.getRefreshJwtTime() * 1000) {
            // 若快要过期了则重新换发token
            // 创建新的token
            JwtHelper jwtHelper = new JwtHelper();
            jwt = jwtHelper.createToken(userId, departId, GatewayUtil.getJwtExpireTime());
            logger.debug("重新换发token:" + jwt);
        }

        // 将token放在返回消息头中
        response.getHeaders().set(tokenName, jwt);
        return chain.filter(exchange);
    }

    public Mono<Void> getErrorResponse(HttpStatus status, ResponseCode code, ServerHttpResponse response) {
        response.setStatusCode(status);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        Object returnObj = ResponseUtil.fail(code, code.getMessage());
        DataBuffer db = response.bufferFactory().wrap(JacksonUtil.toJson(returnObj).getBytes());
        return response.writeWith(Mono.just(db));
    }

    public Mono<Void> getErrorResponse(HttpStatus status, ResponseCode code, ServerHttpResponse response, String message) {
        response.setStatusCode(status);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        Object returnObj = ResponseUtil.fail(code, message);
        DataBuffer db = response.bufferFactory().wrap(JacksonUtil.toJson(returnObj).getBytes());
        return response.writeWith(Mono.just(db));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    public static class Config {
        private String tokenName;

        public Config() {

        }

        public String getTokenName() {
            return tokenName;
        }

        public void setTokenName(String tokenName) {
            this.tokenName = tokenName;
        }
    }
}
