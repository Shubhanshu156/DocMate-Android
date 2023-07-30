package com.example.docmate.ui.Screens.Patient.Tour

import android.icu.text.CaseMap
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.docmate.R
import com.example.docmate.data.models.TourItem
import com.example.docmate.ui.theme.DocColor
import com.example.docmate.ui.theme.DocColorDark
import kotlin.math.min

@Composable
fun TourScreen(function: () -> Unit ) {
    var index by remember {
        mutableStateOf(0)
    }
    var txt by remember {
        mutableStateOf("Next")
    }
    LaunchedEffect(index) {
        val text = if (index == 0 || index == 1) {
            "Next"
        } else {
            "Let's Start"
        }
    }
    val list = listOf(
        TourItem(
            title = "Find the Best Doctors Worldwide",
            description = "Discover and connect with highly skilled doctors from around the globe using our app.",
            image = R.drawable.global_doctors
        ),
        TourItem(
            title = "Schedule Appointments with Top Specialists",
            description = "Easily book appointments with expert doctors in various specialties, backed by positive ratings and reviews.",
            image = R.drawable.specialist
        ),
        TourItem(
            title = "Convenient Video Consultations",
            description = "Access quality healthcare from the comfort of your own home by scheduling video call appointments with doctors.",
            image = R.drawable.video_consultation
        )
    )
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            ChangeableContent(index = index, list = list)
            Spacer(modifier = Modifier.height(10.dp))
        }
        Box(
            modifier = Modifier
                .fillMaxSize()

        ) {
            Image(
                painter = painterResource(id = list[index].image),
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(1f)
            )
            Button(
                onClick = {
                    index = min(index + 1, 2)
                    if (index == 2) {
                        txt = "Let's Start"
                    } else {
                        txt = "Next"
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = DocColor), modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 20.dp, bottom = 20.dp)
                    .wrapContentHeight()

            ) {
                Text(text = txt)
            }
            PageIndicator(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 20.dp, start = 20.dp), index
            ){
                index=it
            }
        }
    }


}

@Composable
fun PageIndicator(modifier: Modifier, currentPage: Int,onclick:(index:Int)->Unit) {
    val dotColor = animateColorAsState(
        targetValue = if (currentPage == 0) DocColor else Color.LightGray
    ).value

    Row(modifier = modifier.padding(16.dp)) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(dotColor)
                .clickable {
                    onclick(0)
                }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(if (currentPage == 1) DocColor else Color.LightGray)
                .clickable {
                    onclick(1)
                }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(if (currentPage == 2) DocColor else Color.LightGray)
                .clickable {
                    onclick(2)
                }
        )
    }
}


@Composable
fun ChangeableContent(index: Int, list: List<TourItem>, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 20.dp)
    ) {
        Title(list[index].title)
        Spacer(modifier = Modifier.height(10.dp))
        Description(list[index].description)


    }

}

@Composable
fun Description(description: String) {
    Text(
        text = description,
        fontSize = 16.sp,
        color = Color.Gray, fontWeight = FontWeight.Light, textAlign = TextAlign.Center
    )
}


@Composable
fun Title(title: String) {
    Text(
        text = title,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

}

@Composable
fun HeadScreen(modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.stethoscope_solid),
            contentDescription = "App logo",
            modifier = Modifier.size(50.dp)
        )
        Spacer(Modifier.height(10.dp))
        Text("DocMate", color = DocColorDark, fontSize = 16.sp)


    }

}
