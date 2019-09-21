package milos.zelko.grtest.network.reponse

import com.google.gson.annotations.SerializedName
import milos.zelko.grtest.model.User

data class UserListResponse (
    @SerializedName("page") val page: Int,
    @SerializedName("per_page") val perPage: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("data") val data: List<User>
)