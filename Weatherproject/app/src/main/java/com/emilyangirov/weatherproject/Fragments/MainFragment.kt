package com.emilyangirov.weatherproject.Fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.emilyangirov.weatherproject.Adapters.ViewPagerAdapter
import com.emilyangirov.weatherproject.Scripts.*
import com.emilyangirov.weatherproject.databinding.FragmentMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import org.json.JSONObject


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var pLauncher: ActivityResultLauncher<String>
    private lateinit var locationClient: FusedLocationProviderClient
    private val dataParser: DataParser = DataParser()
    private val model: MainViewModel by activityViewModels()
    private var standartCity = "Moscow"

    val apiKey = "2179c47fd9814cd7821154957221411"

    private val fragments: List<FragmentData> = listOf(
        FragmentData("Day", DayFragment.newInstance()),
        FragmentData("Week", WeekFragment.newInstance())
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        init()
        updateData()
    }


    override fun onResume() {
        super.onResume()
        checkLocation()
    }

    private fun init() = with(binding) {
        locationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        val adapter = ViewPagerAdapter(activity as FragmentActivity, fragments)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
            tab.text = fragments[pos].name
        }.attach()

        setListeners()
    }

    private fun setListeners()= with(binding){
        reloadButton.setOnClickListener{
            getLocation()
        }

        findButton.setOnClickListener(){
            DialogueManager.SearchByName(requireContext(),object : DialogueManager.FindListener{
                override fun onClick(name: String) {
                    standartCity = name
                    requestData(name)
                }

            })
        }
    }

    //region find location
    private fun isGpsEnabled(): Boolean{
        val lm = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun checkLocation(){
        if(isGpsEnabled())
            getLocation()
        else
            DialogueManager.locationSettingsDialogue(requireContext(), object : DialogueManager.ReloadListener{
                override fun onClick() {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            })
    }

    private fun getLocation(){

        requestData(standartCity)
        return

        //при включенном гпс эмулятор выдает ошибку о том, что им не поддерживается гпс
        //на реальном устройстве Xiaomi Mi8 все работает без ошибок
        //установил стандартный город - город Москва

        if(!isGpsEnabled()) {
            Toast.makeText(requireContext(),"Location Disabled", Toast.LENGTH_SHORT).show()
            return
        }

        val token = CancellationTokenSource()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        locationClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY,token.token).addOnCompleteListener{
                requestData("${it.result.latitude},${it.result.longitude}")
            }
    }
    //endregion

    private fun updateData() = with(binding){
        model.cityData.observe(viewLifecycleOwner){
            cityName.text = it.cityName
            temperatureText.text = it.currentDay.currentTemp.averageTemp
            conditionText.text = it.currentDay.currentTemp.condition
            currentDateText.text = it.currentDay.currentTemp.date
            Picasso.get().load("https:"+it.currentDay.currentTemp.imageUrl).into(temperatureImage)
        }
    }

    private fun registerPermissionListener() {
        pLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            Toast.makeText(activity, "Permission is $it", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkPermission(name: String) {
        if (!hasPermission(name)) {
            registerPermissionListener()
            pLauncher.launch(name)
        }
    }

    private fun requestData(city: String, daysCount: Int = 7) {
        val url = "https://api.weatherapi.com/v1/forecast.json?key=" +
                apiKey +
                "&q=" +
                city +
                "&days=" +
                daysCount +
                "&aqi=no&alerts=no"

        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET,
            url,
            { result ->
                parseWeatherData(result)
            },
            { error ->
                Log.d("logmy", "Error: $error")
            }
        )
        queue.add(request)
    }

    private fun parseWeatherData(result: String) {
        val jsonResult = JSONObject(result)

        val data = dataParser.createCityData(jsonResult)
        model.cityData.value = data
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}