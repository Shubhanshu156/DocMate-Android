package com.example.docmate.ui.graphs

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.docmate.ui.BottomBarScreen
import com.example.docmate.ui.Screens.Doctor.DocAppointment
import com.example.docmate.ui.Screens.Doctor.DocProfile

@Preview(showSystemUi = true)
@Composable
fun DocHome(navController: NavHostController = rememberNavController()) {
    DocNavGraph(navController = navController)
}

@Composable
fun DocNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.DOCHOME,
        startDestination = DocGraph.DOCAPPOINTMENT
    ) {
        composable(route = DocGraph.DOCAPPOINTMENT) {
            DocAppointment(onprofile = {
                navController.navigate(DocGraph.DOC_PROFILE)
            })
        }

        composable(route = DocGraph.DOC_PROFILE) {
            DocProfile(
            )
        }

    }
}

object DocGraph {
    const val DOCROOT = "Docroot_graph"
    const val DOCAPPOINTMENT = "DOCAPPOINTMENT"
    const val DOC_PROFILE = "DOCPROFILE"
}