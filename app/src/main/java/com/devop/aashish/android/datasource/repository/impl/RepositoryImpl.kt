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

package com.devop.aashish.android.datasource.repository.impl

import com.devop.aashish.android.datasource.database.AppDatabase
import com.devop.aashish.android.datasource.database.entity.RequestCascadeEntity
import com.devop.aashish.android.datasource.database.entity.RequestEntity
import com.devop.aashish.android.datasource.database.entity.ResponseEntity
import com.devop.aashish.android.datasource.repository.Repository

class RepositoryImpl
private constructor() : Repository {

    private val appDatabase: AppDatabase = AppDatabase.getInstance()

    companion object {
        private var INSTANCE: RepositoryImpl? = null

        fun getInstance(): RepositoryImpl {
            if (null == INSTANCE) {
                INSTANCE = RepositoryImpl()
            }
            return INSTANCE as RepositoryImpl
        }

    }

    override fun addRequest(requestEntity: RequestEntity) {
        appDatabase.getRequestDao().insert(requestEntity)
    }


    override fun findByRequestIdAndStatus(
        requestId: String,
        requestStatus: String
    ): RequestEntity? {
        return appDatabase.getRequestDao().findByRequestIdAndStatus(requestId, requestStatus)
    }

    override fun findByRequestStatus(requestStatus: String): List<RequestEntity> {
        return appDatabase.getRequestDao().findByRequestStatus(requestStatus)
    }

    override fun deleteRequest(requestEntityList: List<RequestEntity>) {
        appDatabase.getRequestDao().deleteAll(requestEntityList)
    }

    override fun addResponse(responseEntity: ResponseEntity) {
        appDatabase.getResponseDao().insert(responseEntity)
    }

    override fun findLatestResponseByRequestId(requestId: String): ResponseEntity? {
        return appDatabase.getResponseDao().findLastByRequestId(requestId)
    }

    override fun findResponseByRequestId(requestId: String): List<ResponseEntity>? {
        return appDatabase.getResponseDao().findByRequestId(requestId)
    }

    override fun deleteResponse(responseEntityList: List<ResponseEntity>) {
        appDatabase.getResponseDao().deleteAll(responseEntityList)
    }

    override fun addCascadeRequest(requestCascadeEntity: RequestCascadeEntity) {
        appDatabase.getRequestCascadeDao().insert(requestCascadeEntity)
    }
}
