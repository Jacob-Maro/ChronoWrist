package com.jacob.chronowrist.ui.screens.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jacob.chronowrist.data.model.Watch
import com.jacob.chronowrist.data.model.WatchBadge

@Composable
fun WatchCard(
    watch: Watch,
    onClick: () -> Unit,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier,
    featured: Boolean = false
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        if (featured) {
            FeaturedWatchContent(watch, onAddToCart)
        } else {
            GridWatchContent(watch, onAddToCart)
        }
    }
}

@Composable
private fun FeaturedWatchContent(watch: Watch, onAddToCart: () -> Unit) {
    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
        WatchImageBox(watch = watch, size = 140.dp, featured = true)
        Column(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = watch.brand.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.5.sp
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = watch.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                StarRow(rating = watch.rating, reviewCount = watch.reviewCount, large = true)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "$${"%,.0f".format(watch.price)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    watch.oldPrice?.let {
                        Text(
                            text = "${"$"}${"%,.0f".format(it)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }
                AddButton(onAddToCart)
            }
        }
    }
}

@Composable
private fun GridWatchContent(watch: Watch, onAddToCart: () -> Unit) {
    Column {
        WatchImageBox(watch = watch, size = 160.dp)
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = watch.brand.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.sp
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = watch.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(4.dp))
            StarRow(rating = watch.rating, reviewCount = watch.reviewCount)
            Spacer(Modifier.height(6.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "$${"%,.0f".format(watch.price)}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    watch.oldPrice?.let {
                        Text(
                            text = "${"$"}${"%,.0f".format(it)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }
                AddButton(onAddToCart)
            }
        }
    }
}

@Composable
private fun WatchImageBox(watch: Watch, size: androidx.compose.ui.unit.Dp, featured: Boolean = false) {
    Box(
        modifier = if (featured)
            Modifier
                .width(size)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        else
            Modifier
                .fillMaxWidth()
                .height(size)
                .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        // Replace this placeholder with an actual Image composable once you have drawables
        // e.g. Image(painter = painterResource(watch.imageRes!!), contentDescription = watch.name)
       /* Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = watch.brand.take(2).uppercase(),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }*/
        if (watch.imageRes != null) {
            Image(
                painter = painterResource(id = watch.imageRes),
                contentDescription = watch.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            // initials fallback circle
        }

        watch.badge?.let {
            val (bgColor, textColor) = when (it) {
                WatchBadge.NEW -> Pair(Color(0xFF9FE1CB), Color(0xFF0F6E56))
                WatchBadge.SALE -> Pair(Color(0xFFF5C4B3), Color(0xFF993C1D))
                WatchBadge.HOT -> Pair(Color(0xFFFAC775), Color(0xFF854F0B))
            }
            Surface(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp),
                shape = CircleShape,
                color = bgColor
            ) {
                Text(
                    text = it.label.uppercase(),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = textColor,
                    letterSpacing = 0.8.sp
                )
            }
        }
    }
}

@Composable
fun StarRow(rating: Int, reviewCount: Int, large: Boolean = false) {
    val starSize = if (large) 14.dp else 12.dp
    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(5) { i ->
            Icon(
                imageVector = if (i < rating) Icons.Filled.Star else Icons.Outlined.StarOutline,
                contentDescription = null,
                tint = Color(0xFFEF9F27),
                modifier = Modifier.size(starSize)
            )
        }
        Spacer(Modifier.width(4.dp))
        Text(
            text = "($reviewCount)",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun AddButton(onAddToCart: () -> Unit) {
    IconButton(
        onClick = onAddToCart,
        modifier = Modifier
            .size(32.dp)
            .border(0.5.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.small)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add to cart",
            modifier = Modifier.size(16.dp)
        )
    }
}
