package com.noemi.service.source

import android.Manifest.permission.*
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.noemi.service.databinding.ActivityServiceSourceBinding
import com.noemi.service.foreground.ForegroundService
import com.noemi.service.provider.ProviderActivity
import com.noemi.service.source.ServiceSource.ServiceItem
import com.noemi.service.util.MEDIA_NOTIFICATION_CODE
import com.noemi.service.util.READ_WRITE_STORAGE_CODE
import dagger.hilt.android.AndroidEntryPoint

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@AndroidEntryPoint
class ServiceSourceActivity : AppCompatActivity(), ServiceClickListener {

    private val adapter: ServiceAdapter by lazy { ServiceAdapter(this) }
    private val storagePermissions = listOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)
    private val mediaNotificationPermissions = listOf(READ_MEDIA_IMAGES, POST_NOTIFICATIONS)

    private lateinit var binding: ActivityServiceSourceBinding

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceSourceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecycleView()

        when (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            true -> if (!permissionIsGranted(READ_MEDIA_IMAGES) && !permissionIsGranted(POST_NOTIFICATIONS))
                ActivityCompat.requestPermissions(this, mediaNotificationPermissions.toTypedArray(), MEDIA_NOTIFICATION_CODE)
            else -> if (!permissionIsGranted(READ_EXTERNAL_STORAGE) && !permissionIsGranted(WRITE_EXTERNAL_STORAGE))
                ActivityCompat.requestPermissions(this, storagePermissions.toTypedArray(), READ_WRITE_STORAGE_CODE)
        }

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun initRecycleView() {
        binding.managerRecycleView.adapter = adapter
        adapter.submitList(ServiceSource.getServiceItems())
    }

    private fun permissionIsGranted(permission: String) =
        ActivityCompat.checkSelfPermission(this, permission) == PERMISSION_GRANTED

    override fun onRequestPermissionsResult(requestCode: Int, permission: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permission, grantResults)
        when (requestCode) {
            MEDIA_NOTIFICATION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] != PERMISSION_GRANTED && grantResults[1] != PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(this, mediaNotificationPermissions.toTypedArray(), MEDIA_NOTIFICATION_CODE)
            }
            READ_WRITE_STORAGE_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] != PERMISSION_GRANTED && grantResults[1] != PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(this, storagePermissions.toTypedArray(), READ_WRITE_STORAGE_CODE)
            }
        }
    }

    override fun onItemClicked(item: ServiceItem) {
        launchProvider(item)
    }

    private fun launchProvider(item: ServiceItem) {
        val intent = Intent(this, ProviderActivity::class.java)
        intent.putExtra(SERVICE_NAME, item.name)
        startActivity(intent)
    }

    override fun onDestroy() {
        stopService(Intent(this, ForegroundService::class.java))
        super.onDestroy()
    }

    companion object {
        const val SERVICE_NAME = "service name"
    }
}