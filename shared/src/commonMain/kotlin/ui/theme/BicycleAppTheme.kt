package ui.theme

import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BicycleAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = MaterialTheme.colors.copy(
            primary = blue900,
            secondary = blue300
        ),
        shapes = MaterialTheme.shapes.copy(
            small = AbsoluteCutCornerShape(2.dp),
            medium = AbsoluteCutCornerShape(4.dp),
            large = AbsoluteCutCornerShape(6.dp)
        )
    ) {
        content()
    }
}