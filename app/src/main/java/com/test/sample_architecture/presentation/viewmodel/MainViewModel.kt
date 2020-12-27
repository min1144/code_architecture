package com.test.sample_architecture.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.test.sample_architecture.domain.model.Photo
import com.test.sample_architecture.domain.usecase.RestUseCase
import com.test.sample_architecture.util.ListLiveData

class MainViewModel: ViewModel() {

    val mainList = ListLiveData<Photo>()

    fun requestRest() {
        RestUseCase().parameter(RestUseCase.Parameter().apply {
            text = "cat"
        }).success { photos ->
            photos?.let {
                mainList.value = it.photo
            }
        }.fail {}.execute()
    }
}