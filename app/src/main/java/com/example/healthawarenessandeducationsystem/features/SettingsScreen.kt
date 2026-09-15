package com.example.healthawarenessandeducationsystem.features

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import kotlinx.coroutines.launch
import com.example.healthawarenessandeducationsystem.BackgroundGray
import com.example.healthawarenessandeducationsystem.DividerGray
import com.example.healthawarenessandeducationsystem.SubTextGray
import com.example.healthawarenessandeducationsystem.TextDark
import com.example.healthawarenessandeducationsystem.ThemeGreenDark
import com.example.healthawarenessandeducationsystem.isDarkModeGlobal

@Composable
fun SettingsScreen(userEmail: String) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(isDarkModeGlobal) }

    // Dialog States
    var showPasswordDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(20.dp)
    ) {
        Text(
            text = "Settings",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 24.dp, top = 16.dp)
        )

        SettingsSection("Preferences") {
            SettingsToggleItem("Notifications", Icons.Default.Notifications, notificationsEnabled) {
                notificationsEnabled = it
                val msg = if (it) "Notifications Enabled" else "Notifications Disabled"
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
            HorizontalDivider(color = DividerGray)
            SettingsToggleItem("Dark Mode", Icons.Default.DarkMode, darkModeEnabled) {
                darkModeEnabled = it
                isDarkModeGlobal = it // Trigger global theme change
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        SettingsSection("Account & Security") {
            SettingsNavigationItem("Change Password", Icons.Default.Lock) {
                showPasswordDialog = true
            }
            HorizontalDivider(color = DividerGray)
            SettingsNavigationItem("Privacy Policy", Icons.Default.PrivacyTip) {
                showPrivacyDialog = true
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        SettingsSection("Support") {
            SettingsNavigationItem("Help Center", Icons.AutoMirrored.Filled.Help) {
                Toast.makeText(context, "Redirecting to Help Center...", Toast.LENGTH_SHORT).show()
            }
            HorizontalDivider(color = DividerGray)
            SettingsNavigationItem("About HAES", Icons.Default.Info) {
                showAboutDialog = true
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }

    // Dialogs Implementation
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text("About HAES", fontWeight = FontWeight.Bold) },
            text = { Text("Health Awareness and Education System (HAES) v1.0.0\n\nDeveloped for Mobile App Development Assignment.") },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) { Text("OK", color = ThemeGreenDark) }
            }
        )
    }

    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = { Text("Privacy Policy", fontWeight = FontWeight.Bold) },
            text = { Text("Your health data is stored locally on this device and is not shared with any third parties.") },
            confirmButton = {
                TextButton(onClick = { showPrivacyDialog = false }) { Text("Close", color = ThemeGreenDark) }
            }
        )
    }

    if (showPasswordDialog) {
        var newPass by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showPasswordDialog = false },
            title = { Text("Change Password", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Enter new password (min 6 chars):", fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newPass,
                        onValueChange = { newPass = it },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newPass.length >= 6) {
                        coroutineScope.launch {
                            val userDao = com.example.healthawarenessandeducationsystem.AppDatabase.getInstance(context).userDao()
                            val salt = com.example.healthawarenessandeducationsystem.PasswordHasher.generateSalt()
                            val hash = com.example.healthawarenessandeducationsystem.PasswordHasher.hashPassword(newPass, salt)
                            
                            // 1. Update SQLite
                            userDao.updatePassword(userEmail, hash, salt)
                            
                            // 2. Sync full updated profile to Supabase
                            val updatedUser = userDao.getUserByEmail(userEmail)
                            updatedUser?.let { 
                                com.example.healthawarenessandeducationsystem.SupabaseManager.syncProfile(it)
                            }
                            
                            Toast.makeText(context, "Password updated successfully!", Toast.LENGTH_SHORT).show()
                            showPasswordDialog = false
                        }
                    } else {
                        Toast.makeText(context, "Password too short!", Toast.LENGTH_SHORT).show()
                    }
                }) { Text("Update", color = ThemeGreenDark) }
            },
            dismissButton = {
                TextButton(onClick = { showPasswordDialog = false }) { Text("Cancel", color = Color.Gray) }
            }
        )
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = ThemeGreenDark,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(content = content)
        }
    }
}

@Composable
fun SettingsToggleItem(
    title: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = ThemeGreenDark, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = ThemeGreenDark)
        )
    }
}

@Composable
fun SettingsNavigationItem(title: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = ThemeGreenDark, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = SubTextGray)
    }
}
