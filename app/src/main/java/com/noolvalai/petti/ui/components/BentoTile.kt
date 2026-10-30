package com.noolvalai.petti.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.noolvalai.petti.domain.model.StorageCategory
import com.noolvalai.petti.domain.model.StorageSeverity
import com.noolvalai.petti.ui.theme.*
import com.noolvalai.petti.util.formatBytes

@Composable
fun BentoTile(
    category: StorageCategory,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    val isHighContrast = LocalHighContrast.current
    val view = LocalView.current

    // Dynamically resolve color based on state and user preference
    val tileColor = when (category.severity) {
        StorageSeverity.SAFE -> if (isHighContrast) HcSafe else BentoSafe
        StorageSeverity.NOTICE -> if (isHighContrast) HcNotice else BentoNotice
        StorageSeverity.WARNING -> if (isHighContrast) HcWarning else BentoWarning
        StorageSeverity.CRITICAL -> if (isHighContrast) HcCritical else BentoCritical
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            // Apply 20% opacity to the resolved severity color for a glassmorphic tint
            .background(tileColor.copy(alpha = 0.2f))
            .clickable {
                view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
                isExpanded = !isExpanded
            }
            .padding(16.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header Row: Name and Indicator dot
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Small dot indicating the severity color at full opacity
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(RoundedCornerShape(50))
                        .background(tileColor)
                )
            }

            // Always show the formatted size
            Text(
                text = formatBytes(category.sizeBytes),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Reveal percentage and extra details only when tapped
            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))

                // Formats the float (e.g. 45.678f) to "45.6%"
                val percentString = String.format("%.1f%%", category.totalPercentage)

                Text(
                    text = "Takes up $percentString of total scanned space.",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}