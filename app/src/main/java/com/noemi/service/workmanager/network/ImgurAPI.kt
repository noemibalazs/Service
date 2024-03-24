package com.noemi.service.workmanager.network

import com.noemi.service.workmanager.model.ImgurResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImgurAPI {

    @Multipart
    @POST("upload")
    fun postImage(@Part image: MultipartBody.Part): Call<ImgurResponse>
}