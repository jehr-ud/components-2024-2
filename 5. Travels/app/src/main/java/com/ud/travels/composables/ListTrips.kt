package com.ud.travels.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ud.travels.models.Trip


@Composable
fun TripList(trips: List<Trip>){
    Column {
        BasicText("My trips")
        LazyColumn {
            items(trips) { trip ->
                TripCard (trip)
            }
        }
    }
}

@Composable
fun TripCard(trip: Trip){
    Card {
        Row {
            BasicText(trip.id.toString())
            Column {
                BasicText("Start Date: ${trip.initDate}")
                BasicText("End Date: ${trip.endDate}")
            }
        }
    }
}

@Preview
@Composable
private fun PreviewList() {
    val trips = listOf(
        Trip(1.toString(), "2024-10-04", "2024-10-14", "Cali", "activities", "places", 100.2),
        Trip(2.toString(), "2024-10-04", "2024-10-14", "STM", "activities", "places", 100.2)
    )
    TripList(trips)
}