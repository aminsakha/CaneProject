package com.caneproject.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.caneproject.R
import com.caneproject.activities.screenHeight
import com.caneproject.activities.screenWidth


fun loadImageForRecView(context: Context, uri: Uri?, imageView: ImageView) {
    Glide.with(context).load(uri).placeholder(R.drawable.placeholder)
        .apply(RequestOptions.bitmapTransform(RoundedCorners(15))).override(
            screenWidth / 4,
            (screenHeight / 2 * 0.9).toInt()
        ).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imageView)
}