package com.memo.account.internal.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.memo.account.internal.ui.login.model.LoginUiState
import com.memo.core.model.user.AuthParams
import com.memo.features.account.R

@Composable
internal fun LoginRoute(
    onBackPressed: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val loginUiState = viewModel.loginUiState.collectAsStateWithLifecycle()
    LoginScreen(
        onBackPressed = onBackPressed,
        onNavigateToSignUp = onNavigateToSignUp,
        onLogin = viewModel::onLogin,
        loginUiState = loginUiState,
        onLoginSuccess = onLoginSuccess,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginScreen(
    onBackPressed: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onLogin: (AuthParams) -> Unit,
    loginUiState: State<LoginUiState>,
    onLoginSuccess: () -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = {
            LoginScreenTopBar()
        },
        content = { padding ->
            LoginScreenContent(
                padding = padding,
                onNavigateToSignUp = onNavigateToSignUp,
                onLogin = onLogin,
                loginUiState = loginUiState.value,
                onLoginSuccess = onLoginSuccess,
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginScreenTopBar() {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.login_page_title))
        }
    )
}

@Composable
private fun LoginScreenContent(
    padding: PaddingValues,
    onNavigateToSignUp: () -> Unit,
    onLogin: (AuthParams) -> Unit,
    loginUiState: LoginUiState,
    onLoginSuccess: () -> Unit,
) {
    if (loginUiState == LoginUiState.Success) {
        onLoginSuccess()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
    ) {
        LoginScreenInput(onNavigateToSignUp = onNavigateToSignUp, onLogin = onLogin)
        if (loginUiState == LoginUiState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginScreenInput(
    onNavigateToSignUp: () -> Unit,
    onLogin: (AuthParams) -> Unit,
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextField(
            modifier = Modifier.padding(bottom = 16.dp),
            label = { Text(text = stringResource(id = R.string.email_input_field)) },
            value = email.value,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = { email.value = it },
        )
        TextField(
            modifier = Modifier.padding(bottom = 16.dp),
            label = { Text(text = stringResource(id = R.string.password_input_field)) },
            value = password.value,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { password.value = it },
        )
        Button(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .width(TextFieldDefaults.MinWidth)
                .height(50.dp),
            onClick = {
                onLogin(AuthParams(email = email.value, password = password.value))
            },
        ) {
            Text(text = stringResource(id = R.string.login_button_text))
        }
        ClickableText(
            text = AnnotatedString(text = stringResource(id = R.string.to_sign_up_screen_button)),
            onClick = { onNavigateToSignUp() },
            style = TextStyle(
                color = Color.White,
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                textDecoration = TextDecoration.Underline,
            )
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun LoginScreenPreview() {
    val loginUiState = remember { mutableStateOf(LoginUiState.Input) }
    LoginScreen(
        onBackPressed = {},
        onNavigateToSignUp = {},
        onLogin = {},
        loginUiState = loginUiState,
        onLoginSuccess = { },
    )
}
