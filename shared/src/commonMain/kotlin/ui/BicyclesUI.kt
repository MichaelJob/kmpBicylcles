package ui

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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
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
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
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
                if (!uiState.showDetail) {
                    IconButton(onClick = { viewModel.createNewBicycle() }) {
                        Icon(
                            imageVector = Icons.Filled.AddCircle,
                            contentDescription = "New bicycle"
                        )
                    }
                }
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
fun BicycleImageCell(bicycle: Bicycle, viewModel: BicyclesViewModel) {
    KamelImage(
        resource = asyncPainterResource(bicycle.getBicycleImagePath()),
        contentDescription = bicycle.bikename,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxWidth()
            .aspectRatio(1.0f)
            .padding(5.dp)
            .clickable {
                viewModel.selectBicycle(bicycle)
            }
    )
}