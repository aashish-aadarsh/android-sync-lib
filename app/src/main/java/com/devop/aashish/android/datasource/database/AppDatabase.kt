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

package com.devop.aashish.android.datasource.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.devop.aashish.android.core.AppHolder
import com.devop.aashish.android.datasource.database.dao.RequestCascadeDao
import com.devop.aashish.android.datasource.database.dao.RequestDao
import com.devop.aashish.android.datasource.database.dao.ResponseDao
import com.devop.aashish.android.datasource.database.entity.RequestCascadeEntity
import com.devop.aashish.android.datasource.database.entity.RequestEntity
import com.devop.aashish.android.datasource.database.entity.ResponseEntity

@Database(
    entities = [
        RequestEntity::class,
        ResponseEntity::class,
        RequestCascadeEntity::class],
    version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private const val DB_NAME = "request_handler.db"
        private var mAppDatabase: AppDatabase? = null

        private fun provideDatabase(): AppDatabase {
            if (mAppDatabase == null) {
                mAppDatabase = Room.databaseBuilder(
                    AppHolder.getAppContext()!!,
                    AppDatabase::class.java, DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return mAppDatabase as AppDatabase
        }

        fun getInstance(): AppDatabase {
            return mAppDatabase ?: synchronized(this) {
                mAppDatabase ?: provideDatabase().also { mAppDatabase = it }
            }
        }

    }

    abstract fun getRequestDao(): RequestDao

    abstract fun getResponseDao(): ResponseDao

    abstract fun getRequestCascadeDao(): RequestCascadeDao

}
