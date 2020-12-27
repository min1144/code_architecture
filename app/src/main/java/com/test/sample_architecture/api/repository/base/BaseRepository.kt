package com.test.sample_architecture.api.repository.base

import com.test.sample_architecture.api.datasource.RemoteDataSource

abstract class BaseRepository<Param, Result> {

    private var responseListener: ((Result?) -> Unit)? = null

    private var errorListener: ((error: Throwable) -> Unit)? = null

    private var param: Param? = null

//    private val local: LocalDataSource<Result>? by lazy {
//        createLocalDataSource()
//    }

    private val remote: RemoteDataSource<Param, *, *> by lazy {
        createRemoteDataSource()
    }

    private var strategy: DataFetchStrategy = DataFetchStrategy.REMOTE

    protected abstract fun createRemoteDataSource(): RemoteDataSource<Param, *, *>

    fun remote(): BaseRepository<Param, Result> {
        strategy = DataFetchStrategy.REMOTE
        return this
    }

    fun local(): BaseRepository<Param, Result> {
        strategy = DataFetchStrategy.LOCAL
        return this
    }

//    protected open fun createLocalDataSource(): LocalDataSource<Result>? {
//        return null
//    }

    protected open fun toObject(raw: Any?): Result? {
        @Suppress("UNCHECKED_CAST")
        return raw as Result
    }

    fun response(value: ((Result?) -> Unit)?): BaseRepository<Param, Result> {
        responseListener = value
        return this
    }

    fun fail(value: ((error: Throwable) -> Unit)?): BaseRepository<Param, Result> {
        errorListener = value
        return this
    }

    fun parameter(p: Param): BaseRepository<Param, Result> {
        param = p
        return this
    }

    protected open fun onDestroy() {
        responseListener = null
        errorListener = null
        param = null
    }

    fun execute() {
        if (strategy == DataFetchStrategy.REMOTE) {
            remote.response {
                if (it != null) {
                    responseListener?.let { raw ->
                        raw(toObject(it))
                    }
                }
                onDestroy()
            }.fail {
                errorListener?.let { raw ->
                    raw(it)
                }
                onDestroy()
            }.let {
                val p = param
                if (p != null) {
                    it.parameter(p)
                }
                it.execute()
            }
        }
    }

    fun sync(): Result? {
        return (if (strategy == DataFetchStrategy.REMOTE) {
            remote.let {
                val p = param
                if (p != null) {
                    it.parameter(p)
                }
                val item = it.sync()
                if (item != null) {
                    return null
                }
                item
            }
        } else {
            // local add
//            local?.get()
        }).let {
            toObject(it)
        }
    }

    enum class DataFetchStrategy {
        REMOTE,
        LOCAL,
    }
}