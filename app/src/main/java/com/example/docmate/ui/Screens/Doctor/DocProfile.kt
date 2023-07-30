package com.example.docmate.ui.Screens.Doctor

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.docmate.Utility.Status
import com.example.docmate.Utility.Utils
import com.example.docmate.data.models.Response.CategoryResponse
import com.example.docmate.ui.Screens.Patient.Home.SearchScreen.CategoryComponent
import com.example.docmate.ui.Screens.Patient.ProfileScreen.ShowAnimation
import com.example.docmate.ui.Screens.Patient.SettingScreen.Age
import com.example.docmate.ui.Screens.Patient.SettingScreen.GenderComposable
import com.example.docmate.ui.Screens.Patient.SettingScreen.ShowProfileImage
import com.example.docmate.ui.Screens.Patient.SettingScreen.ShowSuccessDialog
import com.example.docmate.ui.Screens.Patient.SettingScreen.UserName
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DocProfile(DocProfileViewModel: DocProfileViewModel = hiltViewModel()) {
    val userProfile by DocProfileViewModel.userProfileState.collectAsState()
    var isSuccess = DocProfileViewModel.ProfileStatus.collectAsState()
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val singlePhotoPickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri -> selectedImageUri = uri })
    val scope = rememberCoroutineScope()
    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    var isdialogvisible by remember {
        mutableStateOf(true)
    }
    if (isSuccess.value.name == Status.Success.name && isdialogvisible) {
        ShowSuccessDialog(title = "Profile Updated",
            msg = "Your Profile has been Updated Successfully",
            onDismiss = {
                isdialogvisible = false
            })
    }
    if (userProfile != null) {

        var fullName by remember {
            mutableStateOf(userProfile!!.fullname)
        }
        var age by remember {
            mutableStateOf(userProfile!!.age)
        }
        var gender by remember {
            mutableStateOf(userProfile!!.gender)
        }
        var about by remember {
            mutableStateOf(userProfile!!.about)
        }
        var workingHourEnd by remember {
            mutableStateOf(userProfile!!.working_hour_end)
        }
        var workingHourStart by remember {
            mutableStateOf(userProfile!!.working_hour_start)
        }
        val context = LocalContext.current
        var category by remember {
            mutableStateOf(userProfile!!.category)
        }
        val categorylst = DocProfileViewModel.categorylst.collectAsState()
        val scaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = sheetState
        )
        BottomSheetScaffold(
            scaffoldState = scaffoldState, sheetContent = {
                ShowCategories(
                    param = { category = categorylst.value[it].category },
                    categorylist = categorylst.value
                )
            }, sheetPeekHeight = 0.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 30.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(if (isSuccess.value == Status.Loading) 0.3f else 1f)
                ) {
                    val imageUri = selectedImageUri ?: userProfile?.url

                    ShowProfileImage(imageUri, gender = userProfile?.gender, onclick = {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }, onsave = {
                        val data = (selectedImageUri?.let { Utils().upload(context, it) })
                        DocProfileViewModel.savedetails(
                            name = fullName,
                            category = category,
                            age = age,
                            gender = gender,
                            about = about,
                            workinghourstart = workingHourStart.toString(),
                            workinghourend = workingHourEnd.toString(),
                            profileurl = data
                        )
                    }, onDismiss = {})

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                            .padding(40.dp)
                    ) {
                        item {
                            UserName(username = userProfile!!.username)
                            NameComposable(fullName) { fullName = it }
                            Age(age.toString()) { age = it }
                            AboutComposable(about) { about = it }
                            CategoryCompose(category.toString()) {
                                scope.launch {
                                    sheetState.expand()
                                }
                            }
                            GenderComposable(gender) { gender = it }
                            WorkingComposable(workingHourStart, workingHourEnd) { x, y ->
                                workingHourStart = x
                                workingHourEnd = y
                            }
                        }

                    }

                }
                if (isSuccess.value.name == Status.Loading.name) {
                    ShowAnimation(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(100.dp)
                    )
                }
            }
        }

    }
}


@Composable
fun NameComposable(fullName: String, function: (it: String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        TextField(value = fullName, onValueChange = {
            function(it)
        }, label = { Text("Name") }, colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
        )
        )
    }
}


@Composable
fun AboutComposable(about: String?, function: (i: String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        TextField(value = about.toString(), onValueChange = {
            function(it)
        }, label = { Text("About") }, colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
        )
        )
    }
}

@Composable
fun CategoryCompose(category: String, function: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Text("Select Category", color = Color.Black, fontWeight = FontWeight.Light)
        IconButton(onClick = function) {
            Row() {
                Text(category)
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
        }
    }
}

@Composable
fun WorkingComposable(
    workingHourstart: Int?, workingHourEnd: Int?, function: (x: Int?, y: Int?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Text(
            "Working Hours",
            color = Color.Black,
            fontWeight = FontWeight.Light,
            modifier = Modifier.padding(start = 3.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = workingHourstart?.toString() ?: "",
                onValueChange = { newValue ->
                    val intValue = newValue.toIntOrNull() ?: 0
                    function(intValue, workingHourEnd)
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                ),
                label = {
                    Text("Start Time")
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(0.4f)
            )
            Spacer(modifier = Modifier

                .weight(0.2f))
            TextField(
                value = workingHourEnd?.toString() ?: "",
                onValueChange = { newValue ->
                    // Convert the input value to Int and pass it to the callback
                    function(workingHourstart ?: 0, newValue.toIntOrNull())
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                ),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                label = {
                    Text("End Time")
                },
                modifier = Modifier.weight(0.4f)
            )
        }
    }

}


@Composable
fun ShowCategories(
    param: (Int) -> Unit, categorylist: List<CategoryResponse>
) {
    var selectedcategory by remember {
        mutableIntStateOf(-1)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(20.dp)

    ) {
        Spacer(
            modifier = Modifier
                .height(7.dp)
                .padding(horizontal = 60.dp)
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(4.dp)
                )
                .background(Color.Black)

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
                items(categorylist.size) { category ->
                    CategoryComponent(
                        categorylist[category].category, isSelected = selectedcategory == category
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