package com.example.wifiscanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.wifi_list_item.view.*

class WifiAdapter(
    val scans: MutableList<WiFiScan> = mutableListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.wifi_list_item, parent, false))

    override fun getItemCount() = scans.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as WifiAdapter.ViewHolder).onBind(scans[position], position)
    }

    fun updateList(list: List<WiFiScan>) {
        scans.clear()
        scans.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(data: WiFiScan, pos: Int) {
            val text = "Iteration $pos = ${data.rssi}dBm"

            view.rssiView.text = text
        }
    }
}