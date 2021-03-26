package com.example.wifiscanner

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Thread.sleep

import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
        shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_WIFI_STATE)
        shouldShowRequestPermissionRationale(Manifest.permission.CHANGE_WIFI_STATE)
    }

    private val list: MutableList<WiFiScan> = mutableListOf()
    val adapter = WifiAdapter(list)

    lateinit var wifiManager: WifiManager


    override fun onResume() {
        super.onResume()

        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        recyclerView.layoutManager = LinearLayoutManager(baseContext)
        recyclerView.adapter = adapter

        scan_button.setOnClickListener {

            scanLoop({
                updateList(it)
            }, {
                progressBar.visibility = View.INVISIBLE
            })

        }

    }

    fun updateList(updatedList: List<WiFiScan>) {

        runOnUiThread {
            adapter.updateList(updatedList)
        }
    }


    private fun scanLoop(callback: (List<WiFiScan>) -> Unit, closeAnimation: () -> Unit) {

        progressBar.visibility = View.VISIBLE

        Log.d("SYDE362",
            "Getting info from: ${wifiManager.connectionInfo.rssi}, " +
                    "${wifiManager.connectionInfo.bssid}, " +
                    "${wifiManager.connectionInfo.networkId}")

        GlobalScope.launch {
            val list: MutableList<WiFiScan> = mutableListOf()

            for (i in 1..30) {
                Log.d(
                    "SYDE362",
                    "Iteration $i - ${wifiManager.connectionInfo.bssid}: ${wifiManager.connectionInfo.rssi}dBm"
                )

                list.add(WiFiScan(wifiManager.connectionInfo.rssi))
                delay(5000L)

                if (i%10 == 0) {
                    callback(list.toList())
                }
            }

            Log.d("SYDE362", "Scans complete")
            closeAnimation()
        }

    }


}