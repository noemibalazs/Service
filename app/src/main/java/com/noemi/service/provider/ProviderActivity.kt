package com.noemi.service.provider

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.noemi.service.R
import com.noemi.service.databinding.ActivityProviderBinding
import com.noemi.service.source.ServiceSourceActivity.Companion.ITEM_CODE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProviderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProviderBinding
    private lateinit var navController: NavController

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProviderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_container)

        val code = intent.getIntExtra(ITEM_CODE, 0)

        when (code) {
            3 -> navController.navigate(R.id.fragmentSelectPhoto)
            6 -> navController.navigate(R.id.fragmentDownloadManager)
            12 -> navController.navigate(R.id.fragmentForegroundService)
            9 -> Log.d(TAG, "Landed on the start destination.")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    companion object {
        private const val TAG = "ProviderActivity"
    }
}