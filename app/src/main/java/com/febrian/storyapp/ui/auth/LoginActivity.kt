package com.febrian.storyapp.ui.auth

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.febrian.storyapp.R
import com.febrian.storyapp.data.response.LoginResult
import com.febrian.storyapp.databinding.ActivityLoginBinding
import com.febrian.storyapp.ui.auth.vm.AuthViewModel
import com.febrian.storyapp.ui.story.MainActivity
import com.febrian.storyapp.utils.Helper
import com.febrian.storyapp.utils.UserPreference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var helper: Helper

    @Inject
    lateinit var userPreference: UserPreference

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
        observerResults()
    }

    private fun login() {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()

        if (validate(email, password)) {
            authViewModel.login(email, password)
        }
    }

    private fun validate(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                val message = getString(R.string.error_email_must_be_filled)
                helper.showToast(message)
                binding.edtEmail.error = message
                false
            }
            password.isEmpty() -> {
                val message = getString(R.string.error_password_must_be_filled)
                helper.showToast(message)
                binding.edtPassword.error = message
                false
            }
            else -> true
        }
    }

    private fun observerResults() {
        authViewModel.resultLogin.observe(this) {
            it.onSuccess { data ->
                if (data.error == true) {
                    helper.showToast(data.message.toString())
                } else {
                    //Success
                    helper.showToast(data.message.toString())
                    saveToken(data.loginResult)
                    helper.moveActivityWithFinish(this, MainActivity())
                }
            }

            it.onFailure { t ->
                helper.showToast(t.message.toString())
            }
        }

        authViewModel.loading.observe(this) { active ->
            helper.showLoading(active, binding.loading)
            helper.setEnableView(!active, binding.btnLogin)
        }
    }

    private fun saveToken(loginResult: LoginResult?) {
        userPreference.setToken(loginResult?.token)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnLogin -> {
                login()
            }
            binding.btnRegister -> {
                helper.moveActivityWithFinish(this, RegisterActivity())
            }
        }
    }
}