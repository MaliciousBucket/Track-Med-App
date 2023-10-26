package com.example.newtrackmed.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

data class NavBarItem(
    val icon: @Composable () -> Unit,
    val label: @Composable () -> Unit,
    val onClick: () -> Unit
)

@Composable
fun BottomNavBar(
    onHomeClick: () -> Unit,
    onMyMedicationsClick: () -> Unit,
    onReportsClick: () -> Unit
) {
    val navBarItems = listOf(
        NavBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            onClick = { onHomeClick() }
        ),
        NavBarItem(
            icon = { Icon(Icons.Filled.MedicalServices, contentDescription = "Medications") },
            label = { Text("MyMedications") },
            onClick = { onMyMedicationsClick() }
        ),
        NavBarItem(
            icon = { Icon(Icons.Filled.ContentPaste, contentDescription = "Reports") },
            label = { Text("Reports") },
            onClick = { onReportsClick() }
        ),
    )

    var selectedItem by remember { mutableIntStateOf(0) }

    NavigationBar {
        navBarItems.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = item.icon,
                label = item.label,
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    item.onClick()
                }
            )
        }
    }
}
