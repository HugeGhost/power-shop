package com.powernode.shop.router;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.powernode.shop.constants.GatewayConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

/**
 * @author ggboy
 * @create2022-11-01 18:20
 */
@Configuration
public class LoginRouter {

    @Autowired
    RedisTemplate redisTemplate;

    //代码级别路由
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder){
        return builder
                .routes()
                //当请求地址是/oauth/token时，转发到auth-server微服务
                .route("auth-server",r -> r.path("/oauth/token")
                        .filters(f -> f.modifyRequestBody(String.class,String.class,((serverWebExchange, s) -> {
                            //s为响应回来的token数据
                            //{access_token:xxx,expires_in:xxx}
                            //将token和它的过期时间存入到Redis中，进行保存和校验操作
                            JSONObject jsonObject = JSON.parseObject(s);

                            //正常返回了token数据
                            if(jsonObject.containsKey(GatewayConstants.REDIS_JWT_ACCESS_TOKEN_KEY)&&jsonObject.containsKey(GatewayConstants.REDIS_JWT_EXPIRES_IN_KEY)){
                                //获取token
                                String accessToken = jsonObject.getString(GatewayConstants.REDIS_JWT_ACCESS_TOKEN_KEY);
                                //获取过期时间
                                Integer expiresIn = jsonObject.getInteger(GatewayConstants.REDIS_JWT_EXPIRES_IN_KEY);
                                //获取存入redis中
                                redisTemplate.opsForValue().set(
                                        GatewayConstants.OAUTH_JWT_PREFIX+accessToken,
                                        "",
                                        //过期时间
                                        expiresIn,
                                        TimeUnit.SECONDS
                                );
                            }
                            return Mono.just(s);
                        })))
                        .uri("lb://auth-server"))
                .build();
    }
}
