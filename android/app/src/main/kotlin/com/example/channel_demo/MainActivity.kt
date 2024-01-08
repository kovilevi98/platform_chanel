package com.example.channel_demo

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationRequest
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import java.util.concurrent.CompletableFuture


class MainActivity : FlutterActivity() {

    companion object {
        private const val CHANNEL = "com.test.messages"
    }


    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine)

        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            CHANNEL
        ).setMethodCallHandler { call, result ->
            when (call.method) {
                "startService" -> {
                    val message: String = startService()
                    result.success(message)
                }

                "stopService" -> {
                    val message: String = stopService()
                    result.success(message)
                }

                "getBatteryLevel" -> {
                    val message: String = getBatteryLevel()
                    result.success(message)
                }

                "getLocation" -> {
                    getLocationAsync().thenAccept { message ->
                        result.success(message)
                    }
                }

                else -> result.notImplemented()
            }
        }

    }

    private fun startService(): String {
        // Code to start a service
        return "Service Started"
    }

    private fun stopService(): String {
        // Code to stop a service
        return "Service Stopped"
    }

    private fun getBatteryLevel(): String {
        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            context.registerReceiver(null, ifilter)
        }


        val batteryPct: Float? = batteryStatus?.let { intent ->
            val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            level * 100 / scale.toFloat()
        }

        return batteryPct.toString()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLocationAsync(): CompletableFuture<String> {
        val future = CompletableFuture<String>()


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            future.complete("Permission not granted")
        }

        val cancellationTokenSource = CancellationTokenSource()
        val task = fusedLocationClient.getCurrentLocation(
            LocationRequest.QUALITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        )
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations, this can be null.
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    // Do something with the latitude and longitude here.
                    val result = "Latitude: $latitude, Longitude: $longitude"
                    // You can pass this result back to Flutter using MethodChannel or perform any other action.
                    // For simplicity, I'll just print the result here.
                    future.complete(result)
                } else {
                    // Handle the case where the location is null.
                    // It might happen in rare situations.
                    future.complete("Location is null")
                }
            }
            .addOnFailureListener { exception ->
                // Handle any exceptions that occurred while trying to get the location.
                // This might happen if location services are disabled on the device or other errors occur.
                future.complete("Failed to get location: ${exception.message}")
            }

        return future;


    }
}
