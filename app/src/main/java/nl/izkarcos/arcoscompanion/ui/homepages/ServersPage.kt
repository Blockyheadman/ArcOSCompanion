package nl.izkarcos.arcoscompanion.ui.homepages

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import nl.izkarcos.arcoscompanion.ui.theme.ArcOSCompanionTheme

@Composable
fun ServersPage() {

}

@Preview(showBackground = true)
@Composable
fun ServersPagePreview() {
    ArcOSCompanionTheme(darkTheme = true, dynamicColor = false) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            ServersPage()
        }
    }
}