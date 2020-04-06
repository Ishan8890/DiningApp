package com.example.diningapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diningapp.Model.MainActivityViewModel
import com.example.diningapp.data.V
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var mainActivityViewModel: MainActivityViewModel
    lateinit var progressBar: ProgressBar
    lateinit var recylerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recylerView = findViewById(R.id.recylerView);
        progressBar = findViewById(R.id.progress_circular);

        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
        mainActivityViewModel.failure.observe(this, Observer {
            progressBar.visibility = View.GONE
            response.visibility = View.VISIBLE
            response.text = it
        })

        mainActivityViewModel.venueList.observe(this, Observer { venueDataList ->
            progressBar.visibility = View.GONE
            recylerView.visibility = View.VISIBLE
            response.visibility = View.GONE
            var diningAdapter = DiningAdapter(venueDataList, this@MainActivity)
            recylerView!!.layoutManager = LinearLayoutManager(this@MainActivity)
            recylerView!!.adapter = diningAdapter
            diningAdapter.notifyDataSetChanged()
            recylerView.scheduleLayoutAnimation()
        })

        clicked.setOnClickListener {
            if (!search.query.isEmpty()) {
                val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(
                    currentFocus!!.windowToken,
                    InputMethodManager.SHOW_FORCED
                )
                progressBar.visibility = View.VISIBLE
                recylerView.visibility = View.GONE
                var str = search.query.toString()
                mainActivityViewModel.getAPIData(V, str, this)
            } else {
                Toast.makeText(this, getString(R.string.empty_field), Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        location.latitude.toString()
                        location.longitude.toString()
                        latlngtoAddress(location.latitude.toString(), location.longitude.toString())
                    }
                }
            } else {
                Toast.makeText(this, getString(R.string.on_location), Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            mLastLocation.latitude.toString();
            mLastLocation.longitude.toString();
            latlngtoAddress(mLastLocation.latitude.toString(), mLastLocation.longitude.toString())
        }
    }

    private fun latlngtoAddress(lat: String, lng: String) {
        var list: List<Address>
        var geoCoder = Geocoder(this, Locale.getDefault())
        list = geoCoder.getFromLocation(lat.toDouble(), lng.toDouble(), 1)
        var mLocality = list.get(0).locality
        if (mLocality != null)
            mainActivityViewModel.getAPIData(V, mLocality, this)
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }
}
