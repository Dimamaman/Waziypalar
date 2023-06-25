package uz.gita.dima.waziypalar.util

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.gms.cast.framework.media.ImagePicker
import com.google.android.material.snackbar.Snackbar
import uz.gita.dima.waziypalar.R
import uz.gita.dima.waziypalar.util.Constants.GLOBAL_TAG
import java.time.Instant
import java.util.*

/** General ui related extension functions */

object UIHelper {

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentTimestamp(): String = Instant.now().toEpochMilli().toString()

    fun logMessage(message: String) {
        Log.d(GLOBAL_TAG, message)
    }

    fun showToast(context: Context, toastMessage: String) {
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
    }

    fun showSnack(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    fun Activity.hideKeyboard() {
        val inputMethodManager: InputMethodManager =
            this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view: View? = this.currentFocus
        if (view == null) view = View(this)
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun Fragment.hideKeyboard() {
        val inputMethodManager: InputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view: View? = requireActivity().currentFocus
        if (view == null) view = View(requireActivity())
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun TextView.strikeThroughText() {
        val text = this.text.toString()
        val spannable = SpannableString(text)
        spannable.setSpan(StrikethroughSpan(), 0, text.length, 0)
        this.text = spannable
    }

    fun TextView.removeStrikeThroughText() {
        val text = this.text.toString()
        val spannable = SpannableString(text)
        spannable.removeSpan(StrikethroughSpan())
        this.text = spannable
    }

    fun CharSequence.isEmailValid(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    fun greetingMessage(): String {
        val calendar = Calendar.getInstance()
        return when (calendar.get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> "Good morning"
            in 12..15 -> "Afternoon"
            in 16..20 -> "Good eve"
            in 21..23 -> "Good night"
            else -> "Hello"
        }
    }

    fun ImageView.loadImage(url: String?) {
        val imagePlaceHolder = this
        url.let {
            Glide.with(imagePlaceHolder.context)
                .load(url)
                .placeholder(R.drawable.ic_image_loading)
                .transform(CenterCrop())
                .into(imagePlaceHolder)
        }
    }


    fun ImageView.setTint(colorId: Int) {
        ImageViewCompat.setImageTintList(
            this, ColorStateList.valueOf(
                ContextCompat.getColor(this.context, colorId)
            )
        )
    }
}