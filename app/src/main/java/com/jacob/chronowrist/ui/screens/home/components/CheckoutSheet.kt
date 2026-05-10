package com.jacob.chronowrist.ui.screens.home.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    MOBILE("Mobile Pay", "Apple Pay, Google Pay", Icons.Outlined.PhoneAndroid),
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
    var expiry by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

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
                        onClick = { selectedMethod = method }
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // Card fields — only shown for CARD method
            if (selectedMethod == PaymentMethod.CARD) {
                Text(
                    text = "Card Details",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                OutlinedTextField(
                    value = cardholderName,
                    onValueChange = { cardholderName = it },
                    label = { Text("Cardholder Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium
                )
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = cardNumber,
                    onValueChange = { raw ->
                        val digits = raw.filter { it.isDigit() }.take(16)
                        cardNumber = digits.chunked(4).joinToString(" ")
                    },
                    label = { Text("Card Number") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = MaterialTheme.shapes.medium,
                    placeholder = { Text("1234 5678 9012 3456") }
                )
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = expiry,
                        onValueChange = { raw ->
                            val digits = raw.filter { it.isDigit() }.take(4)
                            expiry = if (digits.length >= 3) "${digits.take(2)} / ${digits.drop(2)}" else digits
                        },
                        label = { Text("Expiry") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        placeholder = { Text("MM / YY") },
                        shape = MaterialTheme.shapes.medium
                    )
                    OutlinedTextField(
                        value = cvv,
                        onValueChange = { if (it.length <= 4) cvv = it },
                        label = { Text("CVV") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = MaterialTheme.shapes.medium
                    )
                }
                Spacer(Modifier.height(10.dp))
            }

            // Shipping address
            Text(
                text = "Shipping Address",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 10.dp)
            )
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Full Address") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("123 Main St, City, Country") },
                shape = MaterialTheme.shapes.medium
            )

            Spacer(Modifier.height(20.dp))

            // Order summary
            OrderSummaryCard(cartViewModel)

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    val orderId = "CW${(10000..99999).random()}"
                    onOrderPlaced(orderId, cartViewModel.total)
                },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Place Order  —  ${"$"}${"%,.0f".format(cartViewModel.total)}",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Composable
private fun PaymentMethodTile(
    method: PaymentMethod,
    selected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (selected) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.outlineVariant
    val borderWidth = if (selected) 1.5.dp else 0.5.dp
    val bgColor = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.06f)
    else MaterialTheme.colorScheme.surface

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(borderWidth, borderColor, MaterialTheme.shapes.medium)
            .clickable { onClick() },
        color = bgColor,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = method.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(method.label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                Text(method.sub, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (selected) {
                Icon(Icons.Default.CheckCircle, contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
private fun OrderSummaryCard(cartViewModel: CartViewModel) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text("Order Summary", style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 10.dp))
            cartViewModel.cartItems.forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${item.watch.name} ×${item.quantity}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f))
                    Text("${"$"}${"%,.0f".format(item.watch.price * item.quantity)}",
                        style = MaterialTheme.typography.bodySmall)
                }
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            SummaryRow("Shipping", if (cartViewModel.shipping == 0.0) "Free" else "$15")
            SummaryRow("Tax (8%)", "${"$"}${(cartViewModel.tax).roundToInt()}")
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text("${"$"}${"%,.0f".format(cartViewModel.total)}",
                    style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodySmall)
    }
}
