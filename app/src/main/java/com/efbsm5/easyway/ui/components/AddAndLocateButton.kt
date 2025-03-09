package com.efbsm5.easyway.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddAndLocateButton(onAdd: () -> Unit, onLocate: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, top = (30).dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.End
    ) {
        FloatingActionButton(
            onClick = { onAdd() },
            modifier = Modifier.padding(bottom = 16.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "新加无障碍点",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        FloatingActionButton(
            onClick = { onLocate() },
            containerColor = MaterialTheme.colorScheme.primary,
            shape = CircleShape,
            modifier = Modifier.padding(bottom = 30.dp)
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "定位",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun TabSection(tabs: List<String>, onSelect: (Int) -> Unit) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    TabRow(selectedTabIndex = selectedTabIndex) {
        tabs.forEachIndexed { index, title ->
            Tab(selected = index == selectedTabIndex, onClick = {
                onSelect(index)
                selectedTabIndex = index
            }) {
                Text(
                    text = title, modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}