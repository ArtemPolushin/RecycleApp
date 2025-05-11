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
    viewModel: AuthViewModel = hiltViewModel()
) {
    val response = viewModel.signUpResponse

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Регистрация", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        OutlinedTextField(
            value = viewModel.name,
            onValueChange = { viewModel.name = it },
            label = { Text("Имя") },
            modifier = Modifier.fillMaxWidth()
        )

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

        Spacer(modifier = Modifier.height(16.dp))

        when (response) {
            is Response.Idle -> Button(
                onClick = { viewModel.signUp() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Регистрация")
            }

            is Response.Loading -> CircularProgressIndicator()

            is Response.Success -> {
                LaunchedEffect(Unit) {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            }

            is Response.Failure -> {
                Text(
                    text = "Ошибка: ${response.e.message}",
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Button(
                    onClick = { viewModel.signUp() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Повторить")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = { navController.navigate("login") }) {
            Text("Уже есть аккаунт? Войти")
        }
    }
}