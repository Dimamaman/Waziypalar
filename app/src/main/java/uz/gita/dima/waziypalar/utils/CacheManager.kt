package uz.gita.dima.waziypalar.utils

import android.content.Context
import java.io.File


/** Removes the unwanted caches during app close */

class CacheManager {

    fun clearCache(context: Context) {
        try {
            val dir: File? = context.cacheDir
            if (dir != null && dir.isDirectory) {
                deleteDir(dir)
            }
        } catch (e: Exception) {
            /*logMessage("Cache Manager Exception: ${e.message}")*/
        }
    }

    private fun deleteDir(dir: File?): Boolean {
        return if (dir != null && dir.isDirectory) {
            val children = dir.list()!!
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            dir.delete()
        } else if (dir != null && dir.isFile) {
            dir.delete()
        } else {
            false
        }
    }
}