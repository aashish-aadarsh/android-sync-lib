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
import androidx.annotation.RestrictTo
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.devop.aashish.android.datasource.network.Executor
import com.devop.aashish.android.utility.AppConstants
import com.devop.aashish.android.utility.AppUtils
import com.devop.aashish.android.work.WorkUtil

@RestrictTo(RestrictTo.Scope.LIBRARY)
class NetworkWorker(context: Context, private val workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {


    override fun doWork(): Result {
        val data = workerParameters.inputData
        val requestId = data.getString(AppConstants.KEY_REQUEST_ID)
        val requestEntity = AppUtils.validateRequest(requestId)
        val responseEntity = Executor.executeOp(requestEntity)
        Executor.invokeResponseHandler(requestEntity, responseEntity)
        return WorkUtil.returnWorkResult(requestId, responseEntity)
    }
}
