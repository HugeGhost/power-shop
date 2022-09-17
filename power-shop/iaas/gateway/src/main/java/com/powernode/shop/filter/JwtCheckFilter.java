package com.powernode.shop.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关过滤器
 *      用于检查token
 * @author ggboy
 * @create2022-09-16 10:15
 */
@Configuration
public class JwtCheckFilter implements GlobalFilter, Ordered {
    /*
        过滤器
            1、获取请求路径
            2、是否是白名单（/oauth/token）
            3、如果是白名单则放行，如果不是白名单我们需要获取token
                请求头中
                    Authorization : bearer token
            4、如果传递了我们放行
            5、如果没有传递，我们返回没有权限信息
     */
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //获取请求路径
        String address = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        String path = exchange.getRequest().getPath().toString();
        return null;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
