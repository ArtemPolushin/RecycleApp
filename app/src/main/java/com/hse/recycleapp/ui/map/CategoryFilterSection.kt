package com.hse.recycleapp.ui.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hse.recycleapp.data.WasteType


@Composable
fun CategoryFilterSection(
    selected: List<Int>,
    onToggle: (Int, Boolean) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .selectableGroup()
    ) {
        Text("Выберите категориям отходов:", style = MaterialTheme.typography.titleMedium)

        val filterCategories = WasteType.entries
            .filter { it != WasteType.TRASH }
            .map { it.id to it.displayName }

        for ((id, name) in filterCategories) {
            val isSelected = id in selected
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .selectable(
                        selected = isSelected,
                        onClick = { onToggle(id, !isSelected) }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onToggle(id, !isSelected) }
                )
                Text(name, Modifier.padding(start = 8.dp))
            }
        }
    }
}
