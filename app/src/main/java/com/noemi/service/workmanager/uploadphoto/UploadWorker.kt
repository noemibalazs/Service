package com.noemi.service.workmanager.uploadphoto

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.noemi.service.util.PHOTO_URI_KEY
import com.noemi.service.workmanager.datasource.ImgurRemoteDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UploadWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val remoteData: ImgurRemoteDataSource
) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        return try {
            val imageUriInput = inputData.getString(PHOTO_URI_KEY)
            val imageUri = Uri.parse(imageUriInput)
            val response = remoteData.uploadImage(imageUri).execute()

            when (response.isSuccessful && response.body() != null) {
                true -> {
                    val imageUrl = response.body()?.data?.link
                    val outputData = Data.Builder()
                        .putString(PHOTO_URI_KEY, imageUrl)
                        .build()
                    Result.success(outputData)
                }
                else -> {
                    val error = response.errorBody()?.toString()
                    Log.e(TAG, "Failed to upload image, error message: $error")
                    Result.failure()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception, failed to upload image: ${e.printStackTrace()}")
            Result.failure()
        }
    }

    companion object {
        private const val TAG = "UploadWorker"
    }
}