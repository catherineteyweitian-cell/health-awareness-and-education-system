package com.example.healthawarenessandeducationsystem.features

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthawarenessandeducationsystem.AppDatabase
import com.example.healthawarenessandeducationsystem.HealthRecordEntity
import com.example.healthawarenessandeducationsystem.TimeUtils
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

data class AssessmentResult(
    val riskLevel: String,
    val conditions: String,
    val actions: List<String>
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SymptomChecker(
    userEmail: String,
    modifier: Modifier = Modifier,
    onAnalyse: (AssessmentResult) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var selectedSymptoms by remember { mutableStateOf(setOf<String>()) }
    var selectedDuration by remember { mutableStateOf("") }
    var selectedSeverity by remember { mutableStateOf("") }
    var selectedAgeGroup by remember { mutableStateOf("") }

    val symptoms = listOf(
        "Fever", "Headache", "Cough",
        "Sore Throat", "Fatigue", "Dizziness",
        "Nausea", "Chest Pain", "Shortness of Breath",
        "Body Aches", "Runny Nose", "Loss of Taste / Smell"
    )

    val duration = listOf("< 1 day", "1 - 3 days", "4 - 7 days", "> 1 week")
    val severity = listOf("😊 Mild", "😐 Moderate", "😔 Severe")
    val ageGroup = listOf("Child (0-12)", "Teen (13-17)", "Adult (18-59)", "Senior (60+)")

    var analysisResult by remember { mutableStateOf("") }
    val onSurface = MaterialTheme.colorScheme.onSurface

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "What symptoms do you have?",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = onSurface
        )

        // Manual Flow replacement for FlowRow to avoid NoSuchMethodError
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            symptoms.chunked(3).forEach { rowSymptoms ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowSymptoms.forEach { symptom ->
                        val isSelected = selectedSymptoms.contains(symptom)
                        FilterChip(
                            modifier = Modifier.weight(1f),
                            selected = isSelected,
                            onClick = {
                                selectedSymptoms = if (isSelected) selectedSymptoms - symptom else selectedSymptoms + symptom
                            },
                            label = { 
                                Text(
                                    symptom, 
                                    maxLines = 1, 
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                ) 
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFFF7043),
                                selectedLabelColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                    // Fill empty space if row has less than 3 items
                    repeat(3 - rowSymptoms.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        SelectionSection("How long has this lasted?", duration, selectedDuration) { selectedDuration = it }
        SelectionSection("How severe is it?", severity, selectedSeverity) { selectedSeverity = it }
        SelectionSection("What is your age group?", ageGroup, selectedAgeGroup) { selectedAgeGroup = it }

        Button(
            onClick = {
                if (selectedSymptoms.isEmpty() || selectedDuration.isEmpty() || selectedSeverity.isEmpty() || selectedAgeGroup.isEmpty()) {
                    analysisResult = "Please complete all selections."
                } else {
                    analysisResult = ""
                    val riskLevel = if (selectedSymptoms.contains("Chest Pain") || selectedSymptoms.contains("Shortness of Breath")) "High Risk" else "Moderate Risk"
                    val conditions = "🔎 Possible health condition based on selections"
                    val actions = listOf("Monitor symptoms", "Consult a doctor if worsening")
                    val result = AssessmentResult(riskLevel, conditions, actions)
                    
                    // Save to SQL
                    coroutineScope.launch {
                        val userDao = AppDatabase.getInstance(context).userDao()
                        val newRecord = HealthRecordEntity(
                            userEmail = userEmail,
                            type = "Symptom",
                            result = conditions,
                            date = TimeUtils.getFormattedDate()
                        )
                        userDao.insertRecord(newRecord)
                        
                        // Sync to Cloud
                        com.example.healthawarenessandeducationsystem.SupabaseManager.syncRecordToCloud(newRecord)

                        onAnalyse(result)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7043))
        ) {
            Text("Analyse Symptoms", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        if (analysisResult.isNotEmpty()) {
            Text(analysisResult, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun SelectionSection(title: String, items: List<String>, selectedItem: String, onItemSelected: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEach { item ->
                val isSelected = selectedItem == item
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onItemSelected(item) },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) Color(0xFFFF7043) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    border = if (isSelected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                ) {
                    Text(
                        text = item,
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
fun BMICalculator(userEmail: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    val bmiHistory = remember { mutableStateListOf<Double>() }

    // Load BMI history from DB
    LaunchedEffect(userEmail) {
        try {
            val userDao = AppDatabase.getInstance(context).userDao()
            val records = userDao.getRecordsByUser(userEmail)
            val history = records.filter { it.type == "BMI" }
                .mapNotNull { record ->
                    // Extract numeric value from "BMI: 24.5 (Normal)"
                    if (record.result.contains("BMI: ")) {
                        record.result.substringAfter("BMI: ").substringBefore(" (").toDoubleOrNull()
                    } else null
                }.reversed() // Show in chronological order
            bmiHistory.clear()
            bmiHistory.addAll(history)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Body Mass Index", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    OutlinedTextField(
                        value = weight,
                        onValueChange = { weight = it },
                        label = { Text("Weight (kg)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = height,
                        onValueChange = { height = it },
                        label = { Text("Height (m)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                    Button(
                        onClick = {
                            val w = weight.toDoubleOrNull()
                            val h = height.toDoubleOrNull()
                            if (w != null && h != null && h > 0) {
                                val bmi = w / (h * h)
                                bmiHistory.add(bmi)
                                val cat = when {
                                    bmi < 18.5 -> "Underweight"
                                    bmi < 25 -> "Normal"
                                    bmi < 30 -> "Overweight"
                                    else -> "Obese"
                                }
                                val bmiResult = "BMI: ${"%.1f".format(bmi)} ($cat)"
                                result = bmiResult

                                // Save to SQL
                                coroutineScope.launch {
                                    val userDao = AppDatabase.getInstance(context).userDao()
                                    val newRecord = HealthRecordEntity(
                                        userEmail = userEmail,
                                        type = "BMI",
                                        result = bmiResult,
                                        date = TimeUtils.getFormattedDate()
                                    )
                                    userDao.insertRecord(newRecord)
                                    
                                    // Sync to Cloud
                                    com.example.healthawarenessandeducationsystem.SupabaseManager.syncRecordToCloud(newRecord)
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8A65))
                    ) {
                        Text("Calculate", fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (result.isNotEmpty()) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFCC80).copy(alpha = 0.2f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        result,
                        modifier = Modifier.padding(16.dp),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE65100)
                    )
                }
            }

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.height(250.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("BMI History", fontWeight = FontWeight.Bold)
                    BMIGraph(
                        bmiHistory = bmiHistory,
                        modifier = Modifier.fillMaxSize().padding(top = 16.dp)
                    )
                }
            }
        }
    }


@Composable
fun BMIGraph(bmiHistory: List<Double>, modifier: Modifier = Modifier) {
    val primaryColor = Color(0xFFFF9800)
    val lineColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
    val textMeasurer = rememberTextMeasurer()
    val labelStyle = TextStyle(fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    val valueStyle = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = primaryColor)

    Canvas(modifier = modifier) {
        val graphWidth = size.width
        val graphHeight = size.height
        val padding = 50f // Increased padding for labels
        val usableWidth = graphWidth - padding * 2
        val usableHeight = graphHeight - padding * 2

        // Draw grid and Y-axis labels
        val yLabels = listOf("40", "30", "20", "10", "")
        for (i in 0..4) {
            val y = padding + (usableHeight / 4) * i
            drawLine(lineColor, start = androidx.compose.ui.geometry.Offset(padding, y), end = androidx.compose.ui.geometry.Offset(graphWidth - padding, y))
            
            // Draw Y-axis labels
            if (yLabels[i].isNotEmpty()) {
                drawText(
                    textMeasurer = textMeasurer,
                    text = yLabels[i],
                    style = labelStyle,
                    topLeft = androidx.compose.ui.geometry.Offset(5f, y - 15f)
                )
            }
        }

        if (bmiHistory.isNotEmpty()) {
            val path = Path()
            bmiHistory.forEachIndexed { index, bmi ->
                val x = padding + (if (bmiHistory.size > 1) index.toFloat() / (bmiHistory.size - 1) else 0.5f) * usableWidth
                val y = padding + usableHeight - (bmi.coerceIn(10.0, 40.0).toFloat() - 10) / 30f * usableHeight
                
                if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
                drawCircle(primaryColor, radius = 6f, center = androidx.compose.ui.geometry.Offset(x, y))
                
                // Draw BMI value above point
                drawText(
                    textMeasurer = textMeasurer,
                    text = "%.1f".format(bmi),
                    style = valueStyle,
                    topLeft = androidx.compose.ui.geometry.Offset(x - 20f, y - 45f)
                )
            }
            drawPath(path, primaryColor, style = Stroke(width = 4f))
        }
    }
}

@Composable
fun HealthAssessment(userEmail: String, initialPage: String = "Symptom", modifier: Modifier = Modifier) {
    var selectedPage by remember { mutableStateOf(initialPage) }
    
    LaunchedEffect(initialPage) {
        selectedPage = initialPage
    }

    var resultData by remember { mutableStateOf<AssessmentResult?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .background(Brush.horizontalGradient(listOf(Color(0xFFFFB74D), Color(0xFFFF7043))))
                .padding(top = 40.dp, bottom = 32.dp, start = 24.dp, end = 24.dp)
        ) {
            Column {
                Text("Health Assessment", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(modifier = Modifier.height(20.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TabButton("Symptom Checker", Icons.Default.Search, selectedPage == "Symptom") {
                        selectedPage = "Symptom"
                        resultData = null 
                    }
                    TabButton("BMI Calculation", Icons.Default.MonitorWeight, selectedPage == "bmi") {
                        selectedPage = "bmi"
                        resultData = null 
                    }
                }
            }
        }

        when {
            resultData != null -> AssessmentResultPage(resultData!!) { resultData = null }
            selectedPage == "Symptom" -> SymptomChecker(userEmail = userEmail) { res -> resultData = res }
            else -> BMICalculator(userEmail = userEmail)
        }
        
        Spacer(modifier = Modifier.height(80.dp)) // Padding for bottom nav
    }
}

@Composable
fun TabButton(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) Color.White.copy(alpha = 0.25f) else Color.Transparent,
        border = if (isSelected) BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)) else null
    ) {
        Row(modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(label, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

@Composable
fun AssessmentResultPage(result: AssessmentResult, onBack: () -> Unit) {
    Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Result", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Risk Level: ${result.riskLevel}", color = if (result.riskLevel.contains("High")) Color.Red else Color(0xFFFF7043), fontWeight = FontWeight.Bold)
                Text(result.conditions, fontSize = 16.sp)
                result.actions.forEach { Text("• $it", fontSize = 14.sp, color = Color.Gray) }
                Button(onClick = onBack, modifier = Modifier.fillMaxWidth().padding(top = 16.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7043))) {
                    Text("New Assessment")
                }
            }
        }
    }
}
