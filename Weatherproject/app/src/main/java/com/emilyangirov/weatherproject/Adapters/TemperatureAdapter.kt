package com.emilyangirov.weatherproject.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.emilyangirov.weatherproject.R
import com.emilyangirov.weatherproject.Scripts.TemperatureData
import com.emilyangirov.weatherproject.databinding.ListItemBinding
import com.squareup.picasso.Picasso

class TemperatureAdapter : ListAdapter<TemperatureData, Holder>(Comparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.createItem(getItem(position))
    }

}

class Holder(view: View) : RecyclerView.ViewHolder(view){

    val binding = ListItemBinding.bind(view)

    fun createItem(item: TemperatureData) = with(binding){
        dateText.text = item.date
        condText.text = item.condition
        tempText.text = item.averageTemp
        Picasso.get().load("https:"+item.imageUrl).into(conditionImage)
    }
}

class Comparator : DiffUtil.ItemCallback<TemperatureData>(){

    override fun areItemsTheSame(oldItem: TemperatureData, newItem: TemperatureData): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: TemperatureData, newItem: TemperatureData): Boolean {
        return oldItem == newItem
    }

}