package com.hse.recycleapp.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.hse.recycleapp.domain.model.Response

@Composable
fun RegisterScreen(
    navController: NavHostController,
    redirect: String? = null,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val signUpResponse = viewModel.signUpResponse
    val signInResponse = viewModel.signInResponse

    val redirectRoute = navController.currentBackStackEntry
        ?.arguments?.getString("redirect") ?: "map"

    var confirmPassword by remember { mutableStateOf("") }
    var passwordMismatch by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Регистрация", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            label = { Text("Почта") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.password,
            onValueChange = { viewModel.password = it },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Подтверждение пароля") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = passwordMismatch
        )

        if (passwordMismatch) {
            Text(
                text = "Пароли не совпадают",
                color = Color.Red,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (signUpResponse) {
            is Response.Idle -> Button(
                onClick = {
                    if (viewModel.password != confirmPassword) {
                        passwordMismatch = true
                    } else {
                        passwordMismatch = false
                        viewModel.signUp()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Зарегистрироваться")
            }

            is Response.Loading -> CircularProgressIndicator()

            is Response.Success -> {
                LaunchedEffect(Unit) {
                    viewModel.signIn()
                }
            }

            is Response.Failure -> {
                Text(
                    text = "Ошибка регистрации: ${signUpResponse.e.message}",
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Button(
                    onClick = {
                        if (viewModel.password != confirmPassword) {
                            passwordMismatch = true
                        } else {
                            passwordMismatch = false
                            viewModel.signUp()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Повторить")
                }
            }
        }

        when (signInResponse) {
            is Response.Success -> {
                LaunchedEffect(Unit) {
                    navController.navigate(redirectRoute) {
                        popUpTo("register") { inclusive = true }
                    }
                }
            }

            is Response.Failure -> {
                Text(
                    text = "Ошибка входа: ${signInResponse.e.message}",
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            is Response.Loading -> CircularProgressIndicator()

            else -> {}
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = { navController.navigate("login?redirect=$redirectRoute") }) {
            Text("Уже есть аккаунт? Войти")
        }
    }
}

