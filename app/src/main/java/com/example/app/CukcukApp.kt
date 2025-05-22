package com.example.app

import android.app.Application
import android.content.Context
import com.example.app.datas.CukcukDbHelper
import com.example.app.utils.SyncHelper
import java.io.FileOutputStream

class CukcukApp : Application() {
    override fun onCreate() {
        super.onCreate()
        copyDatabaseIfNotExists(this)
        SyncHelper.dbHelper = CukcukDbHelper(this)

    }

    private fun copyDatabaseIfNotExists(context: Context) {
        val dbName = "cukcuk_blank.db"
        val db = "cukcuk.db"
        val dbPath = context.getDatabasePath(db)

        if (!dbPath.exists()) {
            dbPath.parentFile?.mkdirs() // Tạo thư mục nếu chưa có

            context.assets.open(dbName).use { inputStream ->
                FileOutputStream(dbPath).use { outputStream ->
                    val buffer = ByteArray(1024)
                    var length: Int
                    while (inputStream.read(buffer).also { length = it } > 0) {
                        outputStream.write(buffer, 0, length)
                    }
                    outputStream.flush()
                }
            }
        }
    }
}