package com.example.magiccards.CustomAdapters

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.magiccards.cards.data.entities.Card
import com.example.magiccards.R
import android.graphics.BitmapFactory

import android.util.Base64


class CardListAdapter(private val context:Activity, private val cards: ArrayList<Card>)
    : ArrayAdapter<Card>(context, R.layout.card, cards) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.card, null, true)

        val titleText = rowView.findViewById(R.id.card_title) as TextView
        val imageView = rowView.findViewById(R.id.card_icon) as ImageView
        val descriptionText = rowView.findViewById(R.id.card_description) as TextView
        val starsText = rowView.findViewById(R.id.card_stars) as TextView
        val addedOnText = rowView.findViewById(R.id.card_addedOn) as TextView

        titleText.text = cards[position].title
        val imageBytes = Base64.decode(cards[position].image.removePrefix("data:image/png;base64,"), Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        imageView.setImageBitmap(decodedByte)
        descriptionText.text = cards[position].description
        starsText.text = cards[position].stars.toString() + " stars"
        addedOnText.text = cards[position].addedOn.toString()

        return rowView
    }
}