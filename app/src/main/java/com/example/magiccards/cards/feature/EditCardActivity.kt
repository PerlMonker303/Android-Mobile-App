package com.example.magiccards.cards.feature

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import com.example.magiccards.MainActivity
import com.example.magiccards.R
import com.example.magiccards.cards.data.entities.Card
import com.example.magiccards.cards.data.remote.Api
import java.io.ByteArrayOutputStream


class EditCardActivity : AppCompatActivity() {
    private val CAMERA_REQUEST = 1888
    private var image_view: ImageView? = null
    private val MY_CAMERA_PERMISSION_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_card)

        val myIntent = intent
        val cardId = myIntent.getStringExtra("cardId")
        System.out.println(cardId)
        var card = Card()

        val et_title = findViewById(R.id.et_title) as EditText
        val et_description = findViewById(R.id.et_description) as EditText
        val et_stars = findViewById(R.id.et_stars) as EditText
        val cb_rare = findViewById(R.id.cb_rare) as CheckBox
        this.image_view = findViewById(R.id.image_view_camera) as ImageView
        val btn_update = findViewById(R.id.btn_update) as Button
        btn_update.setEnabled(false);

        btn_update.setOnClickListener {
            Thread {
                card.id = Integer.parseInt(cardId)
                card.title = et_title.text.toString()
                card.description = et_description.text.toString()
                card.stars = Integer.parseInt(et_stars.text.toString())
                card.rare = cb_rare.isChecked
                val bitmap = this.image_view?.drawable?.toBitmap()
                val stream = ByteArrayOutputStream()
                bitmap?.compress(Bitmap.CompressFormat.PNG, 90, stream)
                card.image = Base64.encodeToString(stream.toByteArray(), 0)

                val resp = Api().updateCard(card)
                goBack(null)
            }.start()
        }

        // get card by id
        Thread {
            card = Api().getCard(cardId!!)

            runOnUiThread {
                et_title.setText(card.title)
                et_description.setText(card.description)
                et_stars.setText(card.stars.toString())
                cb_rare.setChecked(card.rare)
                val imageBytes = Base64.decode(card.image.removePrefix("data:image/png;base64,"), Base64.DEFAULT)
                val decodedByte = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                image_view?.setImageBitmap(decodedByte)
                btn_update.setEnabled(true);
            }
        }.start()
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
        }
    }

    fun goBack(view: View?){
        val intent = Intent(this, MainActivity::class.java)
        resultLauncher.launch(intent)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun handleCameraClicked(view: View) {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), MY_CAMERA_PERMISSION_CODE)
        } else {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            val photo = data?.extras!!["data"] as Bitmap?
            runOnUiThread {
                image_view?.setImageBitmap(photo)
            }
        }
    }
}