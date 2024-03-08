package com.nyan.photokmm.android.common.textfield

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onImeAction: () -> Unit, // Action to perform when the IME action is triggered
    isRefreshing: Boolean,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    //After typing and when the api is start calling, we want to hide the keyboard automatically.
    if (isRefreshing) {
        clearTextAndHideKeyboard(
            keyboardController,
            focusManager
        )
    }

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Search") },
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(100.dp))
            .padding(12.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(100.dp),
        keyboardActions = KeyboardActions(
            onDone = {
                onImeAction()
                clearTextAndHideKeyboard(keyboardController, focusManager)
            }
        ),
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = {
                    onValueChange("")
                    clearTextAndHideKeyboard(keyboardController, focusManager)
                }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear"
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
private fun clearTextAndHideKeyboard(
    keyboardController: SoftwareKeyboardController?,
    focusManager: FocusManager
) {
    keyboardController?.hide()
    focusManager.clearFocus()
}