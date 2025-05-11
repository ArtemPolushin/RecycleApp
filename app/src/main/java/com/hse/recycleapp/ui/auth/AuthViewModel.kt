package com.hse.recycleapp.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hse.recycleapp.domain.model.Response
import com.hse.recycleapp.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: IAuthRepository
) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var name by mutableStateOf("")
    var signInResponse by mutableStateOf<SignInResponse>(Response.Idle)
    var signUpResponse by mutableStateOf<SignUpResponse>(Response.Idle)


    fun signIn() {
        viewModelScope.launch {
            signInResponse = Response.Loading
            signInResponse = authRepository.firebaseSignInWithEmailAndPassword(email, password)
        }
    }

    fun signUp() {
        viewModelScope.launch {
            signUpResponse = Response.Loading
            signUpResponse = authRepository.firebaseSignUpWithEmailAndPassword(name, email, password)
        }
    }
}