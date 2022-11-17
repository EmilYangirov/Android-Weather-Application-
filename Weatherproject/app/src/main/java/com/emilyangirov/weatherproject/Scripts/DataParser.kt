package com.emilyangirov.weatherproject.Scripts

import org.json.JSONObject

class DataParser {

    public fun createCityData(json: JSONObject): CityData {
        val cityData = CityData(
            json.getJSONObject("location").getString("name"),
            createDayData(json),
            createDayDataArray(json),
        )

        return cityData
    }

    //Создание объекта день
    private fun createDayData(json: JSONObject): DayData {
        val dayData = DayData(
            createCurrentTempData(json),
            createHourDataArray(json),
        )

        return dayData
    }

    //Парсинг данных по температуре на следующие дни
    private fun createHourDataArray(json: JSONObject): List<TemperatureData>{
        val daysTemperature = ArrayList<TemperatureData>()
        val daysArray = json.getJSONObject("forecast").getJSONArray("forecastday")[0]
        val tempArray = (daysArray as JSONObject).getJSONArray("hour")

        for(i in 0 until tempArray.length()){
            val day = tempArray[i] as JSONObject

            //объект температуры за i-ый час
            val newData = TemperatureData(
                day.getString("time"),
                day.getString("temp_c")+"°C",
                day.getJSONObject("condition").getString("text"),
                day.getJSONObject("condition").getString("icon"),
            )

            daysTemperature.add(newData)
        }

        return daysTemperature
    }

    //Парсинг данных по температуре на следующие дни
    private fun createDayDataArray(json: JSONObject): List<TemperatureData>{
        val nextDaysTemperature = ArrayList<TemperatureData>()
        val daysArray = json.getJSONObject("forecast").getJSONArray("forecastday")

        for(i in 0 until daysArray.length()){
            val day = daysArray[i] as JSONObject

            //объект температуры за i-ый день
            val newData = TemperatureData(
                day.getString("date"),
                day.getJSONObject("day").getString("avgtemp_c")+"°C",
                day.getJSONObject("day").getJSONObject("condition").getString("text"),
                day.getJSONObject("day").getJSONObject("condition").getString("icon"),
            )

            nextDaysTemperature.add(newData)
        }

        return nextDaysTemperature
    }

    //Парсинг данных по температуре за текущий день
    private fun createCurrentTempData(json: JSONObject): TemperatureData {
        val tempData = TemperatureData(
            json.getJSONObject("current").getString("last_updated"),
            json.getJSONObject("current").getString("temp_c") + "°C",
            json.getJSONObject("current").getJSONObject("condition").getString("text"),
            json.getJSONObject("current").getJSONObject("condition").getString("icon"),
        )

        return tempData
    }

}