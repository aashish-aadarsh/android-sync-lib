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

package com.devop.aashish.android

import android.content.Context
import com.devop.aashish.android.core.AppHolder
import com.devop.aashish.android.core.RequestExecution
import com.devop.aashish.android.utility.contract.ExecutionCallBack
import com.devop.aashish.android.vo.Request
import com.devop.aashish.android.work.WorkUtil


class SyncLib(context: Context) {
    private val mContext = context

    init {
        WorkUtil.initGarbageCollector(context)
        AppHolder.setAppContext(mContext)
    }

    /**
     * REQUEST->NW_AVAILABLE->PERFORM
     */
    fun <T> now(request: Request<T>, callBack: ExecutionCallBack) {
        RequestExecution<T>().now(request, callBack)
    }

    /**
     * REQUEST->DB->NW_AVAILABLE->PERFORM
     */
    fun <T> later(request: Request<T>) {
        RequestExecution<T>().later(request, mContext)
    }


}

