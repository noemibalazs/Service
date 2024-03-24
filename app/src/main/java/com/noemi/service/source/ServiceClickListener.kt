package com.noemi.service.source

import com.noemi.service.source.ServiceSource.ServiceItem

interface ServiceClickListener {

    fun onItemClicked(item: ServiceItem)
}