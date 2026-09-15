package com.example.healthawarenessandeducationsystem.features

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthawarenessandeducationsystem.SubTextGray
import com.example.healthawarenessandeducationsystem.ThemeGreenDark
import com.example.healthawarenessandeducationsystem.AppDatabase
import com.example.healthawarenessandeducationsystem.isDarkModeGlobal
import androidx.compose.ui.platform.LocalContext

@Composable
fun MyRecordScreen(userEmail: String, onBack: () -> Unit) {
    val context = LocalContext.current
    var records by remember { mutableStateOf<List<HealthRecord>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(userEmail) {
        val userDao = AppDatabase.getInstance(context).userDao()
        val dbRecords = userDao.getRecordsByUser(userEmail)
        records = dbRecords.map { 
            HealthRecord(it.type + " Assessment", it.result, it.date)
        }
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp, top = 16.dp)
        ) {
            IconButton(
                onClick = onBack,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = if (isDarkModeGlobal) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.05f)
                ),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = if (isDarkModeGlobal) Color.White else Color.Black
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "My Health Records",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = ThemeGreenDark)
            }
        } else if (records.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No health records found.", color = SubTextGray)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(records) { record ->
                    RecordItem(record)
                }
            }
        }
    }
}

@Composable
fun RecordItem(record: HealthRecord) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(ThemeGreenDark.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.History, contentDescription = null, tint = ThemeGreenDark)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(record.title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp)
                Text(record.date, color = SubTextGray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(record.value, fontWeight = FontWeight.ExtraBold, color = ThemeGreenDark, fontSize = 16.sp)
            }
        }
    }
}

data class HealthRecord(val title: String, val value: String, val date: String)
