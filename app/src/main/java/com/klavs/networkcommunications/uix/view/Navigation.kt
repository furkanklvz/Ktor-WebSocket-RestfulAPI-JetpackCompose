package com.klavs.networkcommunications.uix.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.klavs.networkcommunications.data.routes.RestfulAPITrailPage
import com.klavs.networkcommunications.data.routes.WebSocketTrailPage
import com.klavs.networkcommunications.data.routes.bottombar.BottomBarItem
import com.klavs.networkcommunications.uix.viewmodel.RestfulViewModel
import com.klavs.networkcommunications.uix.viewmodel.WebSocketViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun Navigation() {
    val navController = rememberNavController()
    Scaffold(bottomBar = {
        NavigationBar {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            BottomBarItem.items.forEach { bottomBarItem ->
                val selected =
                    currentDestination?.hierarchy?.any { it.hasRoute(bottomBarItem.route::class) } == true
                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        navController.navigate(bottomBarItem.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(
                            bottomBarItem.imageVector,
                            contentDescription = bottomBarItem.title
                        )
                    },
                    label = {
                        Text(bottomBarItem.title)
                    }
                )
            }
        }
    }) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
            navController = navController,
            startDestination = RestfulAPITrailPage
        ) {
            composable<RestfulAPITrailPage> {
                val viewModel = koinViewModel<RestfulViewModel>()
                RestfulAPITrialPage(viewModel)
            }
            composable<WebSocketTrailPage> {
                val viewModel = koinViewModel<WebSocketViewModel>()
                WebSocketTrailPage(viewModel)
            }
        }
    }
}