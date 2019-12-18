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

package com.devop.aashish.android.core

import android.content.Context
import androidx.work.WorkManager
import com.devop.aashish.android.datasource.network.Executor
import com.devop.aashish.android.utility.contract.ExecutionCallBack
import com.devop.aashish.android.vo.Request
import com.devop.aashish.android.vo.mapper.RequestToEntity
import com.devop.aashish.android.work.WorkUtil


class RequestExecution<T> {
    private val requestRepo = Injection.repoInstance()

    fun now(request: Request<T>, mCallback: ExecutionCallBack) {
        Injection.provideAppExecutors().networkIO().execute {
            val requestEntity = RequestToEntity<T>().mapToEntity(request)
            requestRepo.addRequest(requestEntity)
            val responseEntity = Executor.executeOp(requestEntity)
            Injection.provideAppExecutors().mainThread().execute {
                mCallback.operationCompleted(requestEntity, responseEntity)
            }
        }
    }

    fun later(request: Request<T>, context: Context) {
        Injection.provideAppExecutors().diskIO().execute {
            val requestEntity = RequestToEntity<T>().mapToEntity(request)
            val workRequest = WorkUtil.networkRequest(requestEntity)
            requestEntity.requestWorkerId = workRequest.id.toString()
            requestRepo.addRequest(requestEntity)
            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}
