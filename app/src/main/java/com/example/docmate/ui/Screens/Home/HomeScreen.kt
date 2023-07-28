package com.example.docmate.ui.Screens.Home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.docmate.R
import com.example.docmate.data.models.Response.Doctor
import com.example.docmate.data.models.Response.Gender
import com.example.docmate.ui.theme.DocColor
import com.example.docmate.ui.theme.DocColorDark
import com.example.docmate.ui.theme.Purple40
import kotlinx.coroutines.launch

@Composable
fun HomeScreenComposable(
    onSearch: () -> Unit,
    HomeScreenViewmodel: HomeScreenViewModel = hiltViewModel()
) {

    Column(modifier = Modifier.padding(horizontal = 10.dp)) {

        Spacer(modifier = Modifier.height(20.dp))
        Profile()
        Spacer(modifier = Modifier.height(20.dp))
        SearchFilter("Search Doctors", onSearch = onSearch)
        Spacer(
            modifier = Modifier
                .height(20.dp)
                .background(Color.Black)
        )
        CarouselSection()
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        CategorySection(HomeScreenViewmodel)
        TopDoctorText()
        topDoctorsSection(HomeScreenViewmodel)
        PermissionScreen()
    }
}


@Composable
fun TopDoctorText() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
    ) {
        Text(
            text = "Top Doctors",
            fontSize = 16.sp,
            modifier = Modifier.align(Alignment.CenterStart)
        )
        Text(text = "See All", modifier = Modifier.align(Alignment.CenterEnd))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun topDoctorsSection(homeScreenViewModel: HomeScreenViewModel) {
    var lst = homeScreenViewModel.topDoctors
    if (!lst.isEmpty()) {
        val pagerState = rememberPagerState(pageCount = {
            lst.size
        })
        HorizontalPager(state = pagerState) { page ->
            DoctoCard(doctor = lst[page])
        }
    }

}

@Composable
fun CategorySection(HomeScreenViewmodel: HomeScreenViewModel) {
    val categoryList = HomeScreenViewmodel.category

    LazyRow {
        items(categoryList.size) { category ->
            Box(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .padding(horizontal = 4.dp)
            ) {
                CategoryItem(categoryname = categoryList[category].category)
            }
        }
    }
}

@Composable
fun CategoryItem(categoryname: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(DocColor)
            .padding(10.dp)
    ) {
        Text(
            text = categoryname,
            color = Color.White,
            modifier = Modifier.padding(10.dp)
        )
    }
}


@Composable
fun DotIndicator(isSelected: Boolean, onclick: () -> Unit) {
    val color = if (isSelected) DocColor else Color.LightGray
    val size = if (isSelected) 14.dp else 8.dp

    Box(
        modifier = Modifier
            .clickable { onclick() }
            .size(size)
            .background(color = color, shape = CircleShape)
            .padding(4.dp)

    )
    Spacer(modifier = Modifier.width(10.dp))
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CarouselSection() {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState { 3 }

    Column(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val item = getItemForPage(page)
            CarouselItem(item)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(pagerState.pageCount) { index ->
                DotIndicator(isSelected = index == pagerState.currentPage) {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            }
        }
    }
}


data class CarouselItemModel(
    val imageResId: Int,
    val headline: String,
    val description: String
)

fun getItemForPage(page: Int): CarouselItemModel {
    return when (page) {
        0 -> CarouselItemModel(
            imageResId = R.drawable.doc_team,
            headline = "Experienced Doctors",
            description = "Our team of experienced doctors is dedicated to providing high-quality healthcare services."
        )

        1 -> CarouselItemModel(
            imageResId = R.drawable.care,
            headline = "Compassionate Care",
            description = "We prioritize compassion in patient care and strive to make your visit comfortable."
        )

        2 -> CarouselItemModel(
            imageResId = R.drawable.specialist,
            headline = "Specialist Services",
            description = "We offer a wide range of specialist services to cater to the unique needs of each patient."
        )

        else -> CarouselItemModel(
            imageResId = R.drawable.username,
            headline = "Welcome",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum vitae metus ac dolor blandit ultrices."
        )
    }
}

@Composable
fun CarouselItem(item: CarouselItemModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(16.dp))
            .background(Purple40)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.weight(0.5f)) {
                Text(
                    text = item.headline,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = item.description,
                    style = TextStyle(fontSize = 16.sp)
                )
            }

            Image(
                painter = painterResource(item.imageResId),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .weight(0.5f),
                contentScale = ContentScale.FillBounds
            )


        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchFilter(hint: String = "", onSearch: () -> Unit) {

    val keyboardController = LocalSoftwareKeyboardController.current

    var textValue by remember { mutableStateOf("") }

    var isHintDisplayed by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        BasicTextField(
            value = textValue,
            onValueChange = {
                textValue = it.take(20) // limit the text to 20 characters
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide() // hide the keyboard when "Done" is pressed
            }),
            modifier = Modifier
                .height(58.dp)
                .border(BorderStroke(1.dp, Color.DarkGray), shape = RoundedCornerShape(13.dp))
                .fillMaxWidth()
                .clickable {

                    onSearch()
                }, enabled = false,
            textStyle = TextStyle(
                color = Color.Black, fontSize = 30.sp, textAlign = TextAlign.Justify
            )
        )

        if (textValue.isEmpty()) {
            Row(
                modifier = Modifier.align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .size(24.dp)
                )
                Text(
                    text = "Search Doctors",

                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 15.dp)
                )
            }
        }
    }

}


@Composable
fun Profile() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.male_doctor),
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .border(1.dp, DocColor, CircleShape)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(text = "Username123dsf", fontSize = 18.sp, fontWeight = FontWeight.ExtraLight)

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.baseline_notifications_24),
            contentDescription = "",
        )
    }
}

@Composable
fun DoctoCard(doctor: Doctor) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ImageSection(doctor)
            Column(modifier = Modifier.padding(10.dp)) {
                Text(text = doctor.fullname)
                Text(text = "Bookings :  ${doctor.PrevSession}")
                Text(text = "Category : ${doctor.category}")
                Text(text = "Rating ${doctor.rating} ⭐")
            }


        }
    }
}

@Composable
fun ImageSection(doctor: Doctor) {
    if (doctor.url != null) {
        AsyncImage(
            model = doctor.url,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.FillBounds
        )

    } else {
        if (doctor.gender == null || doctor.gender.uppercase() == Gender.MALE.name) {
            Image(
                painter = painterResource(id = R.drawable.maledoctoravtar),
                contentDescription = "",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.FillBounds
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.femaledoctoravtar),
                contentDescription = "",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.FillBounds
            )
        }
    }
}

@Composable
fun PermissionScreen() {
    val context = LocalContext.current
    var hasNotificationPermission = remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermission.value = isGranted
    }

    var count = remember { mutableStateOf(0) }
    SideEffect {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {

                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

}

