package com.devop.aashish.android.core

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context


@SuppressLint("StaticFieldLeak")
object AppHolder {


    fun setAppContext(context: Context) {
        sContext = context
    }

    fun getAppContext(): Context? {
        if (sContext == null)
            return context
        return sContext
    }

    private var APP: Application? = null
    private var sContext: Context? = null

    private var context: Context? = null
        get() = if (null != field) field else APP?.applicationContext

    init {
        try {
            @SuppressLint("PrivateApi") val c = Class.forName("android.app.ActivityThread")
            APP = c.getDeclaredMethod("currentApplication").invoke(null) as Application
        } catch (e: Throwable) {
            e.printStackTrace()
        }

    }
}
