package com.example.magiccards.users.feature.data

import com.example.magiccards.cards.data.entities.Card
import com.example.magiccards.users.data.entities.User
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
    val ip = "172.26.112.1"
    val base_url = "http://"+ip+":3000/"
    val client = OkHttpClient()
    val gson = Gson()

    fun login(username: String, password: String): Result<User> {
        try {
            val user = User(-1, username, password, "")
            val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()
            val body: RequestBody = gson.toJson(user).toRequestBody(JSON)

            val request: Request = Request.Builder()
                .url(base_url + "user")
                .post(body)
//                .addHeader("Authorization", header)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw java.io.IOException("Unexpected code $response")
                val resp = response.body!!.string()
                val usr = gson.fromJson(resp, User::class.java) as User
                return Result.Success(User(usr.id, usr.username, usr.password, usr.token))
            }

        } catch (e: Throwable) {
            System.out.println("-------error---------")
            System.out.println(e.toString())
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}