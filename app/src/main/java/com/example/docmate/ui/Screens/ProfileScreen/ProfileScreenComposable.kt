package com.example.docmate.ui.Screens.ProfileScreen

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.docmate.R
import com.example.docmate.Utility.TimeConverter
import com.example.docmate.Utility.Utils
import com.example.docmate.data.models.Response.Doctor
import com.example.docmate.data.models.Response.Gender
import com.example.docmate.data.models.ReviewItem
import com.example.docmate.ui.Screens.Home.SearchScreen.formatHourWithAmPm
import com.example.docmate.ui.theme.DocColor
import com.example.docmate.ui.theme.starcolor
import java.util.Calendar
import java.util.Date

@Composable
fun ProfileScreenComposable(
    string: String?,
    profileScreenViewModel: ProfileScreenViewModel = hiltViewModel()
) {
    var date by remember {
        mutableStateOf(0)
    }
    var url by remember {
        mutableStateOf("")
    }
    var month by remember {
        mutableStateOf(0)
    }
    var year by remember {
        mutableStateOf(0)
    }
    var time by remember {
        mutableStateOf(0)
    }
    LaunchedEffect(key1 = true) {
        profileScreenViewModel.getDoctor(string)
        string?.let { profileScreenViewModel.getReviews(it) }
    }
    Box(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 30.dp, horizontal = 15.dp)
        )
        {
            Row() {
                Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.weight(0.3f)) {

                    Icon(Icons.Default.ArrowBack, contentDescription = "")
                }
                Row(modifier = Modifier.weight(0.6f)) {
                    Text(
                        text = "Profile",
                        fontSize = 18.sp,
                        modifier = Modifier
                            .weight(0.5f)
                            .align(Alignment.CenterVertically)
                    )
                }

            }
            Spacer(modifier = Modifier.height(40.dp))
            DoctorProfileCard(
                id = string.toString(),
                doctor = profileScreenViewModel.doctor.value,
                profileScreenViewModel,
                setDay = { d, m, y ->
                    date = d
                    month = m
                    year = y

                },
                selectTime = {
                    time = it
                },
                seturl = {
                    url = it
                }
            )
        }
        var showerror by remember {
            mutableStateOf(false)
        }
        Button(
            onClick = {
                if (!string.isNullOrEmpty() && year != 0 && month != 0 && date != 0 && time != 0 && !url.isNullOrEmpty() && Utils().isGivenDateGreaterThanToday(
                        year,
                        month,
                        date,
                        time,
                        0
                    )
                )
                    string?.let {
                        profileScreenViewModel.bookAppointment(
                            it,
                            year,
                            month,
                            date,
                            time,
                            url
                        )
                    }
                else {
                    showerror = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(top = 80.dp, start = 10.dp, end = 10.dp)
        ) {
            Text("Book")
        }
        if (profileScreenViewModel._appointmentstatus.collectAsState().value) {
            ShowBookedDialog(
                onDismiss = {
                    profileScreenViewModel._appointmentstatus.value = false
                },
                msg = "Your Appointment has been booked Successfully",
                title = "Appointment Booked Successfully"
            )
        }
        if (showerror) {
            ShowBookedDialog(onDismiss = {
                showerror = false
            }, msg = "Please Fill all Details first", title = "Detail Missing")

        }
    }
}

@Composable
fun DoctorProfileCard(
    id: String,
    doctor: Doctor,
    profileScreenViewModel: ProfileScreenViewModel,
    setDay: (date: Int, month: Int, year: Int) -> Unit,
    selectTime: (time: Int) -> Unit,
    seturl: (url: String) -> Unit
) {
    var isselected by remember {
        mutableStateOf(false)
    }
    val mContext = LocalContext.current
    val mYear: Int
    val mMonth: Int
    val mDay: Int

    val mCalendar = Calendar.getInstance()

    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()
    val mDate = remember { mutableStateOf("") }
    val mDatePickerDialog =
        DatePickerDialog(mContext, { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mDayOfMonth/${mMonth + 1}/$mYear"
            setDay(mDayOfMonth, mMonth + 1, mYear)
            isselected = true
            profileScreenViewModel.getAvailableSlots(id, mDayOfMonth, mMonth + 1, mYear)
        }, mYear, mMonth, mDay)
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
            ImageSection(
                doctor = doctor, modifier = Modifier
                    .height(100.dp)
                    .weight(0.5f)
                    .padding(end = 10.dp)
            )

            Column(
                verticalArrangement = Arrangement.Center, modifier = Modifier
                    .weight(0.5f)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = doctor.fullname, fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 18.sp, modifier = Modifier.weight(0.5f)
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.chatbtn),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(5.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)

                    )
                }

                Text(
                    text = doctor.category, fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic,
                    fontSize = 16.sp,
                )
                Text(
                    text = "Rating : ${doctor.rating}", fontStyle = FontStyle.Italic,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 5.dp)
                )
                Text(
                    text = "Experience : ${doctor.age.toString()}",
                    fontStyle = FontStyle.Italic,
                    fontSize = 16.sp,
                )

            }
        }
        Text(
            text = "About", fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(top = 10.dp, bottom = 5.dp)
        )
        Text(
            text = doctor.about.toString(), fontWeight = FontWeight.Thin,
            color = Color.Black, modifier = Modifier.padding(top = 3.dp)
        )
        Row(modifier = Modifier.padding(vertical = 10.dp)) {
            Text(
                "Available Hour ", fontWeight = FontWeight.Bold,
                color = Color.Black,

                )

            Text(
                text = "${
                    doctor.working_hour_start?.let { formatHourWithAmPm(it) }.toString()
                } - ${doctor.working_hour_end?.let { formatHourWithAmPm(it) }.toString()}"
            )

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                "Pick Date ", fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.weight(0.5f)
            )

            Button(
                onClick = { mDatePickerDialog.show() },
                elevation = ButtonDefaults.buttonElevation(20.dp),
                modifier = Modifier.weight(0.5f)
            ) {

                if (isselected) {
                    Text(mDate.value)
                    Icon(
                        painter = painterResource(id = R.drawable.edit_icon),
                        contentDescription = "",
                        modifier = Modifier.padding(horizontal = 5.dp)
                    )
                } else {
                    Text("Pick a Date")
                }

            }

        }

        var availableSlots = profileScreenViewModel.availableSlots.collectAsState().value
        if (mDate.value != "") {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 15.dp)
            ) {

                Text(
                    text = "Available Time Slots",
                    modifier = Modifier
                        .weight(0.7f)
                )

                if (profileScreenViewModel.isloading.collectAsState().value) {
                    ShowAnimation(
                        modifier = Modifier
                            .height(30.dp)
                            .weight(0.3f)
                    )
                }
            }
        }

        var selectedbutton by remember {
            mutableStateOf(-1)
        }

        if (availableSlots != null) {
            if (availableSlots.isNotEmpty()) {
                LazyRow() {
                    if (availableSlots != null) {
                        items(availableSlots.size) { index ->
                            val start = availableSlots?.get(index)?.first
                            val end = availableSlots?.get(index)?.second
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = if (selectedbutton == start) DocColor else Color.LightGray,
                                    contentColor = if (selectedbutton == start) Color.White else Color.Black,
                                ),
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        if (start != null) {
                                            selectedbutton = start
                                            selectTime(start)
                                        }
                                    }
                            ) {
                                Text(
                                    "${start?.let { TimeConverter().convertTo12HourFormat(it) }}-${
                                        end?.let {
                                            TimeConverter().convertTo12HourFormat(
                                                it
                                            )
                                        }
                                    }",
                                    modifier = Modifier.padding(
                                        vertical = 10.dp,
                                        horizontal = 10.dp
                                    )
                                )

                            }
                        }
                    }
                }
            }
        }



        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                "Add Meet Url", fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.weight(0.5f)
            )
            var url by remember {
                mutableStateOf("")
            }
            var showDialog by remember { mutableStateOf(false) }
            Button(
                onClick = { showDialog = true },
                elevation = ButtonDefaults.buttonElevation(20.dp),
                modifier = Modifier.weight(0.5f)
            ) {
                Text("Add Url")
                if (showDialog) {
                    UrlInputDialog(url = url, showDialog, onUpdateUrl = { updatedUrl ->
                        url = updatedUrl
                        seturl(url)
                        showDialog = false
                    },
                        ondismiss = {
                            showDialog = false
                        })

                }

            }

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                "Reviews", fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.weight(0.5f)
            )
            var rating by remember {
                mutableStateOf(0)
            }
            var msg by remember {
                mutableStateOf("")
            }
            var showDialog by remember { mutableStateOf(false) }
            Button(
                onClick = { showDialog = true },
                elevation = ButtonDefaults.buttonElevation(20.dp),
                modifier = Modifier.weight(0.5f)
            ) {
                Text("Add Review")
                if (showDialog) {
                    RatingDialog(showDialog, onUpdateUrl = { r, m ->
                        rating = r
                        msg = m
                        showDialog = false
                        profileScreenViewModel.addreview(id, message = msg, star = rating)
                    },
                        ondismiss = {
                            showDialog = false
                        })

                }

            }


        }
        var showreviews by remember {
            mutableStateOf(false)
        }
        Text(
            text = if (showreviews) {
                "Hide Reviews"
            } else {
                "Show Reviews"
            }, modifier = Modifier.clickable { showreviews = !showreviews })
        if (showreviews) {
            ShowReviews(profileScreenViewModel.reviews.collectAsState().value)
        } else {
            ShowReviews(
                profileScreenViewModel.reviews.collectAsState().value.subList(
                    0,
                    profileScreenViewModel.reviews.collectAsState().value.size.coerceAtMost(3)
                )
            )
        }
    }


}

@Composable
fun ShowReviews(lst: List<ReviewItem>) {
    if (lst.isNotEmpty()) {
        lst.forEach {
            ReviewUnit(it)
            Divider(color = Color.Gray, thickness = 1.dp)
        }
    }
}

@Composable
fun ReviewUnit(itm: ReviewItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                Icons.Default.Person,
                contentDescription = "User Icon",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "DocMate Patient",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )
            Row {
                repeat(5) { index ->
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "",
                        tint = if (index < itm.patientstar.toInt()) starcolor else Color.LightGray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = itm.patientmessage,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.ExtraLight
        )
    }
}

@Composable
fun ShowBookedDialog(title: String, msg: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = msg)
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

@Composable
fun RatingDialog(showDialog: Boolean, onUpdateUrl: (Int, String) -> Unit, ondismiss: () -> Unit) {
    var rating by remember {
        mutableStateOf(1)
    }
    var msg by remember {
        mutableStateOf("")
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { ondismiss() },
            title = {
                Text(text = "Give Your Reviews")
            },
            text = {
                Column() {
                    Text("Rate Our Doctor out of 5")
                    Row(modifier = Modifier.padding(vertical = 10.dp)) {
                        for (i in 1..5) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "",
                                tint = if (i <= rating) starcolor else Color.LightGray,
                                modifier = Modifier
                                    .size(45.dp)
                                    .clickable { rating = i }
                            )
                        }

                    }
                    Text("Add What you think!!")
                    TextField(
                        value = msg,
                        onValueChange = {
                            msg = it
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(2.dp)

                    )

                }


            },
            confirmButton = {
                Button(
                    onClick = {
                        if (msg.isNotEmpty()) {
                            onUpdateUrl(rating, msg)
                        }
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(
                    onClick = {

                        ondismiss()
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }


}


@Composable
fun ImageSection(doctor: Doctor, modifier: Modifier = Modifier) {
    if (doctor.url != null) {
        AsyncImage(
            model = doctor.url,
            contentDescription = null,
            modifier = modifier
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Fit
        )

    } else {
        if (doctor.gender == null || doctor.gender.uppercase() == Gender.MALE.name) {
            Image(
                painter = painterResource(id = R.drawable.maledoctoravtar),
                contentDescription = "",
                modifier = modifier
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Fit
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.femaledoctoravtar),
                contentDescription = "",
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UrlInputDialog(
    url: String,
    showDialog: Boolean,
    onUpdateUrl: (String) -> Unit,
    ondismiss: () -> Unit
) {

    var newUrl by remember { mutableStateOf(url) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { ondismiss() },
            title = {
                Text(text = "Enter Url")
            },
            text = {
                TextField(
                    value = newUrl,
                    onValueChange = {
                        newUrl = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(8.dp)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateUrl(newUrl)
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        ondismiss()
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }


}

@Composable
fun ShowAnimation(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_anim))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    Column(modifier = modifier) {


        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .fillMaxSize()
                .scale(5f, 5f)
        )
    }


}
