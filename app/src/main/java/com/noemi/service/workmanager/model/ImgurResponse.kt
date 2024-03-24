package com.noemi.service.workmanager.model

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class ImgurResponse(
    @SerialName("data")
    val data: ImgurData,
    @SerialName("success")
    val success: Boolean,
    @SerialName("status")
    val status: Int
)

@Keep
@Serializable
data class ImgurData(
    @SerialName("id")
    val id: String,
    @SerialName("link")
    val link: String
)