package com.morrisware.android.glidelearn

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        load.setOnClickListener { load() }
        debugResourceCache.setOnClickListener { debugResourceCacheGenerator() }
        debugDataCache.setOnClickListener { debugDataCacheGenerator() }
        debugSource.setOnClickListener { debugSourceGenerator() }
    }

    private fun load() {
        Glide.with(this)
            .load(R.mipmap.normal)
            .apply(RequestOptions()
                .transform(CircleCrop()))
            .transition(DrawableTransitionOptions())
            .thumbnail(Glide.with(this).load(R.mipmap.thumb))
            .error(Glide.with(this).load(R.mipmap.error))
            .into(imageView)
    }

    private fun debugResourceCacheGenerator() {
        Glide.with(this)
            .load(R.mipmap.normal)
            .apply(RequestOptions()
                .transform(CircleCrop())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
            .transition(DrawableTransitionOptions().crossFade())
            .into(imageView)
    }

    private fun debugDataCacheGenerator() {
        Glide.with(this)
            .load(R.mipmap.normal)
            .apply(RequestOptions()
                .transform(CircleCrop())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.DATA))
            .transition(DrawableTransitionOptions().crossFade())
            .into(imageView)
    }

    private fun debugSourceGenerator() {
        Glide.with(this)
            .load(R.mipmap.normal)
            .apply(RequestOptions()
                .transform(CircleCrop())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE))
            .transition(DrawableTransitionOptions().crossFade())
            .into(imageView)
    }

}
