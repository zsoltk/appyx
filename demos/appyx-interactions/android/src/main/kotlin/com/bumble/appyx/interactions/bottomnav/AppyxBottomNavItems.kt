package com.bumble.appyx.interactions.bottomnav

import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.ViewCarousel
import androidx.compose.material.icons.outlined.Extension
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material.icons.outlined.ViewCarousel
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import com.bumble.appyx.components.experimental.cards.android.DatingCards
import com.bumble.appyx.components.experimental.puzzle15.android.Puzzle15
import com.bumble.appyx.components.internal.testdrive.android.TestDriveExperiment
import com.bumble.appyx.components.spotlight.ui.sliderrotation.SpotlightSliderRotation
import com.bumble.appyx.interactions.sample.SpotlightExperiment

val appyxBottomNavItems = listOf(
    AppyxBottomNavItem(
        text = "Cards",
        unselectedIcon = Outlined.Style,
        selectedIcon = Filled.Style,
        iconModifier = Modifier.rotate(180f),
        content = { DatingCards() }
    ),

    AppyxBottomNavItem(
        text = "Spotlight",
        unselectedIcon = Outlined.ViewCarousel,
        selectedIcon = Filled.ViewCarousel,
        content = { SpotlightExperiment { SpotlightSliderRotation(it) } }
    ),

    AppyxBottomNavItem(
        text = "TestDrive",
        unselectedIcon = Outlined.GridView,
        selectedIcon = Filled.GridViewCustom,
        content = { TestDriveExperiment() }
    ),

    AppyxBottomNavItem(
        text = "Puzzle",
        unselectedIcon = Outlined.Extension,
        selectedIcon = Filled.Extension,
        content = { Puzzle15() }
    ),
)
