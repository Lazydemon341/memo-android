package com.memo.account.internal.ui.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    modifier: Modifier = Modifier,
) {
    val confirmationCode = remember { mutableStateOf("") }

    if (confirmationUiState == ConfirmationUiState.Loading) {
        CircularProgressIndicator(modifier = Modifier.padding(bottom = 16.dp))
    }

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
    )
}
