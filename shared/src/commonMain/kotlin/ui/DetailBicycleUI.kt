package ui

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import data.Bicycle
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import model.BicyclesViewModel


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
fun CurrentBicycleImage(bicycle: Bicycle, showSmall: Boolean = false) {
    Row (
        modifier = Modifier.fillMaxWidth().padding(10.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        KamelImage(
            resource = asyncPainterResource(bicycle.getBicycleImagePath()),
            contentDescription = bicycle.bikename,
            contentScale = ContentScale.Inside,
            modifier = Modifier.fillMaxWidth(if (showSmall) 0.2F else 1.0F)
                .aspectRatio(1.0f)
                .padding(5.dp)
        )
    }

}