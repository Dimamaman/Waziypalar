package uz.gita.dima.waziypalar.utils

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import uz.gita.dima.waziypalar.utils.Constants.PLAY_STORE_LINK

/**
 * This AppShare object holds [shareApp] method. When called creates a global share intent.
 * */

fun Fragment.shareApp() {
    val sharingIntent = Intent(Intent.ACTION_SEND)
    val shareText =
        "Live Now.\n\nBetter to live with my program \n\n $\"link: https://play.google.com/store/apps/details?id=uz.gita.dima.waziypalar\""
    sharingIntent.apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, shareText)
        startActivity(Intent.createChooser(this, "Todo"))
    }
}

fun Fragment.feedBack() {
    val emailIntent =
        Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "dimamamanov37@gmail.com"))
    startActivity(emailIntent)
}