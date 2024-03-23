package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import data.Bicycle
import model.BicyclesViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource


@Composable
fun BicycleDetailPage(viewModel: BicyclesViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    Column(
        Modifier.fillMaxSize().padding(horizontal = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.fillMaxWidth().weight(0.9F)) {
            CurrentBicycleImage(uiState.currentBicycle!!, showSmall = uiState.showEdit)
            Text(
                text = "Bicycle details:",
                style = typography.h6,
                modifier = Modifier.padding(10.dp)
            )
            if (uiState.showEdit) {
                BicycleEditDetails(uiState.currentBicycle!!)
            } else {
                BicycleDetails(uiState.currentBicycle!!)
            }
        }
        Row(
            modifier = Modifier.fillMaxSize()
                .weight(0.1F)
                .padding(vertical = 4.dp, horizontal = 30.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            if (uiState.showEdit) {
                //on Edit Detail View
                Button(onClick = { viewModel.showEdit(false) }) {
                    Text("Back")
                }
                Button(onClick = { viewModel.remove() }) {
                    Text("Remove bicycle")
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
        Modifier.fillMaxWidth().padding(2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = label)
        Text(text = value)
    }
}

@Composable
fun BicycleDetailColumn(label: String, value: String) {
    Column (
        Modifier.fillMaxWidth().padding(2.dp),
    ) {
        Text(label)
        Text(value)
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CurrentBicycleImage(bicycle: Bicycle, showSmall: Boolean = false) {
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        if (bicycle.imageBitmap!=null){
            Image(
                bitmap = bicycle.imageBitmap!!,
                contentDescription = bicycle.bikename,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth(if (showSmall) 0.2F else 1.0F)
                    .aspectRatio(1.0f)
            )
        } else {
            Image(
                painter = painterResource("defaultbicycle.jpg"),
                contentDescription = bicycle.bikename,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth(if (showSmall) 0.2F else 1.0F)
                    .aspectRatio(1.0f),
            )
        }
    }
}