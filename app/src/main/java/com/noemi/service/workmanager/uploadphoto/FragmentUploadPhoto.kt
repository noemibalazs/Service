package com.noemi.service.workmanager.uploadphoto

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.work.WorkInfo
import com.bumptech.glide.Glide
import com.noemi.service.R
import com.noemi.service.base.BaseFragment
import com.noemi.service.databinding.FragmentUploadPhotoBinding
import com.noemi.service.util.PHOTO_URI_KEY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentUploadPhoto : BaseFragment<FragmentUploadPhotoBinding>(R.layout.fragment_upload_photo), UploadClickListener {

    private val navArgs: FragmentUploadPhotoArgs by navArgs()
    private val viewModel: UploadViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUploadPhotoBinding.inflate(inflater, container, false)
        with(binding) {
            listener = this@FragmentUploadPhoto
            lifecycleOwner = this@FragmentUploadPhoto
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = requireContext().getString(R.string.label_work_manager)
        
        val photoPath = navArgs.photoPath
        displaySelectedPhoto(photoPath)

        with(viewModel) {
            setInputUri(photoPath)

            outPutWorkInfo.observe(viewLifecycleOwner) {
                observeWorkInfo(it)
            }
        }
    }

    private fun displaySelectedPhoto(path: String) {
        Glide.with(requireContext())
            .load(path)
            .into(binding.selectedPhoto)
    }

    private fun observeWorkInfo(workInfo: List<WorkInfo>) {
        if (workInfo.isEmpty()) return

        val work = workInfo[0]
        when (work.state.isFinished) {
            true -> {
                binding.progressBar.visibility = GONE
                val outputUri = work.outputData.getString(PHOTO_URI_KEY)

                if (!outputUri.isNullOrEmpty()) {
                    viewModel.setOutputUri(outputUri)
                    binding.uploadedPhotoCardView.visibility = VISIBLE
                }
            }
            else -> showWorkInProgress()
        }
    }

    private fun showWorkInProgress() {
        with(binding) {
            progressBar.visibility = VISIBLE
            uploadedPhotoCardView.visibility = GONE
        }
    }

    override fun onUploadClicked() {
        viewModel.uploadImage()
    }

    override fun onCancelClicked() {
        viewModel.cancelWork()
    }

    override fun onUploadedPhotoClicked() {
        viewModel.getOutputUri()?.let {
            val intent = Intent(Intent.ACTION_VIEW, it)
            requireActivity().startActivity(intent)
        }
    }
}