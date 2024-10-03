package com.ud.travels

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.ud.travels.composables.TripList
import com.ud.travels.models.Trip
import com.ud.travels.ui.theme.TravelsTheme


class MainActivity : ComponentActivity() {
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        database = Firebase.database.reference

        val trips = mutableStateListOf<Trip>()

        val tripRef = database.child("trips")
        tripRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                trips.clear()

                for (tripSnapshot in dataSnapshot.children) {
                    val trip = tripSnapshot.getValue<Trip>()
                    if (trip != null) {
                        trip.id = tripSnapshot.key.toString()
                        trips.add(trip)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("MainActivity", "Database error: ${databaseError.message}")
            }
        })

        enableEdgeToEdge()
        setContent {
            TravelsTheme {
                Surface {
                    TripList(trips)
                }
            }
        }
    }
}