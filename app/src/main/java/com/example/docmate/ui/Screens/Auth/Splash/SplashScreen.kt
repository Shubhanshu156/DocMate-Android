package com.example.docmate.ui.Screens.Auth.Splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.docmate.R
import com.example.docmate.ui.theme.DocColorDark
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSignin: () -> Unit = {},
    SplashScreenViewModel: SplashScreenViewModel = hiltViewModel(),
    onDocHome: () -> Unit,
    onuserHome: () -> Unit
) {
    LaunchedEffect(true) {
        val user: String? = SplashScreenViewModel.userType.value
        if (SplashScreenViewModel.isFirstTime.value == true) {
            onSignin()
        } else if (SplashScreenViewModel.isFirstTime.value == false) {
            if (user != null) {
                delay(1000)
                if (user.lowercase() == "doctor") {

                    onDocHome()
                } else {
                    onuserHome()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color.White
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.stethoscope_solid),
            contentDescription = "",
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "DocMate${SplashScreenViewModel.isFirstTime.value}}",
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = DocColorDark
        )
        Text(
            text = "A Perfect Mate for Your Health",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Light,
            fontStyle = FontStyle.Italic
        )
    }

    }
