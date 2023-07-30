package com.example.docmate.ui.graphs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.docmate.ui.BottomBarScreen
import com.example.docmate.ui.Screens.Patient.Home.HomeScreenComposable
import com.example.docmate.ui.Screens.Patient.Home.SearchScreen.SearchScreenComposable
import com.example.docmate.ui.Screens.Patient.ProfileScreen.ProfileScreenComposable
import com.example.docmate.ui.Screens.Patient.Schedule.ScheduleComposable
import com.example.docmate.ui.Screens.Patient.SettingScreen.SettingScreen
import com.example.docmate.ui.theme.DocColor

@Composable
fun HomeNavGraph(navController: NavHostController, modifier: Modifier) {
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(route = BottomBarScreen.Home.route) {
            HomeScreenComposable(onSearch = {
                navController.navigate(Graph.HOME_DETAILS)
            })
        }
        composable(route = BottomBarScreen.Profile.route) {
            SettingScreen(){
                navController.navigate(BottomBarScreen.Home.route)
            }
        }
        composable(route = BottomBarScreen.Schedule.route) {

            ScheduleComposable(
            )
        }
        composable(route = BottomBarScreen.Chat.route) {
            ScreenContent(
                name = BottomBarScreen.Chat.route,
                onClick = { }
            )
        }
        detailsNavGraph(navController = navController)
    }
}

fun NavGraphBuilder.detailsNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.HOME_DETAILS,
        startDestination = Home_DetailsScreen.SearchScreen.route
    ) {
        composable(route = Home_DetailsScreen.SearchScreen.route) {
            SearchScreenComposable(onprofile = { id ->
                val routeWithUserId = PROFILE_SCREEN.Profile_Screen.route.replace("{userId}", id)
                navController.navigate(routeWithUserId)
            })
        }
        ProfileNavGraph(navController)
    }
}

fun NavGraphBuilder.ProfileNavGraph(navController: NavHostController) {
    navigation(
        route = Home_DetailsScreen.ProfileSection.route,
        startDestination = PROFILE_SCREEN.Profile_Screen.route
    ) {
        composable(
            route = PROFILE_SCREEN.Profile_Screen.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { it ->
            ProfileScreenComposable(it.arguments?.getString("userId"))
        }

    }

}


@Composable
fun ScreenContent(name: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.clickable { onClick() },
            text = name,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController = rememberNavController()) {
    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) { padding ->
        HomeNavGraph(navController = navController, modifier = Modifier.padding(padding))
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Schedule,
        BottomBarScreen.Chat,
        BottomBarScreen.Profile,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.any { it.route == currentDestination?.route }
    if (bottomBarDestination) {
        BottomNavigation(
            modifier = Modifier.height(60.dp),
            backgroundColor = Color.White,
        ) {
            screens.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController,
) {
    val isSelected = remember { mutableStateOf(false) }
    LaunchedEffect((currentDestination?.hierarchy?.any { it.route == screen.route } == true)) {
        isSelected.value = (currentDestination?.hierarchy?.any { it.route == screen.route } == true)
    }

    BottomNavigationItem(
        icon = {
            Row(horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable {
                        isSelected.value = !isSelected.value
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                        }
                    }
                    .fillMaxSize()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected.value) DocColor else Color.White)

            ) {
                Icon(
                    painter = painterResource(id = screen.icon),
                    contentDescription = "",
                    tint = if (isSelected.value) Color.White else DocColor
                )
            }
        },
        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
        unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
            }
        },
        selectedContentColor = DocColor
    )
}


sealed class Home_DetailsScreen(val route: String) {
    object SearchScreen : Home_DetailsScreen(route = "SEARCH_SCREEN")
    object ProfileSection : Home_DetailsScreen(route = "PROFILE_SECTION")

}

sealed class PROFILE_SCREEN(val route: String) {
    object Profile_Screen : PROFILE_SCREEN(route = "profile/{userId}")
}