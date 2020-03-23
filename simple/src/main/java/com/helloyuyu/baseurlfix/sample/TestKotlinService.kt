package com.helloyuyu.baseurlfix.sample

import android.arch.lifecycle.LiveData
import com.helloyuyu.baseurlfix.annotation.BaseUrl

import retrofit2.http.GET
import retrofit2.http.HTTP

/**
 * @author xjs
 * @date 2019/7/27
 */
@BaseUrl(Consts.BASE_URL)
interface TestKotlinService {

    @GET(Consts.TEST_PATH_GET)
    fun testGet(): LiveData<String>

    @HTTP(method = "PUT", path = Consts.TEST_PATH_HTTP)
    fun testHttp(): LiveData<String>
}
