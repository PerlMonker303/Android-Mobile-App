package com.example.magiccards.cards.feature

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.example.magiccards.MainActivity
import com.example.magiccards.R
import android.content.pm.PackageManager

import android.provider.MediaStore

import android.graphics.Bitmap
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import java.util.*
import android.location.LocationListener
import android.location.LocationManager
import android.provider.Settings
import android.widget.*
import com.example.magiccards.cards.data.entities.Card
import java.text.SimpleDateFormat
import android.util.Base64
import androidx.core.graphics.drawable.toBitmap
import com.example.magiccards.cards.data.remote.Api
import java.io.ByteArrayOutputStream


class AddCardActivity : AppCompatActivity() {
    private val CAMERA_REQUEST = 1888
    private val LOCATION_REQUEST = 1889
    private var image_view: ImageView? = null
    private val MY_CAMERA_PERMISSION_CODE = 100
    lateinit var locationManager: LocationManager
    private var locationGps = Location("here")

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        val et_title = findViewById(R.id.et_title) as EditText
        val et_description = findViewById(R.id.et_description) as EditText
        val et_stars = findViewById(R.id.et_stars) as EditText
        val cb_rare = findViewById(R.id.cb_rare) as CheckBox
        image_view = findViewById(R.id.image_view_camera) as ImageView
        val btn_add = findViewById(R.id.btn_add) as Button

        btn_add.setOnClickListener {
            getLocation()
            val title = et_title.text.toString();
            val description = et_description.text.toString();
            val stars = Integer.parseInt(et_stars.text.toString());
            val added_on = Calendar.getInstance().time.toString("yyyy-MM-dd HH:mm:ss");
            val rare = cb_rare.isSelected
            val bitmap = this.image_view?.drawable?.toBitmap()
            val stream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.PNG, 90, stream)
            var image_base64: String = Base64.encodeToString(stream.toByteArray(), 0)
            var posted_by = 1;
            val latitude = locationGps.latitude
            val longitude = locationGps.longitude
            val card = Card(-1, title, description, stars, added_on, rare, image_base64, posted_by, latitude, longitude)

            Thread {
                val resp = Api().addCard(card)
//                Toast.makeText(this, "Card added successfully", Toast.LENGTH_LONG).show()
                goBack(null)
            }.start()
        }

        val photoButton: Button = findViewById<View>(R.id.btn_camera) as Button
        photoButton.setOnClickListener(View.OnClickListener {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), MY_CAMERA_PERMISSION_CODE)
            } else {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            }
        })
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (hasGps) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,                    android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    ,LOCATION_REQUEST)
                return
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0F, object : LocationListener {
                override fun onLocationChanged(p0: Location) {
                    if (p0 != null) {
                        locationGps = p0
                    }
                }

            })

            val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (localGpsLocation != null)
                locationGps = localGpsLocation

        } else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show()
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show()
            }
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

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
        }
    }

    fun goBack(view: View?){
        val intent = Intent(this, MainActivity::class.java)
        resultLauncher.launch(intent)
    }
}