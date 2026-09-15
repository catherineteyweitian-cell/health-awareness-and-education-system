package com.example.healthawarenessandeducationsystem
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthawarenessandeducationsystem.features.BMICalculator
import com.example.healthawarenessandeducationsystem.ui.theme.HealthAwarenessAndEducationSystemTheme
import com.example.healthawarenessandeducationsystem.features.MyRecordScreen
import com.example.healthawarenessandeducationsystem.features.SettingsScreen
import com.example.healthawarenessandeducationsystem.features.QuizScreen
import com.example.healthawarenessandeducationsystem.features.HealthAssessment
import com.example.healthawarenessandeducationsystem.features.HealthLibraryScreen
import com.example.healthawarenessandeducationsystem.features.HealthTrackerScreen
import java.time.LocalDateTime
import kotlinx.coroutines.launch

val TextDark = Color(0xFF000000)
val TextWhite = Color(0xFFFFFFFF)
val BackgroundGray = Color(0xFFF8FAFC)

val ThemeGreenDark = Color(0xFF00A86B)
val SubTextGray = Color(0xFF64748B)
val CardBgGreen = Color(0xFFE8F8F0)
val BorderGreen = Color(0xFFC6F6D5)
val DividerGray = Color(0xFFF1F5F9)
val ProgressTrackGray = Color(0xFFE2E8F0)

// Global state for Dark Mode toggle
var isDarkModeGlobal by mutableStateOf(false)

val GridSpacing = 12.dp

data class ReminderItemData(
    val title: String,
    val description: String,
    val time: String,
    val indicatorColor: Color
)

data class ActivityItemData(
    val title: String,
    val description: String,
    val time: String,
    val iconEmoji: String,
    val iconBGEmoji :Color,
    val indicatorColor: Color
)

data class AchievementItemData(
    val title: String,
    val iconEmoji: String,
    val color: Color
)

object TimeUtils {
    fun getFormattedDate(dateTime: LocalDateTime = LocalDateTime.now()): String {
        val formatter = java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy")
        return dateTime.format(formatter)
    }

    fun getGreetingMessage(dateTime: LocalDateTime = LocalDateTime.now()): String {
        return when (dateTime.hour) {
            in 0..11 -> "Good Morning"
            in 12..17 -> "Good Afternoon"
            else -> "Good Evening"
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Retrieve the logged-in user email from Intent or SharedPreferences
        var userEmail = intent.getStringExtra("USER_EMAIL") ?: ""
        if (userEmail.isEmpty()) {
            val sharedPref = getSharedPreferences("HAES_SESSION", android.content.Context.MODE_PRIVATE)
            userEmail = sharedPref.getString("LOGGED_USER_EMAIL", "") ?: ""
        }
        
        // If still empty, redirect to login
        if (userEmail.isEmpty()) {
            val intent = Intent(this, Login_Register_System::class.java)
            startActivity(intent)
            finish()
            return
        }

        enableEdgeToEdge()
        setContent {
            HealthAwarenessAndEducationSystemTheme(
                darkTheme = isDarkModeGlobal,
                dynamicColor = false // Force use our toggle colors
            ) {
                MainPage(userEmail = userEmail)
            }
        }
    }
}

@Composable
fun MainPage(
    userEmail: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var userName by remember { mutableStateOf("User") }
    var userEntity by remember { mutableStateOf<UserEntity?>(null) }
    var selectedTab by remember { mutableStateOf("Home") }
    var isLoading by remember { mutableStateOf(true) }
    
    // --- HOISTED STATES FOR PERSISTENCE ---
    var todaySteps by remember { mutableIntStateOf(0) }
    var trackerTemp by remember { mutableStateOf("") }
    var trackerBP by remember { mutableStateOf("") }
    var trackerHR by remember { mutableStateOf("") }
    var trackerSleep by remember { mutableFloatStateOf(0f) }
    var trackerMood by remember { mutableStateOf("") }
    
    // Habits Hoisting
    var trackerExercise by remember { mutableIntStateOf(0) }
    var trackerWater by remember { mutableIntStateOf(0) }
    var trackerFruits by remember { mutableIntStateOf(0) }
    var trackerMeditation by remember { mutableIntStateOf(0) }
    
    // Library Bookmarks Hoisting
    var libraryBookmarks by remember { mutableStateOf(setOf<String>()) }

    // Helper to persist all daily habits to DB
    fun persistHabits() {
        coroutineScope.launch {
            val today = TimeUtils.getFormattedDate()
            val userDao = AppDatabase.getInstance(context).userDao()
            val updatedUser = userEntity?.copy(
                dailySteps = todaySteps,
                dailyWater = trackerWater,
                dailyExercise = trackerExercise,
                dailyFruits = trackerFruits,
                dailyMeditation = trackerMeditation,
                dailySleep = trackerSleep,
                dailyTemp = trackerTemp,
                dailyBP = trackerBP,
                dailyHR = trackerHR,
                lastHabitsDate = today
            )
            
            userDao.updateDailyHabits(
                userEmail, 
                todaySteps, 
                trackerWater, 
                trackerExercise, 
                trackerFruits, 
                trackerMeditation, 
                trackerSleep, 
                trackerTemp,
                trackerBP,
                trackerHR,
                today
            )
            
            // NEW: Sync profile to Supabase on every habit update
            updatedUser?.let { 
                SupabaseManager.syncProfile(it)
                userEntity = it
            }
        }
    }

    fun updateLibraryBookmarks(newBookmarks: Set<String>) {
        libraryBookmarks = newBookmarks
        coroutineScope.launch {
            val bookmarksString = newBookmarks.joinToString(",")
            val userDao = AppDatabase.getInstance(context).userDao()
            userDao.updateBookmarks(userEmail, bookmarksString)
            
            val updatedUser = userEntity?.copy(bookmarkedItems = bookmarksString)
            // Sync to Cloud
            updatedUser?.let { 
                SupabaseManager.syncProfile(it)
                userEntity = it
            }
        }
    }

    // --- ACTIVITY LOG STATE ---
    var recentActivities by remember { mutableStateOf(listOf<ActivityItemData>()) }

    fun addActivityLog(title: String, description: String, emoji: String, color: Color) {
        val currentTime = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))
        val newActivity = ActivityItemData(
            title = title,
            description = description,
            time = currentTime,
            iconEmoji = emoji,
            iconBGEmoji = color.copy(alpha = 0.1f),
            indicatorColor = color
        )
        // Keep only the last 5 activities in UI
        recentActivities = (listOf(newActivity) + recentActivities).take(5)

        // Persist to DB
        coroutineScope.launch {
            val userDao = AppDatabase.getInstance(context).userDao()
            val logEntity = ActivityLogEntity(
                userEmail = userEmail,
                title = title,
                description = description,
                time = currentTime,
                emoji = emoji,
                colorInt = android.graphics.Color.argb(
                    (color.alpha * 255).toInt(),
                    (color.red * 255).toInt(),
                    (color.green * 255).toInt(),
                    (color.blue * 255).toInt()
                )
            )
            userDao.insertActivityLog(logEntity)
            
            // NEW: Sync Activity Log to Cloud
            SupabaseManager.syncActivityLog(logEntity)
        }
    }

    var assessSubPage by remember { mutableStateOf("Symptom") }

    // Connect to SQL Room and fetch user data
    LaunchedEffect(userEmail) {
        if (userEmail.isNotEmpty()) {
            try {
                val userDao = AppDatabase.getInstance(context).userDao()
                val user = userDao.getUserByEmail(userEmail)
                if (user != null) {
                    userEntity = user
                    userName = user.name
                    
                    // Initialize mood and habits from DB if recorded today
                    val today = TimeUtils.getFormattedDate()
                    if (user.lastMoodDate == today) {
                        trackerMood = user.dailyMood ?: ""
                    }
                    
                    if (user.lastHabitsDate == today) {
                        todaySteps = user.dailySteps
                        trackerWater = user.dailyWater
                        trackerExercise = user.dailyExercise
                        trackerFruits = user.dailyFruits
                        trackerMeditation = user.dailyMeditation
                        trackerSleep = user.dailySleep
                        trackerTemp = user.dailyTemp
                        trackerBP = user.dailyBP
                        trackerHR = user.dailyHR
                    }
                    
                    // Initialize bookmarks
                    libraryBookmarks = user.bookmarkedItems.split(",")
                        .filter { it.isNotEmpty() }
                        .toSet()

                    // Initialize Recent Activities from DB
                    val logs = userDao.getRecentLogs(userEmail)
                    recentActivities = logs.map { log ->
                        val color = Color(log.colorInt)
                        ActivityItemData(
                            title = log.title,
                            description = log.description,
                            time = log.time,
                            iconEmoji = log.emoji,
                            iconBGEmoji = color.copy(alpha = 0.1f),
                            indicatorColor = color
                        )
                    }
                } else {
                    // User doesn't exist (possibly after DB migration/wipe)
                    val sharedPref = context.getSharedPreferences("HAES_SESSION", android.content.Context.MODE_PRIVATE)
                    sharedPref.edit().remove("LOGGED_USER_EMAIL").apply()
                    val intent = Intent(context, Login_Register_System::class.java)
                    context.startActivity(intent)
                    (context as? ComponentActivity)?.finish()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }
    
    // Helper to update mood in both state and DB
    fun updateMood(newMood: String) {
        trackerMood = newMood
        coroutineScope.launch {
            val today = TimeUtils.getFormattedDate()
            val userDao = AppDatabase.getInstance(context).userDao()
            userDao.updateDailyMood(userEmail, newMood, today)
            val updatedUser = userEntity?.copy(dailyMood = newMood, lastMoodDate = today)
            
            // Sync to Cloud
            updatedUser?.let { 
                SupabaseManager.syncProfile(it)
                userEntity = it
            }
        }
    }

    // --- DYNAMIC REMINDERS LOGIC ---
    val reminders = remember(trackerTemp, trackerBP, trackerHR, trackerSleep, trackerMood) {
        mutableListOf<ReminderItemData>().apply {
            if (trackerTemp.isEmpty()) add(ReminderItemData("Body Temperature", "Log your temperature", "Pending", Color(0xFFF97316)))
            if (trackerBP.isEmpty()) add(ReminderItemData("Blood Pressure", "Measure your BP today", "Pending", Color(0xFF3B82F6)))
            if (trackerHR.isEmpty()) add(ReminderItemData("Heart Rate", "Check your pulse", "Pending", Color(0xFFEF4444)))
            if (trackerSleep == 0f) add(ReminderItemData("Sleep Duration", "Log last night's sleep", "Pending", Color(0xFF8B5CF6)))
            if (trackerMood.isEmpty()) add(ReminderItemData("Daily Mood", "How are you feeling?", "Pending", Color(0xFF10B981)))
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = ThemeGreenDark)
        }
    } else {
        Scaffold(
            bottomBar = { 
                BottomNavigationBar(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                ) 
            },
            modifier = modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                when (selectedTab) {
                    "Home" -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            item { 
                                HeaderSection(
                                    user = userEntity,
                                    currentMood = trackerMood,
                                    onMoodUpdate = { updateMood(it) }
                                ) 
                            }
                            
                            item {
                                Column(
                                    modifier = Modifier.padding(horizontal = 20.dp),
                                    verticalArrangement = Arrangement.spacedBy(20.dp)
                                ) {
                                    HealthScoreCard(todaySteps)
                                    DailyTipCard()
                                    QuickActionsSection(
                                        onDiseaseClick = { selectedTab = "Health" },
                                        onSymptomClick = { 
                                            assessSubPage = "Symptom"
                                            selectedTab = "Assess" 
                                        },
                                        onBMIClick = { 
                                            assessSubPage = "bmi"
                                            selectedTab = "Assess" 
                                        },
                                        onTrackerClick = { selectedTab = "Tracker" },
                                        onQuizClick = { selectedTab = "Quiz" },
                                        onProfileClick = {selectedTab = "Profile"}
                                    )
                                    TodayReminder(reminders = reminders)
                                    
                                    RecentActivities(activities = recentActivities)
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                        }
                    }
                    "Profile" -> {
                        ProfilePage(
                            user = userEntity,
                            onNavigateToRecord = { selectedTab = "Record" },
                            onNavigateToSettings = { selectedTab = "Settings" }
                        )
                    }
                    "Record" -> {
                        MyRecordScreen(userEmail = userEmail)
                    }
                    "Settings" -> {
                        SettingsScreen(userEmail = userEmail)
                    }
                    "Assess" -> {
                        HealthAssessment(userEmail = userEmail, initialPage = assessSubPage)
                    }
                    "Quiz" -> {
                        QuizScreen()
                    }
                    "Health" -> {
                        HealthLibraryScreen(
                            bookmarkedItems = libraryBookmarks,
                            onBookmarkToggle = { updateLibraryBookmarks(it) }
                        )
                    }
                    "Tracker" ->{
                        HealthTrackerScreen(
                            userEmail = userEmail,
                            currentSteps = todaySteps,
                            onStepsChanged = { 
                                val old = todaySteps
                                todaySteps = it 
                                persistHabits()
                                if (old < 2000 && it >= 2000) addActivityLog("Step Goal", "You've reached 2000 steps!", "👣", Color(0xFFFFB020))
                            },
                            currentTemp = trackerTemp,
                            onTempChanged = { 
                                trackerTemp = it 
                                persistHabits()
                            },
                            currentBP = trackerBP,
                            onBPChanged = { 
                                trackerBP = it 
                                persistHabits()
                            },
                            currentHR = trackerHR,
                            onHRChanged = { 
                                trackerHR = it 
                                persistHabits()
                            },
                            currentSleep = trackerSleep,
                            onSleepChanged = { 
                                val old = trackerSleep
                                trackerSleep = it 
                                persistHabits()
                                if (old < 8f && it >= 8f) addActivityLog("Sleep Goal", "8 hours of rest achieved! 😴", "😴", Color(0xFF8B5CF6))
                            },
                            currentMood = trackerMood,
                            onMoodChanged = { updateMood(it) },
                            currentExercise = trackerExercise,
                            onExerciseChanged = { 
                                val old = trackerExercise
                                trackerExercise = it 
                                persistHabits()
                                if (old < 30 && it >= 30) addActivityLog("Exercise Goal", "30 mins achieved! 💪", "🏃", Color(0xFF00A86B))
                            },
                            currentWater = trackerWater,
                            onWaterChanged = { 
                                val old = trackerWater
                                trackerWater = it 
                                persistHabits()
                                if (old < 8 && it >= 8) addActivityLog("Hydration Goal", "8 cups of water! 💧", "💧", Color(0xFF3E82F7))
                            },
                            currentFruits = trackerFruits,
                            onFruitsChanged = { 
                                val old = trackerFruits
                                trackerFruits = it 
                                persistHabits()
                                if (old < 5 && it >= 5) addActivityLog("Nutrition Goal", "5 servings of fruit! 🍎", "🍎", Color(0xFFFF7043))
                            },
                            currentMeditation = trackerMeditation,
                            onMeditationChanged = { 
                                val old = trackerMeditation
                                trackerMeditation = it 
                                persistHabits()
                                if (old < 10 && it >= 10) addActivityLog("Mindfulness Goal", "10 mins meditation! 🧘", "🧘", Color(0xFF00BCD4))
                            },
                            onNavigateToHealth = { selectedTab = "Health" },
                            onVitalsSaved = { 
                                addActivityLog("Vitals Updated", "Daily health data saved", "✅", ThemeGreenDark)
                            }
                        )
                    }
                    else -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Feature coming soon: $selectedTab")
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun HeaderSection(
    user: UserEntity?, 
    currentMood: String,
    onMoodUpdate: (String) -> Unit
) {
    var currentTime by remember { mutableStateOf(LocalDateTime.now()) }
    
    // Update the time every minute to refresh the greeting and date
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000 * 60) // Wait 1 minute
            currentTime = LocalDateTime.now()
        }
    }

    val currentDateText = remember(currentTime) { TimeUtils.getFormattedDate(currentTime) }
    val greetingText = remember(currentTime) { TimeUtils.getGreetingMessage(currentTime) }

    //SMART GREETING LOGIC
    val personalizedMessage = when (currentMood) {
        "😄" -> "Wonderful! Keep up that positive energy! ✨"
        "😐" -> "Keep going, you're doing fine! 💪"
        "😫" -> "Hang in there, better days are coming! ❤️"
        else -> "$greetingText, ${user?.name ?: "User"} 👋"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(ThemeColorDark, ThemeColorLight)
                )
            )
            .statusBarsPadding()
            .padding(top = 16.dp, start = 20.dp, end = 20.dp, bottom = 30.dp)
    ) {
        Column {
            Text(
                text = currentDateText, 
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = personalizedMessage,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                if (currentMood.isEmpty()) "How are you feeling today?" else "Today you feel $currentMood",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val moods = listOf("😄", "😐", "😫")
                val labels = listOf("Great", "Okay", "Bad")
                moods.forEachIndexed { index, mood ->
                    MoodButton(
                        text = "${labels[index]} $mood",
                        isSelected = currentMood == mood,
                        onClick = { onMoodUpdate(mood) }
                    )
                }
            }
        }
    }
}

@Composable
fun MoodButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = if (isSelected) Color.White else Color.White.copy(alpha = 0.2f),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, if (isSelected) Color.White else Color.White.copy(alpha = 0.4f)),
        modifier = Modifier.height(38.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 26.dp)
        ) {
            Text(
                text = text, 
                color = if (isSelected) ThemeColorDark else Color.White, 
                fontSize = 13.sp, 
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}

@Composable
fun HealthScoreCard(steps: Int) {
    val cardBg = if (isDarkModeGlobal) MaterialTheme.colorScheme.surface else Color.White
    val goal = 2000
    val progress = (steps.toFloat() / goal).coerceIn(0f, 1f)

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("$steps", fontSize = 42.sp, fontWeight = FontWeight.Bold, color = ThemeGreenDark)
                        Text("/$goal", fontSize = 16.sp, color = SubTextGray, modifier = Modifier.padding(bottom = 6.dp))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        color = CardBgGreen,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            if (steps >= goal) "Goal Achieved! 🌟" else "Keep walking!",
                            color = ThemeGreenDark,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                Box(contentAlignment = Alignment.Center,
                    modifier = Modifier.size(70.dp)) {
                    CircularProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxSize(),
                        color = ThemeGreenDark,
                        strokeWidth = 8.dp,
                        trackColor = ProgressTrackGray,
                        strokeCap = StrokeCap.Round
                    )
                    Text(if (steps >= goal) "🎉" else "🏃", fontSize = 24.sp)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = DividerGray)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach { day ->
                    Text(day, fontSize = 12.sp, color = SubTextGray)
                }
            }
        }
    }
}

@Composable
fun DailyTipCard() {
    val tipBg = if (isDarkModeGlobal) Color(0xFF064E3B) else CardBgGreen
    val tipBorder = if (isDarkModeGlobal) Color(0xFF065F46) else BorderGreen
    Surface(
        color = tipBg,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, tipBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("💡", fontSize = 24.sp, modifier = Modifier.padding(end = 12.dp))
            Column {
                Text("Daily Health Tip", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    "Take a 5-minute walk every hour to break up long periods of sitting.",
                    color = if (isDarkModeGlobal) Color.White.copy(alpha = 0.7f) else SubTextGray,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
fun QuickActionsSection(
    onDiseaseClick: () -> Unit,
    onSymptomClick: () -> Unit,
    onBMIClick: () -> Unit,
    onTrackerClick: () -> Unit,
    onQuizClick: () -> Unit,
    onProfileClick: () -> Unit
) {

    Column {
        Text("Quick Actions", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.height(GridSpacing))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(GridSpacing)
        ) {
            QuickActionItem("Symptoms Check", Icons.Default.Search, Color(0xFF10B981), Modifier.weight(1f),  onSymptomClick)
            QuickActionItem("Health Library", Icons.Default.LocalLibrary, Color(0xFF6366F1), Modifier.weight(1f), onDiseaseClick)
            QuickActionItem("BMI Calc", Icons.Default.Scale, Color(0xFFF97316), Modifier.weight(1f), onBMIClick)
        }
        Spacer(modifier = Modifier.height(GridSpacing))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(GridSpacing)
        ) {
            QuickActionItem("Health Tracker", Icons.Default.TrackChanges, Color(0xFF6366F1), Modifier.weight(1f),onTrackerClick)
            QuickActionItem("Quiz", Icons.Default.Quiz, Color(0xFFF97316), Modifier.weight(1f),onQuizClick)
            QuickActionItem("Profile", Icons.Default.Person, Color(0xFF10B981), Modifier.weight(1f),onProfileClick )
        }
    }
}
@Composable
fun QuickActionItem(
    title: String,
    icon: ImageVector,
    iconBgColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
        }
    }

}

@Composable
fun TodayReminder(reminders: List<ReminderItemData>, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "🔔Today's Reminders",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (reminders.isEmpty()) {
                Text(
                    text = "No reminders set yet.",
                    color = SubTextGray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            } else {
                reminders.forEachIndexed { index, reminder ->
                    ReminderItem(reminder)
                    if (index < reminders.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(top = 8.dp),
                            color = DividerGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReminderItem(reminder: ReminderItemData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(reminder.indicatorColor)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = reminder.title,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp
            )
            Text(
                text = reminder.description,
                color = SubTextGray,
                fontSize = 12.sp
            )
        }
        Text(
            text = reminder.time,
            color = SubTextGray,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun RecentActivities(activities: List<ActivityItemData>, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Recent Activities",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (activities.isEmpty()) {
                Text(
                    text = "No recent activities recorded.",
                    color = SubTextGray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            } else {
                activities.forEachIndexed { index, activity->
                    ActivityItem(activity)
                    if (index < activities.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(top = 8.dp),
                            color = DividerGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActivityItem(activity: ActivityItemData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(activity.iconBGEmoji),
            contentAlignment = Alignment.Center
        ) {
            Text(activity.iconEmoji, fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = activity.title,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp
            )
            Text(
                text = activity.description,
                color = SubTextGray,
                fontSize = 12.sp
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = activity.time,
                color = SubTextGray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(activity.indicatorColor)
            )
        }
    }
}

@Composable
fun BottomNavigationBar(selectedTab: String, onTabSelected: (String) -> Unit) {
    val items = listOf(
        NavigationItem("Home", Icons.Default.Home),
        NavigationItem("Health", Icons.Default.FavoriteBorder),
        NavigationItem("Assess", Icons.Default.Search),
        NavigationItem("Tracker", Icons.Default.BarChart),
        NavigationItem("Quiz", Icons.Default.Quiz),
        NavigationItem("Profile", Icons.Default.Person)
    )
    
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface, tonalElevation = 8.dp) {
        items.forEach { item ->
            NavigationBarItem(
                selected = selectedTab == item.title,
                onClick = { onTabSelected(item.title) },
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title, fontSize = 10.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ThemeGreenDark,
                    selectedTextColor = ThemeGreenDark,
                    indicatorColor = CardBgGreen
                )
            )
        }
    }
}

@Composable
fun ProfilePage(
    user: UserEntity?,
    onNavigateToRecord: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Profile Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(ThemeGreenDark, Color(0xFF00D27F))
                )
            )
            .statusBarsPadding()
            .padding(top = 24.dp, bottom = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                        .border(3.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(65.dp),
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = user?.name ?: "Guest User",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = user?.email ?: "No email provided",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Profile Details Card
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionHeader("General Information")
            
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    ProfileDetailRow("Nickname", user?.nickName?.ifEmpty { "Not set" } ?: "Not set", Icons.Default.Person)
                    HorizontalDivider(color = DividerGray, modifier = Modifier.padding(vertical = 12.dp))
                    ProfileDetailRow("Gender", user?.gender?.ifEmpty { "Not set" } ?: "Not set", Icons.Default.Person)
                    HorizontalDivider(color = DividerGray, modifier = Modifier.padding(vertical = 12.dp))
                    ProfileDetailRow("Contact", user?.contactNumber?.ifEmpty { "Not set" } ?: "Not set", Icons.Default.Call)
                    HorizontalDivider(color = DividerGray, modifier = Modifier.padding(vertical = 12.dp))
                    ProfileDetailRow("Birth Date", user?.birthDate?.ifEmpty { "Not set" } ?: "Not set", Icons.Default.Person)
                }
            }
            
            SectionHeader("Health Status")
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HealthStatCard("Weight", if (user?.weight.isNullOrEmpty()) "-- kg" else "${user.weight} kg", Color(0xFF6366F1), Modifier.weight(1f))
                HealthStatCard("Height", if (user?.height.isNullOrEmpty()) "-- cm" else "${user.height} cm", Color(0xFF10B981), Modifier.weight(1f))
            }

            SectionHeader("Achievements")
            AchievementSection()

            SectionHeader("Features")
            
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    ProfileMenuRow("My Health Records", Icons.AutoMirrored.Filled.Assignment, onNavigateToRecord)
                    HorizontalDivider(color = DividerGray, modifier = Modifier.padding(horizontal = 20.dp))
                    ProfileMenuRow("Settings", Icons.Default.Settings, onNavigateToSettings)
                }
            }

            SectionHeader("Health Profile")
            


            if (!user?.introduction.isNullOrEmpty()) {
                SectionHeader("About Me")
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = user?.introduction ?: "",
                        modifier = Modifier.padding(20.dp),
                        fontSize = 15.sp,
                        color = SubTextGray,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            
            Button(
                onClick = {
                    // Clear session
                    val sharedPref = context.getSharedPreferences("HAES_SESSION", android.content.Context.MODE_PRIVATE)
                    sharedPref.edit().remove("LOGGED_USER_EMAIL").apply()

                    val intent = Intent(context, Login_Register_System::class.java)
                    context.startActivity(intent)
                    (context as? ComponentActivity)?.finish()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.5.dp, Color.Red.copy(alpha = 0.6f)),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 2.dp)
            ) {
                Text("Log Out Account", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
    )
}

@Composable
fun ProfileMenuRow(label: String, icon: ImageVector, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = ThemeGreenDark, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(label, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = SubTextGray)
        }
    }
}

@Composable
fun ProfileDetailRow(label: String, value: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = ThemeGreenDark, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, fontSize = 12.sp, color = SubTextGray)
            Text(value, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
fun HealthStatCard(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(label, fontSize = 12.sp, color = SubTextGray)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
fun AchievementSection() {
    val achievements = remember { emptyList<AchievementItemData>() }

    if (achievements.isEmpty()) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, DividerGray),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Complete daily goals to unlock achievements! 🏆",
                    fontSize = 13.sp,
                    color = SubTextGray,
                    textAlign = TextAlign.Center
                )
            }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            achievements.forEach { achievement ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(achievement.color.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(achievement.iconEmoji, fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.height(6.6.dp))
                        Text(
                            text = achievement.title,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

data class NavigationItem(val title: String, val icon: ImageVector)
