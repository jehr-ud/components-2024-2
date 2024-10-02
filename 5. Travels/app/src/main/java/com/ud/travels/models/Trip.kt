package com.ud.travels.models

data class Trip(
    var id: Int,
    var initDate: String,
    var endDate: String,
    var destiny: String,
    var activities: String,
    var places: String,
    var price: Double
)