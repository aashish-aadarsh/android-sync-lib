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

package com.devop.aashish.android.work


import android.content.Context
import androidx.work.*
import com.devop.aashish.android.datasource.database.entity.RequestEntity
import com.devop.aashish.android.datasource.database.entity.ResponseEntity
import com.devop.aashish.android.utility.AppConstants
import com.devop.aashish.android.utility.AppUtils
import com.devop.aashish.android.work.worker.GarbageCollectorWorker
import com.devop.aashish.android.work.worker.NetworkWorker
import java.util.concurrent.TimeUnit


object WorkUtil {
    fun initGarbageCollector(context: Context) {
        val myConstraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiresDeviceIdle(true)
            .build()

        val makeCleanUpRequest = PeriodicWorkRequest.Builder(
            GarbageCollectorWorker::class.java,
            30, TimeUnit.MINUTES)
            .setConstraints(myConstraints)
            .addTag("GARBAGE_CLEANING")
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            AppConstants.KEY_WORK_GARBAGE_COLLECTION,
            ExistingPeriodicWorkPolicy.REPLACE,
            makeCleanUpRequest
        )
    }

    fun networkRequest(requestEntity: RequestEntity): OneTimeWorkRequest {
        val myConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val myData = Data.Builder()
            .putString(AppConstants.KEY_REQUEST_ID, requestEntity.requestId)
            .build()
        return OneTimeWorkRequest.Builder(NetworkWorker::class.java)
            .setConstraints(myConstraints)
            .setInputData(myData)
            .addTag(
                "PERFORMING_NW_CALL_LATER..." + requestEntity.requestId + "...." +
                        requestEntity.requestEndPoint
            ).build()
    }

    fun returnWorkResult(
        requestId: String?,
        responseEntity: ResponseEntity
    ): ListenableWorker.Result {
        val key = AppConstants.KEY_RESULT_IDENTIFIER + requestId
        val inputMap = HashMap<String, String>()
        inputMap[key] = AppUtils.gsonObject.toJson(responseEntity)
        val output: Data =
            Data.Builder().putAll(inputMap as Map<String, String>).build()
        return if (responseEntity.responseSucceeded!!) {
            ListenableWorker.Result.success(output)
        } else
            ListenableWorker.Result.failure()
    }
}