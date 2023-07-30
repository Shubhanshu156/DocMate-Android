package com.example.docmate.ui.Screens.Doctor

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.docmate.R
import com.example.docmate.Utility.TimeConverter
import com.example.docmate.Utility.Utils
import com.example.docmate.data.models.Response.DocAppointmentItem
import com.example.docmate.data.models.Response.Doctor
import com.example.docmate.ui.Screens.Patient.ProfileScreen.ImageSection
import com.example.docmate.ui.Screens.Patient.ProfileScreen.ShowAnimation
import com.example.docmate.ui.Screens.Patient.Schedule.StatusComposable
import com.example.docmate.ui.theme.AcceptedColor
import com.example.docmate.ui.theme.DocColor
import com.example.docmate.ui.theme.PendingColor
import com.example.docmate.ui.theme.RejectedColor

@Composable
fun DocAppointment(
    onprofile: () -> Unit,
    DocHomeScreenViewModel: DocHomeScreenViewModel = hiltViewModel()
) {
    var isLoading = DocHomeScreenViewModel.isLoading.collectAsState()
    var userProfile = DocHomeScreenViewModel.userProfileState.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .alpha(if (isLoading.value) 0.3f else 1f)
                .padding(30.dp)
        ) {
            if (userProfile != null) {
                Profile(userProfile.value!!) {
                    onprofile()
                }
            }
            AppointmentSchedule()
        }
        if (isLoading.value) {
            ShowAnimation(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun AppointmentSchedule(DocHomeScreenViewModel: DocHomeScreenViewModel = hiltViewModel()) {
    var res = DocHomeScreenViewModel.docappointment.collectAsState().value
    Column(modifier = Modifier.padding(top = 30.dp)) {
        Text(
            "Appointment :",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.SansSerif
        )
        LazyColumn() {
            itemsIndexed(res) { index, item ->
                DoctorAppointmentCard(item)
            }
        }
    }
}

@Composable
fun DoctorAppointmentCard(
    i: DocAppointmentItem,
    DocHomeScreenViewModel: DocHomeScreenViewModel = hiltViewModel()
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 10.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
            Text(
                text = i.patientname,
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
                        maxLines = 1, overflow = TextOverflow.Ellipsis, modifier =
                        if (Utils().isURLValid(i.url)) {
                            Modifier.clickable {
                                context.openUri(i.url)
                            }
                        } else {
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
                var toshow by remember {
                    mutableStateOf(i.status.lowercase() == "pending")
                }
                if (toshow) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        StatusComposable(AcceptedColor, "Accept") {
                            toshow = false
                            DocHomeScreenViewModel.Accept(i.id)
                        }
                        StatusComposable(RejectedColor, "Reject") {
                            toshow = false
                            DocHomeScreenViewModel.Reject(i.id)
                        }
                    }
                }
            }

        }

    }
}


@Composable
fun Profile(doctor: Doctor, onprofile: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        ImageSection(doctor = doctor, modifier = Modifier
            .aspectRatio(1f)
            .border(1.dp, DocColor, CircleShape)
            .clip(CircleShape)
            .clickable {
                onprofile()
            }
            .size(50.dp)
        )

        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = "${doctor.username.toString()}",
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraLight
        )

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.baseline_notifications_24),
            contentDescription = "",
        )
    }
}