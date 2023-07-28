package com.example.docmate.ui.graphs

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun RootNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.AUTHENTICATION
    ) {
        authNavGraph(navController = navController)
        composable(route=Graph.DOCHOME){
            DocHome()
        }
        composable(route = Graph.HOME) {
            HomeScreen()
        }
    }
}



object Graph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val HOME = "userhome_graph"
    const val DOCHOME="doctorhome"
    const val HOME_DETAILS="home_details"
}