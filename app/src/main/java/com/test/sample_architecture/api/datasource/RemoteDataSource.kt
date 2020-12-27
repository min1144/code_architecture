package com.test.sample_architecture.api.datasource

import com.test.sample_architecture.api.ApiCall
import com.test.sample_architecture.api.datasource.base.BaseDataSource

abstract class RemoteDataSource<Param, Result, RawResponse> : BaseDataSource<Param, Result>() {

    protected abstract fun fetchApi(param: Param?): ApiCall<RawResponse>

    abstract fun toObject(raw: RawResponse?): Result?

    override fun execute() {
        fetchApi(this.param)
            .response { raw ->
                onResponse(raw)
                responseListener?.let { re ->
                    re(toObject(raw))
                }
                onDestroy()
            }
            .failure { e ->
                errorListener?.let { re ->
                    re(e)
                }
                onDestroy()
            }
            .execute()
    }

    override fun sync(): Result? {
        val item = fetchApi(param).async()
        return toObject(item)
    }

    open fun onResponse(response: RawResponse?) {}
}
