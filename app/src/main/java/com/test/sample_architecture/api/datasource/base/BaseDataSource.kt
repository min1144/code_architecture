package com.test.sample_architecture.api.datasource.base

abstract class BaseDataSource<Param, Result> {

    var responseListener: ((Result?) -> Unit)? = null

    var errorListener: ((error: Throwable) -> Unit)? = null

    var param: Param? = null

    fun response(value: ((Result?) -> Unit)?): BaseDataSource<Param, Result> {
        responseListener = value
        return this
    }

    fun fail(value: ((error: Throwable) -> Unit)?): BaseDataSource<Param, Result> {
        errorListener = value
        return this
    }

    fun parameter(p: Param): BaseDataSource<Param, Result> {
        param = p
        return this
    }

    abstract fun execute()
    abstract fun sync(): Result?

    open fun onDestroy() {
        responseListener = null
        errorListener = null
        param = null
    }
}
