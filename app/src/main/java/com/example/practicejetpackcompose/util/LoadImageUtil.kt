package com.example.practicejetpackcompose.util

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

fun bitmapRequestCallback(
    onSuccess: (Bitmap) -> Unit,
    onError: (Throwable) -> Unit = {}
) = object : Target {
    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
        runCatching { requireNotNull(bitmap) }
            .onSuccess(onSuccess)
            .onFailure(onError)
    }

    override fun onBitmapFailed(e: Exception, errorDrawable: Drawable?) = onError(e)
    override fun onPrepareLoad(placeHolderDrawable: Drawable?) = Unit
}
