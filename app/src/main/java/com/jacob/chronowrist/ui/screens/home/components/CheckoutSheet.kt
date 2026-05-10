package com.jacob.chronowrist.ui.screens.home.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.jacob.chronowrist.ui.viewmodel.CartViewModel
import kotlin.math.roundToInt

enum class PaymentMethod(val label: String, val sub: String, val icon: ImageVector) {
    CARD("Credit / Debit Card", "Visa, Mastercard, Amex", Icons.Outlined.CreditCard),
    MPESA("M-Pesa", "Pay using Safaricom M-Pesa", Icons.Outlined.PhoneAndroid),
    PAYPAL("PayPal", "Pay via PayPal account", Icons.Outlined.AccountBalance)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutSheet(
    cartViewModel: CartViewModel,
    onDismiss: () -> Unit,
    onOrderPlaced: (orderId: String, total: Double) -> Unit
) {
    var selectedMethod by remember { mutableStateOf(PaymentMethod.CARD) }
    var cardholderName by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var mpesaNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = MaterialTheme.shapes.large,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 40.dp)
        ) {
            Text(
                text = "Checkout",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Payment method selector
            Text(
                text = "Payment Method",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 10.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                PaymentMethod.entries.forEach { method ->
                    PaymentMethodTile(
                        method = method,
                        selected = selectedMethod == method,
                        onClick = { selectedMethod = method; errorMsg = null }
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            if (selectedMethod == PaymentMethod.CARD) {
                Text("Card Details", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                OutlinedTextField(
                    value = cardNumber,
                    onValueChange = { cardNumber = it },
                    label = { Text("Card Number") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            } else if (selectedMethod == PaymentMethod.MPESA) {
                Text("M-Pesa Details", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                OutlinedTextField(
                    value = mpesaNumber,
                    onValueChange = { mpesaNumber = it },
                    label = { Text("M-Pesa Phone Number") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("0712345678") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
            }

            Spacer(Modifier.height(20.dp))

            Text("Shipping Address", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Full Address") },
                modifier = Modifier.fillMaxWidth()
            )

            errorMsg?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(20.dp))
            OrderSummaryCard(cartViewModel)
            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    if (address.isBlank()) {
                        errorMsg = "Please enter an address"
                    } else if (selectedMethod == PaymentMethod.CARD && cardNumber.length < 16) {
                        errorMsg = "Invalid Card Number"
                    } else if (selectedMethod == PaymentMethod.MPESA && mpesaNumber.length < 10) {
                        errorMsg = "Invalid M-Pesa Number"
                    } else {
                        val orderId = "CW${(10000..99999).random()}"
                        onOrderPlaced(orderId, cartViewModel.total)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Confirm & Pay ${"$"}${"%,.0f".format(cartViewModel.total)}", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun PaymentMethodTile(method: PaymentMethod, selected: Boolean, onClick: () -> Unit) {
    val borderColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
    Surface(
        modifier = Modifier.fillMaxWidth().border(1.dp, borderColor, MaterialTheme.shapes.medium).clickable { onClick() },
        color = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(method.icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Column(Modifier.weight(1f)) {
                Text(method.label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                Text(method.sub, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (selected) Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun OrderSummaryCard(cartViewModel: CartViewModel) {
    Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.medium) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text("Order Summary", fontWeight = FontWeight.SemiBold)
            cartViewModel.cartItems.forEach { item ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("${item.watch.name} x${item.quantity}", style = MaterialTheme.typography.bodySmall)
                    Text("${"$"}${item.watch.price * item.quantity}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
