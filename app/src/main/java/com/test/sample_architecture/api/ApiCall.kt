package com.test.sample_architecture.api

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class ApiCall<R>(
    private val originalCall: Call<R>,
    override val coroutineContext: CoroutineContext = Dispatchers.Default
) : CoroutineScope {

    private var responseListener: ((r: R?) -> Unit)? = null

    private var failureListener: ((e: Throwable) -> Unit)? = null

    fun failure(l: (e: Throwable) -> Unit): ApiCall<R> {
        failureListener = l
        return this
    }

    fun response(r: (raw: R?) -> Unit): ApiCall<R> {
        responseListener = r
        return this
    }

    fun async(): R? {
        try {
            return originalCall.execute().body()
        } catch (e: Exception) { }
        return null
    }

    fun execute() {
        fun release() {
            failureListener = null
            responseListener = null
        }

        val c = originalCall.clone()
        c.enqueue(object : retrofit2.Callback<R> {
            override fun onFailure(call: Call<R>, t: Throwable) {
                failureListener?.let {
                    launch(Dispatchers.Main) {
                        it(t)
                        release()
                    }
                }.run {
                    release()
                }
            }

            override fun onResponse(call: Call<R>, response: Response<R>) {
                responseListener?.let {
                    launch(Dispatchers.Main) {
                        it(response.body())
                        release()
                    }
                }.run {
                    release()
                }
            }
        })
    }
}