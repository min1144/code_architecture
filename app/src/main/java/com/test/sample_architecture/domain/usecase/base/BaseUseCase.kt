package com.test.sample_architecture.domain.usecase.base

import com.test.sample_architecture.api.repository.base.BaseRepository
import com.test.sample_architecture.util.ClassUtil

abstract class BaseUseCase<Repo : BaseRepository<*, RawResult>, Parameter, DomainResult, RawResult> {

    private var responseListener: ((DomainResult?) -> Unit)? = null

    private var errorListener: ((error: Throwable) -> Unit)? = null

    private val repository: Repo by lazy {
        val innerClass = ClassUtil.getReclusiveGenericClass(this::class.java, 0) as Class<*>
        innerClass.newInstance() as Repo
    }

    fun remote(): BaseUseCase<Repo, Parameter, DomainResult, RawResult> {
        repository.remote()
        return this
    }

    fun local(): BaseUseCase<Repo, Parameter, DomainResult, RawResult> {
        repository.local()
        return this
    }

    open fun toObject(raw: RawResult?): DomainResult? {
        @Suppress("UNCHECKED_CAST")
        return raw as DomainResult
    }

    fun success(value: ((DomainResult?) -> Unit)?): BaseUseCase<Repo, Parameter, DomainResult, RawResult> {
        responseListener = value
        return this
    }

    fun fail(value: ((error: Throwable) -> Unit)?): BaseUseCase<Repo, Parameter, DomainResult, RawResult> {
        errorListener = value
        return this
    }

    fun parameter(p: Parameter): BaseUseCase<Repo, Parameter, DomainResult, RawResult> {
        ClassUtil.applyParameter(repository, p)
        return this
    }

    fun execute() {
        repository.response {
            responseListener?.let { raw ->
                raw(toObject(it))
                onDestroy()
            }
        }.fail {
            errorListener?.let { raw ->
                raw(it)
                onDestroy()
            }
        }.execute()
    }

    fun sync(): DomainResult? {
        return repository.let {
            repository.sync()
        }.let {
            toObject(it)
        }
    }

    private fun onDestroy() {
        responseListener = null
        errorListener = null
    }
}