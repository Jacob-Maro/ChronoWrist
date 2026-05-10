package com.jacob.chronowrist.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackOrderScreen(orderId: String, navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Track Order #$orderId") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Text(
                text = "Order Status",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(Modifier.height(32.dp))
            
            TrackingStep(title = "Order Placed", description = "Your order has been received", isCompleted = true)
            TrackingLine(isCompleted = true)
            TrackingStep(title = "Payment Confirmed", description = "Transaction processed successfully", isCompleted = true)
            TrackingLine(isCompleted = false)
            TrackingStep(title = "Processing", description = "We are preparing your watch for shipment", isCompleted = false)
            TrackingLine(isCompleted = false)
            TrackingStep(title = "Shipped", description = "Your order is on the way", isCompleted = false)
            TrackingLine(isCompleted = false)
            TrackingStep(title = "Delivered", description = "Package arrived at your destination", isCompleted = false)
        }
    }
}

@Composable
fun TrackingStep(title: String, description: String, isCompleted: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    if (isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(18.dp))
            }
        }
        
        Spacer(Modifier.width(16.dp))
        
        Column {
            Text(text = title, fontWeight = FontWeight.Bold, color = if (isCompleted) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun TrackingLine(isCompleted: Boolean) {
    Box(
        modifier = Modifier
            .padding(start = 15.dp)
            .width(2.dp)
            .height(40.dp)
            .background(if (isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
    )
}
