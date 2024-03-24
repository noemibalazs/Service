package com.noemi.service.base

import android.content.Intent
import android.os.Build
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.noemi.service.source.ServiceSourceActivity

abstract class BaseFragment<T : ViewBinding>(@LayoutRes val layout: Int) : Fragment(layout) {

    lateinit var binding: T

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun navigateToSource() {
        val intent = Intent(requireActivity(), ServiceSourceActivity::class.java)
        requireActivity().startActivity(intent)
    }
}