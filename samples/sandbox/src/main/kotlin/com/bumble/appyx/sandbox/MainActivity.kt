package com.bumble.appyx.sandbox

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.bumble.appyx.core.integration.NodeHost
import com.bumble.appyx.core.integrationpoint.NodeActivity
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.sandbox.client.cardsexample.CardsExampleNode
import com.bumble.appyx.sandbox.client.container.ContainerBuilder
import com.bumble.appyx.sandbox.client.promoter.PromoterTeaserNode
import com.bumble.appyx.sandbox.client.spotlightadvancedexample.SpotlightAdvancedExampleNode
import com.bumble.appyx.sandbox.ui.AppyxSandboxTheme

@OptIn(ExperimentalUnitApi::class)
class MainActivity : NodeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppyxSandboxTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Column {
                        NodeHost(integrationPoint = appyxIntegrationPoint) {
                            PromoterTeaserNode(buildContext = it)
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppyxSandboxTheme {
        Column {
            ContainerBuilder().build(buildContext = BuildContext.root(null)).Compose()
        }
    }
}
