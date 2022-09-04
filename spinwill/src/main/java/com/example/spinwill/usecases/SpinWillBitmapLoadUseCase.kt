package com.example.spinwill.usecases

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.spinwill.di.SpinWillInjector
import com.example.spinwill.models.SpinWillItem
import com.example.spinwill.utils.Resource
import kotlinx.coroutines.*
import kotlin.coroutines.resume

interface SpinWillBitmapLoadUseCase {
    suspend fun setBitmapFromUrl(items: List<SpinWillItem>): Resource<List<SpinWillItem>>
}

class SpinWillBitmapLoadUseCaseImpl() : SpinWillBitmapLoadUseCase {

    override suspend fun setBitmapFromUrl(items: List<SpinWillItem>): Resource<List<SpinWillItem>> =
        withContext(Dispatchers.IO) {

            val list = mutableListOf<Deferred<Boolean>>()

            items.forEach { item ->
                list.add(async { setBitmap(item) })
            }

            list.awaitAll()
            Log.d("spinwill", "bitmap result set 26 " + System.currentTimeMillis())
            return@withContext Resource.Success(items)
        }

    private suspend fun setBitmap(item: SpinWillItem): Boolean = withContext(Dispatchers.IO) {
        return@withContext suspendCancellableCoroutine { coroutine ->
            Glide.with(SpinWillInjector.getContext()).asBitmap().diskCacheStrategy(
                DiskCacheStrategy.ALL
            ).load(item.rewardImage).into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    Log.d("spinwill", "bitmap set 40 " + System.currentTimeMillis())
                    item.rewardBitmap = resource
                    coroutine.resume(true)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    coroutine.resume(false)
                }
            })
        }
    }
}