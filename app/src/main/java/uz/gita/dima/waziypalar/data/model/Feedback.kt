package uz.gita.dima.waziypalar.data.model

import com.google.gson.annotations.SerializedName


data class Feedback(
    var user: String? = "", // Wayne Dyer
    var topic: String? = "", // suggestions heading feedback.
    var description: String? = "" // suggestions description feedback.
)
