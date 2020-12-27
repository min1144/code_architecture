package com.test.sample_architecture.api

import com.test.sample_architecture.api.service.PhotoService
import retrofit2.Retrofit

object Request {

    private val api: Retrofit by lazy { RetrofitFactory.create() }

    val photoService: PhotoService
    get() {
        return api.newBuilder().build().create(PhotoService::class.java)
    }
}