package com.example.magiccards.cards.data.remote

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

class Api {
    val ip = "172.26.112.1"
    val base_url = "http://"+ip+":3000/"
    val client = OkHttpClient()
    val gson = Gson()

    public fun getCards(token: String) : ArrayList<Card> {
        val request: Request = Request.Builder()
            .url(base_url + "cards?token=" + token)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful){
                throw IOException("Unexpected code $response")
            }

            val resp = response.body!!.string()
            return this.gson.fromJson(resp, Array<Card>::class.java).toList() as ArrayList<Card>
        }
    }

    public fun getCard(id: String, token: String) : Card {
        val request: Request = Request.Builder()
            .url(base_url + "card?id=" + id + "&token=" + token)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val resp = response.body!!.string()
            return this.gson.fromJson(resp, Card::class.java) as Card
        }
    }

    public fun addCard(card: Card, token: String) : String {

        val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()
        val body: RequestBody = gson.toJson(card).toRequestBody(JSON)

        val request: Request = Request.Builder()
            .url(base_url + "card?token=" + token)
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw java.io.IOException("Unexpected code $response")

            return response.body!!.string()
        }
    }

    public fun updateCard(card: Card, token: String) : String {
        val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()
        val body: RequestBody = gson.toJson(card).toRequestBody(JSON)

        val client = OkHttpClient()

        val request: Request = Request.Builder()
            .url(base_url + "card?id=" + card.id + "&token=" + token)
            .put(body)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            return response.body!!.string()
        }
    }

    public fun getUser(username: String, token: String) : User {
        val request: Request = Request.Builder()
            .url(base_url + "user?username=" + username + "&token=" + token)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val resp = response.body!!.string()
            return this.gson.fromJson(resp, User::class.java) as User
        }
    }
}