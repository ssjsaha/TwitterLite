package com.example.twitterlite.presentation.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.twitterlite.R
import com.example.twitterlite.domain.model.User
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupPage(
    onEvent: (LoginPageEvent) -> Unit, state: StateFlow<LoginState>, navController: NavController
) {
    val loginState = state.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    var emailError by remember {
        mutableStateOf(false)
    }
    var passwordError by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    Scaffold(modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Email Input
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                isError = emailError,

                label = { Text(text = stringResource(id = R.string.email_hint)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email, contentDescription = null
                    )
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = colorResource(id = R.color.colorAccent),
                    focusedBorderColor = colorResource(id = R.color.colorAccent),
                    errorBorderColor = colorResource(id = R.color.red),
                    unfocusedBorderColor = Color.Gray,
                ),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Input
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                isError = passwordError,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                label = { Text(text = stringResource(id = R.string.password_hint)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock, contentDescription = null
                    )
                },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(id = R.color.colorAccent),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = colorResource(id = R.color.colorAccent),
                    errorBorderColor = colorResource(id = R.color.red)
                ),
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (email.isBlank()) {
                        emailError = true
                        passwordError = false
                        scope.launch {
                            snackbarHostState.showSnackbar("Please enter a email")
                        }
                    } else if (password.isBlank()) {
                        emailError = false
                        passwordError = true
                        scope.launch {
                            snackbarHostState.showSnackbar("Please enter a password")
                        }
                    } else {
                        emailError = false
                        passwordError = false
                        onEvent.invoke(LoginPageEvent.AttemptSignup(User(email, password)))
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                if (loginState.value.isLoading) {
                    CircularProgressIndicator(
                        color = colorResource(id = R.color.purple_500),
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(text = stringResource(id = R.string.signup_button))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                text = "Or"
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),

                onClick = {
                    navController.navigate("login")
                }) {
                Text(text = stringResource(id = R.string.login_button))
            }

            // Demo: Display a message when the login is successful
            if (loginState.value.isLoggedIn && loginState.value.error.isEmpty()) {
                val text = stringResource(id = R.string.login_success_message)
                LaunchedEffect(key1 = true) {
                    scope.launch {
                        snackbarHostState.showSnackbar(text)
                    }
                    navController.navigate("home") {
                        popUpTo("auth") {
                            inclusive = true
                        }
                    }
                }
            } else if (loginState.value.error.isNotEmpty()) {
                LaunchedEffect(key1 = loginState.value.error) {
                    scope.launch {
                        snackbarHostState.showSnackbar(loginState.value.error)
                        loginState.value.error = ""
                    }
                }
            }
        }

    }
}