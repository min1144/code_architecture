package com.test.sample_architecture.api.service

import com.test.sample_architecture.entity.MainEntity
import com.test.sample_architecture.api.ApiCall
import com.test.sample_architecture.api.RequestData.API_KEY
import com.test.sample_architecture.api.RequestData.CALLBACK_TYPE
import com.test.sample_architecture.api.RequestData.FORMAT
import com.test.sample_architecture.api.RequestData.PER_PAGE
import com.test.sample_architecture.api.RequestData.REQUEST_METHOD
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoService {

    @GET("/services/rest/")
    fun restList(
        @Query("format") format: String = FORMAT,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("method") method: String = REQUEST_METHOD,
        @Query("per_page") perPage: Int = PER_PAGE,
        @Query("nojsoncallback") nojsoncallback: Int = CALLBACK_TYPE,
        @Query("text") text: String
    ): ApiCall<MainEntity>
}