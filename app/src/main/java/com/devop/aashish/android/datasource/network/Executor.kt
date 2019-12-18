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

@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.devop.aashish.android.datasource.network

import android.util.Log
import androidx.annotation.RestrictTo
import com.devop.aashish.android.datasource.database.entity.RequestEntity
import com.devop.aashish.android.datasource.database.entity.ResponseEntity
import com.devop.aashish.android.datasource.network.util.RequestUtil
import com.devop.aashish.android.datasource.network.util.ResponseUtil
import com.devop.aashish.android.utility.AppUtils
import com.devop.aashish.android.utility.contract.ResponseHandler

@RestrictTo(RestrictTo.Scope.LIBRARY)
object Executor {
    private const val TAG = "ExecutorHelper"

    fun executeOp(
        requestEntity: RequestEntity
    ): ResponseEntity {
        val okHttpClient = OkHttpClientProvider().getClient()
        val request = RequestUtil.buildRequest(requestEntity)
        val responseEntity: ResponseEntity
        responseEntity = try {
            val response = okHttpClient!!.newCall(request).execute()
            Log.i("Executor", "Server Response...$response")
            ResponseUtil.processResponse(requestEntity, response)
        } catch (e: Exception) {
            Log.e(TAG, "error in getting response for request \n$requestEntity \n", e)
            ResponseUtil.processErrorResponse(requestEntity, e)
        }
        AppUtils.updateRequestResponse(requestEntity, responseEntity)
        return responseEntity
    }

    fun invokeResponseHandler(
        requestEntity: RequestEntity,
        responseEntity: ResponseEntity?
    ) {
        if (null != requestEntity.responseHandler) {
            val handlerName = requestEntity.responseHandler!!
            val clazz = Class.forName(handlerName)
            val rh = clazz.newInstance() as ResponseHandler<*>
            rh.handleResponse(requestEntity, responseEntity)
        }
    }
}

