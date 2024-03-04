package com.example.workingwiththemenu


import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private lateinit var icon: ImageView
    private lateinit var descriptionWeather: TextView

    private lateinit var temp: TextView
    private lateinit var pressure: TextView
    private lateinit var humidity: TextView

    private lateinit var speed: TextView
    private lateinit var deg: TextView

    private val API_KEY = "afd3f31c472731bed0074b6a14cbf7f1"
    private var weatherURL_lan = "https://api.openweathermap.org/data/2.5/weather?q=Irkutsk&appid=$API_KEY&units=metric&lang="
    private lateinit var weatherURL: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        icon = findViewById(R.id.icon)
        descriptionWeather = findViewById(R.id.descriptionWeather1)

        temp = findViewById(R.id.tempData)
        pressure = findViewById(R.id.pressureData)
        humidity = findViewById(R.id.humidityData)

        speed = findViewById(R.id.speedData)
        deg = findViewById(R.id.degData)

        val currentLanguage = getCurrentLanguage(this)
        weatherURL = weatherURL_lan + currentLanguage

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setTitle(R.string.app_name)
        toolbar.inflateMenu(R.menu.languages)

        toolbar.setOnMenuItemClickListener {
            item -> updateLocale("$item"); true
        }

        updateWeatheer()

    }


    fun getCurrentLanguage(context: Context): String {
        val configuration = context.resources.configuration
        return configuration.locales.get(0).language
    }


    fun updateLocale(lan:String){

        val locale = Locale(lan)
        Locale.setDefault(locale)

        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)
        weatherURL = weatherURL_lan + "$lan"
        recreate()
    }


    private suspend fun loadWeather(): WeatherData? {

        return try {
            val stream = URL(weatherURL).openStream()

            // JSON отдаётся одной строкой,
            val data = stream.bufferedReader().use { it.readText() }

            val gson = Gson()

            gson.fromJson(data, WeatherData::class.java)

        } catch (e: IOException) {
            // Обработка ошибок при работе с сетью
            e.printStackTrace()
            null
        }


    }

    private fun updateWeatheer(){
        var weatherData: WeatherData?
        CoroutineScope(Dispatchers.IO).launch {
            weatherData = loadWeather()
            withContext(Dispatchers.Main) {
                if (weatherData != null) {
                    val iconWeatherURL = "https://openweathermap.org/img/wn/${weatherData!!.weather[0].icon}@4x.png"
                    Picasso.get().load(iconWeatherURL).into(icon)

                    descriptionWeather.text = weatherData!!.weather[0].description

                    temp.text = weatherData!!.main.temp.toString()
                    pressure.text = weatherData!!.main.pressure.toString()
                    humidity.text = weatherData!!.main.humidity.toString()

                    speed.text = weatherData!!.wind.speed.toString()
                    deg.text = weatherData!!.wind.deg.toString()

                }
            }
        }
    }


}