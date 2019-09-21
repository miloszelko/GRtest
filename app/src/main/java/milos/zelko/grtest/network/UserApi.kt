package milos.zelko.grtest.network

import io.reactivex.Observable
import milos.zelko.grtest.network.reponse.UserListResponse
import milos.zelko.grtest.network.reponse.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Contains endpoints for fetching data
 */
interface UserApi {

    @GET("users/{id}")
    fun getUser(@Path("id") id: Int): Observable<UserResponse>

    @GET("users")
    fun getUsers(@Query("page") page: Int, @Query("per_page") perPage: Int): Observable<UserListResponse>

}