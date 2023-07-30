package com.example.docmate.ui.graphs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.docmate.ui.Screens.Auth.Splash.SplashScreen
import com.example.docmate.ui.Screens.Patient.Tour.TourScreen
import com.example.docmate.ui.theme.Screens.SignIn.Signin
import com.example.docmate.ui.theme.Screens.SignUp.SignUp


fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = AuthScreen.Splash.route
    ) {
        composable(route = AuthScreen.Splash.route) {
            AnimatedVisibility(
                visible = true, // Update this visibility condition as per your logic
                enter = slideInVertically(initialOffsetY = { 300 }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { 300 }) + fadeOut()
            ) {
                SplashScreen(onuserHome = {
                        navController.popBackStack()
                        navController.navigate(Graph.HOME)
                    },onDocHome={
                        navController.popBackStack()
                    navController.navigate(Graph.DOCHOME)}
                    , onSignin =
                {
                    navController.popBackStack()
                    navController.navigate(AuthScreen.Login.route)
                }
                )
            }
        }
        composable(route = AuthScreen.Login.route) {
            AnimatedVisibility(
                visible = true, // Update this visibility condition as per your logic
                enter = slideInVertically(initialOffsetY = { 300 }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { 300 }) + fadeOut()
            ) {
                Signin(
                    onUserHome = {
                        navController.popBackStack()
                        navController.navigate(Graph.HOME)
                    },
                    onDocHome={
                        navController.popBackStack()
                        navController.navigate(Graph.DOCHOME)
                    },
                    onTour = {
                        navController.popBackStack()
                        navController.navigate(AuthScreen.Tour.route)
                    }
                ,
                onSignUpClick= {
                    navController.popBackStack()
                    navController.navigate(AuthScreen.SignUp.route)
                })
            }
        }
        composable(route = AuthScreen.SignUp.route) {
            AnimatedVisibility(
                visible = true, // Update this visibility condition as per your logic
                enter = slideInVertically(initialOffsetY = { 300 }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { 300 }) + fadeOut()
            ) {
                SignUp() {
                    navController.popBackStack()
                    navController.navigate(AuthScreen.Login.route)
                }
            }
        }
        composable(route = AuthScreen.Tour.route) {
            TourScreen(

            ) {
                navController.popBackStack()
                navController.navigate(Graph.HOME)
            }

        }
    }

}

sealed class AuthScreen(val route: String) {
    object Login : AuthScreen(route = "LOGIN")
    object SignUp : AuthScreen(route = "SIGN_UP")
    object Tour : AuthScreen(route = "TOUR_SCREEN")
    object Splash : AuthScreen(route = "SPLASH_SCREEN")


}