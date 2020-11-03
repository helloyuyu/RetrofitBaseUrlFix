package com.helloyuyu.baseurlfix.sample

import android.arch.lifecycle.LiveData
import com.helloyuyu.baseurlfix.annotation.BaseUrl
import retrofit2.http.DELETE

import retrofit2.http.GET
import retrofit2.http.HTTP

/**
 * @author xjs
 */
@BaseUrl(Consts.BASE_URL)
interface TestKotlinService {

    @GET(Consts.TEST_PATH_GET)
    fun testGet(): LiveData<String>

    @HTTP(method = "PUT", path = Consts.TEST_PATH_HTTP)
    fun testHttp(): LiveData<String>

    @DELETE(value = "/test/delete")
    fun testDelete(): LiveData<String>
}
