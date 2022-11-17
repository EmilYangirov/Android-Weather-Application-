package com.emilyangirov.weatherproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.emilyangirov.weatherproject.Fragments.MainFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.placeHolder, MainFragment.newInstance()).commit()
    }


}