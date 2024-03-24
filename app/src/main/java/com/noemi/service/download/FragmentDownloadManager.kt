package com.noemi.service.download

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.noemi.service.R
import com.noemi.service.base.BaseFragment
import com.noemi.service.databinding.FragmentDownloadManagerBinding

class FragmentDownloadManager : BaseFragment<FragmentDownloadManagerBinding>(R.layout.fragment_download_manager) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDownloadManagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = requireContext().getString(R.string.label_download_manager)

        binding.downloadCardView.setOnClickListener {
            enqueueWork()
            navigateToSource()
        }
    }

    private fun enqueueWork() {
        val intent = Intent()
        intent.putExtra(DownloadPhotoManager.KEY_URL, PHOTO_URL)
        DownloadPhotoManager().enqueueWork(requireContext(), intent)
    }

    companion object {
        const val PHOTO_URL = "https://upload.wikimedia.org/wikipedia/en/f/f9/Basic_Instinct.png"
    }
}