package com.test.sample_architecture.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Photo(

    @SerializedName("id")
    @Expose
    var id: String,

    @SerializedName("secret")
    @Expose
    var secret: String,

    @SerializedName("server")
    @Expose
    var server: String,

    @SerializedName("farm")
    @Expose
    var farm: Int,

    @SerializedName("title")
    @Expose
    var title: String
) {

    val photoUrl: String
    get() = "https://farm${farm}.staticflickr.com/$server/${id}_${secret}.jpg"

}