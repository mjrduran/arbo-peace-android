package com.arbo.oracoes.util

import android.util.Log
import com.arbo.oracoes.BuildConfig
import com.crashlytics.android.Crashlytics

object AppLogger {

    /**
     * Log only if debug mode. Does not log on production environments.
     */
    fun d(tag: String, message: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            when (throwable) {
                null -> Log.d(tag, message)
                else -> Log.d(tag, message, throwable)
            }
        }
    }

    fun i(tag: String, message: String, throwable: Throwable? = null) = when (throwable) {
        null -> Log.i(tag, message)
        else -> Log.i(tag, message, throwable)
    }

    fun iDebug(tag: String, message: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            i(tag, message, throwable)
        }
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) = when (throwable) {
        null -> Log.e(tag, message)
        else -> Log.e(tag, message, throwable)
    }

    fun eOnlyDebug(tag: String, message: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            e(tag, message, throwable)
        }
    }

    /**
     * Log errors in remote server too. Actually we use only Crashlytics, but in future we
     * can add other crash servers if necessary (like Firebase).
     * Use this method to log unexpected errors. Do not use it to log errors and exceptions that
     * are handled by the app. Instead, use [AppLogger.e]
     */
    fun eRemote(tag: String, message: String, throwable: Throwable? = null) = when (throwable) {
        null -> {
            Log.e(tag, message)
            Crashlytics.log(message)
        }

        else -> {
            Log.e(tag, message, throwable)
            Crashlytics.logException(throwable)
        }
    }

    fun v(tag: String, message: String, throwable: Throwable? = null) = when (throwable) {
        null -> Log.v(tag, message)
        else -> Log.v(tag, message, throwable)
    }

    fun w(tag: String, message: String? = null, throwable: Throwable? = null) = when {
        message == null && throwable != null -> Log.w(tag, throwable)

        throwable == null && message != null -> Log.w(tag, message)

        else -> Log.w(tag, message, throwable)
    }

    fun wtf(tag: String, message: String? = null, throwable: Throwable? = null) = when {
        message == null && throwable != null -> Log.wtf(tag, throwable)

        throwable == null && message != null -> Log.wtf(tag, message)

        else -> Log.wtf(tag, message, throwable)
    }

}