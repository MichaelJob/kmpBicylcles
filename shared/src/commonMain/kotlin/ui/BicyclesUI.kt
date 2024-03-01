package ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import data.Bicycle
import model.BicyclesViewModel


@Composable
fun BicyclesPage(viewModel: BicyclesViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(uiState.currentBicycle==null) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                //  verticalArrangement = Arrangement.SpaceEvenly,
            )  {
                items(uiState.bicycles) {
                    BicycleImageCell(it, viewModel)
                }
            }
        }

        AnimatedVisibility(uiState.currentBicycle!=null) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.fillMaxSize().padding(horizontal = 5.dp),
                content = {
                    item {
                        CurrentBicycleImage(uiState.currentBicycle!!)
                    }
                    item {
                        BicycleDetails(uiState.currentBicycle!!)
                    }
                }
            )
        }
    }
}

@Composable
fun BicycleDetails(currentBicycle: Bicycle) {
    Column {
        Text("Bicycle details")
        Text("Bicycle name: ${currentBicycle.bikename}")
        Text("Category: ${currentBicycle.category}")
        Text("Year: ${currentBicycle.year}")
        Text("Price: ${currentBicycle.priceCent}")
        Text("Description: ${currentBicycle.description}")
    }
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
fun CurrentBicycleImage(bicycle: Bicycle) {
    KamelImage(
        asyncPainterResource("https://www.michaeljob.ch/bicycles/${bicycle.imagePath}"),
        "${bicycle.category} by ${bicycle.bikename}",
        contentScale = ContentScale.Inside,
        modifier = Modifier.fillMaxWidth().aspectRatio(1.0f).padding(5.dp)
    )
}