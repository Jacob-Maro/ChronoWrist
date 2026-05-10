package com.jacob.chronowrist.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jacob.chronowrist.data.model.Watch
import com.jacob.chronowrist.data.model.WatchCategory
import com.jacob.chronowrist.ui.screens.home.components.CartDrawer
import com.jacob.chronowrist.ui.screens.home.components.CheckoutSheet
import com.jacob.chronowrist.ui.screens.home.components.OrderConfirmationSheet
import com.jacob.chronowrist.ui.screens.home.components.WatchCard
import com.jacob.chronowrist.ui.screens.home.components.WatchDetailSheet
import com.jacob.chronowrist.ui.theme.ChronoWristTheme
import com.jacob.chronowrist.ui.viewmodel.CartViewModel
import kotlin.collections.drop
import kotlin.collections.first
import kotlin.collections.isNotEmpty

/*
@Composable
fun HomeScreen(){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text="SMILE! ")
    }
}*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel()
) {
    var selectedWatch by remember { mutableStateOf<Watch?>(null) }
    var showCart by remember { mutableStateOf(false) }
    var showCheckout by remember { mutableStateOf(false) }
    var orderResult by remember { mutableStateOf<Pair<String, Double>?>(null) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "ChronoWrist",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Timekeeping, Elevated.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary,
                            letterSpacing = 0.5.sp
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* search */ }) {
                        Icon(Icons.Outlined.Search, contentDescription = "Search")
                    }
                    BadgedBox(
                        badge = {
                            if (cartViewModel.itemCount > 0) {
                                Badge { Text("${cartViewModel.itemCount}") }
                            }
                        }
                    ) {
                        IconButton(onClick = { showCart = true }) {
                            Icon(Icons.Outlined.ShoppingBag, contentDescription = "Cart")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            // ── Hero Banner ────────────────────────────────────────────────
            item(span = { GridItemSpan(2) }) {
                HeroBanner()
            }

            // ── Feature strip ──────────────────────────────────────────────
            item(span = { GridItemSpan(2) }) {
                FeatureStrip()
            }

            // ── Section header + category filter ──────────────────────────
            item(span = { GridItemSpan(2) }) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Collection",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        TextButton(onClick = {}) { Text("View all") }
                    }
                    CategoryFilterRow(
                        selectedCategory = homeViewModel.selectedCategory,
                        onCategorySelected = { homeViewModel.selectCategory(it) }
                    )
                }
            }

            // ── Featured card (first watch, full width) ────────────────────
            if (homeViewModel.filteredWatches.isNotEmpty()) {
                item(span = { GridItemSpan(2) }) {
                    val watch = homeViewModel.filteredWatches.first()
                    WatchCard(
                        watch = watch,
                        featured = true,
                        onClick = { selectedWatch = watch },
                        onAddToCart = { cartViewModel.addToCart(watch) },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            // ── Grid cards (remaining watches) ────────────────────────────
            items(
                items = homeViewModel.filteredWatches.drop(1),
                key = { it.id }
            ) { watch ->
                WatchCard(
                    watch = watch,
                    onClick = { selectedWatch = watch },
                    onAddToCart = { cartViewModel.addToCart(watch) },
                    modifier = Modifier
                        .padding(
                            start = 8.dp,
                            end = 8.dp
                        )
                )
            }
        }
    }

    // ── Watch Detail Bottom Sheet ──────────────────────────────────────────
    selectedWatch?.let { watch ->
        WatchDetailSheet(
            watch = watch,
            onDismiss = { selectedWatch = null },
            onAddToCart = { cartViewModel.addToCart(it) }
        )
    }

    // ── Cart Modal Drawer ──────────────────────────────────────────────────
    if (showCart) {
        val drawerState = rememberDrawerState(DrawerValue.Open)

        LaunchedEffect(drawerState.currentValue) {
            if (drawerState.currentValue == DrawerValue.Closed) {
                showCart = false
            }
        }
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = true,
            drawerContent = {
                CartDrawer(
                    cartViewModel = cartViewModel,
                    onClose = { showCart = false },
                    onCheckout = {
                        showCart = false
                        showCheckout = true
                    }
                )
            }
        ) {}
    }

    // ── Checkout Bottom Sheet ──────────────────────────────────────────────
    if (showCheckout) {
        CheckoutSheet(
            cartViewModel = cartViewModel,
            onDismiss = { showCheckout = false },
            onOrderPlaced = { orderId, total ->
                cartViewModel.clearCart()
                showCheckout = false
                orderResult = Pair(orderId, total)
            }
        )
    }

    // ── Order Confirmation ─────────────────────────────────────────────────
    orderResult?.let { (orderId, total) ->
        OrderConfirmationSheet(
            orderId = orderId,
            total = total,
            onDismiss = { orderResult = null }
        )
    }
}

@Composable
private fun HeroBanner() {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 28.dp)
        ) {
            Text(
                text = "New Collection — 2025",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 1.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Precision on\nyour wrist.",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Swiss-crafted movements. Free shipping over \$500.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = {},
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Shop Now", fontWeight = FontWeight.Bold)
                }
                OutlinedButton(
                    onClick = {},
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Our Story")
                }
            }
        }
    }
}

@Composable
private fun FeatureStrip() {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            FeatureItem(icon = "✦", label = "Swiss Certified", modifier = Modifier.weight(1f))
            VerticalDivider(modifier = Modifier.height(56.dp))
            FeatureItem(icon = "⬡", label = "Free Shipping", modifier = Modifier.weight(1f))
            VerticalDivider(modifier = Modifier.height(56.dp))
            FeatureItem(icon = "↺", label = "30-Day Returns", modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun FeatureItem(icon: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(icon, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun CategoryFilterRow(
    selectedCategory: WatchCategory?,
    onCategorySelected: (WatchCategory?) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 4.dp)
    ) {
        item {
            FilterChip(
                selected = selectedCategory == null,
                onClick = { onCategorySelected(null) },
                label = { Text("All") },
                shape = MaterialTheme.shapes.large
            )
        }
        items(WatchCategory.entries) { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick = { onCategorySelected(category) },
                label = { Text(category.label) },
                shape = MaterialTheme.shapes.large
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ChronoWristTheme {
        HomeScreen(navController = rememberNavController())
    }
}
