package com.example.magiccards.cards.data.remote

import com.example.magiccards.cards.data.entities.Card
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class Api {
    val ip = "172.29.240.1"
    val base_url = "http://"+ip+":3000/"
    val client = OkHttpClient()
    val gson = Gson()


    public fun getCards() : ArrayList<Card> {
        val request: Request = Request.Builder()
            .url(base_url + "cards")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val resp = response.body!!.string()
            return this.gson.fromJson(resp, Array<Card>::class.java).toList() as ArrayList<Card>
        }
    }

    public fun getCard(id: String) : Card {
        val request: Request = Request.Builder()
            .url(base_url + "card?id=" + id)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val resp = response.body!!.string()
            return this.gson.fromJson(resp, Card::class.java) as Card
        }
    }

    public fun addCard(card: Card) : String {

        val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()
        val body: RequestBody = gson.toJson(card).toRequestBody(JSON)

        val request: Request = Request.Builder()
            .url(base_url + "card")
            .post(body)
//                .addHeader("Authorization", header)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw java.io.IOException("Unexpected code $response")

            return response.body!!.string()
        }
    }

    public fun updateCard(card: Card) : String {
        val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()
        val body: RequestBody = gson.toJson(card).toRequestBody(JSON)

        val client = OkHttpClient()

        val request: Request = Request.Builder()
            .url(base_url + "card?id=" + card.id)
            .put(body) //PUT
//                .addHeader("Authorization", header)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            return response.body!!.string()
        }
    }
}