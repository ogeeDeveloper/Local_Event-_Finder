//package com.ogeedeveloper.local_event_finder_frontend.ui.components
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.height
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.MoreVert
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import com.ogeedeveloper.local_event_finder_frontend.ui.theme.LocaleventfinderfrontendTheme
//
///**
// * A simple top app bar with a title and optional back button
// */
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AppTopBar(
//    title: String,
//    onNavigateBack: (() -> Unit)? = null,
//    modifier: Modifier = Modifier
//) {
//    TopAppBar(
//        title = {
//            Text(
//                text = title,
//                fontWeight = FontWeight.Bold,
//                color = MaterialTheme.colorScheme.onPrimary
//            )
//        },
//        navigationIcon = {
//            if (onNavigateBack != null) {
//                IconButton(onClick = onNavigateBack) {
//                    Icon(
//                        imageVector = Icons.Default.ArrowBack,
//                        contentDescription = "Back",
//                        tint = MaterialTheme.colorScheme.onPrimary
//                    )
//                }
//            }
//        },
//        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = MaterialTheme.colorScheme.primary,
//            titleContentColor = MaterialTheme.colorScheme.onPrimary,
//            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
//        ),
//        modifier = modifier
//    )
//}
//
///**
// * Reusable app top bar component with actions
// */
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AppTopBarWithActions(
//    title: String,
//    onNavigateBack: (() -> Unit)? = null,
//    modifier: Modifier = Modifier,
//    actions: @Composable () -> Unit
//) {
//    TopAppBar(
//        title = {
//            Text(
//                text = title,
//                fontWeight = FontWeight.Bold,
//                color = MaterialTheme.colorScheme.onPrimary
//            )
//        },
//        navigationIcon = {
//            if (onNavigateBack != null) {
//                IconButton(onClick = onNavigateBack) {
//                    Icon(
//                        imageVector = Icons.Default.ArrowBack,
//                        contentDescription = "Back",
//                        tint = MaterialTheme.colorScheme.onPrimary
//                    )
//                }
//            }
//        },
//        actions = actions,
//        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = MaterialTheme.colorScheme.primary,
//            titleContentColor = MaterialTheme.colorScheme.onPrimary,
//            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
//        ),
//        modifier = modifier
//    )
//}
//
///**
// * Action button for the app top bar
// */
//@Composable
//fun AppTopBarAction(
//    icon: ImageVector,
//    contentDescription: String,
//    onClick: () -> Unit
//) {
//    IconButton(onClick = onClick) {
//        Icon(
//            imageVector = icon,
//            contentDescription = contentDescription,
//            tint = MaterialTheme.colorScheme.onPrimary
//        )
//    }
//}
//
//@Preview
//@Composable
//fun AppTopBarPreview() {
//    LocaleventfinderfrontendTheme {
//        Column {
//            // With back button
//            AppTopBar(
//                title = "Screen Title",
//                onNavigateBack = {}
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // With back button and actions
//            AppTopBarWithActions(
//                title = "Screen Title",
//                onNavigateBack = {},
//                actions = {
//                    AppTopBarAction(
//                        icon = Icons.Default.MoreVert,
//                        contentDescription = "More Options",
//                        onClick = {}
//                    )
//                }
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Without back button
//            AppTopBar(
//                title = "Screen Title"
//            )
//        }
//    }
//}
