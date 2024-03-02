package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import data.Bicycle
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import model.BicyclesViewModel

@Composable
fun BicyclesUI(viewModel: BicyclesViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        TopAppBar {
            Text(
                text = if (uiState.showDetail) "Your bicycle" else "Bicycles",
                modifier = Modifier.padding(10.dp),
                style = typography.h6,
            )
            if (uiState.showDetail) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Detail"
                )
            } else {
                Icon(
                    imageVector = Icons.Rounded.List,
                    contentDescription = "List"
                )
            }

        }
        if (uiState.showDetail) {
            BicycleDetailPage(viewModel)
        } else {
            BicyclesPage(viewModel)
        }
    }

}


@Composable
fun BicyclesPage(viewModel: BicyclesViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(30.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(uiState.bicycles) {
            BicycleImageCell(it, viewModel)
        }
    }
}

@Composable
fun BicycleDetailPage(viewModel: BicyclesViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.fillMaxWidth().weight(0.95F)) {
            CurrentBicycleImage(uiState.currentBicycle!!, showSmall = uiState.showEdit)
            Text(
                text = "Bicycle details:",
                style = typography.h6,
            )
            if (uiState.showEdit) {
                BicycleEditDetails(uiState.currentBicycle!!)
            } else {
                BicycleDetails(uiState.currentBicycle!!)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
                .weight(0.05F)
                .background(Color.DarkGray)
                .padding(horizontal = 30.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            if (uiState.showEdit) {
                //on Edit Detail View
                Button(onClick = { viewModel.showEdit(false) }) {
                    Text("Back")
                }
                Button(onClick = { viewModel.save() }) {
                    Text("Save")
                }
            } else {
                //on Detail View
                Button(onClick = { viewModel.showDetail(false) }) {
                    Text("Back")
                }
                Button(onClick = { viewModel.showEdit() }) {
                    Text("Edit")
                }
            }

        }
    }
}

@Composable
fun BicycleDetails(currentBicycle: Bicycle) {
    val scrollState = rememberScrollState()
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp).verticalScroll(scrollState),
        content = {
            BicycleDetailRow(label = "Bicycle name:", value = currentBicycle.bikename)
            BicycleDetailRow(label = "Category:", value = currentBicycle.category)
            BicycleDetailRow(label = "Year:", value = currentBicycle.year)
            BicycleDetailRow(label = "Price:", value = currentBicycle.price)
            BicycleDetailColumn(label = "Description:", value = currentBicycle.description)
        }
    )
}

@Composable
fun BicycleDetailRow(label: String, value: String) {
    Row(
        Modifier.fillMaxWidth().padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = label)
        Text(text = value)
    }
}

@Composable
fun BicycleDetailColumn(label: String, value: String) {
    Column (
        Modifier.fillMaxWidth().padding(20.dp),
    ) {
        Text(label)
        Text(value)
    }
}

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

@Composable
fun BicycleImageCell(bicycle: Bicycle, viewModel: BicyclesViewModel) {
    KamelImage(
        asyncPainterResource("https://www.michaeljob.ch/bicycles/${bicycle.imagePath}"),
        "${bicycle.category} by ${bicycle.bikename}",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxWidth().aspectRatio(1.0f).padding(5.dp).clickable {
            viewModel.selectBicycle(bicycle)
        }
    )
}

@Composable
fun CurrentBicycleImage(bicycle: Bicycle, showSmall: Boolean = false) {
    Row (
        modifier = Modifier.fillMaxWidth().padding(10.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        KamelImage(
            asyncPainterResource("https://www.michaeljob.ch/bicycles/${bicycle.imagePath}"),
            "${bicycle.category} by ${bicycle.bikename}",
            contentScale = ContentScale.Inside,
            modifier = Modifier.fillMaxWidth(if (showSmall) 0.2F else 1.0F).aspectRatio(1.0f).padding(5.dp)
        )
    }

}