package com.test.myevent.util

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.URL


object Base64ImageUtil {

    private const val prefixBase64 = "data:image/png;base64,"
    val TAG = Base64ImageUtil::class.java.simpleName

    fun getPath(context: Context, uri: Uri): String? {
        val result: String?
        val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = uri.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }

    fun fileImageToBase64(file: File): String {
        val bm = BitmapFactory.decodeFile(file.absolutePath)
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos) //bm is the bitmap object

        return prefixBase64 + Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
    }

    fun convertUrlToBase64(url: String?): String? {
        val newurl: URL
        val bitmap: Bitmap
        var base64: String? = ""
        try {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            newurl = URL(url)
            bitmap = BitmapFactory.decodeStream(newurl.openConnection().getInputStream())
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            base64 = prefixBase64 + Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return base64
    }
}