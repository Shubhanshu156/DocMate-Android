package com.example.docmate.ui.theme.Screens.SignIn

//import androidx.compose.ui.text.style.TextForegroundStyle.Unspecified.alpha
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.docmate.R
import com.example.docmate.ui.Screens.Auth.SignIn.SigninViewModel

//import com.example.docmate.ui.Screens.SignIn.SigninViewModel

//@Preview(showSystemUi = true)
@Composable
fun Signin(
    onTour: () -> Unit,
    onSignUpClick: () -> Unit,
    onUserHome: () -> Unit,
    onDocHome: () -> Unit
) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        ImageComposable()
        TextSection()
        Spacer(modifier = Modifier.height(30.dp))
        FormSection(navigatetoHome = onUserHome, navigatetoDoc = onDocHome, navigatetoTour = onTour)
        SignupText("Doesn't have account SignUp??", onSignUpClick)
    }
}

@Composable
fun SignupText(s: String, onSignUpClick: () -> Unit = {}) {
    Row(
        horizontalArrangement = Arrangement.Center, modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            "SignUp??",
            color = Color(92, 77, 120),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                onSignUpClick()
            }
        )
    }
}

@Composable
fun FormSection(
    navigatetoHome: () -> Unit,
    navigatetoTour: () -> Unit,
    signinViewModel: SigninViewModel = hiltViewModel(),
    navigatetoDoc: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var Type by remember { mutableStateOf(Gender.Doctor) }
    var isSuccess = signinViewModel.isSuccess
    var showErrorSnackbar = signinViewModel.showsnackbar
    var errorMessage = signinViewModel.signinmsg
    var isFirsTime = signinViewModel.isFirstTime
    var usertype = signinViewModel.usertype
    if (!isFirsTime.value) {
        if (usertype.value == "DOCTOR") navigatetoDoc
        else {
            navigatetoHome
        }
    }
    Column {
        EmailField(
            drawable = painterResource(id = R.drawable.username),
            hint = "username",
            value = email,
            modifier = Modifier.padding(20.dp),
            call = { it -> email = it }
        )
        Spacer(modifier = Modifier.height(10.dp))
        PasswordField(
            drawable = painterResource(id = R.drawable.lock_solid),
            hint = "Password",
            modifier = Modifier.padding(20.dp),
            value = password,
            call = { it -> password = it }
        )
        Spacer(modifier = Modifier.height(30.dp))
        GenderPicker(Type) { Type = it }
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 60.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(Color(145, 149, 241))
                .clickable {
                    signinViewModel.Signin(email, password, type = Type.name)
                }
        ) {
            Text(
                text = "Login",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 10.dp)
            )
        }

    }

    if (showErrorSnackbar.value && errorMessage.value != "") {
        CustomAlertDialog(
            title = "SignIn",
            message = errorMessage.value,
            confirmButtonText = "OK",
            onConfirmClick = {
                showErrorSnackbar.value = false;errorMessage.value = "";
                if (isSuccess.value) {
                    if (isFirsTime.value) {
                        navigatetoTour()
                    } else {
                        if (Type.name == "Doctor") {

                            navigatetoDoc()
                        } else {
                            navigatetoHome()
                        }
                    }
                }
            },
            dismissButtonText = "Cancel",
            onDismissClick = {
                showErrorSnackbar.value = false;errorMessage.value = ""; if (isSuccess.value) {
                if (isFirsTime.value) {
                    navigatetoTour()
                } else {
                    navigatetoHome()
                }
            }
            },
            showDialog = showErrorSnackbar.value,
            dismissDialog = {
                showErrorSnackbar.value = false;errorMessage.value = ""; if (isSuccess.value) {
                if (isFirsTime.value) {
                    navigatetoTour()
                } else {
                    if (signinViewModel.usertype.value == "PATIENT") navigatetoHome()
                    else navigatetoDoc
                }
            }
            }
        )

    }

}

@Composable
fun GenderPicker(
    selectedGender: Gender,
    onGenderSelected: (Gender) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = 30.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Type",
            color = Color(92, 77, 120),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Spacer(modifier = Modifier.width(40.dp))
        GenderButton(
            text = "Doctor",
            isSelected = selectedGender == Gender.Doctor,
            onClick = { onGenderSelected(Gender.Doctor) }
        )
        Spacer(modifier = Modifier.width(10.dp))
        GenderButton(
            text = "Patient",
            isSelected = selectedGender == Gender.Patient,
            onClick = { onGenderSelected(Gender.Patient) }
        )
    }

}

@Composable
fun GenderButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        Color(145, 149, 241)
    } else {
        MaterialTheme.colorScheme.surface
    }
    val animatedColor by animateColorAsState(
        targetValue = backgroundColor,
        animationSpec = tween(durationMillis = 500) // Adjust the duration as needed
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color.LightGray
    )


    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(animatedColor)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = contentColor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun TextSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text("SignIn", color = Color(92, 77, 120), fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            "Enter  username and Password for Login",
            color = Color(92, 77, 120),
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraLight
        )
    }


}


@Composable
fun ImageComposable() {
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(R.drawable.girldoctor), // Replace with your image resource
            contentDescription = "My Image",
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailField(
    drawable: Painter,
    hint: String,
    modifier: Modifier = Modifier,
    value: String,
    call: (it: String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {

        TextField(
            value = value,
            onValueChange = call,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = "username",
                    fontWeight = FontWeight.ExtraLight,
                    color = Color.DarkGray
                )
            },
            leadingIcon = {
                Image(
                    painter = drawable,
                    contentDescription = "email button",
                    modifier = Modifier.size(20.dp)
                )
            },

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,


                )
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(
    drawable: Painter,
    hint: String,
    modifier: Modifier = Modifier,
    value: String,
    call: (String) -> Unit
) {
    var visible by remember { mutableStateOf(true) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()

            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = call,
            placeholder = {
                Text(
                    text = " Password",
                    fontWeight = FontWeight.ExtraLight,
                    color = Color.DarkGray
                )
            },
            leadingIcon = {
                Image(
                    painter = drawable,
                    contentDescription = "password button",
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                if (visible) {
                    Image(
                        painter = painterResource(id = R.drawable.eye_solid),
                        contentDescription = "password icon",
                        modifier = modifier
                            .clickable {
                                visible = !visible
                                call(value) // Update the value
                            }
                            .size(20.dp)
                    )
                }
                if (!visible) {
                    Image(
                        painter = painterResource(id = R.drawable.eye_slash_solid),
                        contentDescription = "password icon",
                        modifier = Modifier
                            .clickable {
                                visible = !visible
                                call(value) // Update the value
                            }
                            .size(20.dp)
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,


                )

        )

    }

}


enum class Gender {
    Doctor,
    Patient
}


@Composable
fun CustomAlertDialog(
    title: String,
    message: String,
    confirmButtonText: String,
    onConfirmClick: () -> Unit,
    dismissButtonText: String,
    onDismissClick: () -> Unit,
    showDialog: Boolean,
    dismissDialog: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { dismissDialog() },
            title = { Text(text = title) },
            text = { Text(text = message) },
            confirmButton = {
                Button(onClick = { onConfirmClick(); dismissDialog() }) {
                    Text(text = confirmButtonText)
                }
            },
            dismissButton = {
                Button(onClick = { dismissDialog() }) {
                    Text(text = dismissButtonText)
                }
            }
        )
    }
}
