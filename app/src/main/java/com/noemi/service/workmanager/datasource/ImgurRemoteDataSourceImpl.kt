package com.noemi.service.workmanager.datasource

import android.content.Context
import android.net.Uri
import com.noemi.service.util.toFile
import com.noemi.service.util.toMultipartBody
import com.noemi.service.workmanager.model.ImgurResponse
import com.noemi.service.workmanager.network.ImgurAPI
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Call
import javax.inject.Inject

class ImgurRemoteDataSourceImpl @Inject constructor(
    private val imgurAPI: ImgurAPI,
    @ApplicationContext val context: Context
) : ImgurRemoteDataSource {

    override fun uploadImage(imageUri: Uri): Call<ImgurResponse> {
        val file = imageUri.toFile()
        val body = file.toMultipartBody()
        return imgurAPI.postImage(body)
    }
}