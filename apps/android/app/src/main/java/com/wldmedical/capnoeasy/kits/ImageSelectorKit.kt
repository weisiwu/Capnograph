package com.wldmedical.capnoeasy.kits

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

/**
 * 打印设置-公司LOGO选择
 */
fun saveImageToInternalStorage(context: Context, uri: Uri, fileName: String): Boolean {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        val directory = context.getDir("images", Context.MODE_PRIVATE) // 创建私有目录
        val file = File(directory, fileName)
        val outputStream = FileOutputStream(file)

        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream) // 将 Bitmap 压缩为 JPEG 并保存
        outputStream.flush()
        outputStream.close()
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun loadImageFromInternalStorage(context: Context, fileName: String): Bitmap? {
    return try {
        val directory = context.getDir("images", Context.MODE_PRIVATE)
        val file = File(directory, fileName)
        BitmapFactory.decodeFile(file.absolutePath)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

val logoImgName = "print_logo.jpg"