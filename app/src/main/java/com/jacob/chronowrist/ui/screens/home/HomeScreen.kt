package com.jacob.chronowrist.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jacob.chronowrist.R
import com.jacob.chronowrist.data.model.Watch
import com.jacob.chronowrist.data.model.WatchCategory
import com.jacob.chronowrist.ui.navigation.ROUTES
import com.jacob.chronowrist.ui.screens.home.components.*
import com.jacob.chronowrist.ui.theme.ChronoWristTheme
import com.jacob.chronowrist.ui.viewmodel.CartViewModel
import kotlinx.coroutines.launch

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
    
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                // Account Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = homeViewModel.userProfile?.fullName ?: "User",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = homeViewModel.userProfile?.email ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(8.dp))
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = "Balance: $${"%,.2f".format(homeViewModel.userProfile?.balance ?: 0.0)}",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
                
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                
                Text(
                    "Account", 
                    style = MaterialTheme.typography.labelLarge, 
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = MaterialTheme.colorScheme.primary
                )

                NavigationDrawerItem(
                    label = { Text("Recent Purchases") },
                    selected = false,
                    onClick = { /* Navigate to purchases */ },
                    icon = { Icon(Icons.Default.ShoppingBag, null) }
                )
                
                NavigationDrawerItem(
                    label = { Text("Loyalty Points") },
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.Star, null) }
                )
                
                Spacer(Modifier.weight(1f))
                
                NavigationDrawerItem(
                    label = { Text("Logout") },
                    selected = false,
                    onClick = {
                        homeViewModel.logout {
                            navController.navigate(ROUTES.Login.name) {
                                popUpTo(ROUTES.Home.name) { inclusive = true }
                            }
                        }
                    },
                    icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, null) },
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_chronowrist),
                                contentDescription = null,
                                modifier = Modifier.size(32.dp).padding(end = 8.dp),
                                tint = Color.Unspecified // Show actual icon colors
                            )
                            Text(
                                text = "ChronoWrist",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
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
                    }
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
                item(span = { GridItemSpan(2) }) { HeroBanner() }
                item(span = { GridItemSpan(2) }) { FeatureStrip() }
                item(span = { GridItemSpan(2) }) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        CategoryFilterRow(
                            selectedCategory = homeViewModel.selectedCategory,
                            onCategorySelected = { homeViewModel.selectCategory(it) }
                        )
                    }
                }

                items(
                    items = homeViewModel.filteredWatches,
                    key = { it.id }
                ) { watch ->
                    WatchCard(
                        watch = watch,
                        onClick = { selectedWatch = watch },
                        onAddToCart = { cartViewModel.addToCart(watch) },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }
    }

    // Sheets
    selectedWatch?.let { watch ->
        WatchDetailSheet(
            watch = watch,
            onDismiss = { selectedWatch = null },
            onAddToCart = { cartViewModel.addToCart(it) }
        )
    }

    if (showCart) {
        CartDrawer(
            cartViewModel = cartViewModel,
            onClose = { showCart = false },
            onCheckout = {
                showCart = false
                showCheckout = true
            }
        )
    }

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

    orderResult?.let { (orderId, total) ->
        OrderConfirmationSheet(
            orderId = orderId,
            total = total,
            onDismiss = { orderResult = null },
            onTrackOrder = { id ->
                orderResult = null
                navController.navigate("track_order/$id")
            }
        )
    }
}

@Composable
private fun HeroBanner() {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Precision on your wrist.", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            Button(onClick = {}) { Text("Shop Now") }
        }
    }
}

@Composable
private fun FeatureStrip() {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
        Text("✦ Swiss Certified", style = MaterialTheme.typography.labelSmall)
        Text("⬡ Free Shipping", style = MaterialTheme.typography.labelSmall)
        Text("↺ 30-Day Returns", style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun CategoryFilterRow(selectedCategory: WatchCategory?, onCategorySelected: (WatchCategory?) -> Unit) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(horizontal = 16.dp)) {
        item { FilterChip(selected = selectedCategory == null, onClick = { onCategorySelected(null) }, label = { Text("All") }) }
        items(WatchCategory.entries) { cat ->
            FilterChip(selected = selectedCategory == cat, onClick = { onCategorySelected(cat) }, label = { Text(cat.label) })
        }
    }
}
