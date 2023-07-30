package com.example.docmate.ui

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import com.example.docmate.R
sealed class BottomBarScreen(
    val route: String,
    val title: String,
@DrawableRes    val icon: Int
) {
    object Home : BottomBarScreen(
        route = "HOME",
        title = "HOME",
        icon = R.drawable.home
    )

    object Profile : BottomBarScreen(
        route = "PROFILE",
        title = "PROFILE",
        icon = R.drawable.username
    )
    object Schedule : BottomBarScreen(
        route = "SCHEDULE",
        title = "Schedule",
        icon=R.drawable.baseline_calendar_today_24
    )
//    object Chat : BottomBarScreen(
//        route = "CHAT",
//        title = "Chat",
//        icon = R.drawable.baseline_chat_24
//    )


}