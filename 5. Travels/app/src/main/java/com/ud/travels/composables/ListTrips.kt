package com.ud.travels.composables

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
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

        Button (
            onClick = {
                val trip = Trip(
                    initDate = initDate,
                    endDate = endDate,
                    destiny = destiny,
                    activities = activities,
                    places = places,
                    price = price.toDoubleOrNull() ?: 0.0
                )

                saveTrip(trip)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Create Trip")
        }
    }
}

fun saveTrip(trip: Trip){
    val database = Firebase.database.reference
    val key = database.child("trips").push().key
    if (key == null) {
        Log.w("trips", "Couldn't get push key for trips")
        return
    }

    database.child("trips").child(key).setValue(trip)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripList(trips: List<Trip>){
    val estado = remember { mutableStateOf(true) }
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "My trips",
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    /*Carga el nuevo compose*/
                    estado.value = !estado.value
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "add travel")
            }
        }
    ) {innerPadding ->
        if(estado.value){
            LazyColumn (modifier = Modifier.padding(innerPadding)){
                items(trips) { trip ->
                    TripCard (trip)
                }
            }
        }else{
            Column(Modifier.padding(innerPadding)){
                TripForm()
            }
        }
    }
}



@Composable
fun TripCard(trip: Trip){
    Card (
        modifier= Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ){
        Row (
            Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                trip.id,
                style = MaterialTheme.typography.displayLarge
            )
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    trip.destiny,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "Start Date: ${trip.initDate}",
                    style=MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "End Date: ${trip.endDate}",
                    style=MaterialTheme.typography.bodyMedium
                )
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

