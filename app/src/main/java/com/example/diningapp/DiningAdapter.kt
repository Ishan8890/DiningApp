package com.example.diningapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.diningapp.data.network.response.Venue
import kotlinx.android.synthetic.main.dine_view.view.*
import java.lang.StringBuilder

class DiningAdapter(private val venueList: ArrayList<Venue>, val context: Context) :
    RecyclerView.Adapter<DiningAdapter.MyViewHolder>() {

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val name = view.name
        val address = view.address
        val dinStatus = view.dineStatus
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): DiningAdapter.MyViewHolder {
        val textView = LayoutInflater.from(parent.context).inflate(R.layout.dine_view, parent, false)
        return MyViewHolder(textView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = venueList.get(position).name
        var list:ArrayList<String> = venueList.get(position).location.formattedAddress
        val address = StringBuilder()
        for (i in list) {
            address.append(i).append(" ,")
        }
        holder.address.text = address
        if(venueList.get(position).verified){
            holder.dinStatus.setBackgroundResource(R.drawable.ic_thumb_up_black_24dp)
        }else{
            holder.dinStatus.setBackgroundResource(R.drawable.ic_thumb_down_black_24dp)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = venueList.size
}