package com.example.workingwiththemenu

data class WeatherData (
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Main(
    val temp: Float,
    val feels_like: Float,
    val temp_min: Float,
    val temp_max: Float,
    val pressure: Int,
    val humidity: Int
)

data class Wind(
    val speed: Float,
    val deg: Float,
    val gust: Float
)