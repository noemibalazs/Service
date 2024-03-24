package com.noemi.service.workmanager.uploadphoto

import android.annotation.SuppressLint
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.noemi.service.util.PHOTO_URI_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val workManager: WorkManager
) : ViewModel() {

    private var outPutUri: Uri? = null
    private var inputUri: Uri? = null

    val outPutWorkInfo: LiveData<List<WorkInfo>> = workManager.getWorkInfosByTagLiveData(TAG_OUTPUT)

    fun cancelWork() {
        workManager.cancelUniqueWork(IMAGE_MANIPULATION_WORK_NAME)
    }

    fun uploadImage() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val uploadWorker = OneTimeWorkRequest.Builder(UploadWorker::class.java)
            .setConstraints(constraints)
            .setInputData(createInputData())
            .addTag(TAG_OUTPUT)
            .build()

        workManager.beginUniqueWork(IMAGE_MANIPULATION_WORK_NAME, ExistingWorkPolicy.KEEP, uploadWorker).enqueue()
    }

    fun setInputUri(input: String) {
        inputUri = Uri.parse(input)
    }

    fun setOutputUri(input: String) {
        outPutUri = Uri.parse(input)
    }

    @SuppressLint("RestrictedApi")
    private fun createInputData(): Data {
        val builder = Data.Builder()
        inputUri?.let {
            builder.put(PHOTO_URI_KEY, it.toString())
        }
        return builder.build()
    }

    fun getOutputUri(): Uri? = outPutUri

    companion object {
        private const val IMAGE_MANIPULATION_WORK_NAME = "image_manipulation_work"
        private const val TAG_OUTPUT = "out_put"
    }
}