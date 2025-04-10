package com.benchopo.notitareas.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuBox(
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = Modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        Column {
            TextField(
                value = selectedItem,
                onValueChange = {},
                readOnly = true,
                label = { Text("Materia") },
                modifier = Modifier.menuAnchor(),
                colors = TextFieldDefaults.colors()
            )
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}
