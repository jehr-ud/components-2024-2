package com.ud.travels.composables

import android.util.Log
import androidx.compose.foundation.clickable
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
fun TravelsApp(trips: List<Trip>) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "tripList") {
        composable("tripList") {
            TripListScreen(
                trips = trips,
                onTripClick = { tripId ->
                    navController.navigate("tripDetail/$tripId")
                }
            )
        }
        composable(
            "tripDetail/{tripId}",
            arguments = listOf(navArgument("tripId") { type = NavType.StringType })
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getString("tripId")
            TripDetailScreen(tripId = tripId)
        }
    }
}

@Composable
fun TripListScreen(trips: List<Trip>, onTripClick: (String) -> Unit) {
    TripList(trips, onTripClick)
}

@Composable
fun TripList(trips: List<Trip>, onTripClick: (String) -> Unit) {
    Column {
        BasicText("My trips")
        LazyColumn {
            items(trips) { trip ->
                TripCard(trip, onClick = { onTripClick(trip.id) })
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        TripForm()
    }
}

@Composable
fun TripCard(trip: Trip, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Row {
            BasicText(trip.id)
            Column {
                BasicText("Start Date: ${trip.initDate}")
                BasicText("End Date: ${trip.endDate}")
            }
        }
    }
}


@Composable
fun TripDetailScreen(tripId: String?) {
    if (tripId == null) {
        BasicText("Trip not found")
        return
    }

    val trip = remember { mutableStateOf<Trip?>(null) }

    LaunchedEffect(tripId) {
        trip.value = Trip(
            id = tripId,
            initDate = "2024-10-04",
            endDate = "2024-10-14",
            destiny = "Sample Destination",
            activities = "Sample Activities",
            places = "Sample Places",
            price = 100.2
        )
    }

    trip.value?.let { trip ->
        Column(modifier = Modifier.padding(16.dp)) {
            BasicText("Trip ID: ${trip.id}")
            BasicText("Start Date: ${trip.initDate}")
            BasicText("End Date: ${trip.endDate}")
            BasicText("Destination: ${trip.destiny}")
            BasicText("Activities: ${trip.activities}")
            BasicText("Places: ${trip.places}")
            BasicText("Price: ${trip.price}")
        }
    } ?: run {
        BasicText("Loading trip details...")
    }
}

@Preview
@Composable
private fun PreviewList() {
    val trips = listOf(
        Trip("1", "2024-10-04", "2024-10-14", "Cali", "activities", "places", 100.2),
        Trip("2", "2024-10-04", "2024-10-14", "STM", "activities", "places", 100.2)
    )
    TravelsApp(trips)
}