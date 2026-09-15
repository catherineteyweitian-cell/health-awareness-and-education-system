package com.example.healthawarenessandeducationsystem.features

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthawarenessandeducationsystem.isDarkModeGlobal

//Colors setting
private val HeaderBlue = Color(0xFF5274F2)
private val HeaderPurple = Color(0xFF8B5CF6) 
private val SelectedBlue = Color(0xFF3E82F7)
private val ScreenBackground = Color(0xFFF8F9FF) 
private val DarkText = Color(0xFF15213A)
private val GrayText = Color(0xFF728097)
private val BookmarkYellow = Color(0xFFFFB020)

//Gradient brush
private val LibraryGradient = Brush.horizontalGradient(listOf(HeaderBlue, HeaderPurple))


//Data model
data class HealthItem(
    val title: String,
    val category: String,
    val description: String,
    val overview: String,
    val causes: List<String>,
    val symptoms: List<String>,
    val prevention: String,
    val emoji: String,  //Emoji uses as the icon
    val iconBackground: Color,
    val tagColor: Color   //Color of the category tag
)

//Information
private val diseases = listOf(

    HealthItem(
        title = "Influenza",
        category = "Respiratory",
        description = "Seasonal viral infection affecting the respiratory system",
        overview = "Influenza, also called flu, is a contagious respiratory illness caused by influenza viruses. It can affect the nose, throat, and lungs.",
        causes = listOf(
            "Influenza A, B, or C viruses",
            "Close contact with infected persons",
            "Contaminated surfaces"
        ),
        symptoms = listOf(
            "High fever",
            "Body aches and fatigue",
            "Dry cough",
            "Sore throat",
            "Runny nose"
        ),
        prevention = "Get an annual flu vaccine, wash your hands regularly, cover your mouth when coughing or sneezing, and avoid close contact with people who are unwell.",
        emoji = "🫁",
        iconBackground = Color(0xFFEAF2FF),
        tagColor = Color(0xFF4A7EF3)
    ),

    HealthItem(
        title = "COVID-19",
        category = "Infectious",
        description = "Respiratory illness caused by SARS-CoV-2 coronavirus",
        overview = "COVID-19 is an infectious disease caused by the SARS-CoV-2 virus. Symptoms can range from mild respiratory symptoms to more serious illness.",
        causes = listOf(
            "Infection with SARS-CoV-2",
            "Close contact with an infected person",
            "Exposure to respiratory particles"
        ),
        symptoms = listOf(
            "Fever or chills",
            "Cough",
            "Tiredness",
            "Loss of taste or smell",
            "Shortness of breath"
        ),
        prevention = "Wash your hands regularly, keep indoor spaces well ventilated, avoid close contact with people who are sick, and follow current health guidance.",
        emoji = "🦠",
        iconBackground = Color(0xFFFFECEE),
        tagColor = Color(0xFFFF4D5B)
    ),

    HealthItem(
        title = "Dengue Fever",
        category = "Infectious",
        description = "Mosquito-borne viral infection common in tropical regions",
        overview = "Dengue fever is a viral infection spread mainly through the bites of infected Aedes mosquitoes. It is common in tropical and subtropical regions.",
        causes = listOf(
            "Bite from an infected Aedes mosquito",
            "Mosquitoes breeding in standing water",
            "Living or travelling in dengue-risk areas"
        ),
        symptoms = listOf(
            "Sudden high fever",
            "Severe headache",
            "Pain behind the eyes",
            "Muscle and joint pain",
            "Skin rash"
        ),
        prevention = "Use mosquito repellent, wear long sleeves, use mosquito nets, and remove standing water from containers around your home.",
        emoji = "🦟",
        iconBackground = Color(0xFFFFF2E8),
        tagColor = Color(0xFFFF762E)
    ),

    HealthItem(
        title = "Diabetes",
        category = "Metabolic",
        description = "Chronic condition affecting blood sugar levels",
        overview = "Diabetes is a chronic condition in which the body has difficulty regulating blood glucose levels. It requires ongoing health management.",
        causes = listOf(
            "Family history",
            "Insulin resistance",
            "Overweight or obesity",
            "Physical inactivity"
        ),
        symptoms = listOf(
            "Frequent urination",
            "Increased thirst",
            "Increased hunger",
            "Unexplained weight loss",
            "Tiredness or blurred vision"
        ),
        prevention = "Maintain a healthy lifestyle, eat balanced meals, exercise regularly, reduce sugary drinks, and attend regular health check-ups.",
        emoji = "🩸",
        iconBackground = Color(0xFFF4ECFF),
        tagColor = Color(0xFF9C5BFF)
    ),

    HealthItem(
        title = "Asthma",
        category = "Respiratory",
        description = "A condition that causes narrowing and inflammation of the airways",
        overview = "Asthma is a chronic condition that affects the airways. The airways can become inflamed and narrowed, making breathing difficult.",
        causes = listOf(
            "Allergens such as dust or pollen",
            "Respiratory infections",
            "Smoke and air pollution",
            "Exercise or cold air"
        ),
        symptoms = listOf(
            "Wheezing",
            "Shortness of breath",
            "Chest tightness",
            "Coughing",
            "Difficulty breathing"
        ),
        prevention = "Avoid known triggers, reduce exposure to smoke and dust, follow prescribed treatment plans, and keep indoor areas clean and well ventilated.",
        emoji = "🫁",
        iconBackground = Color(0xFFE8F5FF),
        tagColor = Color(0xFF2588E8)
    ),

    HealthItem(
        title = "Food Poisoning",
        category = "Digestive",
        description = "Illness caused by consuming contaminated food or drinks",
        overview = "Food poisoning occurs when a person consumes food or drinks contaminated with harmful bacteria, viruses, parasites, or toxins.",
        causes = listOf(
            "Contaminated food",
            "Contaminated drinking water",
            "Poor food handling",
            "Improper food storage"
        ),
        symptoms = listOf(
            "Nausea",
            "Vomiting",
            "Diarrhea",
            "Stomach pain",
            "Fever"
        ),
        prevention = "Wash hands before preparing food, cook food thoroughly, separate raw and cooked foods, and store food at safe temperatures.",
        emoji = "🍽️",
        iconBackground = Color(0xFFFFF4E5),
        tagColor = Color(0xFFFF9F2E)
    ),
)

private val symptomsData = listOf(

    HealthItem(
        title = "Fever",
        category = "General",
        description = "A temporary rise in body temperature",
        overview = "A fever is a temporary increase in body temperature. It can occur when the immune system responds to infection or other conditions.",
        causes = listOf(
            "Viral infection",
            "Bacterial infection",
            "Inflammation",
            "Heat-related illness"
        ),
        symptoms = listOf(
            "Feeling hot or cold",
            "Sweating",
            "Headache",
            "Muscle aches",
            "Weakness"
        ),
        prevention = "Wash hands regularly, stay hydrated, get enough rest, and seek medical advice if fever is severe or persistent.",
        emoji = "🌡️",
        iconBackground = Color(0xFFFFECEE),
        tagColor = Color(0xFFFF4D5B)
    ),

    HealthItem(
        title = "Cough",
        category = "Respiratory",
        description = "A reflex that helps clear your airways",
        overview = "Coughing is a natural reflex that helps remove mucus, germs, and irritants from the throat and airways.",
        causes = listOf(
            "Common cold or flu",
            "Allergies",
            "Asthma",
            "Air pollution or smoke"
        ),
        symptoms = listOf(
            "Dry cough",
            "Wet cough",
            "Irritated throat",
            "Mucus production",
            "Chest discomfort"
        ),
        prevention = "Avoid smoking and second-hand smoke, wash hands often, and reduce exposure to known allergens and irritants.",
        emoji = "😮‍💨",
        iconBackground = Color(0xFFEAF2FF),
        tagColor = Color(0xFF4A7EF3)
    ),

    HealthItem(
        title = "Headache",
        category = "Neurological",
        description = "Pain or discomfort in the head or face",
        overview = "A headache is pain or discomfort in the head, scalp, or face. It can occur for many reasons, including stress, fatigue, illness, or dehydration.",
        causes = listOf(
            "Stress",
            "Lack of sleep",
            "Dehydration",
            "Eye strain"
        ),
        symptoms = listOf(
            "Head pain or pressure",
            "Sensitivity to light",
            "Nausea",
            "Difficulty concentrating",
            "Neck tension"
        ),
        prevention = "Drink enough water, sleep well, manage stress, take breaks from screens, and avoid skipping meals.",
        emoji = "🤕",
        iconBackground = Color(0xFFF4ECFF),
        tagColor = Color(0xFF9C5BFF)
    ),

    HealthItem(
        title = "Fatigue",
        category = "General",
        description = "A feeling of tiredness or lack of energy",
        overview = "Fatigue is a feeling of physical or mental tiredness that can make normal activities more difficult.",
        causes = listOf(
            "Lack of sleep",
            "Stress",
            "Poor nutrition",
            "Infections or illness"
        ),
        symptoms = listOf(
            "Low energy",
            "Difficulty concentrating",
            "Sleepiness",
            "Reduced motivation",
            "Physical weakness"
        ),
        prevention = "Maintain a regular sleep schedule, eat balanced meals, stay hydrated, exercise appropriately, and manage stress.",
        emoji = "😴",
        iconBackground = Color(0xFFEFF2FF),
        tagColor = Color(0xFF687BE8)
    ),

    HealthItem(
        title = "Nausea",
        category = "Digestive",
        description = "An unpleasant feeling that you may vomit",
        overview = "Nausea is an uncomfortable sensation in the stomach that may occur with or without vomiting.",
        causes = listOf(
            "Food poisoning",
            "Stomach infection",
            "Motion sickness",
            "Certain medicines"
        ),
        symptoms = listOf(
            "Feeling sick",
            "Loss of appetite",
            "Stomach discomfort",
            "Sweating",
            "Possible vomiting"
        ),
        prevention = "Eat food safely, stay hydrated, avoid known triggers, and maintain good hygiene.",
        emoji = "🤢",
        iconBackground = Color(0xFFEAF9EE),
        tagColor = Color(0xFF31A85B)
    ),

    HealthItem(
        title = "Diarrhea",
        category = "Digestive",
        description = "Frequent loose or watery bowel movements",
        overview = "Diarrhea refers to frequent loose or watery stools. It can be caused by infections, food-related problems, or other conditions.",
        causes = listOf(
            "Viral infection",
            "Bacterial infection",
            "Contaminated food or water",
            "Food intolerance"
        ),
        symptoms = listOf(
            "Loose stools",
            "Abdominal cramps",
            "Urgent bowel movements",
            "Nausea",
            "Dehydration"
        ),
        prevention = "Wash hands regularly, drink safe water, handle food safely, and maintain good food hygiene.",
        emoji = "💧",
        iconBackground = Color(0xFFEAF7FF),
        tagColor = Color(0xFF2D91D6)
    ),
)

//This is the main screen
@Composable
fun HealthLibraryScreen(
    bookmarkedItems: Set<String>,
    onBookmarkToggle: (Set<String>) -> Unit
) {

    var searchText by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf("Diseases") }
    var selectedHealthItem by remember { mutableStateOf<HealthItem?>(null) }
    
    // Select which list to display
    val currentItems = when (selectedTab) {
        "Diseases" -> diseases
        "Symptoms" -> symptomsData
        "Saved" -> (diseases + symptomsData).filter { bookmarkedItems.contains(it.title) }
        else -> diseases
    }
    
    //Search filter
    val displayedItems = currentItems.filter { item ->
        item.title.contains(searchText, ignoreCase = true) ||
        item.category.contains(searchText, ignoreCase = true) ||
        item.description.contains(searchText, ignoreCase = true)
    }


    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        //detail page
        selectedHealthItem?.let { item ->

            DiseaseDetailScreen(

                item = item,
                //Check whether this item is bookmarked
                isBookmarked = bookmarkedItems.contains(item.title),
                //Called when user clicks bookmark
                onBookmarkClick = {
                    val newList = if (bookmarkedItems.contains(item.title)) {
                        bookmarkedItems - item.title
                    } else {
                        bookmarkedItems + item.title
                    }
                    onBookmarkToggle(newList)
                },

                paddingValues = paddingValues,
                //Go back to Health Library
                onBack = { selectedHealthItem = null }
            )

        } ?: run {

            //Main library page
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                //Header and Search
                HeaderSection(
                    searchText = searchText,
                    onSearchChange = { searchText = it }
                )

                //Diseases,symptoms and saved
                MainTabs(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )

                //Health item list (for large number of items)
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {

                    //No search result
                    if (displayedItems.isEmpty()) {
                        item {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(top = 60.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = if (selectedTab == "Saved") "🔖" else "🔍",
                                    fontSize = 50.sp
                                )
                                Text(
                                    text = if (selectedTab == "Saved") "No bookmarked items yet." else "No health items found.",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    //Health cards(display each health item)
                    items(displayedItems) { item ->
                        HealthCard(
                            item = item,
                            isBookmarked = bookmarkedItems.contains(item.title),
                            //Bookmark button
                            onBookmarkClick = {
                                val newList = if (bookmarkedItems.contains(item.title)) {
                                    bookmarkedItems - item.title
                                } else {
                                    bookmarkedItems + item.title
                                }
                                onBookmarkToggle(newList)
                            },
                            //Click card to open detail page
                            onClick = { selectedHealthItem = item }
                        )
                    }
                }
            }
        }
    }
}

//Header section for Title and Search Box
@Composable
fun HeaderSection(
    searchText: String,
    onSearchChange: (String) -> Unit
) {
    val currentGradient = if (isDarkModeGlobal) {
        Brush.horizontalGradient(listOf(Color(0xFF1E1B4B), Color(0xFF4C1D95)))
    } else {
        LibraryGradient
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
            .background(currentGradient)
            .padding(top = 32.dp, bottom = 28.dp, start = 20.dp, end = 20.dp)
    ) {
        Column {
            //Page title
            Text(
                text = "Health Library",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            //Search box
            TextField(
                value = searchText,
                onValueChange = onSearchChange,
                modifier = Modifier.fillMaxWidth().height(54.dp),
                placeholder = { Text("Search...", color = Color.White.copy(alpha = 0.7f)) },
                leadingIcon = { Text("🔍", modifier = Modifier.padding(start = 8.dp)) },
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White.copy(alpha = 0.2f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.2f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
        }
    }
}

//Main tabs for Disease, Symptoms, Saved tabs
@Composable
fun MainTabs(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val tabs = listOf("Diseases", "Symptoms", "Saved")
        tabs.forEach { tab ->
            val isSelected = selectedTab == tab
            Surface(
                modifier = Modifier.weight(1f).clickable { onTabSelected(tab) },
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) SelectedBlue else MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp
            ) {
                Box(modifier = Modifier.padding(vertical = 10.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = tab,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

//HealthCard
@Composable
fun HealthCard(
    item: HealthItem,
    isBookmarked: Boolean,
    onBookmarkClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }, //Clicking the card then opens detail page
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //Health icon
            Box(
                modifier = Modifier.size(50.dp).clip(RoundedCornerShape(12.dp))
                    .background(if (isDarkModeGlobal) item.iconBackground.copy(alpha = 0.2f) else item.iconBackground),
                contentAlignment = Alignment.Center
            ) {
                Text(item.emoji, fontSize = 26.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            //Health information
            Column(modifier = Modifier.weight(1f)) {
                Text(item.title, color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                
                //Category tag
                Text(
                    text = item.category,
                    color = item.tagColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                //Description
                Text(
                    text = item.description,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            //Bookmark button
            Text(
                //Filled bookmark if saved
                //Heart shape if not saved
                text = if (isBookmarked) "🔖" else "♡",
                fontSize = 24.sp,
                color = if (isBookmarked) BookmarkYellow else Color.LightGray,
                modifier = Modifier.clickable { onBookmarkClick() }.padding(8.dp)
            )
        }
    }
}

//This page is shown all information details when clicking the health card
@Composable
fun DiseaseDetailScreen(

    item: HealthItem,
    isBookmarked: Boolean,
    onBookmarkClick: () -> Unit,
    paddingValues: PaddingValues,
    onBack: () -> Unit

) {
    val currentGradient = if (isDarkModeGlobal) {
        Brush.horizontalGradient(listOf(Color(0xFF1E1B4B), Color(0xFF4C1D95)))
    } else {
        LibraryGradient
    }

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        //RATIONALIZED HEADER BOX
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp) 
                .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                .background(currentGradient)
                .padding(20.dp)
        ) {
            // Back button - Professional floating style
            Surface(
                onClick = onBack,
                color = Color.White.copy(alpha = 0.2f),
                shape = CircleShape,
                modifier = Modifier.statusBarsPadding().size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        //FLOATING ICON BADGE
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .offset(y = (-55).dp) // This makes the icon overlap perfectly
        ) {
            Card(
                modifier = Modifier.size(110.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(if (isDarkModeGlobal) item.iconBackground.copy(alpha = 0.2f) else item.iconBackground.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = item.emoji, fontSize = 60.sp)
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = (-35).dp) // Adjust content to follow the overlapping icon
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            contentPadding = PaddingValues(bottom = 60.dp)
        ) {
            //Title and bookmark
            item {
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically) {
                    
                    //title
                    Column(modifier = Modifier.weight(1f)) {
                        Text(item.title, color = MaterialTheme.colorScheme.onSurface, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                        
                        //category
                        Text(
                            text = item.category,
                            color = item.tagColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    //Bookmark button
                    Surface(
                        onClick = onBookmarkClick,
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 2.dp,
                        shadowElevation = 2.dp,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = if (isBookmarked) "🔖" else "♡",
                                fontSize = 28.sp
                            )
                        }
                    }
                }
            }

            //Overview
            item {
                DetailSection(
                    title = "Overview",
                    content = item.overview
                )
            }

            //Causes and Symptoms
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Potential Causes", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(14.dp))
                        
                        item.causes.forEach { cause ->
                            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                                Text("•", color = item.tagColor, fontWeight = FontWeight.Bold, modifier = Modifier.width(20.dp))
                                Text(cause, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp), color = MaterialTheme.colorScheme.outlineVariant)

                        //Symptoms title
                        Text("Common Symptoms", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(14.dp))

                        //Display every symptom
                        item.symptoms.forEach { symptom ->
                            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                                Text("✓", color = Color(0xFF00A86B), fontWeight = FontWeight.Bold, modifier = Modifier.width(20.dp))
                                Text(symptom, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }

            //Prevention
            item {
                DetailSection(
                    title = "Prevention",
                    content = item.prevention
                )
            }

            //Notice
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = if (isDarkModeGlobal) Color(0xFF1E3A8A).copy(alpha = 0.2f) else Color(0xFFEAF2FF)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("💡", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "This information is for awareness only. Please seek professional medical advice.",
                            color = MaterialTheme.colorScheme.onSurface, fontSize = 12.sp, fontWeight = FontWeight.Medium,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

//Overview and prevention
@Composable
fun DetailSection(
    title: String,
    content: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            //Section title
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(10.dp))

            //Section content
            Text(
                text = content,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 15.sp,
                lineHeight = 22.sp
            )
        }
    }
}
