package com.test.sample_architecture.domain.usecase

import com.google.gson.reflect.TypeToken
import com.test.sample_architecture.api.repository.RestRepository
import com.test.sample_architecture.domain.GsonLoader.gson
import com.test.sample_architecture.domain.model.Photos
import com.test.sample_architecture.domain.toJson
import com.test.sample_architecture.domain.usecase.base.BaseUseCase
import com.test.sample_architecture.entity.PhotosEntity

class RestUseCase : BaseUseCase<RestRepository, RestRepository.Parameter, Photos, PhotosEntity>() {

    override fun toObject(raw: PhotosEntity?): Photos? {
        return raw?.toJson()?.let {
            gson.fromJson(it, object : TypeToken<Photos>() {}.type)
        }
    }

    class Parameter : RestRepository.Parameter()
}