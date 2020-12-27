package com.test.sample_architecture.domain

import com.google.gson.Gson
import com.google.gson.GsonBuilder

internal object GsonLoader {
    val gson: Gson by lazy {
        GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create()
    }
}

fun <T> T.toJson(): String {
    return GsonLoader.gson.toJson(this)
}