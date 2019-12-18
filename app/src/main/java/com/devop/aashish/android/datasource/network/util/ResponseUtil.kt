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

import androidx.annotation.NonNull
import com.devop.aashish.android.datasource.database.entity.RequestEntity
import com.devop.aashish.android.datasource.database.entity.ResponseEntity
import com.devop.aashish.android.utility.AppUtils
import com.devop.aashish.android.vo.RequestStatus
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener

object ResponseUtil {

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun processResponse(@NonNull requestEntity: RequestEntity, response: Response?): ResponseEntity {
        val rawResponse = response?.body()?.string()
        var value: Any? = null
        val responseEntity: ResponseEntity =
            buildResponseEntity(requestEntity, response, rawResponse)

        if (response?.isSuccessful!!) {
            requestEntity.requestProcessingStatus = RequestStatus.COMPLETED.name
            val json = JSONTokener(rawResponse).nextValue()
            if (json is JSONObject) {
                value = AppUtils.gsonObject.fromJson(
                    rawResponse,
                    Class.forName(requestEntity.requestClazz)
                )
            } else if (json is JSONArray) {
                value = AppUtils.parseListData(requestEntity.requestClazz!!, rawResponse!!)
            }
            responseEntity.clazz = value
        } else {
            requestEntity.requestProcessingStatus = RequestStatus.FAILED.name
        }
        return responseEntity
    }

    private fun buildResponseEntity(
        requestEntity: RequestEntity,
        response: Response?,
        rawResponse: String?
    ): ResponseEntity {
        return ResponseEntity(
            responseId = 0,
            requestId = requestEntity.requestId,
            responseRaw = rawResponse,
            responseSucceeded = response?.isSuccessful,
            responseTime = (response!!.receivedResponseAtMillis() - response.sentRequestAtMillis()),
            requestClazz = requestEntity.requestClazz,
            responseCode = response.code()
        )
    }

    fun processErrorResponse(requestEntity: RequestEntity, e: Exception): ResponseEntity {
        requestEntity.requestProcessingStatus = RequestStatus.FAILED.name
        val responseEntity = ResponseEntity(
            requestId = requestEntity.requestId,
            responseRaw = e.message,
            responseSucceeded = false,
            responseCode = null,
            requestClazz = requestEntity.requestClazz,
            responseId = -1
        )
        responseEntity.exception = e
        return responseEntity
    }

}