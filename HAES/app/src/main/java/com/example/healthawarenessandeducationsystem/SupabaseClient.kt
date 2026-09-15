package com.example.healthawarenessandeducationsystem

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.logging.LogLevel
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.Realtime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

@Serializable
data class ProfileSyncModel(
    val email: String,
    val name: String,
    val nickName: String,
    val gender: String,
    val birthDate: String,
    val contactNumber: String,
    val weight: String,
    val height: String,
    val introduction: String,
    val dailySteps: Int,
    val dailyWater: Int,
    val dailyExercise: Int,
    val dailyFruits: Int,
    val dailyMeditation: Int,
    val dailySleep: Float,
    val dailyTemp: String,
    val dailyBP: String,
    val dailyHR: String,
    val dailyMood: String,
    val lastHabitsDate: String,
    val bookmarkedItems: String,
    val passwordHash: String,
    val salt: String
)

@Serializable
data class RecordSyncModel(
    val userEmail: String,
    val type: String,
    val result: String,
    val date: String
)

@Serializable
data class ActivityLogSyncModel(
    val userEmail: String,
    val title: String,
    val description: String,
    val time: String,
    val emoji: String,
    val colorInt: Int
)

object SupabaseManager {
    private const val SUPABASE_URL = "https://aximprgtfesdjsvqoiry.supabase.co"
    private const val SUPABASE_KEY = "sb_publishable_Y8RoBqFA9JyOUyJwCQm8Sg_Ncv8hD4i"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Postgrest)
        install(Auth)
        install(Realtime)
        defaultLogLevel = LogLevel.NONE
    }

    suspend fun syncRecordToCloud(record: HealthRecordEntity) {
        withContext(Dispatchers.IO) {
            try {
                val data = RecordSyncModel(
                    userEmail = record.userEmail,
                    type = record.type,
                    result = record.result,
                    date = record.date
                )
                client.from("health_records").insert(data)
            } catch (e: Exception) {
                // Silently fail
            }
        }
    }

    suspend fun syncProfile(user: UserEntity) {
        withContext(Dispatchers.IO) {
            try {
                val profile = ProfileSyncModel(
                    email = user.email,
                    name = user.name,
                    nickName = user.nickName,
                    gender = user.gender,
                    birthDate = user.birthDate,
                    contactNumber = user.contactNumber,
                    weight = user.weight,
                    height = user.height,
                    introduction = user.introduction,
                    dailySteps = user.dailySteps,
                    dailyWater = user.dailyWater,
                    dailyExercise = user.dailyExercise,
                    dailyFruits = user.dailyFruits,
                    dailyMeditation = user.dailyMeditation,
                    dailySleep = user.dailySleep,
                    dailyTemp = user.dailyTemp,
                    dailyBP = user.dailyBP,
                    dailyHR = user.dailyHR,
                    dailyMood = user.dailyMood ?: "",
                    lastHabitsDate = user.lastHabitsDate ?: "",
                    bookmarkedItems = user.bookmarkedItems,
                    passwordHash = user.passwordHash,
                    salt = user.salt
                )
                client.from("profiles").upsert(profile)
            } catch (e: Exception) {
                // Silently fail
            }
        }
    }

    suspend fun syncActivityLog(log: ActivityLogEntity) {
        withContext(Dispatchers.IO) {
            try {
                val data = ActivityLogSyncModel(
                    userEmail = log.userEmail,
                    title = log.title,
                    description = log.description,
                    time = log.time,
                    emoji = log.emoji,
                    colorInt = log.colorInt
                )
                client.from("activity_logs").insert(data)
            } catch (e: Exception) {
                // Silently fail
            }
        }
    }
}
