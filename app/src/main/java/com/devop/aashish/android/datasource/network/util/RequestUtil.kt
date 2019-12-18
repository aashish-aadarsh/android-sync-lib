/*
 * Copyright(c)  2019 Aashish Aadarsh
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 *    @author : Aashish Aadarsh
 *    Follow Me:  "https://github.com/aashish-aadarsh"
 */

package com.devop.aashish.android.datasource.network.util

import com.devop.aashish.android.datasource.database.entity.RequestEntity
import com.devop.aashish.android.utility.AppUtils
import com.google.gson.reflect.TypeToken
import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.internal.http.HttpMethod
import okio.BufferedSink
import java.io.IOException

object RequestUtil {

    fun buildRequest(requestEntity: RequestEntity): Request {
        val requestBuilder = Request.Builder()
        val type = object : TypeToken<Map<String, String>>() {}.type

        requestBuilder.url(
            buildURL(
                requestEntity
            )
        )

        if (requestEntity.requestHeader != null) {
            val headerMap = AppUtils.gsonObject.
                fromJson<Map<String, String>>(requestEntity.requestHeader, type)
            for ((key, value) in headerMap) {
                requestBuilder.addHeader(key, value)
            }
        }

        if(HttpMethod.permitsRequestBody(requestEntity.requestMethod)){
            val requestBody = object : RequestBody() {
                override fun contentType(): MediaType? {
                    return MediaType.parse("application/json")
                }
                override fun writeTo(sink: BufferedSink) {
                    sink.write(requestEntity.requestBody!!.toByteArray())
                }
            }
            requestBuilder.method(requestEntity.requestMethod, requestBody)
        }else{
            requestBuilder.method(requestEntity.requestMethod, null)
        }

        if (requestEntity.requestMetaData != null) {
            requestBuilder.tag(requestEntity.requestMetaData!!)
        }
        return requestBuilder.build()
    }

    private fun buildURL(requestEntity: RequestEntity): String {
        val builder =
            HttpUrl.parse(requestEntity.requestBaseURL + "/" + requestEntity.requestEndPoint)!!.newBuilder()
        val type = object : TypeToken<Map<String, String>>() {}.type

        if (null != requestEntity.requestParam) {
            val paramMap = AppUtils.gsonObject.fromJson<Map<String, String>>(
                requestEntity.requestParam,
                type
            )
            for ((key, value) in paramMap) {
                builder.setQueryParameter(key, value)
            }
        }

        if (null != requestEntity.requestPathParam) {
            val pathMap = AppUtils.gsonObject.fromJson<Map<String, String>>(
                requestEntity.requestPathParam,
                type
            )
            for ((key, value) in pathMap) {
                //todo to implement path param
            }
        }
        return builder.build().toString()
    }

}