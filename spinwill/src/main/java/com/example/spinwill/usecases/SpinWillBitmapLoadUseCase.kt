package com.example.spinwill.usecases

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.spinwill.adapter.WillItemAdapter
import com.example.spinwill.di.SpinWillInjector
import com.example.spinwill.utils.Resource
import kotlinx.coroutines.*
import kotlin.coroutines.resume

interface SpinWillBitmapLoadUseCase<item> {
    suspend fun setBitmapFromUrl(items: List<item>): Resource<List<item>>
}

class SpinWillBitmapLoadUseCaseImpl<item> constructor(
    private val context: Context,
    private val willItemAdapter: WillItemAdapter<item>
) : SpinWillBitmapLoadUseCase<item> {

    override suspend fun setBitmapFromUrl(items: List<item>): Resource<List<item>> =
        withContext(Dispatchers.IO) {

            val list = mutableListOf<Deferred<Boolean>>()

            items.forEach { item ->
                list.add(async { setBitmap(item) })
            }

            list.awaitAll()
            Log.d("spinwill", "bitmap result set 26 " + System.currentTimeMillis())
            return@withContext Resource.Success(items)
        }

    private suspend fun setBitmap(item: item): Boolean = withContext(Dispatchers.IO) {
        return@withContext suspendCancellableCoroutine { coroutine ->
            Glide.with(context).asBitmap().diskCacheStrategy(
                DiskCacheStrategy.ALL
            ).load(willItemAdapter.getRewardImageUrl(item))
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        Log.d("spinwill", "bitmap set 40 " + System.currentTimeMillis())
                        willItemAdapter.setRewardBitmap(item, resource)
                        coroutine.resume(true)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        }
    }
}