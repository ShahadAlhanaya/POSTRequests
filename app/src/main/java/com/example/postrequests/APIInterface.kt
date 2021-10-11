package com.example.postrequests

import retrofit2.Call
import retrofit2.http.*

interface APIInterface {
    @Headers("Content-Type: application/json")
    @GET("/test/")
    fun getUser(): Call<List<Users.User>>


    @Headers("Content-Type: application/json")
    @POST("/test/")
    fun addUser(@Body userData: Users.User): Call<Users.User>

    @Headers("Content-Type: application/json")
    @PUT("/test/{id}")
    fun updateUser(@Path("id") id: Int, @Body userData: Users.User): Call<Users.User>

    @Headers("Content-Type: application/json")
    @DELETE("/test/{id}")
    fun deleteUser(@Path("id") id: Int): Call<Void>
}
