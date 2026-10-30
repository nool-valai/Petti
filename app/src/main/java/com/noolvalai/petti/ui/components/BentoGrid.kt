package com.noolvalai.petti.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.noolvalai.petti.domain.model.StorageCategory
import com.noolvalai.petti.domain.model.StorageSeverity

@Composable
fun BentoGrid(
    categories: List<StorageCategory>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp,
            // Adds dynamic padding so the list scrolls neatly above the system nav bar
            bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories, key = { it.id }) { category ->
            BentoTile(
                category = category,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F13)
@Composable
private fun BentoGridPreview() {
    val sampleCategories = listOf(
        StorageCategory("1", "Media", 118_111_600_640L, 45.2f, StorageSeverity.CRITICAL),
        StorageCategory("2", "Apps", 45_000_000_000L, 17.2f, StorageSeverity.WARNING),
        StorageCategory("3", "System & Other", 35_000_000_000L, 13.4f, StorageSeverity.NOTICE),
        StorageCategory("4", "Free Space", 63_000_000_000L, 24.2f, StorageSeverity.SAFE)
    )

    BentoGrid(categories = sampleCategories)
}