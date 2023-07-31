package com.bumble.appyx.interactions.bottomnav

import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector


//abstract class AbstractAppyxNavItem : AppyxNavItem {
//    abstract val appyxNavItem: AppyxNavItem
//    override val text = appyxNavItem.text
//    override val icon = appyxNavItem.icon
//    override val content = appyxNavItem.content
//}
//
//interface AppyxNavItem {
//
//    val text: @Composable (isSelected: Boolean) -> Unit
//
//    val icon: @Composable (isSelected: Boolean) -> Unit
//
//    val content: @Composable () -> Unit
//
//    companion object {
//        fun from(
//            text: String,
//            unselectedIcon: ImageVector,
//            selectedIcon: ImageVector,
//            content: @Composable () -> Unit,
//            iconModifier: Modifier = Modifier
//        ) = object : AppyxNavItem {
//
//            override val text: @Composable (isSelected: Boolean) -> Unit = { Text(text, color = MaterialTheme.colorScheme.onPrimaryContainer) },
//
//            override val icon: @Composable (isSelected: Boolean) -> Unit = { isSelected ->
//                Icon(
//                    imageVector = if (isSelected) selectedIcon else unselectedIcon,
//                    contentDescription = text,
//                    modifier = iconModifier,
//                    tint = MaterialTheme.colorScheme.onPrimaryContainer
//                )
//            }
//
//            override val content = content
//        }
//    }
//}

class AppyxNavItemQ(
    val text: @Composable (isSelected: Boolean) -> Unit,
    val icon: @Composable (isSelected: Boolean) -> Unit,
    val content: @Composable () -> Unit
) {
    constructor(
        text: String,
        unselectedIcon: ImageVector,
        selectedIcon: ImageVector,
        content: @Composable () -> Unit,
        iconModifier: Modifier = Modifier
    ) : this(
        text = { Text(text, color = MaterialTheme.colorScheme.onPrimaryContainer) },
        icon = { isSelected ->
            Icon(
                imageVector = if (isSelected) selectedIcon else unselectedIcon,
                contentDescription = text,
                modifier = iconModifier,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        },
        content = content
    )
}
