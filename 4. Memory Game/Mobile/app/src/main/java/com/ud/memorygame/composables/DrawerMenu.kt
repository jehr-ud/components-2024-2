package com.ud.memorygame.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DrawerMenuContent(
    onMenuItemClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mis partidas",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .clickable { onMenuItemClick("mis_partidas") }
        )
        Text(
            text = "Jugar",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .clickable { onMenuItemClick("jugar") }
        )
    }
}