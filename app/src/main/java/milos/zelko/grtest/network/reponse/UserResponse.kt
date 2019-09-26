package milos.zelko.grtest.network.reponse

import com.google.gson.annotations.SerializedName
import milos.zelko.grtest.model.User

data class UserResponse(
    @SerializedName("data") val data: User
)