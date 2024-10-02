package com.ud.travels

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import com.ud.travels.composables.TripList
import com.ud.travels.models.Trip
import com.ud.travels.ui.theme.TravelsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val trips = listOf(
            Trip(1, "2024-10-04", "2024-10-14", "Cali", "activities", "places", 100.2),
            Trip(2, "2024-10-04", "2024-10-14", "STM", "activities", "places", 100.2)
        )

        enableEdgeToEdge()
        setContent {
            TravelsTheme {
                Surface { TripList(trips) }
            }
        }
    }
}