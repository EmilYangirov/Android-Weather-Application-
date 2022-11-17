package com.emilyangirov.weatherproject.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.emilyangirov.weatherproject.Adapters.TemperatureAdapter
import com.emilyangirov.weatherproject.R
import com.emilyangirov.weatherproject.Scripts.MainViewModel
import com.emilyangirov.weatherproject.Scripts.TemperatureData
import com.emilyangirov.weatherproject.databinding.FragmentDayBinding
import com.squareup.picasso.Picasso

class DayFragment : Fragment() {

    private lateinit var binding : FragmentDayBinding
    private lateinit var adapter : TemperatureAdapter
    private val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        InitRecyclerView()
    }

    private fun updateData() = with(binding){
        model.cityData.observe(viewLifecycleOwner){
            adapter.submitList(it.currentDay.tempByHours)
        }
    }

    private fun InitRecyclerView() = with(binding){
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = TemperatureAdapter()
        recyclerView.adapter = adapter
        updateData()
    }

    companion object {
        @JvmStatic
        fun newInstance() =  DayFragment()
    }

}