package com.noemi.service.source

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.noemi.service.databinding.ItemsServiceBinding
import com.noemi.service.source.ServiceSource.ServiceItem

class ServiceAdapter(private val serviceListener: ServiceClickListener) : ListAdapter<ServiceItem, ServiceAdapter.ServiceViewHolder>(ServiceDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val binding: ItemsServiceBinding = ItemsServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ServiceViewHolder(private val binding: ItemsServiceBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(serviceItem: ServiceItem) {
            with(binding) {
                item = serviceItem
                listener = serviceListener
            }
        }
    }

    class ServiceDiffUtil : ItemCallback<ServiceItem>() {
        override fun areItemsTheSame(oldItem: ServiceItem, newItem: ServiceItem): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: ServiceItem, newItem: ServiceItem): Boolean = oldItem.icon == newItem.icon
    }
}