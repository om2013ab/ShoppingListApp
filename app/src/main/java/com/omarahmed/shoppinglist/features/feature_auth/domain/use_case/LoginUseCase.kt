package com.omarahmed.shoppinglist.features.feature_auth.domain.use_case

import com.omarahmed.shoppinglist.features.feature_auth.data.remote.response.LoginResponse
import com.omarahmed.shoppinglist.features.feature_auth.domain.models.AuthResult
import com.omarahmed.shoppinglist.features.feature_auth.domain.repository.AuthRepository
import com.omarahmed.shoppinglist.features.feature_auth.presentation.util.ValidationUtil
import javax.inject.Inject


class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): AuthResult<LoginResponse>{
        val emailError = ValidationUtil.validateEmail(email)

        if (emailError != null) {
            return AuthResult(
                emailError = emailError,
            )
        }

        val result = authRepository.loginUser(email, password)
        return AuthResult(
            result = result
        )
    }
}