package com.example.docmate.ui.Screens.Schedule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.util.Util
import com.example.docmate.Utility.TimeConverter
import com.example.docmate.Utility.Utils
import com.example.docmate.data.models.Appointmentlist
import com.example.docmate.ui.theme.AcceptedColor
import com.example.docmate.ui.theme.PendingColor
import com.example.docmate.ui.theme.RejectedColor

@Composable
fun ScheduleComposable(scheduleViewModel: ScheduleViewModel = hiltViewModel()) {
    var appointments = scheduleViewModel.userAppointments.collectAsState().value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Row() {
            Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.weight(0.3f)) {

                Icon(Icons.Default.ArrowBack, contentDescription = "")
            }
            Row(modifier = Modifier.weight(0.6f)) {
                Text(
                    text = "Appointments",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .weight(0.5f)
                        .align(Alignment.CenterVertically)
                )
            }

        }
        LazyColumn(modifier = Modifier.padding(top = 20.dp)) {
            items(appointments.size) {
                AppointmentCard(appointments[it])
            }
        }
    }


}

@Composable
fun AppointmentCard(i: Appointmentlist) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 10.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
            Text(
                text = i.doctorname,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )
            Column(modifier = Modifier.padding(start = 10.dp)) {

                Row(modifier = Modifier.padding(vertical = 5.dp)) {
                    Text(
                        "Time :",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                    Text(
                        "${TimeConverter().convertTo12HourFormat(i.time)}-${
                            TimeConverter().convertTo12HourFormat(
                                i.time + 1
                            )
                        }",
                        fontWeight = FontWeight.Thin,
                        fontSize = 15.sp,
                        fontStyle = FontStyle.Italic,
                        color = Color.Black
                    )
                }
                Row(modifier = Modifier.padding(vertical = 5.dp)) {
                    Text(
                        "Date :",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                    Text(
                        "${i.date}/${i.month}/${i.year}",
                        fontWeight = FontWeight.Thin,
                        fontSize = 15.sp,
                        fontStyle = FontStyle.Italic,
                        color = Color.Black

                    )
                }
                Row(modifier = Modifier.padding(vertical = 5.dp)) {
                    val context = LocalUriHandler.current

                    Text(
                        "Meeting Url :",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                    Text(
                        i.url,
                        fontWeight = FontWeight.Thin,
                        fontSize = 15.sp,
                        fontStyle = FontStyle.Italic,
                        color = Color.Black,
                        maxLines = 1
                        ,overflow = TextOverflow.Ellipsis
                        ,modifier =
                            if (Utils().isURLValid(i.url)) {
                                Modifier.clickable {
                                    context.openUri(i.url)
                                }
                            }
                            else {
                                Modifier

                        }
                    )
                }
                Row(modifier = Modifier.padding(vertical = 5.dp)) {
                    Text(
                        "Status :",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = Color.Black,
                    )
                    when (i.status) {
                        "ACCEPTED" -> StatusComposable(AcceptedColor, "Accepted")
                        "REJECTED" -> StatusComposable(RejectedColor, "Rejected")
                        "PENDING" -> StatusComposable(PendingColor, "Pending")
                    }

                }
            }

        }

    }
}

@Composable
fun StatusComposable(acceptedColor: Color, s: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = acceptedColor
        )
    ) {
        Text(s, color = Color.White, modifier = Modifier.padding(horizontal = 4.dp))
    }

}

