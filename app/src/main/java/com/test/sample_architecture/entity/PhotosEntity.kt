package com.test.sample_architecture.entity

import com.google.gson.annotations.SerializedName

data class PhotosEntity (

	@SerializedName("page") val page : Int,
	@SerializedName("pages") val pages : Int,
	@SerializedName("perpage") val perpage : Int,
	@SerializedName("total") val total : Int,
	@SerializedName("photo") val photoEntity : ArrayList<PhotoEntity>
)