package com.example.anonymouschat.AnonymousandroidApp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.anonymouschat.AnonymousandroidApp.FragmentMenu.BlogFragment
import com.example.anonymouschat.AnonymousandroidApp.FragmentMenu.ChatFragment
import com.example.anonymouschat.AnonymousandroidApp.FragmentMenu.SettingFragment
import com.example.anonymouschat.AnonymousandroidApp.MVP.InterFaceMain
import com.example.anonymouschat.AnonymousandroidApp.MVP.PresenterLogin
import com.example.anonymouschat.shared.Greeting
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap


fun greet(): String {
    return Greeting().greeting()
}
class MainActivity : AppCompatActivity() , InterFaceMain.View , LocationListener  {
    private lateinit var tabLayout:TabLayout
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val realTime = FirebaseDatabase.getInstance()
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val blogMenu:Fragment = BlogFragment()
    private val chatMenu:Fragment = ChatFragment()
    private val settingMenu:Fragment = SettingFragment()
    private lateinit var mLocationManager: LocationManager
    private val presenterLogin:PresenterLogin = PresenterLogin(this)
    private var currentFragment:Fragment = blogMenu
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenterLogin.loginAnonymous()
        setFragment()
        tabLayout = findViewById(R.id.tabLayoutMenu)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab!!.position){
                    0 -> {
                        supportFragmentManager.beginTransaction().show(blogMenu)
                            .hide(currentFragment).apply {

                             }.commit()
                        currentFragment = blogMenu
                    }
                    1 -> {
                        supportFragmentManager.beginTransaction().show(chatMenu)
                            .hide(currentFragment).apply {

                             }.commit()
                        currentFragment = chatMenu
                    }
                    2 -> {
                        supportFragmentManager.beginTransaction().show(settingMenu)
                            .hide(currentFragment).apply {

                            }.commit()
                        currentFragment = settingMenu
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setFragment() {
        supportFragmentManager.beginTransaction().apply {
            add(R.id.frameMenu,blogMenu).hide(blogMenu)
            add(R.id.frameMenu,chatMenu).hide(chatMenu)
            add(R.id.frameMenu,settingMenu).hide(settingMenu)
        }.commit()
        supportFragmentManager.beginTransaction().show(blogMenu).commit()
    }

    override fun result(result: String) {
        Toast.makeText(this@MainActivity,result,Toast.LENGTH_SHORT).show()
        getLocation(this@MainActivity)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 1){
            getLocation(this@MainActivity)
        }
    }
    private fun getLocation(context: Context) {
        mLocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this@MainActivity, arrayOf<String?>(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION),
                    1)
        }else {
            val location=  mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if(location != null) {
                getLocationXY(location)
            } else {
                Log.d("TAG_LOCATION","Location")
                mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 100, 0f, this@MainActivity )
            }
        }
    }

    private fun getLocationXY(location: Location) {
            val locationPreferences = getSharedPreferences("Location", Context.MODE_PRIVATE).edit()
            locationPreferences.putFloat("x", location.latitude.toFloat())
            locationPreferences.putFloat("y", location.longitude.toFloat())
            val geoCoder = Geocoder(this@MainActivity.applicationContext, Locale.forLanguageTag("th"))
            val userId = mAuth.currentUser!!.uid
            val address = geoCoder.getFromLocation(location.latitude, location.longitude, 1)
            val ref = realTime.reference.child("Users").child(userId).child("Location")
            val data:HashMap<String,Any> = hashMapOf(
                "city" to address[0].locality,
                "state" to address[0].adminArea,
                "country" to address[0].countryName
            )
            ref.updateChildren(data)
            locationPreferences.putString("state", address[0].adminArea)
             locationPreferences.apply()
    }

    private fun dialogGps(context: Context) {
        val mGPSDialog: Dialog
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.gps_title)
        builder.setMessage(R.string.gps_message)
        builder.setPositiveButton(R.string.gps_confirm) { _, _ ->
            (context as Activity).startActivityForResult(
                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0
            )
        }.setNegativeButton(R.string.gps_dismiss) { _, _ -> }
        mGPSDialog = builder.create()
        mGPSDialog.show()
    }

    override fun onLocationChanged(p0: Location?) {
            Log.d("TAG_LOCATION", "Location Update")
            val locationPreferences = getSharedPreferences("Location", Context.MODE_PRIVATE).edit()
            locationPreferences.putFloat("x", p0!!.latitude.toFloat())
            locationPreferences.putFloat("y", p0.longitude.toFloat())
            val userId = mAuth.currentUser!!.uid
            val geoCoder = Geocoder(this@MainActivity, Locale.forLanguageTag("th"))
            val address = geoCoder.getFromLocation(p0.latitude, p0.longitude, 1)
            val ref = realTime.reference.child("Users").child(userId).child("Location")
            val data:HashMap<String,Any> = hashMapOf(
                "city" to address[0].locality,
                "state" to address[0].adminArea,
                "country" to address[0].countryName
            )
            ref.updateChildren(data)
            locationPreferences.putString("state", address[0].adminArea)
            locationPreferences.apply()
            mLocationManager.removeUpdates(this@MainActivity)
    }
    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}
    override fun onProviderEnabled(p0: String?) {
        getLocation(this@MainActivity)
    }
    override fun onProviderDisabled(p0: String?) {
        if (p0 == LocationManager.GPS_PROVIDER) {
            dialogGps(this@MainActivity)
        }
    }
}
