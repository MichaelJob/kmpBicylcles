package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import model.BicyclesViewModel


@Composable
fun BicycleEditDetails(viewModel: BicyclesViewModel) {
    val scrollState = rememberScrollState()
    val uiState by viewModel.uiState.collectAsState()
    with(uiState.currentBicycle!!) {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
                .verticalScroll(scrollState),
            content = {
                BicycleEditField(
                    label = "Bicycle name:",
                    value = bikename,
                    onValueChange = {
                        bikename = it
                        viewModel.checkBicyclenameIsUnique()
                    },
                )
                BicycleEditField(
                    label = "Frame #:",
                    value = framenumber,
                    onValueChange = { framenumber = it
                        true
                    },
                )
                BicycleEditField(
                    label = "Category:",
                    value = category,
                    onValueChange = { category = it
                        true
                    },
                )
                BicycleEditField(
                    label = "Year:",
                    value = year,
                    onValueChange = { year = it
                        true
                    },
                )
                BicycleEditField(
                    label = "Price:",
                    value = price,
                    onValueChange = { price = it
                        true
                    },
                )
                BicycleEditField(
                    label = "Description:",
                    singleLine = false,
                    value = description,
                    onValueChange = {
                        description = it
                        true
                    },
                )
            }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BicycleEditField(label: String, value: String, singleLine: Boolean = true, onValueChange: (String) -> Boolean) {
    val keyboard = LocalSoftwareKeyboardController.current
    var stateValue by remember { mutableStateOf(value) }
    var valid by remember { mutableStateOf(true) }
    OutlinedTextField(
        value = stateValue,
        onValueChange = {
            stateValue = it //updates view
            valid = onValueChange.invoke(it) //updates model
                        },
        singleLine = singleLine,
        colors = TextFieldDefaults.textFieldColors(
            textColor = if (valid) MaterialTheme.colors.onSurface else MaterialTheme.colors.error
        ),
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