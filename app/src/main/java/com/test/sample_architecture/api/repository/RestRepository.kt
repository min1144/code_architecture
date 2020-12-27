package com.test.sample_architecture.api.repository

import com.test.sample_architecture.entity.MainEntity
import com.test.sample_architecture.api.ApiCall
import com.test.sample_architecture.api.Request
import com.test.sample_architecture.api.datasource.RemoteDataSource
import com.test.sample_architecture.api.datasource.base.nonnull
import com.test.sample_architecture.api.repository.base.BaseRepository
import com.test.sample_architecture.entity.PhotosEntity

class RestRepository : BaseRepository<RestRepository.Parameter, PhotosEntity>() {

    override fun createRemoteDataSource(): RemoteDataSource<Parameter, *, *> {
        return object : RemoteDataSource<Parameter, PhotosEntity, MainEntity>() {
            override fun fetchApi(param: Parameter?): ApiCall<MainEntity> {
                return Request.photoService.restList(text = param?.text.nonnull())
            }

            override fun toObject(raw: MainEntity?): PhotosEntity? {
                return raw?.photosEntity
            }
        }
    }

    open class Parameter {
        var text: String = ""
    }
}