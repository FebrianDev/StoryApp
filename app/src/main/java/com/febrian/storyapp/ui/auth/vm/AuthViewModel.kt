package com.febrian.storyapp.ui.auth.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febrian.storyapp.data.repository.AuthRepository
import com.febrian.storyapp.data.response.LoginResponse
import com.febrian.storyapp.data.response.RegisterResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private var _resultRegister = MutableLiveData<Result<RegisterResponse>>()
    val resultRegister: LiveData<Result<RegisterResponse>> get() = _resultRegister

    private var _resultLogin = MutableLiveData<Result<LoginResponse>>()
    val resultLogin: LiveData<Result<LoginResponse>> get() = _resultLogin

    private var _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> get() = _loading

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            _resultRegister.value = authRepository.register(name, email, password)
            _loading.value = false
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            _resultLogin.value = authRepository.login(email, password)
            _loading.value = false
        }
    }
}