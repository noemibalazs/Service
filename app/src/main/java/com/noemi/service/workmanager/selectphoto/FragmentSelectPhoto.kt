package com.noemi.service.workmanager.selectphoto

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.noemi.service.R
import com.noemi.service.base.BaseFragment
import com.noemi.service.databinding.FragmentSelectPhotoBinding
import com.noemi.service.util.CONTENT_SCHEME

class FragmentSelectPhoto : BaseFragment<FragmentSelectPhotoBinding>(R.layout.fragment_select_photo) {

    private val pickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        when (result.resultCode == Activity.RESULT_OK) {
            true -> handleResponse(result.data)
            else -> Log.e("FragmentSelectPhoto", "Unexpected error, see result code: ${result.resultCode}")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSelectPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = requireContext().getString(R.string.label_work_manager)
        binding.photoCardView.setOnClickListener {
            selectPhoto()
        }
    }

    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        pickerLauncher.launch(intent)
    }

    private fun handleResponse(data: Intent?) {
        var imageUri: Uri? = null
        data?.let { imageUri = it.clipData?.getItemAt(0)?.uri ?: it.data }

        when (imageUri != null) {
            true -> {
                val filePath = imageUri?.let { getFilePathFromUri(it) }
                filePath?.let { path ->
                     findNavController().navigate(FragmentSelectPhotoDirections.actionToUploadPhoto(path))
                }
            }
            else -> {
                Toast.makeText(requireContext(), requireContext().getString(R.string.label_error), Toast.LENGTH_LONG).show()
                return
            }
        }
    }

    private fun getFilePathFromUri(imageUri: Uri): String? {
        var filePtah: String? = null
        when (CONTENT_SCHEME == imageUri.scheme) {
            true -> {
                val cursor = requireContext().contentResolver.query(
                    imageUri,
                    arrayOf(MediaStore.Images.ImageColumns.DATA),
                    null, null, null
                )
                cursor?.let {
                    it.moveToFirst()
                    filePtah = it.getString(0)
                }
                cursor?.close()
            }
            else -> filePtah = imageUri.path
        }
        return filePtah
    }
}