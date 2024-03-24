package com.noemi.service.workmanager.datasource

import android.net.Uri
import com.noemi.service.workmanager.model.ImgurResponse
import retrofit2.Call

interface ImgurRemoteDataSource {

     fun uploadImage(imageUri: Uri): Call<ImgurResponse>
}