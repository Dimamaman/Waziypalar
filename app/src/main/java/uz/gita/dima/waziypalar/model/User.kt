package uz.gita.dima.waziypalar.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


/** Data class to hold [User] data of logged in user */

data class User(
    @SerializedName("uid")
    var uid: String = "",
    @SerializedName("email")
    var email: String? = "",
    @SerializedName("displayName")
    var displayName: String? = "",
    @SerializedName("token")
    var token: String? = "",
) : Serializable