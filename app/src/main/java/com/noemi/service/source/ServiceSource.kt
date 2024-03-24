package com.noemi.service.source

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.noemi.service.R


object ServiceSource {

    data class ServiceItem(@DrawableRes val icon: Int, @StringRes val name: Int, val code: Int)

    fun getServiceItems(): List<ServiceItem> =
        listOf(
            ServiceItem(R.drawable.ic_upload, R.string.label_work_manager, 3),
            ServiceItem(R.drawable.ic_download, R.string.label_download_manager, 6),
            ServiceItem(R.drawable.ic_alarms, R.string.label_alarm_manager, 9),
            ServiceItem(R.drawable.ic_foreground, R.string.label_foreground_service, 12)
        )
}