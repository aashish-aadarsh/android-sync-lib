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

package com.devop.aashish.android.datasource.database.entity

import androidx.annotation.NonNull
import androidx.annotation.RestrictTo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "request")
@RestrictTo(RestrictTo.Scope.LIBRARY)
data class RequestEntity(

    @PrimaryKey
    @NonNull
    var requestId: String,

    var requestWorkerId: String? = null,
    var requestMethod: String,
    var requestBaseURL: String,
    var requestEndPoint: String,
    var requestBody: String? = null,
    var requestHeader: String? = null,
    var requestPathParam: String? = null,
    var requestParam: String? = null,
    var requestMetaData: String? = null,
    var requestClazz: String? = null,
    var responseHandler: String? = null,
    var requestProcessingStatus: String? = null
) : Serializable









