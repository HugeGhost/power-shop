package com.powernode.shop.constants;

import java.util.Arrays;
import java.util.List;

/**
 * @author ggboy
 * @create2022-09-16 10:32
 */
public class GatewayConstants {
    //请求白名单路径
    List<String> ALLOW_URLS = Arrays.asList("/oauth/token");

    //请求头中token的key
    String AUTHORIZATION = "Authorization";
    //请求头中token的value前缀
    String BEARER_PREFIX = "bearer ";
}
