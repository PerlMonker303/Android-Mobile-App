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
import kotlin.collections.ArrayList
import com.google.gson.Gson
import android.content.Intent
import android.view.View
import android.widget.ProgressBar
import androidx.activity.result.contract.ActivityResultContracts
import com.example.magiccards.cards.data.remote.Api
import com.example.magiccards.cards.feature.EditCardActivity
import com.example.magiccards.users.data.entities.User
import com.example.magiccards.users.feature.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var list_cards: ListView
    private lateinit var loading: ProgressBar
    var cards = ArrayList<Card>()
    private lateinit var user: User

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list_cards = findViewById<ListView>(R.id.list_cards)
        loading = findViewById<ProgressBar>(R.id.main_loading)

        // update username text
        val username = intent.getStringExtra("username")
        setTitle("Magic Cards - " + username)

        val token = intent.getStringExtra("token")

        // fetch current user
        if (username != null) {
            val t = Thread { this.user = Api().getUser(username, token!!) }
            t.start()
            t.join() // waiting for the previous operation to finish
            loadCards()
        }
    }

    override fun onResume() {
        super.onResume()
        loadCards()
    }

    fun loadCards() {
        var cardListAdapter = CardListAdapter(this, ArrayList())
        list_cards.adapter = cardListAdapter;
        loading.visibility = View.VISIBLE
        Thread {
            this.cards = Api().getCards(user?.token)

            runOnUiThread {
                val cardListAdapter = CardListAdapter(this, cards)
                list_cards.adapter = cardListAdapter;
                loading.visibility = View.GONE
            }
        }.start()

        cardListAdapter = CardListAdapter(this, cards)
        list_cards.adapter = cardListAdapter;
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
        }
    }

    fun handleAddClicked(item: MenuItem) {
        val intent = Intent(this, AddCardActivity::class.java)
        intent.putExtra("loggedUser", Gson().toJson(this.user))
        intent.putExtra("token", user.token)
        resultLauncher.launch(intent)
    }

    fun handleLogoutCLicked(item: MenuItem) {
        val intent = Intent(this, LoginActivity::class.java)
        resultLauncher.launch(intent)
    }

    fun handleCardClicked(view: View) {
        val intent = Intent(this, EditCardActivity::class.java)
        intent.putExtra("cardId", view.getTag().toString())
        intent.putExtra("token", user.token)
        resultLauncher.launch(intent)
    }

}