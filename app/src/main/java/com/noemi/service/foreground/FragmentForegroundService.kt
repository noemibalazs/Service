package com.noemi.service.foreground

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.noemi.service.R
import com.noemi.service.base.BaseFragment
import com.noemi.service.databinding.FragmentForegroundServiceBinding

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class FragmentForegroundService : BaseFragment<FragmentForegroundServiceBinding>(R.layout.fragment_foreground_service) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentForegroundServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = requireContext().getString(R.string.label_foreground_service)

        binding.alarmCardView.setOnClickListener {
            startMusicService()
            navigateToSource()
        }
    }

    private fun startMusicService() {
        val intent = Intent(requireActivity(), ForegroundService::class.java)
        ContextCompat.startForegroundService(requireActivity(), intent)
    }
}