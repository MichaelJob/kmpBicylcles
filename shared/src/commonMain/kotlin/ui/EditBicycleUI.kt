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
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import data.Bicycle


@Composable
fun BicycleEditDetails(currentBicycle: Bicycle) {
    val scrollState = rememberScrollState()
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp).verticalScroll(scrollState),
        content = {
            BicycleEditField(
                label = "Bicycle name:",
                value = currentBicycle.bikename,
                onValueChange = { currentBicycle.bikename = it },
            )
            BicycleEditField(
                label = "Category:",
                value = currentBicycle.category,
                onValueChange = { currentBicycle.category = it },
            )
            BicycleEditField(
                label = "Year:",
                value = currentBicycle.year,
                onValueChange = { currentBicycle.year = it },
            )
            BicycleEditField(
                label = "Price:",
                value = currentBicycle.price,
                onValueChange = { currentBicycle.price = it },
            )
            BicycleEditField(
                label = "Description:",
                value = currentBicycle.description,
                onValueChange = { currentBicycle.description = it },
            )
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BicycleEditField(label: String, value: String, onValueChange: (String) -> Unit) {
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