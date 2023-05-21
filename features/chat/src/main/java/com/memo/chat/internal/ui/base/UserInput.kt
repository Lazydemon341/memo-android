package com.memo.chat.internal.ui.base

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.memo.core.design.theme.MemoAppTheme
import com.memo.features.chat.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun UserInput(
    onSendMessage: (String) -> Unit,
    modifier: Modifier = Modifier,
    resetScroll: () -> Unit = {},
) {
    var textState by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    var textFieldFocusState by remember { mutableStateOf(false) }

    Surface(tonalElevation = 2.dp) {
        Row(modifier = modifier) {
            UserInputText(
                textFieldValue = textState,
                onTextChanged = { textState = it },
                onTextFieldFocused = { focused ->
                    if (focused) {
                        resetScroll()
                    }
                    textFieldFocusState = focused
                },
                focusState = textFieldFocusState,
                sendMessageEnabled = textState.text.isNotBlank(),
                onMessageSent = {
                    onSendMessage(textState.text)
                    // Reset text field and close keyboard
                    textState = TextFieldValue()
                    // Move scroll to bottom
                    resetScroll()
                },
            )
        }
    }
}

@Composable
private fun SendMessageButton(
    sendMessageEnabled: Boolean,
    onMessageSent: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier.height(36.dp),
        enabled = sendMessageEnabled,
        onClick = onMessageSent,
        colors = ButtonDefaults.buttonColors(
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(
            modifier = Modifier.padding(horizontal = 16.dp),
            imageVector = Icons.Filled.Send,
            tint = Color.White,
            contentDescription = stringResource(id = R.string.send_button_content_description),
        )
    }
}

@ExperimentalFoundationApi
@Composable
private fun UserInputText(
    keyboardType: KeyboardType = KeyboardType.Text,
    onTextChanged: (TextFieldValue) -> Unit,
    textFieldValue: TextFieldValue,
    onTextFieldFocused: (Boolean) -> Unit,
    focusState: Boolean,
    sendMessageEnabled: Boolean,
    onMessageSent: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
    ) {
        var lastFocusState by remember { mutableStateOf(false) }
        BasicTextField(
            value = textFieldValue,
            onValueChange = { onTextChanged(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 64.dp)
                .align(Alignment.CenterStart)
                .onFocusChanged { state ->
                    if (lastFocusState != state.isFocused) {
                        onTextFieldFocused(state.isFocused)
                    }
                    lastFocusState = state.isFocused
                },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = ImeAction.Send
            ),
            cursorBrush = SolidColor(LocalContentColor.current),
            textStyle = LocalTextStyle.current.copy(color = LocalContentColor.current)
        )

        val disableContentColor =
            MaterialTheme.colorScheme.onSurfaceVariant
        if (textFieldValue.text.isEmpty() && !focusState) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 32.dp),
                text = stringResource(id = R.string.user_input_hint),
                style = MaterialTheme.typography.bodyLarge.copy(color = disableContentColor)
            )
        }
        SendMessageButton(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp),
            sendMessageEnabled = sendMessageEnabled,
            onMessageSent = onMessageSent,
        )
    }
}

@Preview
@Composable
private fun UserInputPreview() {
    MemoAppTheme {
        UserInput(onSendMessage = {})
    }
}
