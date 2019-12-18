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

package com.devop.aashish.android.utility

import android.text.TextUtils
import com.devop.aashish.android.core.Injection
import com.devop.aashish.android.datasource.database.entity.RequestEntity
import com.devop.aashish.android.datasource.database.entity.ResponseEntity
import com.devop.aashish.android.vo.RequestStatus
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.text.DateFormat
import java.util.*


object AppUtils {

    val gsonObject: Gson
        get() {

            val ser =
                JsonSerializer<Date> { src, _, _ -> if (src == null) null else JsonPrimitive(src.time) }

            val deser =
                JsonDeserializer<Date> { json, _, _ -> if (json == null) null else Date(json.asLong) }

            return GsonBuilder()
                .setDateFormat(DateFormat.FULL, DateFormat.FULL)
                .registerTypeAdapter(Date::class.java, ser)
                .registerTypeAdapter(Date::class.java, deser).create()
        }


    fun parseListData(className: String, json: String): List<*>? {
        try {
            val typeToken = TypeToken.getParameterized(List::class.java, Class.forName(className))
            return AppUtils.gsonObject.fromJson<List<*>>(json, typeToken.type)
        } catch (e: ClassNotFoundException) {
            throw IllegalArgumentException("Unsupported type: $className", e)
        }
    }

    fun updateRequestResponse(requestEntity: RequestEntity, responseEntity: ResponseEntity) {
        Injection.repoInstance().addRequest(requestEntity)
        Injection.repoInstance().addResponse(responseEntity)
    }

    fun validateRequest(requestId: String?): RequestEntity {
        if (TextUtils.isEmpty(requestId)) {
            throw IllegalArgumentException("RequestId can not be NULL !!")
        }
        return Injection.repoInstance().findByRequestIdAndStatus(
            requestId!!,
            RequestStatus.INSERTED.name
        ) ?: throw IllegalArgumentException("RequestObject can not be NULL !!")
    }
}
