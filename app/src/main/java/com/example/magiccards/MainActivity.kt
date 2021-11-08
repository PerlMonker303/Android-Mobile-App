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
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.magiccards.cards.data.remote.Api
import com.example.magiccards.cards.feature.EditCardActivity

class MainActivity : AppCompatActivity() {

    private lateinit var list_cards: ListView
    var cards = ArrayList<Card>();

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list_cards = findViewById<ListView>(R.id.list_cards)

        Thread {
            this.cards = Api().getCards()

            runOnUiThread {
                val cardListAdapter = CardListAdapter(this, cards)
                list_cards.adapter = cardListAdapter;
            }
        }.start()

        val cardListAdapter = CardListAdapter(this, cards)

        list_cards.adapter = cardListAdapter;
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
        }
    }

    fun handleAddClicked(item: MenuItem) {
        val intent = Intent(this, AddCardActivity::class.java)
        resultLauncher.launch(intent)
    }

    fun handleCardClicked(view: View) {
        val intent = Intent(this, EditCardActivity::class.java)
        intent.putExtra("cardId", view.getTag().toString())
        resultLauncher.launch(intent)
    }

}