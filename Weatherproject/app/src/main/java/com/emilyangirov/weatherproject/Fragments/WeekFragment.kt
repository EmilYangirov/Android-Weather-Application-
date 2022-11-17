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
import com.emilyangirov.weatherproject.databinding.FragmentWeekBinding

class WeekFragment : Fragment() {

    private lateinit var binding: FragmentWeekBinding
    private lateinit var adapter: TemperatureAdapter

    private val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeekBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Init()
    }

    private fun Init()=with(binding){
        adapter = TemperatureAdapter()
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter=adapter
        updateData()
    }

    private fun updateData() = with(binding){
        model.cityData.observe(viewLifecycleOwner){
            adapter.submitList(it.nextDays)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = WeekFragment()
    }

}