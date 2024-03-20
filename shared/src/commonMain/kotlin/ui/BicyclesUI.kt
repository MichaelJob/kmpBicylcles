package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import data.Bicycle
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import model.BicyclesViewModel

@Composable
fun BicyclesUI(viewModel: BicyclesViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    Box(
        Modifier
            .background(colors.background)
            .windowInsetsPadding(WindowInsets.safeDrawing) //Box for safe areas
    ) {
        // App Content goes here
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            TopAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (uiState.showDetail) uiState.currentBicycle?.bikename ?: "your bicycle" else "Bicycles",
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
                    if (uiState.isLoggedIn) {
                        IconButton(onClick = { viewModel.logout() }) {
                            Icon(
                                imageVector = Icons.Filled.ExitToApp,
                                contentDescription = "Logout"
                            )
                        }
                    }
                }
            }
            if (uiState.isLoggedIn) {
                if (uiState.showDetail) {
                    BicycleDetailPage(viewModel)
                } else {
                    BicyclesPage(viewModel)
                }
            } else {
                //user is logged out (or first app start - not registered)
                SignUpIn(viewModel)
            }
        }
    }
}


@Composable
fun BicyclesPage(viewModel: BicyclesViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val bicycles by mutableStateOf(uiState.bicycles)
    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (bicycles.isEmpty()) {
            item {
                Text(
                    text = "no bicycles yet",
                    modifier = Modifier.padding(40.dp)
                )
            }
            item {
                CircularProgressIndicator(modifier = Modifier.padding(40.dp))
            }
        }
        itemsIndexed(bicycles) { index, bicycle ->
            BicycleImageCell(index+1, bicycle, viewModel)
        }
    }
}


@Composable
fun BicycleImageCell(index: Int, bicycle: Bicycle, viewModel: BicyclesViewModel) {
    Box {
        KamelImage(
            resource = asyncPainterResource(bicycle.storagePath),
            contentDescription = bicycle.bikename,
            contentScale = ContentScale.Crop,
            onLoading = { CircularProgressIndicator() },
            onFailure = { Text("loading failed") },
            modifier = Modifier.fillMaxWidth()
                .aspectRatio(1.0f)
                .padding(5.dp)
                .clickable {
                    viewModel.selectBicycle(bicycle)
                }
        )
        Row {
            val secondayColor = colors.onSecondary
            Text(
                text = "$index",
                style = typography.h6,
                color = colors.onPrimary,
                modifier = Modifier.padding(8.dp)
                    .drawBehind {
                        drawCircle(
                            color = secondayColor,
                            radius = this.size.maxDimension*0.75F
                        )
                    },
            )
            Text(
                text = bicycle.bikename,
                style = typography.h6,
                modifier = Modifier.padding(top=8.dp, bottom=8.dp, start=14.dp),
            )

        }
    }

}