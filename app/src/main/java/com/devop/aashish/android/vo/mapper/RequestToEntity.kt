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

package com.devop.aashish.android.vo.mapper

import android.text.TextUtils
import com.devop.aashish.android.datasource.database.entity.RequestEntity
import com.devop.aashish.android.utility.AppUtils
import com.devop.aashish.android.vo.Request
import com.devop.aashish.android.vo.RequestStatus
import java.lang.IllegalArgumentException
import java.util.*


class RequestToEntity<T> {

    fun mapToEntity(request: Request<T>): RequestEntity {
        if (TextUtils.isEmpty(request.requestBaseURL) || TextUtils.isEmpty(request.requestEndPoint)) {
            throw IllegalArgumentException("BaseURL or end-point can not be empty")
        }
        val gson = AppUtils.gsonObject
        val requestEntity = RequestEntity(
            requestId = UUID.randomUUID().toString(),
            requestBaseURL = request.requestBaseURL,
            requestEndPoint = request.requestEndPoint,
            requestMethod = request.requestMethod.name
        )

        if (null != request.requestBody)
            requestEntity.requestBody = gson.toJson(request.requestBody)

        if (null != request.requestParam && request.requestParam!!.isNotEmpty())
            requestEntity.requestParam = gson.toJson(request.requestParam)

        if (null != request.requestHeader && request.requestHeader!!.isNotEmpty())
            requestEntity.requestHeader = gson.toJson(request.requestHeader)

        if (null != request.requestPathParam && request.requestPathParam!!.isNotEmpty())
            requestEntity.requestPathParam = gson.toJson(request.requestPathParam)

        requestEntity.requestClazz = request.responseClass?.name

        if (request.responseHandler != null) {
            requestEntity.responseHandler = request.responseHandler!!.javaClass.name
        }
        requestEntity.requestProcessingStatus = RequestStatus.INSERTED.name
        return requestEntity
    }
}
