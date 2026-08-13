package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LocationInfo
import com.example.data.network.CloudflareLocations
import com.example.ui.MainViewModel
import com.example.ui.theme.CfOrangePrimary
import com.example.ui.theme.CfOrangeSecondary
import com.example.ui.theme.MutedText
import com.example.ui.theme.OffWhiteText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EdgeLocationsScreen(
    viewModel: MainViewModel
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedRegion by remember { mutableStateOf("ALL") }

    val allLocations = remember { CloudflareLocations.DEFAULT_LOCATIONS.values.toList() }
    val regions = listOf("ALL", "Asia", "North America", "Europe", "Oceania", "South America", "Africa")

    val filteredLocations = remember(searchQuery, selectedRegion) {
        allLocations.filter { loc ->
            val matchesRegion = selectedRegion == "ALL" || loc.region.equals(selectedRegion, ignoreCase = true)
            val matchesSearch = searchQuery.isBlank() ||
                    loc.iata.contains(searchQuery, ignoreCase = true) ||
                    loc.city.contains(searchQuery, ignoreCase = true) ||
                    loc.cca2.contains(searchQuery, ignoreCase = true)
            matchesRegion && matchesSearch
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Public,
                contentDescription = "Global Network",
                tint = CfOrangePrimary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "Cloudflare Global Edge Network",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = OffWhiteText
                    )
                )
                Text(
                    text = "300+ edge datacenters worldwide",
                    style = MaterialTheme.typography.bodySmall.copy(color = MutedText)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search by City, Airport Code (IATA) or Country...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = MutedText) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("location_search_input"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CfOrangePrimary,
                unfocusedBorderColor = MutedText
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Region Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(regions) { region ->
                FilterChip(
                    selected = selectedRegion == region,
                    onClick = { selectedRegion = region },
                    label = { Text(region, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CfOrangePrimary,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Location Cards List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredLocations, key = { it.iata }) { loc ->
                LocationItemCard(
                    locationInfo = loc,
                    onSelectColo = {
                        val current = viewModel.scanConfig.value
                        viewModel.updateScanConfig(current.copy(coloFilter = loc.iata))
                    }
                )
            }
        }
    }
}

@Composable
fun LocationItemCard(
    locationInfo: LocationInfo,
    onSelectColo: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(CfOrangePrimary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = locationInfo.iata,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = CfOrangePrimary
                        )
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = locationInfo.city,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = OffWhiteText
                        )
                    )
                    Text(
                        text = "${locationInfo.region} • Country: ${locationInfo.cca2}",
                        style = MaterialTheme.typography.bodySmall.copy(color = MutedText)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Lat: ${locationInfo.lat.toInt()}, Lon: ${locationInfo.lon.toInt()}",
                    style = MaterialTheme.typography.labelSmall.copy(color = CfOrangeSecondary)
                )
            }
        }
    }
}
