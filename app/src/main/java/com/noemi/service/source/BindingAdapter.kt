package com.noemi.service.source

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("imageResource")
fun bindImageResource(view: ImageView, imageResource: Int) {
    view.setImageResource(imageResource)
}