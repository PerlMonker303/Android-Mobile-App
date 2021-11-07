package com.example.magiccards

import android.app.Activity
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ListView
import androidx.annotation.RequiresApi
import com.example.magiccards.CustomAdapters.CardListAdapter
import com.example.magiccards.cards.data.entities.Card
import com.example.magiccards.cards.feature.AddCardActivity
import okhttp3.*
import java.util.*
import kotlin.collections.ArrayList
import okhttp3.OkHttpClient
import java.io.IOException;
import okhttp3.Request;
import com.google.gson.Gson
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

private val ADD_CARD_REQUEST = 1

class MainActivity : AppCompatActivity() {

    private lateinit var list_cards: ListView
    val client = OkHttpClient()
    val ip = "172.29.240.1"
    val base_url = "http://"+ip+":3000/"
    var cards = ArrayList<Card>();
    val gson = Gson()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list_cards = findViewById<ListView>(R.id.list_cards)

        Toast.makeText(this, "Welcome", Toast.LENGTH_LONG).show()

        Thread {
                var resp = query(base_url + "cards");
                this.cards =
                    this.gson.fromJson(resp, Array<Card>::class.java).toList() as ArrayList<Card>

            runOnUiThread {
                this.cards = this.gson.fromJson(resp, Array<Card>::class.java).toList() as ArrayList<Card>
                val cardListAdapter = CardListAdapter(this, cards)
                list_cards.adapter = cardListAdapter;
            }
        }.start()

        val cardListAdapter = CardListAdapter(this, cards)

        list_cards.adapter = cardListAdapter;
    }

    @Throws(IOException::class)
    fun query(url: String): String? {
        val request: Request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            return response.body!!.string()
        }
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
        }
    }

    fun handleAddClicked(item: MenuItem) {
        val intent = Intent(this, AddCardActivity::class.java)
        resultLauncher.launch(intent)
    }

}