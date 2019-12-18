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

package com.devop.aashish.android.work.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.devop.aashish.android.core.Injection
import com.devop.aashish.android.datasource.repository.impl.RepositoryImpl
import com.devop.aashish.android.vo.RequestStatus
import java.util.*


class GarbageCollectorWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    private val repository = Injection.repoInstance()

    override fun doWork(): Result {
        Log.i("GarbageCollector", "doWork() started...." + Date().toString())
        val requestEntities = repository.findByRequestStatus(RequestStatus.COMPLETED.name)
        requestEntities.forEach {
            val responseEntities = repository.findResponseByRequestId(it.requestId)
            if (null != responseEntities) {
                Log.d("GarbageCollector", "deleting Response Entities....$responseEntities")
                repository.deleteResponse(responseEntities)
            }
        }
        Log.d("GarbageCollector", "deleting Request Entities....$requestEntities")
        repository.deleteRequest(requestEntities)
        Log.i("GarbageCollector", "doWork() finished...." + Date().toString())
        return Result.success()
    }
}
