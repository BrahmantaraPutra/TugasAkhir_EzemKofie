package com.example.ezemkofie.api

import androidx.constraintlayout.widget.ReactiveGuide
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Header
import retrofit2.http.Path
import com.google.gson.annotations.SerializedName

//C1

data class LoginRequest(
    val username: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val fullname: String,
    val email: String,
    val password: String
)


data class UserResponse(
    val id: Int,
    val username: String,
    val fullName: String,
    val email: String
)

data class TransResponse(
    val response: String
)

//C2
data class CoffeResponse(
    val id: Int,
    val name: String,
    val category: String,
    val rating: Double,
    val price: Double,
    val imagePath: String
)

data class CoffeById(
    val id: Int,
    val name: String,
    val description: String,
    val category: String,
    val rating: Double,
    val price: Double,
    val imagePath: String
)


data class TopPick(
    val id: Int,
    val name: String,
    val category: String,
    val rating: Double,
    val price: Double,
    val imagePath: String
)

data class coffeCategory(
    val id: Int,
    val name: String
)

//C3
data class CheckoutReq(
    @SerializedName("coffeeId")
    val coffeeId: Int,
    @SerializedName("size")
    val size: String,
    @SerializedName("qty")
    val qty: Int
)


interface ApiServices {
    @POST("api/auth")
    suspend fun login(@Body req: LoginRequest): Response<String>

    @POST("api/register")
    suspend fun register(@Body req: RegisterRequest): Response<String>

    @GET("api/me")
    suspend fun getUser(@Header("Authorization") token: String): Response<UserResponse>

    @GET("api/me/transaction")
    suspend fun getTrabs(@Header("Authorization") token: String): Response<TransResponse>

    @GET("api/coffee-category")
    suspend fun getCategory(@Header("Authorization") token: String): Response<List<coffeCategory>>

    @GET("api/coffee")
    suspend fun  getCoffe(@Header("Authorization") token: String): Response<List<CoffeResponse>>

    @GET("api/coffee/top-picks")
    suspend fun getTop(@Header("Authorization") token: String): Response<List<TopPick>>

    @POST("api/checkout")
    suspend fun transaksi(
        @Header("Authorization") token: String,
        @Body req: List<CheckoutReq>
    ): Response<okhttp3.ResponseBody>

    @GET("api/coffee/{coffeeID}")
    suspend fun getCoffeeById(
        @Header("Authorization") token: String,
        @Path("coffeeID") id: Int
    ): Response<CoffeById>

}