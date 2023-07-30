package com.example.docmate.ui.Screens.Patient.Home.SearchScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isUnspecified
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.docmate.R
import com.example.docmate.data.models.Response.Doctor
import com.example.docmate.data.models.Response.Gender
import com.example.docmate.ui.Screens.Patient.ProfileScreen.ImageSection
import com.example.docmate.ui.theme.DocColor
import kotlinx.coroutines.launch
import com.example.docmate.ui.Screens.Patient.ProfileScreen.ImageSection
@OptIn(ExperimentalMaterialApi::class)
//@Preview(showSystemUi = true)
@Composable
fun SearchScreenComposable(onprofile: (String) -> Unit,SearchScreenViewModel: SearchScreenViewModel = hiltViewModel()) {
    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    var selecteddoctor by remember {
        mutableStateOf(Doctor())
    }
    var categoryindex by remember {
        mutableStateOf(-1)
    }
    var isvisible by remember {
        mutableStateOf(false)
    }
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    val scope = rememberCoroutineScope()
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            ShowCategoryScreen(SearchScreenViewModel =SearchScreenViewModel,param = { newind ->
                categoryindex = newind
                scope.launch {
                    SearchScreenViewModel.Search("", categoryindex)
                }

            })
        },
        sheetPeekHeight = 0.dp
    ) {
        Box(contentAlignment = Alignment.Center) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        isvisible = false
                    }
                    .graphicsLayer {
                        if (isvisible) {
                            this.alpha = 0.2f
                        } else {
                            this.alpha = 1f
                        }
                    }
                    .padding(20.dp)


            ) {
                TitleSection()
                Spacer(modifier = Modifier.height(20.dp))
                SearchBar(function = {
                    scope.launch {
                        sheetState.expand()
                    }
                }, categoryindex)
                ResultSection(isvisible = isvisible) { doctor: Doctor ->
                    selecteddoctor = doctor
                    onprofile(doctor.id)
                }

            }


        }

    }
}


@Composable
fun ShowCategoryScreen(
    param: (Int) -> Unit,
    SearchScreenViewModel: SearchScreenViewModel
) {
    var selectedcategory by remember {
        mutableStateOf(-1)
    }
    val categoryList = SearchScreenViewModel.categorylst
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .padding(20.dp)

    ) {
        Spacer(
            modifier = Modifier
                .height(7.dp)
                .padding(horizontal = 60.dp)
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(4.dp)
                )
                .background(Color.LightGray)

        )
        Text(
            "Choose Category",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(top = 10.dp)
        )

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            content = {
                items(categoryList.size) { category ->
                    CategoryComponent(
                        categoryList[category].category,
                        isSelected = selectedcategory == category
                    ) {
                        selectedcategory = if (selectedcategory == category) {
                            -1
                        } else {
                            category
                        }
                        param(selectedcategory)
                    }
                }
            },
            verticalItemSpacing = 10.dp,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        )
    }

}

@Composable
fun CategoryComponent(s: String, isSelected: Boolean, onclick: () -> Unit) {

    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .wrapContentHeight()
            .clip(RoundedCornerShape(5.dp))
            .background(DocColor)
            .clickable {
                onclick()
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = isSelected, onClick = { onclick() })
        AutoResizedText(
            text = s,
            color = Color.White,
            modifier = Modifier.padding(end = 3.dp)
        )
    }
}



fun formatHourWithAmPm(hour: Int): String {
    if (hour < 0 || hour > 23) {
        throw IllegalArgumentException("Invalid hour. Hour should be between 0 and 23.")
    }

    val formattedHour: Int
    val period: String

    if (hour == 0) {
        formattedHour = 12
        period = "AM"
    } else if (hour == 12) {
        formattedHour = 12
        period = "PM"
    } else if (hour > 12) {
        formattedHour = hour - 12
        period = "PM"
    } else {
        formattedHour = hour
        period = "AM"
    }

    return "$formattedHour $period"
}


@Composable
fun ResultSection(
    isvisible: Boolean,
    SearchScreenViewModel: SearchScreenViewModel = hiltViewModel(),
    onbook: (doctor: Doctor) -> Unit
) {
    var res = SearchScreenViewModel.lst
    Column() {
        LazyColumn() {
            itemsIndexed(res) { index, item ->
                DoctorCard(item, onbook, isvisible)
            }
        }
    }

}

@Composable
fun DoctorCard(item: Doctor, onbook: (doctor: Doctor) -> Unit, isvisible: Boolean) {


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable {
                onbook(item)
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(5.dp)
        ) {
            ImageSection(doctor = item,modifier=Modifier.weight(0.3f))
            Column(modifier = Modifier.padding((10.dp)).weight(0.7f)) {
                Text(
                    text = item.fullname,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 18.sp
                )
                Text(
                    text = item.category.toString(),
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                Text(
                    text = item.about.toString(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Normal,
                    lineHeight = 15.sp,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "${item.rating}⭐")
                    Button(
                        onClick = { onbook(item) }, modifier = Modifier
                            .wrapContentSize()
                            .padding(0.dp)
                    ) {
                        Text(text = "Book", color = Color.White)

                    }

                }
            }
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
    function: () -> Unit,
    categoryindex: Int,
    SearchScreenViewModel: SearchScreenViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    var textValue by remember { mutableStateOf("") }

    var isHintDisplayed by remember { mutableStateOf(true) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(
            onClick = { function() },
            modifier = Modifier
                .wrapContentHeight()
                .border(
                    BorderStroke(1.dp, Color.Gray),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.filtericon),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .padding(2.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 10.dp)


        ) {

            BasicTextField(
                value = textValue,
                onValueChange = {
                    textValue = it.take(20) // limit the text to 20 characters
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    scope.launch {
                        SearchScreenViewModel.Search(textValue, categoryindex)
                        textValue = ""
                    }
                    keyboardController?.hide() // hide the keyboard when "Done" is pressed
                }),
                modifier = Modifier
                    .height(58.dp)
                    .border(BorderStroke(1.dp, Color.DarkGray), shape = RoundedCornerShape(13.dp))
                    .fillMaxWidth()
                    .padding(10.dp),
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


}

@Composable
fun TitleSection() {
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.weight(0.3f)) {

            Icon(Icons.Default.ArrowBack, contentDescription = "")
        }
        Row(modifier = Modifier.weight(0.6f)) {
            Text(
                text = "All Doctors",
                fontSize = 18.sp,
                modifier = Modifier
                    .weight(0.5f)
                    .align(Alignment.CenterVertically)
            )
        }

    }
}

//@Composable
//fun ImageSection(doctor: Doctor, modifier: Modifier = Modifier) {
//    if (doctor.url != null) {
//        AsyncImage(
//            model = doctor.url,
//            contentDescription = null,
//            modifier = modifier
//                .size(100.dp)
//                .clip(RoundedCornerShape(10.dp)),
//            contentScale = ContentScale.FillBounds
//        )
//
//    } else {
//        if (doctor.gender == null || doctor.gender.uppercase() == Gender.MALE.name) {
//            Image(
//                painter = painterResource(id = R.drawable.maledoctoravtar),
//                contentDescription = "",
//                modifier = modifier
//                    .size(100.dp)
//                    .clip(RoundedCornerShape(10.dp)),
//                contentScale = ContentScale.FillBounds
//            )
//        } else {
//            Image(
//                painter = painterResource(id = R.drawable.femaledoctoravtar),
//                contentDescription = "",
//                modifier = Modifier
//                    .size(100.dp)
//                    .clip(RoundedCornerShape(10.dp)),
//                contentScale = ContentScale.FillBounds
//            )
//        }
//    }
//}


@Composable
fun AutoResizedText(
    text: String,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    modifier: Modifier = Modifier,
    color: Color = style.color
) {
    var resizedTextStyle by remember {
        mutableStateOf(style)
    }
    var shouldDraw by remember {
        mutableStateOf(false)
    }

    val defaultFontSize = MaterialTheme.typography.bodyMedium.fontSize

    Text(
        text = text,
        color = color,
        modifier = modifier.drawWithContent {
            if (shouldDraw) {
                drawContent()
            }
        },
        softWrap = false,
        style = resizedTextStyle,
        onTextLayout = { result ->
            if (result.didOverflowWidth) {
                if (style.fontSize.isUnspecified) {
                    resizedTextStyle = resizedTextStyle.copy(
                        fontSize = defaultFontSize
                    )
                }
                resizedTextStyle = resizedTextStyle.copy(
                    fontSize = resizedTextStyle.fontSize * 0.95
                )
            } else {
                shouldDraw = true
            }
        }
    )
}



