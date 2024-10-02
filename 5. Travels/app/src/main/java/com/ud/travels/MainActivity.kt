package com.ud.travels

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import com.google.firebase.Firebase
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
        database = Firebase.database.reference

        val tripRef = database.child("trips")

        tripRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val trips = mutableListOf<Trip>()

                for (tripSnapshot in dataSnapshot.children){
                    val trip = tripSnapshot.getValue<Trip>()

                    if (trip != null){
                        trips.add(trip)
                    }
                }

                enableEdgeToEdge()
                setContent {
                    TravelsTheme {
                        Surface { TripList(trips) }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // ...
            }
        })



    }
}