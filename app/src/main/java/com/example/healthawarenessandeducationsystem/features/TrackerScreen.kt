package com.example.healthawarenessandeducationsystem.features

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthawarenessandeducationsystem.AppDatabase
import com.example.healthawarenessandeducationsystem.HealthRecordEntity
import com.example.healthawarenessandeducationsystem.ThemeGreenDark
import com.example.healthawarenessandeducationsystem.TimeUtils
import com.example.healthawarenessandeducationsystem.isDarkModeGlobal
import kotlinx.coroutines.launch

@Composable
fun HealthTrackerScreen(
    userEmail: String,
    // Daily Vitals (Hoisted)
    currentSteps: Int, onStepsChanged: (Int) -> Unit,
    currentTemp: String, onTempChanged: (String) -> Unit,
    currentBP: String, onBPChanged: (String) -> Unit,
    currentHR: String, onHRChanged: (String) -> Unit,
    currentSleep: Float, onSleepChanged: (Float) -> Unit,
    currentMood: String, onMoodChanged: (String) -> Unit,
    // Habits (Hoisted)
    currentExercise: Int, onExerciseChanged: (Int) -> Unit,
    currentWater: Int, onWaterChanged: (Int) -> Unit,
    currentFruits: Int, onFruitsChanged: (Int) -> Unit,
    currentMeditation: Int, onMeditationChanged: (Int) -> Unit,
    onNavigateToHealth: () -> Unit = {},
    onVitalsSaved: () -> Unit = {} // New Callback for Home Activities
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isDailySelected by remember { mutableStateOf(true) }

    val mainGradient = if (isDarkModeGlobal) {
        Brush.horizontalGradient(listOf(Color(0xFF064E3B), Color(0xFF065F46)))
    } else {
        Brush.horizontalGradient(listOf(Color(0xFF00A86B), Color(0xFF00E5FF)))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TrackerHeader(isDailySelected, mainGradient, onToggle = { isDailySelected = it })

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                if (isDailySelected) {
                    Column {
                        DailyTrackerContent(
                            steps = currentSteps, onSteps = onStepsChanged,
                            temp = currentTemp, onTemp = onTempChanged,
                            bp = currentBP, onBp = onBPChanged,
                            hr = currentHR, onHr = onHRChanged,
                            sleep = if (currentSleep == 0f) "" else currentSleep.toString(),
                            onSleep = { onSleepChanged(it.toFloatOrNull() ?: 0f) },
                            mood = currentMood, onMood = onMoodChanged
                        )
                        
                        // SAVE BUTTON
                        Button(
                            onClick = {
                                val tempVal = currentTemp.toDoubleOrNull() ?: 0.0
                                val hrVal = currentHR.toIntOrNull() ?: 0
                                
                                when {
                                    currentTemp.isEmpty() && currentHR.isEmpty() && currentSteps == 0 && currentSleep == 0f -> {
                                        Toast.makeText(context, "Please enter some data", Toast.LENGTH_SHORT).show()
                                    }
                                    tempVal > 0 && (tempVal < 34 || tempVal > 43) -> {
                                        Toast.makeText(context, "Realistic temperature (34-43°C) required", Toast.LENGTH_SHORT).show()
                                    }
                                    hrVal > 0 && (hrVal < 40 || hrVal > 200) -> {
                                        Toast.makeText(context, "Realistic heart rate required", Toast.LENGTH_SHORT).show()
                                    }
                                    else -> {
                                        coroutineScope.launch {
                                            val userDao = AppDatabase.getInstance(context).userDao()
                                            // Pack all 10 health items into one string for SQLite
                                            val recordContent = "Steps: $currentSteps, Sleep: $currentSleep hrs, Temp: $currentTemp°C, BP: $currentBP, HR: $currentHR bpm, Mood: $currentMood, Exer: ${currentExercise}m, Water: ${currentWater}c, Fruit: ${currentFruits}s, Medit: ${currentMeditation}m"
                                            
                                            val newRecord = HealthRecordEntity(
                                                userEmail = userEmail,
                                                type = "Full Daily Log",
                                                result = recordContent,
                                                date = TimeUtils.getFormattedDate()
                                            )

                                            // 1. Save to SQLite (Local)
                                            userDao.insertRecord(newRecord)

                                            // 2. Sync to Supabase (Cloud) - Run in background
                                            launch {
                                                com.example.healthawarenessandeducationsystem.SupabaseManager.syncRecordToCloud(newRecord)
                                            }

                                            onVitalsSaved() // Trigger activity log on Home
                                            Toast.makeText(context, "Data saved to SQLite & Cloud! ✅", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 8.dp)
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ThemeGreenDark)
                        ) {
                            Icon(Icons.Default.Save, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Save Today's Vitals", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                } else {
                    HabitsTrackerContent(
                        exercise = currentExercise, onExerciseChange = onExerciseChanged,
                        water = currentWater, onWaterChange = onWaterChanged,
                        sleep = currentSleep, onSleepChange = onSleepChanged,
                        fruitsVeg = currentFruits, onFruitsVegChange = onFruitsChanged,
                        meditation = currentMeditation, onMeditationChange = onMeditationChanged,
                        steps = currentSteps, onStepsChange = onStepsChanged
                    )
                }
            }

            // Smart Recommendation
            item {
                val shouldShowRec = (currentTemp.toDoubleOrNull() ?: 0.0) > 37.5 || currentMood == "😫" || (currentSleep in 0.1f..5.0f)
                if (shouldShowRec) {
                    RecommendationCard(onNavigateToHealth)
                }
            }
        }
    }
}

@Composable
fun TrackerHeader(isDaily: Boolean, gradient: Brush, onToggle: (Boolean) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(gradient)
            .statusBarsPadding()
            .padding(top = 24.dp, bottom = 32.dp, start = 20.dp, end = 20.dp)
    ) {
        Column {
            Text("Health Tracker", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(20.dp))
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White.copy(alpha = 0.15f))
                    .padding(4.dp)
            ) {
                listOf(true to "📋 Daily Vitals", false to "👤 Daily Habits").forEach { (type, label) ->
                    val isSelected = isDaily == type
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) Color.White else Color.Transparent)
                            .clickable { onToggle(type) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) ThemeGreenDark else Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DailyTrackerContent(
    steps: Int,
    onSteps: (Int) -> Unit,
    temp: String,
    onTemp: (String) -> Unit,
    bp: String,
    onBp: (String) -> Unit,
    hr: String,
    onHr: (String) -> Unit,
    sleep: String,
    onSleep: (String) -> Unit,
    mood: String,
    onMood: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Today's Vitals", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface)
        
        SmartInputField(
            label = "🏃 Daily Steps (Goal: 2000)",
            value = if (steps == 0) "" else steps.toString(),
            onValueChange = { 
                val input = it.toIntOrNull() ?: 0
                onSteps(if (input < 0) 0 else input) 
            },
            badge = if (steps >= 2000) "Goal Reached! 🌟" else null,
            badgeColor = Color(0xFF00A86B)
        )

        val tempVal = temp.toDoubleOrNull() ?: 0.0
        SmartInputField(
            label = "🌡️ Temperature (°C)",
            value = temp,
            onValueChange = onTemp,
            badge = when {
                tempVal > 37.5 -> "Fever 🤒"
                tempVal in 35.0..37.5 -> "Normal"
                else -> null
            },
            badgeColor = if (tempVal > 37.5) Color.Red else Color(0xFF00A86B)
        )

        SmartInputField(
            label = "🩺 Blood Pressure (mmHg)",
            value = bp,
            onValueChange = onBp,
            placeholder = "e.g. 120/80"
        )

        val hrVal = hr.toIntOrNull() ?: 0
        SmartInputField(
            label = "❤️ Heart Rate (bpm)",
            value = hr,
            onValueChange = onHr,
            badge = when {
                hrVal > 100 -> "High 💓"
                hrVal in 40..100 -> "Stable"
                else -> null
            },
            badgeColor = if (hrVal > 100) Color(0xFFFFB020) else Color(0xFF00A86B)
        )

        SmartInputField(
            label = "😴 Sleep Duration (hrs)", 
            value = sleep, 
            onValueChange = onSleep, 
            keyboardType = KeyboardType.Decimal,
            badge = if ((sleep.toFloatOrNull() ?: 0f) >= 8f) "Good Rest! ✨" else null,
            badgeColor = Color(0xFF8B5CF6)
        )
        
        MoodCard(selectedMood = mood, onMoodSelected = onMood)
    }
}

@Composable
fun SmartInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    badge: String? = null,
    badgeColor: Color = Color.Gray,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Number
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(label, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
                if (badge != null) {
                    Surface(color = badgeColor.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp)) {
                        Text(badge, color = badgeColor, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                    }
                }
            }
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { if (placeholder.isNotEmpty()) Text(placeholder, fontSize = 14.sp) },
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun HabitsTrackerContent(
    exercise: Int,
    onExerciseChange: (Int) -> Unit,
    water: Int,
    onWaterChange: (Int) -> Unit,
    sleep: Float,
    onSleepChange: (Float) -> Unit,
    fruitsVeg: Int,
    onFruitsVegChange: (Int) -> Unit,
    meditation: Int,
    onMeditationChange: (Int) -> Unit,
    steps: Int,
    onStepsChange: (Int) -> Unit
) {
    val isAllDone = exercise >= 30 && water >= 8 && sleep >= 8f && fruitsVeg >= 5 && meditation >= 10 && steps >= 2000

    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (isAllDone) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFD700).copy(alpha = 0.15f)),
                border = BorderStroke(2.dp, Color(0xFFFFD700))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🏆 All Daily Goals Met!", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = Color(0xFFB8860B))
                    Text("You've completed all your healthy habits for today. Outstanding work! ✨", 
                        fontSize = 13.sp, color = Color(0xFFB8860B), textAlign = TextAlign.Center, modifier = Modifier.padding(top = 4.dp))
                }
            }
        }

        Text("Healthy Habits", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface)
        
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            HabitGridCard("Exercise", "🏃", exercise.toFloat(), 30f, "min", Color(0xFF00A86B), Modifier.weight(1f), 5f) { onExerciseChange(it.toInt()) }
            HabitGridCard("Water", "💧", water.toFloat(), 8f, "cups", Color(0xFF3E82F7), Modifier.weight(1f), 1f) { onWaterChange(it.toInt()) }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            HabitGridCard("Sleep", "😴", sleep, 8f, "hrs", Color(0xFF8B5CF6), Modifier.weight(1f), 0.5f) { onSleepChange(it) }
            HabitGridCard("Fruits", "🍎", fruitsVeg.toFloat(), 5f, "serv", Color(0xFFFF7043), Modifier.weight(1f), 1f) { onFruitsVegChange(it.toInt()) }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            HabitGridCard("Meditation", "🧘", meditation.toFloat(), 10f, "min", Color(0xFF00BCD4), Modifier.weight(1f), 1f) { onMeditationChange(it.toInt()) }
            HabitGridCard("Steps", "👣", steps.toFloat(), 2000f, "steps", Color(0xFFFFB020), Modifier.weight(1f), 100f) { onStepsChange(it.toInt()) }
        }
    }
}

@Composable
fun HabitGridCard(
    title: String,
    icon: String,
    current: Float,
    target: Float,
    unit: String,
    color: Color,
    modifier: Modifier,
    step: Float,
    onUpdate: (Float) -> Unit
) {
    val progress = (current / target).coerceIn(0f, 1f)
    val isDone = progress >= 1f

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDone) color.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surface
        ),
        border = if (isDone) androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.3f)) else null,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(60.dp)) {
                CircularProgressIndicator(
                    progress = { progress },
                    color = color,
                    trackColor = color.copy(alpha = 0.1f),
                    strokeWidth = 6.dp,
                    strokeCap = StrokeCap.Round,
                    modifier = Modifier.fillMaxSize()
                )
                Text(icon, fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
            
            // Format for Float display
            val displayText = if (current % 1 == 0f) current.toInt().toString() else current.toString()
            Text("$displayText/${target.toInt()} $unit", fontSize = 11.sp, color = Color.Gray)
            
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // User requested: Only increase, no decrease. 
                // We provide only the "+" button to reinforce positive habits.
                Button(
                    onClick = { onUpdate(current + step) },
                    modifier = Modifier.fillMaxWidth().height(36.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = color),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }
            
            if (isDone) {
                Text("Goal Met! 🌟", color = color, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}

@Composable
fun RecommendationCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier.padding(20.dp).fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF2FF))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF3E82F7))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Health Advice", fontWeight = FontWeight.Bold, color = Color(0xFF15213A), fontSize = 14.sp)
                Text("We noticed some unusual data. Read more in our library.", color = Color(0xFF728097), fontSize = 12.sp)
            }
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color(0xFF3E82F7))
        }
    }
}

@Composable
fun MoodCard(selectedMood: String, onMoodSelected: (String) -> Unit) {
    val moods = listOf("😄", "🙂", "😐", "😔", "😫")
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Mood Today", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                moods.forEach { mood ->
                    val isSelected = selectedMood == mood
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) ThemeGreenDark.copy(alpha = 0.1f) else Color.Transparent)
                            .clickable { onMoodSelected(mood) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(mood, fontSize = 24.sp)
                    }
                }
            }
        }
    }
}
