package com.helloyuyu.baseurlfix.sample;

import android.arch.lifecycle.LiveData;

import com.helloyuyu.baseurlfix.annotation.BaseUrl;

import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;

/**
 * @author xjs
 * @date 2019/7/27
 */
@BaseUrl(Consts.BASE_URL)
public interface TestJavaService {

    @GET(Consts.TEST_PATH_GET)
    LiveData<String> testGet();

    @HTTP(method = "PUT", path = Consts.TEST_PATH_HTTP)
    LiveData<String> testHttp();

    @DELETE(value = "/test/delete")
    LiveData<String> testDelete();
}
