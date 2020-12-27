package com.test.sample_architecture.entity

import com.google.gson.annotations.SerializedName

data class MainEntity (

	@SerializedName("photos") val photosEntity : PhotosEntity,
	@SerializedName("stat") val stat : String
)