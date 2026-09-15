package com.example.healthawarenessandeducationsystem

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Query
import kotlinx.serialization.Serializable
import kotlinx.coroutines.launch
import com.example.healthawarenessandeducationsystem.ui.theme.HealthAwarenessAndEducationSystemTheme
import java.util.Calendar
import java.security.SecureRandom
import java.security.spec.KeySpec
import java.util.Base64
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

val ThemeColorDark = Color(0xFF00A86B)
val ThemeColorLight = Color(0xFF00D27F)

object PasswordHasher {
    private const val ALGORITHM = "PBKDF2WithHmacSHA1"
    private const val ITERATIONS = 10000
    private const val KEY_LENGTH = 160
    private const val SALT_LENGTH = 16

    fun generateSalt(): String {
        val random = SecureRandom()
        val salt = ByteArray(SALT_LENGTH)
        random.nextBytes(salt)
        return Base64.getEncoder().encodeToString(salt)
    }

    fun hashPassword(password: String, salt: String): String {
        val saltBytes = Base64.getDecoder().decode(salt)
        val spec: KeySpec = PBEKeySpec(password.toCharArray(), saltBytes, ITERATIONS, KEY_LENGTH)
        val factory = SecretKeyFactory.getInstance(ALGORITHM)
        val hash = factory.generateSecret(spec).encoded
        return Base64.getEncoder().encodeToString(hash)
    }
}

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val email: String,
    val passwordHash: String,
    val salt: String,
    val name: String,
    val nickName: String = "",
    val gender: String = "",
    val birthDate: String = "",
    val contactNumber: String = "",
    val weight: String = "",
    val height: String = "",
    val introduction: String = "",
    val securityQuestion: String,
    val securityAnswerHash: String,
    val dailyMood: String? = null,
    val lastMoodDate: String? = null,
    
    // Daily Habits Persistence
    val dailySteps: Int = 0,
    val dailyWater: Int = 0,
    val dailyExercise: Int = 0,
    val dailyFruits: Int = 0,
    val dailyMeditation: Int = 0,
    val dailySleep: Float = 0f,
    val dailyTemp: String = "",
    val dailyBP: String = "",
    val dailyHR: String = "",
    val lastHabitsDate: String? = null,
    val bookmarkedItems: String = "" // Comma-separated titles
)

@Serializable
@Entity(tableName = "health_records")
data class HealthRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userEmail: String,
    val type: String, // "Symptom" or "BMI"
    val result: String,
    val date: String
)

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("UPDATE user SET dailyMood = :mood, lastMoodDate = :date WHERE email = :email")
    suspend fun updateDailyMood(email: String, mood: String, date: String)

    @Query("UPDATE user SET dailySteps = :steps, dailyWater = :water, dailyExercise = :exercise, dailyFruits = :fruits, dailyMeditation = :meditation, dailySleep = :sleep, dailyTemp = :temp, dailyBP = :bp, dailyHR = :hr, lastHabitsDate = :date WHERE email = :email")
    suspend fun updateDailyHabits(email: String, steps: Int, water: Int, exercise: Int, fruits: Int, meditation: Int, sleep: Float, temp: String, bp: String, hr: String, date: String)

    @Query("UPDATE user SET bookmarkedItems = :bookmarks WHERE email = :email")
    suspend fun updateBookmarks(email: String, bookmarks: String)

    @Query("UPDATE user SET passwordHash = :newHash, salt = :newSalt WHERE email = :email")
    suspend fun updatePassword(email: String, newHash: String, newSalt: String)

    @Insert
    suspend fun insertRecord(record: HealthRecordEntity)

    @Query("SELECT * FROM health_records WHERE userEmail = :email ORDER BY id DESC")
    suspend fun getRecordsByUser(email: String): List<HealthRecordEntity>

    @Query("SELECT * FROM user")
    suspend fun getAllUsers(): List<UserEntity>

    // Activity Logs Persistence
    @Insert
    suspend fun insertActivityLog(log: ActivityLogEntity)

    @Query("SELECT * FROM activity_logs WHERE userEmail = :email ORDER BY id DESC LIMIT 5")
    suspend fun getRecentLogs(email: String): List<ActivityLogEntity>
}

@Entity(tableName = "activity_logs")
data class ActivityLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userEmail: String,
    val title: String,
    val description: String,
    val time: String,
    val emoji: String,
    val colorInt: Int
)

class Login_Register_System : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HealthAwarenessAndEducationSystemTheme(darkTheme = isDarkModeGlobal) {
                AuthScreen()
            }
        }
    }
}

@Composable
fun AuthScreen() {
    var currentScreen by remember { mutableStateOf("login") }
    
    // Data carried from Step 1 to Step 2
    var regEmail by remember { mutableStateOf("") }
    var regPassword by remember { mutableStateOf("") }

    val bgGradient = if (isDarkModeGlobal) {
        Brush.verticalGradient(colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B)))
    } else {
        Brush.verticalGradient(colors = listOf(ThemeColorDark, ThemeColorLight))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = bgGradient)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Transparent
        ) {
            when (currentScreen) {
                "login" -> LoginMenu(
                    onNavigateToRegister = { currentScreen = "register" },
                    onNavigateToReset = { currentScreen = "reset_password" }
                )
                "register" -> RegisterMenu(
                    onNavigateToLogin = { currentScreen = "login" },
                    onContinue = { email, password ->
                        regEmail = email
                        regPassword = password
                        currentScreen = "register_sub"
                    }
                )
                "register_sub" -> RegisterSubMenu(
                    initialEmail = regEmail,
                    initialPassword = regPassword,
                    onNavigateToLogin = { currentScreen = "login" },
                    onBack = { currentScreen = "register" }
                )
                "reset_password" -> ResetPasswordScreen(
                    onNavigateToLogin = { currentScreen = "login" }
                )
            }
        }
    }
}

@Composable
fun LoginMenu(
    onNavigateToRegister: () -> Unit,
    onNavigateToReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userDao = remember { AppDatabase.getInstance(context).userDao() }
    val scrollState = rememberScrollState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(24.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "HAES",
            color = Color.White,
            fontSize = 80.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            "Health Awareness and Education System",
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Welcome Back",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "Login to your account",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 28.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = ThemeColorDark) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = ThemeColorDark,
                        focusedLabelColor = ThemeColorDark
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = ThemeColorDark) },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(image, contentDescription = null, tint = Color.Gray)
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = ThemeColorDark,
                        focusedLabelColor = ThemeColorDark
                    )
                )

                if (statusMessage.isNotEmpty()) {
                    Text(
                        statusMessage,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 8.dp),
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                Button(
                    onClick = {
                        if (email.isBlank() || password.isBlank()) {
                            statusMessage = "Please fill in all fields"
                            return@Button
                        }
                        isLoading = true
                        coroutineScope.launch {
                            val user = userDao.getUserByEmail(email)
                            if (user != null) {
                                val inputHash = PasswordHasher.hashPassword(password, user.salt)
                                if (user.passwordHash == inputHash) {
                                    statusMessage = "Success!"
                                    
                                    // Save session in SharedPreferences
                                    val sharedPref = context.getSharedPreferences("HAES_SESSION", android.content.Context.MODE_PRIVATE)
                                    sharedPref.edit().putString("LOGGED_USER_EMAIL", email).apply()

                                    val intent = Intent(context, MainActivity::class.java).apply {
                                        putExtra("USER_EMAIL", email)
                                    }
                                    context.startActivity(intent)
                                    (context as? ComponentActivity)?.finish()
                                } else {
                                    statusMessage = "Incorrect password!"
                                }
                            } else {
                                statusMessage = "User not found!"
                            }
                            isLoading = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ThemeColorDark),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Login", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }

                TextButton(
                    onClick = onNavigateToReset,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Forgot Password?", color = Color.Red , fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Don't have an account?", color = Color.White.copy(alpha = 0.8f), fontWeight = FontWeight.SemiBold)
            TextButton(onClick = onNavigateToRegister) {
                Text("Register", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun RegisterMenu(
    onNavigateToLogin: () -> Unit,
    onContinue: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf("") }

    // Password strength logic
    val passwordStrength = remember(password) {
        val hasDigit = password.any { it.isDigit() }
        val hasUpper = password.any { it.isUpperCase() }
        val hasSpecial = password.any { it in "!@#$%^&*()_+-=[]{}|;':\",./<>?" }
        
        when {
            password.isEmpty() -> 0f
            password.length < 6 -> 0.2f
            password.length >= 10 && hasDigit && hasUpper && hasSpecial -> 1f
            password.length >= 8 && (hasDigit && hasUpper || hasSpecial) -> 0.75f
            password.length >= 6 && (hasDigit || hasUpper || hasSpecial) -> 0.5f
            else -> 0.3f
        }
    }
    
    val strengthColor = when {
        passwordStrength <= 0.25f -> Color.Red
        passwordStrength <= 0.5f -> Color(0xFFFFA500) // Orange
        passwordStrength <= 0.75f -> Color.Yellow
        else -> Color(0xFF10B981) // Green
    }
    
    val strengthText = when {
        passwordStrength <= 0.25f -> "Weak"
        passwordStrength <= 0.5f -> "Fair"
        passwordStrength <= 0.75f -> "Good"
        else -> "Strong"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(24.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Create Account",
            color = Color.White,
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            "Step 1 of 2: Security Details",
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 15.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = ThemeColorDark) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = ThemeColorDark,
                        focusedLabelColor = ThemeColorDark
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = ThemeColorDark) },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(image, contentDescription = null, tint = Color.Gray)
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = ThemeColorDark,
                        focusedLabelColor = ThemeColorDark
                    )
                )
                
                // Password Strength Indicator
                if (password.isNotEmpty()) {
                    Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Strength: $strengthText", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { passwordStrength },
                            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                            color = strengthColor,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = ThemeColorDark) },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(image, contentDescription = null, tint = Color.Gray)
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = ThemeColorDark,
                        focusedLabelColor = ThemeColorDark
                    )
                )

                if (statusMessage.isNotEmpty()) {
                    Text(
                        statusMessage,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 12.dp),
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
                        if (!email.matches(emailPattern.toRegex())) {
                            statusMessage = "Please enter a valid email address"
                            return@Button
                        }
                        if (password.length < 6) {
                            statusMessage = "Password must be at least 6 characters"
                            return@Button
                        }
                        if (password != confirmPassword) {
                            statusMessage = "Passwords do not match!"
                            return@Button
                        }
                        onContinue(email, password)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ThemeColorDark),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text("Continue", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Already have an account?", color = Color.White.copy(alpha = 0.8f))
            TextButton(onClick = onNavigateToLogin) {
                Text("Login", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun RegisterSubMenu(
    initialEmail: String,
    initialPassword: String,
    onNavigateToLogin: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userDao = remember { AppDatabase.getInstance(context).userDao() }
    val scrollState = rememberScrollState()

    var name by remember { mutableStateOf("") }
    var nickName by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var birthDate by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var introduction by remember { mutableStateOf("") }
    
    val securityQuestions = listOf(
        "What is your mother's maiden name?",
        "What was the name of your first pet?",
        "What was the name of your elementary school?",
        "In what city were you born?"
    )
    var selectedQuestion by remember { mutableStateOf(securityQuestions[0]) }
    var securityAnswer by remember { mutableStateOf("") }
    var showQuestionDropdown by remember { mutableStateOf(false) }
    
    val selectedHobbies = remember { mutableStateListOf<String>() }
    val hobbiesList = listOf("Reading", "Gaming", "Sports", "Music", "Cooking", "Travel", "Tech", "Art")

    var statusMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            birthDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(24.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Complete Profile",
            color = Color.White,
            fontSize = 36.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            "Step 2 of 2: Personal Details",
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 15.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text("Account Identity", fontWeight = FontWeight.Bold, color = ThemeColorDark, modifier = Modifier.padding(bottom = 12.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = ThemeColorDark) },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = ThemeColorDark,
                        focusedLabelColor = ThemeColorDark
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = nickName,
                    onValueChange = { nickName = it },
                    label = { Text("Nickname") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = ThemeColorDark) },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = ThemeColorDark,
                        focusedLabelColor = ThemeColorDark
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))
                Text("Gender", fontWeight = FontWeight.Bold, color = ThemeColorDark)
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                    listOf("Male", "Female", "Other").forEach { option ->
                        val isSelected = gender == option
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 4.dp)
                                .clickable { gender = option },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) ThemeColorDark else MaterialTheme.colorScheme.surfaceVariant,
                            border = if (isSelected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        ) {
                            Text(
                                text = option,
                                modifier = Modifier.padding(vertical = 10.dp),
                                textAlign = TextAlign.Center,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("Birth Date", fontWeight = FontWeight.Bold, color = ThemeColorDark)
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { datePickerDialog.show() },
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = ThemeColorDark)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = if (birthDate.isEmpty()) "Select Birth Date" else birthDate,
                            color = if (birthDate.isEmpty()) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("Security Question (For Recovery)", fontWeight = FontWeight.Bold, color = ThemeColorDark)
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { showQuestionDropdown = !showQuestionDropdown },
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = ThemeColorDark)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = selectedQuestion, modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurface)
                    }
                }
                
                if (showQuestionDropdown) {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column {
                            securityQuestions.forEach { question ->
                                Text(
                                    text = question,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedQuestion = question
                                            showQuestionDropdown = false
                                        }
                                        .padding(16.dp),
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = securityAnswer,
                    onValueChange = { securityAnswer = it },
                    label = { Text("Your Answer") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = ThemeColorDark) },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = ThemeColorDark,
                        focusedLabelColor = ThemeColorDark
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text("Contact & Body Info", fontWeight = FontWeight.Bold, color = ThemeColorDark)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = contactNumber,
                    onValueChange = { contactNumber = it },
                    label = { Text("Contact Number") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    leadingIcon = { Icon(Icons.Default.Call, contentDescription = null, tint = ThemeColorDark) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = ThemeColorDark,
                        focusedLabelColor = ThemeColorDark
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = weight,
                        onValueChange = { weight = it },
                        label = { Text("Weight (kg)") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        leadingIcon = { Icon(Icons.Default.Accessibility, contentDescription = null, tint = ThemeColorDark) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = ThemeColorDark,
                            focusedLabelColor = ThemeColorDark
                        )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    OutlinedTextField(
                        value = height,
                        onValueChange = { height = it },
                        label = { Text("Height (cm)") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        leadingIcon = { Icon(Icons.Default.Height, contentDescription = null, tint = ThemeColorDark) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = ThemeColorDark,
                            focusedLabelColor = ThemeColorDark
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text("Hobbies", fontWeight = FontWeight.Bold, color = ThemeColorDark)
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    hobbiesList.chunked(2).forEach { rowHobbies ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            rowHobbies.forEach { hobby ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f).clickable {
                                        if (selectedHobbies.contains(hobby)) selectedHobbies.remove(hobby)
                                        else selectedHobbies.add(hobby)
                                    }
                                ) {
                                    Checkbox(
                                        checked = selectedHobbies.contains(hobby),
                                        onCheckedChange = {
                                            if (it) selectedHobbies.add(hobby)
                                            else selectedHobbies.remove(hobby)
                                        },
                                        colors = CheckboxDefaults.colors(checkedColor = ThemeColorDark)
                                    )
                                    Text(hobby, fontSize = 14.sp)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                OutlinedTextField(
                    value = introduction,
                    onValueChange = { introduction = it },
                    label = { Text("About You") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    leadingIcon = { Icon(Icons.Default.Info, contentDescription = null, tint = ThemeColorDark) },
                    singleLine = false,
                    minLines = 3,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = ThemeColorDark,
                        focusedLabelColor = ThemeColorDark
                    )
                )

                if (statusMessage.isNotEmpty()) {
                    Text(
                        statusMessage,
                        color = if (statusMessage.contains("Success") || statusMessage.contains("Account")) Color(0xFF10B981) else MaterialTheme.colorScheme.error,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 12.dp),
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (name.isBlank() || initialEmail.isBlank() || initialPassword.isBlank() || securityAnswer.isBlank()) {
                            statusMessage = "Name, account info and security answer are required"
                            return@Button
                        }
                        isLoading = true
                        coroutineScope.launch {
                            val existing = userDao.getUserByEmail(initialEmail)
                            if (existing == null) {
                                val salt = PasswordHasher.generateSalt()
                                val passwordHash = PasswordHasher.hashPassword(initialPassword, salt)
                                val answerHash = PasswordHasher.hashPassword(securityAnswer.lowercase().trim(), salt)
                                
                                val newUser = UserEntity(
                                    email = initialEmail,
                                    passwordHash = passwordHash,
                                    salt = salt,
                                    name = name,
                                    nickName = nickName,
                                    gender = gender,
                                    birthDate = birthDate,
                                    contactNumber = contactNumber,
                                    weight = weight,
                                    height = height,
                                    introduction = introduction,
                                    securityQuestion = selectedQuestion,
                                    securityAnswerHash = answerHash
                                )
                                userDao.insertUser(newUser)
                                
                                // NEW: Sync initial profile to Supabase
                                SupabaseManager.syncProfile(newUser)

                                statusMessage = "Account created successfully!"
                                kotlinx.coroutines.delay(1500L)
                                onNavigateToLogin()
                            } else {
                                statusMessage = "User already exists!"
                            }
                            isLoading = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ThemeColorDark),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Register", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
                
                TextButton(
                    onClick = onBack,
                    modifier = Modifier.padding(top = 8.dp).align(Alignment.CenterHorizontally)
                ) {
                    Text("Back to Step 1", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Already have an account?", color = Color.White.copy(alpha = 0.8f))
            TextButton(onClick = onNavigateToLogin) {
                Text("Login", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ResetPasswordScreen(
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userDao = remember { AppDatabase.getInstance(context).userDao() }
    val scrollState = rememberScrollState()

    var email by remember { mutableStateOf("") }
    var step by remember { mutableStateOf(1) } // 1: Email, 2: Question, 3: New Password
    
    var foundUser by remember { mutableStateOf<UserEntity?>(null) }
    var securityAnswer by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    
    var statusMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(24.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Reset Password", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(modifier = Modifier.padding(28.dp)) {
                when (step) {
                    1 -> {
                        Text("Enter your account email", color = Color.Gray, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email Address") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = ThemeColorDark) },
                            colors = TextFieldDefaults.colors(focusedIndicatorColor = ThemeColorDark)
                        )
                    }
                    2 -> {
                        Text("Security Question", fontWeight = FontWeight.Bold, color = ThemeColorDark)
                        Text(foundUser?.securityQuestion ?: "", modifier = Modifier.padding(vertical = 12.dp))
                        OutlinedTextField(
                            value = securityAnswer,
                            onValueChange = { securityAnswer = it },
                            label = { Text("Your Answer") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = TextFieldDefaults.colors(focusedIndicatorColor = ThemeColorDark)
                        )
                    }
                    3 -> {
                        OutlinedTextField(
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            label = { Text("New Password") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null)
                                }
                            },
                            colors = TextFieldDefaults.colors(focusedIndicatorColor = ThemeColorDark)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Confirm New Password") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            colors = TextFieldDefaults.colors(focusedIndicatorColor = ThemeColorDark)
                        )
                    }
                }

                if (statusMessage.isNotEmpty()) {
                    Text(statusMessage, color = MaterialTheme.colorScheme.error, fontSize = 13.sp, modifier = Modifier.padding(top = 12.dp))
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        isLoading = true
                        coroutineScope.launch {
                            when (step) {
                                1 -> {
                                    val user = userDao.getUserByEmail(email)
                                    if (user != null) {
                                        foundUser = user
                                        step = 2
                                        statusMessage = ""
                                    } else {
                                        statusMessage = "User find Error!"
                                    }
                                }
                                2 -> {
                                    val user = foundUser!!
                                    val answerHash = PasswordHasher.hashPassword(securityAnswer.lowercase().trim(), user.salt)
                                    if (user.securityAnswerHash == answerHash) {
                                        step = 3
                                        statusMessage = ""
                                    } else {
                                        statusMessage = "Incorrect answer!"
                                    }
                                }
                                3 -> {
                                    if (newPassword == confirmPassword && newPassword.length >= 6) {
                                        val salt = PasswordHasher.generateSalt()
                                        val hash = PasswordHasher.hashPassword(newPassword, salt)
                                        userDao.updatePassword(email, hash, salt)
                                        statusMessage = "Password reset successful!"
                                        kotlinx.coroutines.delay(1500L)
                                        onNavigateToLogin()
                                    } else {
                                        statusMessage = "Passwords mismatch or too short!"
                                    }
                                }
                            }
                            isLoading = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    enabled = !isLoading
                ) {
                    Text(if (step < 3) "Next" else "Reset Password", fontWeight = FontWeight.Bold)
                }

                TextButton(
                    onClick = onNavigateToLogin,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 8.dp)
                ) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        }
    }
}
