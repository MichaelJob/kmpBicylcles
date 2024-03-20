package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import model.BicyclesViewModel



@Composable
fun SignUpIn(viewModel: BicyclesViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    if (uiState.isRegistered) {
        SignIn(viewModel)
    } else {
        SignUp(viewModel)
    }
}

@Composable
fun SignUp(viewModel: BicyclesViewModel) {
    SignInUpColumn {
        Text(text = "Sign up to save your bicycles", style = typography.h6)
        EmailPWFields(viewModel)
        Button(onClick = {
            viewModel.signUp()
        }) {
            Text("Sign up as new user")
        }
    }
}


@Composable
fun SignIn(viewModel: BicyclesViewModel) {
    SignInUpColumn{
        Text(text = "Sign in to get your bicycles data", style = typography.h6)
        EmailPWFields(viewModel)
        Button(onClick = {
            viewModel.signIn()
        }) {
            Text("Log in")
        }
        Text(text = viewModel.uiState.value.errorMsg, style = typography.caption)
    }
}

@Composable
fun SignInUpColumn(content : @Composable ColumnScope.() -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(vertical = 55.dp, horizontal = 25.dp),
        content = content
    )
}

@Composable
fun EmailPWFields(viewModel: BicyclesViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    BicycleSignField(
        label = "E-Mail:",
        value = uiState.email,
        onValueChange = {
            viewModel.updateEmail(it)
        },
    )
    BicycleSignField(
        label = "Password:",
        value = uiState.password,
        onValueChange = {
            viewModel.updatePW(it)
        },
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BicycleSignField(label: String, value: String, onValueChange: (String) -> Unit) {
    val keyboard = LocalSoftwareKeyboardController.current
    var stateValue by remember { mutableStateOf(value) }
    OutlinedTextField(
        value = stateValue,
        onValueChange = {
            stateValue = it //updates view
            onValueChange.invoke(it) //updates model
                        },
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = {
                keyboard?.hide()
                stateValue = ""
                onValueChange.invoke("")
            })
            {
                Icon(Icons.Filled.Clear, "clear")
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboard?.hide()
            },
            onDone = {
                keyboard?.hide()
            }
        ),
        label = { Text(text = label) },
        modifier = Modifier
            .fillMaxWidth()
    )
}