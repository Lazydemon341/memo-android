package com.memo.account.internal.ui.signup

import android.R.string
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
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.memo.account.internal.ui.signup.model.ConfirmationUiState
import com.memo.account.internal.ui.signup.model.ConfirmationUiState.Input
import com.memo.account.internal.ui.signup.model.SignUpUiState
import com.memo.core.model.user.SignUpParams
import com.memo.features.account.R

@Composable
internal fun SignUpRoute(
    onBackPressed: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    val signUpUiState = viewModel.signUpUiState.collectAsStateWithLifecycle()
    val confirmationUiState = viewModel.confirmationUiState.collectAsStateWithLifecycle()

    SignUpScreen(
        onBackPressed = onBackPressed,
        onSignUpPressed = viewModel::signUp,
        onConfirmPressed = viewModel::confirm,
        signUpUiState = signUpUiState,
        confirmationUiState = confirmationUiState,
        onRetrySignUp = viewModel::retrySignUp,
        onRetryConfirmation = viewModel::retryConfirmation,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignUpScreen(
    onBackPressed: () -> Unit,
    onSignUpPressed: (SignUpParams) -> Unit,
    onConfirmPressed: (Int) -> Unit,
    signUpUiState: State<SignUpUiState>,
    confirmationUiState: State<ConfirmationUiState>,
    onRetrySignUp: () -> Unit,
    onRetryConfirmation: () -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = {
            SignUpScreenTopBar(onBackPressed)
        },
        content = { padding ->
            SignUpScreenContent(
                padding = padding,
                onSignUpPressed = onSignUpPressed,
                onConfirmPressed = onConfirmPressed,
                onConfirmSuccess = onBackPressed,
                signUpUiState = signUpUiState.value,
                confirmationUiState = confirmationUiState,
                onRetrySignUp = onRetrySignUp,
                onRetryConfirmation = onRetryConfirmation,
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignUpScreenTopBar(onBackPressed: () -> Unit) {
    // TODO: move topbar creation above screen logic
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.sign_up_page_title))
        },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(Filled.ArrowBack, "")
            }
        },
    )
}

@Composable
private fun SignUpScreenContent(
    padding: PaddingValues,
    onSignUpPressed: (SignUpParams) -> Unit,
    onConfirmPressed: (Int) -> Unit,
    onConfirmSuccess: () -> Unit,
    signUpUiState: SignUpUiState,
    confirmationUiState: State<ConfirmationUiState>,
    onRetrySignUp: () -> Unit,
    onRetryConfirmation: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
    ) {
        if (signUpUiState == SignUpUiState.Success) {
            SignUpScreenConfirmation(
                modifier = Modifier.align(Alignment.Center),
                onConfirmPressed = onConfirmPressed,
                confirmationUiState = confirmationUiState.value,
                onConfirmSuccess = onConfirmSuccess,
                onRetryConfirmation = onRetryConfirmation,
            )
        } else {
            SignUpScreenInput(
                modifier = Modifier.align(Alignment.Center),
                onSignUpPressed = onSignUpPressed
            )
            if (signUpUiState == SignUpUiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                )
            }
            if (signUpUiState == SignUpUiState.Failure) {
                AlertDialog(
                    onDismissRequest = { onRetrySignUp() },
                    title = {
                        Text(text = stringResource(R.string.sign_up_failed_title))
                    },
                    text = {
                        Text(text = stringResource(R.string.sign_up_failed_subtitle))
                    },
                    confirmButton = {
                        ClickableText(
                            text = AnnotatedString(stringResource(string.ok)),
                            onClick = { onRetrySignUp() },
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Default,
                                textDecoration = TextDecoration.Underline,
                            ),
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignUpScreenInput(
    onSignUpPressed: (SignUpParams) -> Unit,
    modifier: Modifier = Modifier,
) {
    val name = remember { mutableStateOf("") }
    val surname = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextField(
            modifier = Modifier.padding(bottom = 16.dp),
            label = { Text(text = stringResource(id = R.string.name_input_field)) },
            value = name.value,
            onValueChange = { name.value = it },
        )
        TextField(
            modifier = Modifier.padding(bottom = 16.dp),
            label = { Text(text = stringResource(id = R.string.surname_input_field)) },
            value = surname.value,
            onValueChange = { surname.value = it },
        )
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
                onSignUpPressed(
                    SignUpParams(
                        name = name.value,
                        surname = surname.value,
                        email = email.value,
                        password = password.value,
                    )
                )
            },
        ) {
            Text(text = stringResource(id = R.string.sign_up_button_text))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignUpScreenConfirmation(
    onConfirmPressed: (Int) -> Unit,
    onConfirmSuccess: () -> Unit,
    confirmationUiState: ConfirmationUiState,
    onRetryConfirmation: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val confirmationCode = remember { mutableStateOf("") }

    if (confirmationUiState == ConfirmationUiState.Success) {
        onConfirmSuccess()
    }

    Column(modifier = modifier) {
        TextField(
            modifier = Modifier.padding(bottom = 16.dp),
            label = { Text(text = stringResource(id = R.string.confirmation_code_input_field)) },
            value = confirmationCode.value,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = {
                // TODO: check string validity
                confirmationCode.value = it
            },
        )
        Button(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .width(TextFieldDefaults.MinWidth)
                .height(50.dp),
            onClick = { onConfirmPressed(confirmationCode.value.toInt()) },
        ) {
            Text(text = stringResource(id = R.string.confirmation_button_text))
        }
    }

    if (confirmationUiState == ConfirmationUiState.Loading) {
        CircularProgressIndicator(modifier = Modifier.padding(bottom = 16.dp))
    }

    if (confirmationUiState == ConfirmationUiState.Failure) {
        AlertDialog(
            onDismissRequest = { onRetryConfirmation() },
            title = {
                Text(text = stringResource(R.string.confirmation_failed_title))
            },
            text = {
                Text(text = stringResource(R.string.sign_up_failed_subtitle))
            },
            confirmButton = {
                ClickableText(
                    text = AnnotatedString(stringResource(string.ok)),
                    onClick = { onRetryConfirmation() },
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Default,
                        textDecoration = TextDecoration.Underline,
                    ),
                )
            }
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun SignUpScreenPreview() {
    val signUpUiState = remember { mutableStateOf(SignUpUiState.Success) }
    val confirmationUiState = remember { mutableStateOf(Input) }
    SignUpScreen(
        onBackPressed = {},
        onSignUpPressed = { },
        onConfirmPressed = {},
        signUpUiState = signUpUiState,
        confirmationUiState = confirmationUiState,
        onRetrySignUp = {},
        onRetryConfirmation = {},
    )
}
