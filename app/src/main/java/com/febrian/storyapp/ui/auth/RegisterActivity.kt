package com.febrian.storyapp.ui.auth

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.febrian.storyapp.R
import com.febrian.storyapp.databinding.ActivityRegisterBinding
import com.febrian.storyapp.ui.auth.vm.AuthViewModel
import com.febrian.storyapp.utils.Helper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegisterBinding
    private val authViewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var helper: Helper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
        observerResults()
    }

    private fun register() {
        val name = binding.edtName.text.toString()
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()

        if (validate(name, email, password)) {
            authViewModel.register(name, email, password)
        }
    }

    private fun validate(name: String, email: String, password: String): Boolean {
        return when {
            name.isEmpty() -> {
                helper.showToast(getString(R.string.error_name_must_be_filled))
                false
            }

            email.isEmpty() -> {
                helper.showToast(getString(R.string.error_email_must_be_filled))
                false
            }

            password.isEmpty() -> {
                helper.showToast(getString(R.string.error_password_must_be_filled))
                false
            }

            else -> true
        }
    }

    private fun observerResults() {
        authViewModel.resultRegister.observe(this) {
            it.onSuccess { data ->
                if (data.error == true) {
                    helper.showToast(data.message.toString())
                } else {
                    //Success
                    helper.showToast(data.message.toString())
                    helper.moveActivityWithFinish(this, LoginActivity())
                }
            }

            it.onFailure { t ->
                helper.showToast(t.message.toString())
            }
        }

        authViewModel.loading.observe(this) { active ->
            helper.showLoading(active, binding.loading)
            helper.setEnableView(!active, binding.btnRegister)
        }
    }

    override fun onClick(v: View) {
        when (v) {
            binding.btnRegister -> {
                register()
            }

            binding.btnLogin -> {
                helper.moveActivityWithFinish(this, LoginActivity())
            }
        }
    }

}