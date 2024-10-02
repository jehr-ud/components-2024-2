package com.ud.travels.models

data class Trip(
    var id: String = "",
    var initDate: String = "",
    var endDate: String = "",
    var destiny: String = "",
    var activities: String = "",
    var places: String = "",
    var price: Double = 0.0
)