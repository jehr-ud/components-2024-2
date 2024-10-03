package com.ud.travels.composables

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.ud.travels.models.Trip


@Composable
fun TripForm() {
    var initDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var destiny by remember { mutableStateOf("") }
    var activities by remember { mutableStateOf("") }
    var places by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    val context = LocalContext.current
    val database = Firebase.database.reference

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = initDate,
            onValueChange = { initDate = it },
            label = { Text("Start Date") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = endDate,
            onValueChange = { endDate = it },
            label = { Text("End Date") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = destiny,
            onValueChange = { destiny = it },
            label = { Text("Destination") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = activities,
            onValueChange = { activities = it },
            label = { Text("Activities") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = places,
            onValueChange = { places = it },
            label = { Text("Places") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                val trip = Trip(
                    initDate = initDate,
                    endDate = endDate,
                    destiny = destiny,
                    activities = activities,
                    places = places,
                    price = price.toDoubleOrNull() ?: 0.0
                )
                saveTrip(trip, database)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Create Trip")
        }
    }
}

fun saveTrip(trip: Trip, database: DatabaseReference) {
    val key = database.child("trips").push().key
    if (key == null) {
        Log.w("trips", "Couldn't get push key for trips")
        return
    }
    database.child("trips").child(key).setValue(trip)
}

@Composable
fun TripList(trips: List<Trip>){
    Column {
        BasicText("My trips")
        LazyColumn {
            items(trips) { trip ->
                TripCard (trip)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        TripForm()
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
        Trip("1", "2024-10-04", "2024-10-14", "Cali", "activities", "places", 100.2),
        Trip("2", "2024-10-04", "2024-10-14", "STM", "activities", "places", 100.2)
    )
    TripList(trips)
}