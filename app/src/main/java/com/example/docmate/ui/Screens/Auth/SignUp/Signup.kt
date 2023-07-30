package com.example.docmate.ui.theme.Screens.SignUp

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.docmate.R
import com.example.docmate.ui.Screens.Auth.SignUp.SignUpViewModel
import com.example.docmate.ui.theme.Screens.SignIn.CustomAlertDialog


@Composable
fun SignUp(function: () -> Unit) {
    Column {
        ImageComposable()
        TextSection()
        Spacer(modifier = Modifier.height(30.dp))
        FormSection(function)
        LoginText("Already have account Signin??", function)
    }
}

@Composable
fun LoginText(s: String, onSignUpClick: () -> Unit = {}) {
    Row(
        horizontalArrangement = Arrangement.Center, modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            "SignIn??",
            color = Color(92, 77, 120),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {

                onSignUpClick()
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormSection(function: () -> Unit, signupViewModel: SignUpViewModel = hiltViewModel()) {
    var username by remember {
        mutableStateOf("")
    }
    var isaccountcreated = signupViewModel.IsAccountCreated
    var password by remember {
        mutableStateOf("")
    }
    var Type by remember {
        mutableStateOf(Type.Doctor)
    }
    var showErrorSnackbar = signupViewModel.showsnackbar
    var errorMessage = signupViewModel.signupmsg
    Column {

        EmailField(
            drawable = painterResource(id = R.drawable.username),
            hint = "Email",
            value = username,
            modifier = Modifier.padding(20.dp),
            call = {
                it
                username = it
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        PasswordField(
            drawable = painterResource(id = R.drawable.lock_solid),
            hint = "Password",
            modifier = Modifier.padding(20.dp),
            value = password,
            call = { it ->
                password = it
            }
        )
        Spacer(modifier = Modifier.height(30.dp))
        TypePicker(Type) { Type = it }
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
                    signupViewModel.signUp(username, password = password, type = Type)
                }
        ) {
            Text(
                text = "SignUp",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 10.dp)
            )
        }

    }

    if (showErrorSnackbar.value && errorMessage.value != "") {
        CustomAlertDialog(
            title = "SignUp",
            message = errorMessage.value,
            confirmButtonText = "OK",
            onConfirmClick = {
                showErrorSnackbar.value = false;errorMessage.value = "";
                if (isaccountcreated.value) {

                    function()
                }
            },
            dismissButtonText = "Cancel",
            onDismissClick = {
                showErrorSnackbar.value = false;errorMessage.value =
                ""; if (isaccountcreated.value) {
                function()
            }
            },
            showDialog = showErrorSnackbar.value,
            dismissDialog = {
                showErrorSnackbar.value = false;errorMessage.value =
                ""; if (isaccountcreated.value) {
                function()
            }
            }
        )
    }
}

@Composable
fun TypePicker(
    selectedType: Type,
    onTypeSelected: (Type) -> Unit
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
        TypeButton(
            text = "Doctor",
            isSelected = selectedType == Type.Doctor,
            onClick = { onTypeSelected(Type.Doctor) }
        )
        Spacer(modifier = Modifier.width(10.dp))
        TypeButton(
            text = "Patient",
            isSelected = selectedType == Type.Patient,
            onClick = { onTypeSelected(Type.Patient) }
        )
    }

}

@Composable
fun TypeButton(
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


@Preview
@Composable
fun TextSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text("SignUp", color = Color(92, 77, 120), fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            "Please Enter Your Email and Password for SignUp",
            color = Color(92, 77, 120),
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraLight
        )
    }


}


@Preview
@Composable
fun ImageComposable() {
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(R.drawable.male_doctor),
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
                    text = "Email",
                    fontWeight = FontWeight.ExtraLight,
                    color = Color.DarkGray
                )
            },
            leadingIcon = {
                Image(
                    painter = drawable,
                    contentDescription = "username button",
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

enum class Type {
    Doctor,
    Patient
}