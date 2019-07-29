package com.holloyuyu.baseurlfix.testlib;

import com.helloyuyu.baseurlfix.annotation.BaseUrl;
import retrofit2.http.GET;
import retrofit2.http.HTTP;

/**
 * @author xjs
 * @date 2019/7/29
 */
@BaseUrl("www.taobao.com")
public interface LibService {

    @GET("/test/get")
    String testGet();

    @HTTP(method = "DELETE", path = "/test/http")
    String testHttp();
}
