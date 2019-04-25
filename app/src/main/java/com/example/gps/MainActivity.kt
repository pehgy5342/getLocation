package com.example.gps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.api.GoogleApiClient
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private lateinit var location: Location


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestUserLocation()
        btn_start.setOnClickListener {
            getLocation()
        }



    }


    fun requestUserLocation() {

        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)


        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, permissions, 0)
        }


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            0 ->
                //
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //開始做事
                    Toast.makeText(this, "已授權，可開始取得經緯度", Toast.LENGTH_SHORT).show()


                } else {
                    Toast.makeText(this, "請給與授權", Toast.LENGTH_SHORT).show()
                }



        }
    }


    val locationListener = object : LocationListener {

        override fun onLocationChanged(location: Location) {
            println("******************** $location")

            tv_lat.text = "經度 : ${location.latitude}"
            tv_lon.text = "緯度 : ${location.longitude}"
            this@MainActivity.location = location
            getLocation()

        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

        override fun onProviderEnabled(provider: String?) {
        }

        override fun onProviderDisabled(provider: String?) {

            Toast.makeText(this@MainActivity, "取得位置失敗，請開啟網路和定位", Toast.LENGTH_SHORT).show()
        }


    }

    //資料顯示
    fun getLocation() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val netWork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        val gpsCheckSelfPermission = (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED)
        val netCheckSelfPermission = (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED)
        println("******************** $gps  $netWork")


        if (gpsCheckSelfPermission) {
            if (gps) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0F, locationListener)
            } else {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0F, locationListener)

            }


        }
        else if (netCheckSelfPermission) {

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0F, locationListener)

        }

        else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0F, locationListener)

        }


    }


}






