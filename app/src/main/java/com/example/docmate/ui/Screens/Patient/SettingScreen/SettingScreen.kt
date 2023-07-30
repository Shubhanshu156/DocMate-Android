package com.example.docmate.ui.Screens.Patient.SettingScreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.example.docmate.R
import com.example.docmate.Utility.Status
import com.example.docmate.Utility.Utils
import com.example.docmate.data.models.Response.Gender
import com.example.docmate.ui.Screens.Patient.ProfileScreen.ShowAnimation

@Composable
fun SettingScreen(
    SettingScreenViewmodel: SettingScreenViewModel = hiltViewModel(),
    goback: () -> Unit
) {
    val userProfile by SettingScreenViewmodel.userProfileState.collectAsState()
    val isSuccess = SettingScreenViewmodel.ProfileStatus.collectAsState()
    if (isSuccess.value.name == Status.Success.name) {
        ShowSuccessDialog(
            title = "Profile Updated",
            msg = "Your Profile has been Updated Successfully",
            goback
        )
    }

    if (userProfile != null) {
        var selectedImageUri by remember {
            mutableStateOf<Uri?>(null)
        }
        var email by remember {
            mutableStateOf(userProfile!!.email)
        }
        var number by remember {
            mutableStateOf(userProfile!!.contactNumber)
        }
        var adress by remember {
            mutableStateOf(userProfile!!.address)
        }
        var age by remember {
            mutableStateOf(userProfile!!.age)
        }
        var name by remember {
            mutableStateOf(userProfile!!.name)
        }
        var gender by remember {
            mutableStateOf(userProfile!!.gender)
        }
        val context = LocalContext.current
        val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri -> selectedImageUri = uri }
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 30.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(if (isSuccess.value == Status.Loading) 0.3f else 1f)
                    .verticalScroll(rememberScrollState()),
            ) {
                val imageUri = selectedImageUri ?: userProfile?.profileurl

                ShowProfileImage(imageUri, gender = userProfile?.gender, onclick = {
                    singlePhotoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }, onsave = {
                    val data = (selectedImageUri?.let { Utils().upload(context, it) })
                    SettingScreenViewmodel.savedetails(
                        name = name,
                        email = email,
                        mobilenumber = number,
                        age = age.toString(),
                        gender = gender, profileurl = data, address = adress
                    )
                }, onDismiss = goback)

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(40.dp)
                ) {
                    UserName(userProfile!!.username.toString())
                    Name(name.toString()) { it ->
                        name = it
                    }
                    Email(email.toString()) {
                        email = it
                    }
                    PhoneNumber(number.toString()) {
                        number = it
                    }
                    Address(adress) {
                        adress = it
                    }
                    Age(age.toString()) {
                        age = it.toInt()
                    }
                    GenderComposable(gender) {
                        gender = it
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Name(name: String, function: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = name,
            onValueChange = {
                function(it)
            },
            label = { Text("Name") },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Address(address: String?, onclick: (it: String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = address.toString(),
            onValueChange = {
                onclick(it)
            },

            label = { Text("Address") },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
//                underlineColor = TextFieldDefaults.colors().textHelp
            ), modifier = Modifier.fillMaxWidth()
        )

    }
}

@Composable
fun GenderComposable(gender: String?, onclick: (it: String) -> Unit) {
    val (selectedGender, setSelectedGender) = remember {
        if ((gender?.lowercase() ?: "") == "male")
            mutableStateOf(Gender.MALE)
        else
            mutableStateOf(Gender.FEMALE)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Gender")
        IconButton(
            onClick = { setSelectedGender(Gender.MALE); onclick("Male") },
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = if (selectedGender == Gender.MALE) Color.Cyan else Color.Gray
            )
        ) {
            Row() {
                Icon(
                    painterResource(R.drawable.male_48px),
                    contentDescription = null
                )
                Text("Male")
            }
        }
        IconButton(
            onClick = { setSelectedGender(Gender.FEMALE); onclick("Female") },
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = if (selectedGender == Gender.FEMALE) Color.Magenta else Color.Gray
            )
        ) {
            Row() {
                Icon(
                    painterResource(R.drawable.female_48px),
                    contentDescription = null
                )
                Text("Female")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Age(age: String, onclick: (it: String) -> Unit) {
    MaterialTheme { // Make sure the composable is wrapped in MaterialTheme
        Column(modifier = Modifier.fillMaxWidth()) {
            var textState by remember {
                mutableStateOf(
                    try {
                        age.toInt()
                    } catch (e: Exception) {
                        0
                    }
                )
            }
            TextField(
                value = textState.toString(),
                onValueChange = { newValue ->
                    val intValue = newValue.toIntOrNull()
                    textState = intValue ?: 0
                    onclick(intValue.toString())
                },
                label = { Text(text = "Age") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(onDone = { /* Handle keyboard Done button if needed */ }),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    cursorColor = Color.Black,
                    focusedBorderColor = Color.Black,
                    focusedLabelColor = Color.Black, // Add this if you want the label color to change on focus
                    focusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                    focusedTrailingIconColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneNumber(number: String, onclick: (it: String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = number,
            onValueChange = { onclick(it) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            label = { Text("Mobile Number") },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
//                underlineColor = TextFieldDefaults.colors().textHelp
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Email(email: String, onclick: (it: String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = email,
            onValueChange = {
                onclick(it)
            },
            label = { Text("Email") },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserName(username: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = username,
            onValueChange = {},
            readOnly = true,
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.White),
            label = { Text("UserName") }

        )
    }
}

@Composable
fun ShowProfileImage(
    url: Any?,
    modifier: Modifier = Modifier,
    gender: String?,
    onclick: () -> Unit,
    onsave: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(Color(16, 28, 52))
            .padding(10.dp)
    ) {
        TopBar(onsave, onDismiss)
        Box(
            modifier = Modifier
                .padding(40.dp)
                .aspectRatio(1f)
                .clip(CircleShape)
                .background(Color.Gray)
            // Apply the circular shape here
        ) {

            if (url != null) {
                SubcomposeAsyncImage(
                    model = url,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(), // Use fillMaxSize here to fill the circular shape
                    contentScale = ContentScale.FillBounds,
                    loading = {
                        CircularProgressIndicator()
                    }
                )
            } else {
                if (gender == null || gender.uppercase() == Gender.MALE.name) {
                    Image(
                        painter = painterResource(id = R.drawable.maledoctoravtar),
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize(), // Use fillMaxSize here to fill the circular shape
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.femaledoctoravtar),
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize(), // Use fillMaxSize here to fill the circular shape
                        contentScale = ContentScale.Fit
                    )
                }
            }
            Icon(
                Icons.Default.AddCircle,
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(50.dp)
                    .clickable {
                        onclick()
                    },
                tint = Color.White
            )
        }

    }
}

@Composable
fun TopBar(onsave: () -> Unit, onDismiss: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        Icon(
            Icons.Default.ArrowBack,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.clickable {
                onDismiss()
            })
        Text("EditProfile", color = Color.White)
        Text("Save", color = Color.White, modifier = Modifier.clickable {
            onsave()
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowSuccessDialog(title: String, msg: String, onDismiss: () -> Unit) {
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
        },

    )
}