package com.dwgu.linkive.ImageLoader

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

// url로 이미지 Bitmap Load
object ImageLoader {
    suspend fun loadImage(imageUrl: String): Bitmap? {
        val bitmap: Bitmap? = null
        try {
            val url = URL(imageUrl)
            val stream = url.openStream()

            return BitmapFactory.decodeStream(stream)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap
    }
}