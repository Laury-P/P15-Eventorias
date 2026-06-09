package com.openclassroom.eventorias.features.events.eventList.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.openclassroom.eventorias.R
import com.openclassroom.eventorias.core.domain.model.EventCategory
import com.openclassroom.eventorias.core.ui.theme.EventoriasTheme

@Composable
fun FilterScreen(
    modifier: Modifier,
    selectedCategory: EventCategory?,
    onChipsSelected: (EventCategory?) -> Unit
) {
    val dims = EventoriasTheme.dimensions
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.sort_date_text),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = stringResource(R.string.sort_category_text),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = dims.padding16, bottom = dims.padding8)
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dims.padding8),
            verticalArrangement = Arrangement.spacedBy(dims.padding4)
        ) {
            CategoryFilterChip(
                modifier = Modifier,
                currentCategory = selectedCategory,
                chipsCategory = null,
                onChipsSelected = { onChipsSelected(null) }
            )
            EventCategory.entries.forEach { category ->
                CategoryFilterChip(
                    modifier = Modifier,
                    currentCategory = selectedCategory,
                    chipsCategory = category,
                    onChipsSelected = { onChipsSelected(category) }
                )
            }
        }
    }
}