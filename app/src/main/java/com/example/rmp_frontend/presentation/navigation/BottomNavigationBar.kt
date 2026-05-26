package com.example.rmp_frontend.presentation.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentRoute: String?
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        bottomBarScreens.forEach { screen ->
            val selected = currentRoute == screen.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(screen.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(Screen.Market.route) {
                                saveState = true
                            }
                        }
                    }
                },
                icon = {
                    Text(
                        text = screen.label().take(1),
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                label = { Text(screen.label()) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.16f)
                )
            )
        }
    }
}

private fun Screen.label(): String {
    return when (this) {
        Screen.Market -> "Market"
        Screen.Portfolio -> "Portfolio"
        Screen.History -> "History"
        Screen.Profile -> "Profile"
        else -> ""
    }
}
