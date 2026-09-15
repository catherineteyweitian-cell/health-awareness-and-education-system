package com.example.healthawarenessandeducationsystem.features

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthawarenessandeducationsystem.BackgroundGray
import com.example.healthawarenessandeducationsystem.SubTextGray
import com.example.healthawarenessandeducationsystem.TextDark
import com.example.healthawarenessandeducationsystem.ThemeGreenDark
import com.example.healthawarenessandeducationsystem.isDarkModeGlobal

val QuizBlue = Color(0xFF3B82F6)
val QuizPurple = Color(0xFF8B5CF6)

@Composable
fun getQuizGradient(): Brush {
    return if (isDarkModeGlobal) {
        Brush.verticalGradient(listOf(Color(0xFF1E1B4B), Color(0xFF4C1D95))) // 深紫色系
    } else {
        Brush.verticalGradient(listOf(QuizBlue, QuizPurple))
    }
}

@Composable
fun getQuizHeaderGradient(): Brush {
    return if (isDarkModeGlobal) {
        Brush.horizontalGradient(listOf(Color(0xFF064E3B), Color(0xFF065F46))) // 深绿色系
    } else {
        Brush.horizontalGradient(listOf(Color(0xFF006D44), Color(0xFF00E5FF)))
    }
}

val MyQuizzes = listOf(
    // 1. Disease Quiz (50 Questions)
    QuizCategory(
        name = "Disease Quiz",
        icon = "🩺",
        questions = listOf(
            QuizQuestion("Which organism causes Malaria?", listOf("Virus", "Bacteria", "Parasite", "Fungi"), 2, "Malaria is transmitted by Plasmodium parasites through mosquito bites."),
            QuizQuestion("What is the primary cause of Type 1 Diabetes?", listOf("Obesity", "Autoimmune reaction", "Excessive sugar", "Lack of exercise"), 1, "It occurs when the immune system destroys insulin-producing beta cells."),
            QuizQuestion("Which organ does Hepatitis primarily affect?", listOf("Heart", "Lungs", "Liver", "Kidneys"), 2, "Hepatitis refers to inflammation of the liver tissue."),
            QuizQuestion("Tuberculosis is primarily an infection of which organ?", listOf("Brain", "Lungs", "Skin", "Stomach"), 1, "TB most commonly affects the lungs but can spread to other parts."),
            QuizQuestion("Which of these is a common symptom of Influenza?", listOf("High fever", "Joint stiffness", "Hair loss", "Blindness"), 0, "Flu often causes a sudden high temperature of 38C or above."),
            QuizQuestion("What is the main cause of the common cold?", listOf("Bacteria", "Viruses", "Cold weather", "Allergies"), 1, "Over 200 viruses can cause the common cold, rhinoviruses being most common."),
            QuizQuestion("Which condition is characterized by high blood pressure?", listOf("Anemia", "Hypoglycemia", "Hypertension", "Asthma"), 2, "Hypertension often has no symptoms but increases heart disease risk."),
            QuizQuestion("Which disease is prevented by the MMR vaccine?", listOf("Measles", "Malaria", "Meningitis", "Mononucleosis"), 0, "MMR protects against Measles, Mumps, and Rubella."),
            QuizQuestion("What is a common risk factor for Lung Cancer?", listOf("High salt intake", "Smoking", "Lack of sleep", "Sugar"), 1, "Smoking causes about 85% of all lung cancer cases."),
            QuizQuestion("Cholera is typically spread through what?", listOf("Air", "Contaminated water", "Mosquitoes", "Touch"), 1, "Cholera is an acute diarrheal infection caused by ingestion of contaminated food or water."),
            QuizQuestion("Which of these is a chronic respiratory disease?", listOf("COPD", "Ebola", "Scurvy", "Chickenpox"), 0, "COPD makes it hard to breathe and gets worse over time."),
            QuizQuestion("What characterizes Alzheimer’s disease?", listOf("Muscle weakness", "Memory loss", "High fever", "Skin rash"), 1, "Alzheimer's is the most common cause of dementia in older adults."),
            QuizQuestion("Osteoporosis affects which part of the body?", listOf("Muscles", "Bones", "Eyes", "Blood"), 1, "It causes bones to become weak and brittle."),
            QuizQuestion("Which virus causes AIDS?", listOf("HPV", "HIV", "HSV", "H1N1"), 1, "HIV attacks the body's immune system, specifically the CD4 cells."),
            QuizQuestion("Glaucoma is a disease that affects what?", listOf("Hearing", "Vision", "Smell", "Touch"), 1, "Glaucoma damages the eye's optic nerve and can cause blindness."),
            QuizQuestion("What is the primary symptom of Anemia?", listOf("Fatigue", "Chest pain", "Fever", "Nausea"), 0, "Anemia means you don't have enough healthy red blood cells to carry oxygen."),
            QuizQuestion("Which of these is a bacterial infection?", listOf("Flu", "Strep throat", "COVID-19", "Zika"), 1, "Strep throat is caused by Group A Streptococcus bacteria."),
            QuizQuestion("Arthritis causes inflammation in which area?", listOf("Brain", "Joints", "Lungs", "Skin"), 1, "Arthritis involves pain and swelling in one or more joints."),
            QuizQuestion("Scurvy is caused by a deficiency of which vitamin?", listOf("Vitamin A", "Vitamin B", "Vitamin C", "Vitamin D"), 2, "Vitamin C is essential for collagen production and tissue repair."),
            QuizQuestion("What is the main vector for Lyme disease?", listOf("Mosquitoes", "Ticks", "Fleas", "Rats"), 1, "Lyme disease is spread by the bite of infected black-legged ticks."),
            QuizQuestion("Which organ produces insulin?", listOf("Liver", "Pancreas", "Gallbladder", "Spleen"), 1, "The pancreas releases insulin to help regulate blood sugar levels."),
            QuizQuestion("Epilepsy is a disorder of which system?", listOf("Digestive", "Circulatory", "Nervous", "Skeletal"), 2, "Epilepsy is a central nervous system (neurological) disorder."),
            QuizQuestion("Which condition involves the hardening of arteries?", listOf("Atherosclerosis", "Cirrhosis", "Dermatitis", "Gingivitis"), 0, "Atherosclerosis is the buildup of fats and cholesterol in artery walls."),
            QuizQuestion("Celiac disease is triggered by what?", listOf("Dairy", "Gluten", "Peanuts", "Shellfish"), 1, "Gluten is a protein found in wheat, barley, and rye."),
            QuizQuestion("Which disease is known as the 'Silent Killer'?", listOf("Diabetes", "Hypertension", "Flu", "Asthma"), 1, "Hypertension often has no symptoms until serious damage occurs."),
            QuizQuestion("What causes Ringworm?", listOf("Worms", "Fungi", "Bacteria", "Virus"), 1, "Despite the name, ringworm is a fungal infection, not a worm."),
            QuizQuestion("Parkinson's disease primarily affects what?", listOf("Digestion", "Movement", "Vision", "Hearing"), 1, "It is a progressive disorder that affects the nervous system and movement."),
            QuizQuestion("Which of these is an autoimmune disease?", listOf("Lupus", "Tetanus", "Rabies", "Smallpox"), 0, "Lupus occurs when the immune system attacks its own tissues."),
            QuizQuestion("Gout is a type of what?", listOf("Allergy", "Arthritis", "Virus", "Infection"), 1, "Gout is characterized by sudden, severe attacks of joint pain, often in the big toe."),
            QuizQuestion("What is the main cause of Cirrhosis?", listOf("Sugar", "Alcohol abuse", "Smoking", "Salt"), 1, "Cirrhosis is late-stage scarring of the liver caused by many forms of liver diseases."),
            QuizQuestion("Which condition is defined by high blood sugar levels?", listOf("Hypotension", "Diabetes", "Anemia", "Insomnia"), 1, "Diabetes refers to a group of diseases that affect how your body uses blood sugar."),
            QuizQuestion("Dengue fever is transmitted by?", listOf("Ticks", "Mosquitoes", "Flies", "Birds"), 1, "Dengue is spread by Aedes species mosquitoes."),
            QuizQuestion("Meningitis is inflammation of the membranes surrounding what?", listOf("Heart", "Brain and spinal cord", "Lungs", "Kidneys"), 1, "Meningitis affects the protective membranes (meninges) of the brain and spine."),
            QuizQuestion("Which deficiency causes Rickets?", listOf("Vitamin D", "Iron", "Vitamin K", "Iodine"), 0, "Vitamin D helps the body absorb calcium and phosphorus for bone health."),
            QuizQuestion("What is a common sign of Jaundice?", listOf("Blue nails", "Yellowing of skin", "Red rash", "Pale lips"), 1, "Jaundice is caused by a buildup of bilirubin in the blood."),
            QuizQuestion("Which of these is a viral skin infection?", listOf("Acne", "Shingles", "Eczema", "Psoriasis"), 1, "Shingles is caused by the varicella-zoster virus, the same virus that causes chickenpox."),
            QuizQuestion("Cystic Fibrosis primarily affects which systems?", listOf("Nervous", "Respiratory and Digestive", "Skeletal", "Immune"), 1, "It causes thick, sticky mucus to build up in the lungs and digestive tract."),
            QuizQuestion("What causes Tetanus?", listOf("Bacteria", "Virus", "Fungi", "Protozoa"), 0, "Tetanus is a serious disease caused by Clostridium tetani bacteria entering wounds."),
            QuizQuestion("Hypothyroidism involves an underactive what?", listOf("Adrenal gland", "Thyroid gland", "Pituitary gland", "Thymus"), 1, "The thyroid gland doesn't produce enough of certain crucial hormones."),
            QuizQuestion("Which disease did the Jonas Salk vaccine eliminate in many countries?", listOf("Measles", "Polio", "Smallpox", "Mumps"), 1, "Salk developed the first successful polio vaccine in 1955."),
            QuizQuestion("Angina is a symptom related to which organ?", listOf("Brain", "Heart", "Stomach", "Liver"), 1, "Angina is chest pain caused by reduced blood flow to the heart."),
            QuizQuestion("Which condition is caused by a lack of iron?", listOf("Scurvy", "Anemia", "Beriberi", "Rickets"), 1, "Iron is needed to make hemoglobin, which carries oxygen in red blood cells."),
            QuizQuestion("Leukemia is a cancer of what?", listOf("Skin", "Blood", "Bones", "Lungs"), 1, "Leukemia is cancer of the body's blood-forming tissues, including bone marrow."),
            QuizQuestion("What is the primary cause of Emphysema?", listOf("Sugar", "Smoking", "Pollution", "Genetics"), 1, "Smoking is the leading cause of emphysema, which damages the air sacs in the lungs."),
            QuizQuestion("Which pathogen causes the Plague?", listOf("Virus", "Bacteria", "Parasite", "Prion"), 1, "The plague is caused by the bacterium Yersinia pestis."),
            QuizQuestion("Psoriasis is a condition affecting the?", listOf("Hair", "Skin", "Nails", "Teeth"), 1, "Psoriasis is a skin disease that causes red, itchy scaly patches."),
            QuizQuestion("Which virus is responsible for many cervical cancers?", listOf("HIV", "HPV", "HSV", "EBV"), 1, "Human Papillomavirus (HPV) is a very common virus linked to cancer."),
            QuizQuestion("Multiple Sclerosis (MS) affects which part of the body?", listOf("Skin", "Myelin sheath of nerves", "Bones", "Blood vessels"), 1, "MS is a disease in which the immune system eats away at the protective covering of nerves."),
            QuizQuestion("What is the main symptom of Cataracts?", listOf("Eye pain", "Cloudy vision", "Hearing loss", "Redness"), 1, "Cataracts cause the lens of the eye to become cloudy, like looking through a frosty window."),
            QuizQuestion("Rabies is usually transmitted via?", listOf("Air", "Animal bite", "Water", "Food"), 1, "Rabies is a deadly virus spread to people from the saliva of infected animals.")
        )
    ),

    // 2. Symptom Quiz (50 Questions)
    QuizCategory(
        name = "Symptom Quiz",
        icon = "🩺",
        questions = listOf(
            QuizQuestion("What does a 'productive cough' mean?", listOf("Dry cough", "Coughing up mucus", "Silent cough", "Constant cough"), 1, "A productive cough helps clear irritants and infections from your lungs."),
            QuizQuestion("Sudden numbness on one side of the body may indicate?", listOf("Heart attack", "Stroke", "Seizure", "Fainting"), 1, "Numbness on one side is a classic sign of a stroke. Think FAST!"),
            QuizQuestion("What is 'Edema'?", listOf("Redness", "Swelling caused by fluid", "Muscle pain", "Fever"), 1, "Edema is swelling caused by excess fluid trapped in your body's tissues."),
            QuizQuestion("Which symptom is most common with a migraine?", listOf("Earache", "Severe headache", "Lower back pain", "Skin rash"), 1, "Migraines are often accompanied by nausea and sensitivity to light."),
            QuizQuestion("Shortness of breath is medically known as?", listOf("Apnea", "Dyspnea", "Tachypnea", "Cyanosis"), 1, "Dyspnea is often described as intense tightening in the chest or air hunger."),
            QuizQuestion("A 'butterfly rash' on the face is a symptom of?", listOf("Lupus", "Acne", "Eczema", "Measles"), 0, "A butterfly-shaped rash across the cheeks and nose is common in systemic lupus."),
            QuizQuestion("What does 'Cyanosis' look like?", listOf("Yellowing", "Bluish discoloration", "Bright redness", "Pale skin"), 1, "Cyanosis occurs when there isn't enough oxygen in the blood, causing a blue tint."),
            QuizQuestion("Chest pressure and pain radiating to the arm is a sign of?", listOf("Asthma", "Heart Attack", "Indigestion", "Flu"), 1, "Pain that spreads to the left arm, neck, or jaw is a warning sign of a heart attack."),
            QuizQuestion("Excessive thirst and frequent urination are signs of?", listOf("Anemia", "Diabetes", "Hypertension", "Dehydration"), 1, "High blood sugar levels cause the kidneys to work harder to filter sugar, leading to thirst."),
            QuizQuestion("What is the term for a ringing sound in the ears?", listOf("Vertigo", "Tinnitus", "Aphasia", "Dyslexia"), 1, "Tinnitus is the perception of noise or ringing in the ears when no external sound is present."),
            QuizQuestion("Persistent night sweats may be a symptom of?", listOf("Tuberculosis", "Asthma", "Allergies", "Psoriasis"), 0, "Heavy night sweats can be a sign of underlying infections like TB or certain cancers."),
            QuizQuestion("What is the medical term for 'fainting'?", listOf("Coma", "Syncope", "Vertigo", "Spasm"), 1, "Syncope is a temporary loss of consciousness usually related to insufficient blood flow to the brain."),
            QuizQuestion("A 'stiff neck' combined with a high fever could mean?", listOf("Common cold", "Meningitis", "Flu", "Migraine"), 1, "A stiff neck (nuchal rigidity) is a hallmark symptom of meningitis."),
            QuizQuestion("What does 'Vertigo' feel like?", listOf("Numbness", "Spinning sensation", "Sharp pain", "Fatigue"), 1, "Vertigo is the sensation that you or your surroundings are moving or spinning."),
            QuizQuestion("Blood in the urine is medically called?", listOf("Anuria", "Hematuria", "Polyuria", "Dysuria"), 1, "Hematuria can be visible or only seen under a microscope."),
            QuizQuestion("Rapid weight loss without trying can be a sign of?", listOf("Hyperthyroidism", "Hypothyroidism", "Hypoglycemia", "Obesity"), 0, "An overactive thyroid gland (hyperthyroidism) speeds up metabolism."),
            QuizQuestion("What is 'Pruritus'?", listOf("Pain", "Itching", "Swelling", "Fever"), 1, "Pruritus is the medical term for itchy skin."),
            QuizQuestion("Cold, clammy skin is often a symptom of?", listOf("Heat stroke", "Shock", "Fever", "High BP"), 1, "Cool, moist skin is a sign of shock, which is a life-threatening emergency."),
            QuizQuestion("What is the primary symptom of 'Alopecia'?", listOf("Weight loss", "Hair loss", "Blurred vision", "Joint pain"), 1, "Alopecia areata is an autoimmune disorder that causes hair to fall out in patches."),
            QuizQuestion("Painful swallowing is called?", listOf("Dysphagia", "Odynophagia", "Dysphasia", "Dyspepsia"), 1, "Odynophagia is often felt as a burning or squeezing sensation in the throat."),
            QuizQuestion("What is 'Photophobia'?", listOf("Fear of photos", "Light sensitivity", "Sound sensitivity", "Darkness fear"), 1, "Photophobia is extreme sensitivity to light."),
            QuizQuestion("Wheezing is most commonly associated with?", listOf("Anemia", "Asthma", "Diabetes", "Gout"), 1, "Wheezing is a high-pitched whistling sound made while breathing, common in asthma."),
            QuizQuestion("Yellowing of the whites of the eyes indicates?", listOf("Anemia", "Jaundice", "Infection", "Glaucoma"), 1, "This yellowing is caused by excess bilirubin, often indicating liver issues."),
            QuizQuestion("Extreme fatigue even after sleep is called?", listOf("Lethargy", "Insomnia", "Apnea", "Euphoria"), 0, "Lethargy describes a lack of energy and enthusiasm."),
            QuizQuestion("What is 'Arrhythmia'?", listOf("Irregular heartbeat", "Regular breathing", "Muscle twitch", "Vision blur"), 0, "Arrhythmia means the heart beats too fast, too slow, or with an irregular rhythm."),
            QuizQuestion("A 'burning sensation' in the chest after eating is?", listOf("Asthma", "Heartburn", "Pneumonia", "Flu"), 1, "Heartburn is a symptom of acid reflux, where stomach acid backs up into the esophagus."),
            QuizQuestion("Enlarged lymph nodes usually indicate?", listOf("Allergy", "Infection", "Dehydration", "High sugar"), 1, "Nodes swell when the body is fighting off bacteria or viruses."),
            QuizQuestion("What is 'Paresthesia'?", listOf("Dizziness", "Pins and needles sensation", "Nausea", "Fainting"), 1, "Paresthesia is often caused by pressure on or damage to peripheral nerves."),
            QuizQuestion("A 'persistent dry cough' can be a side effect of?", listOf("ACE inhibitors", "Aspirin", "Insulin", "Antacids"), 0, "ACE inhibitors, used for high blood pressure, commonly cause a dry cough."),
            QuizQuestion("Sensitivity to cold could be a symptom of?", listOf("Hyperthyroidism", "Hypothyroidism", "Hypertension", "Diabetes"), 1, "Hypothyroidism slows down metabolism, leading to decreased body heat."),
            QuizQuestion("What is the main symptom of 'Conjunctivitis'?", listOf("Red eyes", "Ear pain", "Sore throat", "Skin rash"), 0, "Commonly called pink eye, it is an inflammation of the clear membrane of the eye."),
            QuizQuestion("Unexplained bruising can indicate problems with?", listOf("Bones", "Platelets", "Lungs", "Vision"), 1, "Platelets are blood cells that help your blood clot; low levels can cause bruising."),
            QuizQuestion("What is 'Halitosis'?", listOf("Bad breath", "Headache", "Hearing loss", "Blurred vision"), 0, "Persistent bad breath can be caused by oral bacteria or underlying health issues."),
            QuizQuestion("Lower right abdominal pain is a classic sign of?", listOf("Gallstones", "Appendicitis", "Stomach flu", "Kidney stone"), 1, "Appendicitis pain typically starts near the navel and moves to the lower right."),
            QuizQuestion("What is 'Orthostatic hypotension'?", listOf("Dizziness when standing", "High sugar", "Racing heart", "Leg cramps"), 0, "It is a sudden drop in blood pressure that happens when you stand up quickly."),
            QuizQuestion("What is 'Polyphagia'?", listOf("Excessive thirst", "Excessive hunger", "Excessive sleep", "Excessive urination"), 1, "Polyphagia is one of the three main signs of diabetes."),
            QuizQuestion("A 'whooping' sound during coughing is a sign of?", listOf("Croup", "Pertussis", "Asthma", "Bronchitis"), 1, "Pertussis, or whooping cough, is a highly contagious respiratory tract infection."),
            QuizQuestion("Sudden loss of vision in one eye is an?", listOf("Emergency", "Allergy", "Normal sign", "Vitamin lack"), 0, "Sudden vision loss can indicate a retinal detachment or stroke and requires immediate care."),
            QuizQuestion("What is 'Diplopia'?", listOf("Single vision", "Double vision", "Blurred vision", "Night blindness"), 1, "Double vision can be caused by problems with eye muscles or nerves."),
            QuizQuestion("Tenderness in the calf with swelling could be?", listOf("Cramp", "DVT (Blood clot)", "Sprain", "Bruise"), 1, "Deep Vein Thrombosis (DVT) is a serious condition that can lead to a pulmonary embolism."),
            QuizQuestion("Loss of sense of smell is known as?", listOf("Anosmia", "Ageusia", "Aphasia", "Apraxia"), 0, "Anosmia can be temporary or permanent and has various causes, including viral infections."),
            QuizQuestion("What is 'Petechiae'?", listOf("Small red/purple spots", "Large blisters", "Dry skin", "Warts"), 0, "These tiny spots result from bleeding under the skin."),
            QuizQuestion("A 'gritty' feeling in the eye is common in?", listOf("Glaucoma", "Dry eye syndrome", "Cataracts", "Myopia"), 1, "Dry eye occurs when your eyes don't produce enough tears for lubrication."),
            QuizQuestion("Which symptom defines 'Insomnia'?", listOf("Oversleeping", "Difficulty falling asleep", "Sleepwalking", "Nightmares"), 1, "Insomnia also includes waking up too early or waking up during the night."),
            QuizQuestion("What is 'Amenorrhea'?", listOf("Heavy periods", "Absence of menstruation", "Painful periods", "Irregular periods"), 1, "It is the absence of a menstrual period in women of reproductive age."),
            QuizQuestion("A 'strawberry tongue' is a symptom of?", listOf("Flu", "Scarlet fever", "Anemia", "Scurvy"), 1, "A swollen, red, bumpy tongue is a classic sign of scarlet fever or Kawasaki disease."),
            QuizQuestion("What is 'Tachycardia'?", listOf("Slow heart rate", "Fast heart rate", "Irregular heart rate", "No heart rate"), 1, "Tachycardia is a heart rate that exceeds the normal resting rate (over 100 bpm)."),
            QuizQuestion("Feeling 'full' after very little food is?", listOf("Anorexia", "Early satiety", "Dyspepsia", "Bulimia"), 1, "Early satiety can be caused by various conditions, including ulcers or gastroparesis."),
            QuizQuestion("What is 'Malaise'?", listOf("General feeling of discomfort", "Specific sharp pain", "Mental confusion", "High energy"), 0, "Malaise is an overall feeling of being unwell, tired, or lacking energy."),
            QuizQuestion("Excessive daytime sleepiness is the main symptom of?", listOf("Insomnia", "Narcolepsy", "Apnea", "Anxiety"), 1, "Narcolepsy is a chronic sleep disorder characterized by overwhelming daytime drowsiness.")
        )
    ),

    // 3. Mental Health Quiz (50 Questions)
    QuizCategory(
        name = "Mental Health Quiz",
        icon = "🧠",
        questions = listOf(
            QuizQuestion("Which of these is a common symptom of Anxiety?", listOf("Calmness", "Excessive worry", "Slow heart rate", "Increased hunger"), 1, "Anxiety involves persistent and excessive worry about everyday situations."),
            QuizQuestion("What is 'Bipolar Disorder' characterized by?", listOf("Memory loss", "Mood swings", "Fear of spiders", "Eating habits"), 1, "It causes extreme mood swings that include emotional highs (mania) and lows (depression)."),
            QuizQuestion("What does PTSD stand for?", listOf("Post-Traumatic Stress Disorder", "Pre-Traumatic Stress Disorder", "Post-Time Stress Disorder", "Permanent Traumatic Stress Disorder"), 0, "PTSD is triggered by experiencing or witnessing a terrifying event."),
            QuizQuestion("Which hormone is often called the 'Feel Good' hormone?", listOf("Cortisol", "Dopamine", "Melatonin", "Adrenaline"), 1, "Dopamine plays a big role in how we feel pleasure."),
            QuizQuestion("What is a 'Panic Attack'?", listOf("A long nap", "Sudden episode of intense fear", "Brief headache", "Feeling of extreme joy"), 1, "Panic attacks cause severe physical reactions when there is no real danger."),
            QuizQuestion("What is 'Schizophrenia' primarily?", listOf("A mood disorder", "A psychotic disorder", "An eating disorder", "A personality quirk"), 1, "Schizophrenia affects a person's ability to think, feel, and behave clearly."),
            QuizQuestion("Which of these is an eating disorder?", listOf("Anorexia Nervosa", "Insomnia", "Dyslexia", "Anemia"), 0, "Eating disorders are serious conditions related to persistent eating behaviors."),
            QuizQuestion("What is the primary focus of 'Cognitive Behavioral Therapy' (CBT)?", listOf("Medication only", "Changing thought patterns", "Hypnosis", "Dream analysis"), 1, "CBT helps you manage problems by changing the way you think and behave."),
            QuizQuestion("What is 'Postpartum Depression'?", listOf("Depression after a job loss", "Depression after childbirth", "Depression after a surgery", "Depression during teenage years"), 1, "It is a complex mix of physical, emotional, and behavioral changes after giving birth."),
            QuizQuestion("What is 'OCD' characterized by?", listOf("Laziness", "Obsessions and Compulsions", "Socializing", "Frequent naps"), 1, "OCD features a pattern of unwanted thoughts and fears (obsessions)."),
            QuizQuestion("Which of these can help reduce stress?", listOf("Ignoring it", "Physical exercise", "Heavy caffeine", "Isolated staying"), 1, "Exercise pumps up your endorphins, which are natural stress fighters."),
            QuizQuestion("What is 'Social Anxiety Disorder'?", listOf("Love of parties", "Fear of social judgment", "Need for attention", "Being extroverted"), 1, "It is a chronic mental health condition where social interactions cause irrational anxiety."),
            QuizQuestion("What does 'Stigma' mean in mental health?", listOf("A diagnosis", "Social disapproval/shame", "A medication", "A treatment center"), 1, "Stigma can lead to discrimination and prevent people from seeking help."),
            QuizQuestion("What is 'ADHD'?", listOf("Attention-Deficit/Hyperactivity Disorder", "Adult-Deficit Heart Disorder", "Attention-Delay Head Disorder", "Acute-Deficit Hyper Disorder"), 0, "ADHD is a chronic condition including attention difficulty and impulsiveness."),
            QuizQuestion("Which neurotransmitter is most linked to Depression?", listOf("Adrenaline", "Serotonin", "Insulin", "Thyroxine"), 1, "Serotonin helps regulate mood, sleep, and appetite."),
            QuizQuestion("What is 'Burnout'?", listOf("Physical injury", "Emotional/Mental exhaustion from work", "A tan", "High energy"), 1, "Burnout is a state of emotional, physical, and mental exhaustion caused by excessive stress."),
            QuizQuestion("What is 'Mindfulness'?", listOf("Thinking about everything", "Focusing on the present moment", "Daydreaming", "Sleeping deeply"), 1, "Mindfulness is the practice of being fully present and engaged in the moment."),
            QuizQuestion("What is 'Agoraphobia'?", listOf("Fear of heights", "Fear of open/crowded spaces", "Fear of water", "Fear of spiders"), 1, "Agoraphobia involves fearing places or situations that might cause panic or feeling trapped."),
            QuizQuestion("Seasonal Affective Disorder (SAD) is linked to?", listOf("Summer heat", "Lack of sunlight in winter", "Spring allergies", "Full moons"), 1, "SAD is a type of depression that's related to changes in seasons."),
            QuizQuestion("What is 'Empathy'?", listOf("Hating others", "Understanding others' feelings", "Ignoring feelings", "Being selfish"), 1, "Empathy is the ability to understand and share the feelings of another."),
            QuizQuestion("What is a 'Phobia'?", listOf("A slight dislike", "An irrational, extreme fear", "A common hobby", "A medical test"), 1, "A phobia is an overwhelming and debilitating fear of an object, place, or situation."),
            QuizQuestion("What is 'Resilience'?", listOf("Giving up quickly", "Ability to recover from difficulties", "Being fragile", "Ignoring problems"), 1, "Resilience is the process of adapting well in the face of adversity or trauma."),
            QuizQuestion("What is 'Generalized Anxiety Disorder' (GAD)?", listOf("Fear of cats", "Chronic, excessive worry about many things", "Fear of heights", "A one-time panic"), 1, "GAD involves persistent and excessive anxiety and worry about various things."),
            QuizQuestion("What is 'Dissociation'?", listOf("Connecting with others", "Feeling disconnected from reality", "Extreme focus", "Happiness"), 1, "Dissociation is a mental process of disconnecting from one's thoughts, feelings, or sense of identity."),
            QuizQuestion("Which of these is a sign of Depression?", listOf("Loss of interest in activities", "High energy levels", "Extreme productivity", "Clear skin"), 0, "Depression often causes a persistent feeling of sadness and loss of interest."),
            QuizQuestion("What is 'Self-Care'?", listOf("Ignoring needs", "Activities for physical/mental health", "Only spa days", "Working 24/7"), 1, "Self-care means taking the time to do things that help you live well and improve your health."),
            QuizQuestion("What is 'Psychosis'?", listOf("A type of fruit", "Loss of contact with reality", "Being very angry", "High intelligence"), 1, "Psychosis is a condition where a person has lost some contact with reality."),
            QuizQuestion("What is a 'Trigger' in mental health?", listOf("A weapon", "Something that causes a symptom flare-up", "A vitamin", "A doctor"), 1, "A trigger is a stimulus such as a smell, sound, or sight that triggers feelings of trauma."),
            QuizQuestion("What is 'Binge Eating Disorder'?", listOf("Not eating", "Eating large amounts in a short time", "Healthy snacking", "Vegetarianism"), 1, "It is a severe, life-threatening, and treatable eating disorder."),
            QuizQuestion("What is the role of a 'Psychiatrist'?", listOf("Just talking", "Medical doctor who can prescribe meds", "A massage therapist", "A gym coach"), 1, "Psychiatrists are medical doctors who diagnose and treat mental illnesses."),
            QuizQuestion("What is 'Borderline Personality Disorder'?", listOf("Fear of birds", "Instability in moods and relationships", "Being very organized", "Living on a border"), 1, "BPD is a mental health disorder that impacts the way you think and feel about yourself and others."),
            QuizQuestion("What is 'Euphoria'?", listOf("Deep sadness", "Intense happiness", "Fear", "Numbness"), 1, "Euphoria is an overwhelming feeling of happiness, joy, and well-being."),
            QuizQuestion("What is 'Narcissistic Personality Disorder'?", listOf("Excessive self-importance", "Low self-esteem", "Fear of mirrors", "Generosity"), 0, "It is a mental condition in which people have an inflated sense of their own importance."),
            QuizQuestion("Which substance is a depressant?", listOf("Caffeine", "Alcohol", "Cocaine", "Sugar"), 1, "Depressants slow down the function of the central nervous system."),
            QuizQuestion("What is 'Dementia'?", listOf("A specific disease", "Group of symptoms affecting memory", "A mood swing", "Hearing loss"), 1, "Dementia is not a specific disease, but an overall term that describes a group of symptoms."),
            QuizQuestion("What is 'Validation'?", listOf("Testing a theory", "Acknowledging someone's feelings", "Ignoring a person", "Charging a fee"), 1, "Validation is the recognition and acceptance of another person's thoughts or feelings."),
            QuizQuestion("What is 'Hypomania'?", listOf("Low blood sugar", "A milder form of mania", "Extreme sleepiness", "Fear of doctors"), 1, "Hypomania is a period of over-active and excited behavior that can occur in bipolar disorder."),
            QuizQuestion("What is 'Hoarding Disorder'?", listOf("Cleaning too much", "Difficulty discarding possessions", "Shopping for food", "Collecting stamps only"), 1, "Hoarding disorder is a persistent difficulty discarding or parting with possessions."),
            QuizQuestion("What is 'Body Dysmorphia'?", listOf("Muscle growth", "Obsessive focus on perceived flaws", "Skin tanning", "Weight loss"), 1, "Body dysmorphic disorder is a mental health condition where you can't stop thinking about flaws in your appearance."),
            QuizQuestion("Which is a healthy coping mechanism?", listOf("Drug use", "Journaling", "Avoidance", "Binge eating"), 1, "Journaling helps you organize your thoughts and feelings."),
            QuizQuestion("What is 'Introversion'?", listOf("Being shy", "Gaining energy from being alone", "Hating people", "Talking loudly"), 1, "Introverts tend to be more focused on internal thoughts and moods rather than seeking external stimulation."),
            QuizQuestion("What is 'Manic Episode'?", listOf("Period of low energy", "Period of high energy and grandiosity", "A dream", "A headache"), 1, "A manic episode is an emotional state characterized by high energy and euphoria."),
            QuizQuestion("What is 'Projection'?", listOf("A movie screen", "Attributing one's own feelings to others", "A work plan", "Looking forward"), 1, "Projection is a defense mechanism where people attribute their own unacceptable traits to others."),
            QuizQuestion("What is 'Altruism'?", listOf("Selfishness", "Selfless concern for others", "Greed", "Laziness"), 1, "Altruism involves acting out of concern for the well-being of other people."),
            QuizQuestion("What is 'Cognitive Dissonance'?", listOf("Thinking clearly", "Inconsistent thoughts/beliefs", "Hearing voices", "Losing memory"), 1, "It is the mental discomfort experienced by a person who holds two or more contradictory beliefs."),
            QuizQuestion("What is 'Group Therapy'?", listOf("A party", "Therapy with several people at once", "Watching a movie", "Online gaming"), 1, "Group therapy involves one or more therapists working with several people at the same time."),
            QuizQuestion("What is 'Autism Spectrum Disorder'?", listOf("A mood swing", "Developmental disorder affecting social interaction", "A broken bone", "Lack of vitamins"), 1, "ASD is a neurological and developmental disorder that affects how people interact with others."),
            QuizQuestion("What is 'Melancholy'?", listOf("Great joy", "Deep sadness", "High energy", "Confusion"), 1, "Melancholy is a feeling of pensive sadness, typically with no obvious cause."),
            QuizQuestion("What is 'Attachment Theory'?", listOf("How to fix things", "How humans form bonds", "A physics law", "A diet plan"), 1, "Attachment theory is focused on the relationships and bonds between people, particularly long-term."),
            QuizQuestion("Mental health is as important as physical health?", listOf("False", "True", "Only for adults", "Only for kids"), 1, "Good mental health is vital to our overall health and well-being.")
        )
    ),

    // 4. First Aid Quiz (50 Questions)
    QuizCategory(
        name = "First Aid Quiz",
        icon = "🚨",
        questions = listOf(
            QuizQuestion("What is the first step in an emergency?", listOf("Perform CPR", "Check for safety", "Call 911", "Move the victim"), 1, "Always ensure the scene is safe before helping to avoid becoming another victim."),
            QuizQuestion("What does CPR stand for?", listOf("Cardio Pulmonary Resuscitation", "Core Pulse Relief", "Central Pressure Recovery", "Cardio Pulse Rescue"), 0, "CPR is a life-saving technique used in emergencies like heart attacks."),
            QuizQuestion("How deep should chest compressions be for an adult?", listOf("1 inch", "2 inches", "4 inches", "0.5 inches"), 1, "Compressions should be at least 2 inches deep for effective blood flow."),
            QuizQuestion("What is the ratio of compressions to breaths in CPR?", listOf("15:2", "30:2", "50:5", "10:1"), 1, "The recommended cycle is 30 compressions followed by 2 rescue breaths."),
            QuizQuestion("What is an AED used for?", listOf("Measuring BP", "Restarting heart rhythm", "Bandaging", "Checking fever"), 1, "Automated External Defibrillators analyze heart rhythm and deliver a shock if needed."),
            QuizQuestion("What should you do for a choking person who can cough?", listOf("Heimlich maneuver", "Encourage them to cough", "Blind finger sweep", "Back slaps"), 1, "If they are coughing, their airway is only partially blocked. Let them clear it."),
            QuizQuestion("How do you treat a minor burn?", listOf("Apply ice", "Run cool water", "Apply butter", "Pop blisters"), 1, "Run cool (not cold) water over the burn for about 10-20 minutes."),
            QuizQuestion("What is the sign of a severe allergic reaction?", listOf("Sneezing", "Anaphylaxis", "Mild rash", "Dry mouth"), 1, "Anaphylaxis is a life-threatening emergency requiring an EpiPen and 911."),
            QuizQuestion("What should you use to stop severe bleeding?", listOf("Cotton ball", "Direct pressure", "Warm water", "Moisturizer"), 1, "Apply firm, steady pressure directly over the wound with a clean cloth."),
            QuizQuestion("How should you position a person in shock?", listOf("Standing up", "Lying flat with legs raised", "Sitting in a chair", "On their stomach"), 1, "Raising the legs helps improve blood flow to the vital organs."),
            QuizQuestion("What does the 'R' in R.I.C.E stand for?", listOf("Run", "Rest", "Reaction", "Repeat"), 1, "R.I.C.E stands for Rest, Ice, Compression, and Elevation."),
            QuizQuestion("What is the 'Heimlich Maneuver' used for?", listOf("Bleeding", "Choking", "Fainting", "Fever"), 1, "It is used to dislodge an object from a person's airway."),
            QuizQuestion("How do you treat a nosebleed?", listOf("Lean back", "Lean forward and pinch nose", "Stuff with tissue only", "Lie down"), 1, "Leaning forward prevents blood from going down the throat."),
            QuizQuestion("What should you do if someone is having a seizure?", listOf("Hold them down", "Clear the area of hazards", "Put a spoon in their mouth", "Give them water"), 1, "Protect them from injury and do not restrain their movements."),
            QuizQuestion("What is a sign of Heat Stroke?", listOf("Heavy sweating", "Hot, dry skin", "Shivering", "Pale skin"), 1, "Heat stroke is a medical emergency where the body loses the ability to cool down."),
            QuizQuestion("How do you treat a bee sting?", listOf("Squeeze it", "Scrape the stinger out", "Apply heat", "Ignore it"), 1, "Scrape the stinger sideways with a card to avoid releasing more venom."),
            QuizQuestion("What is the recovery position?", listOf("On the back", "On the side", "On the stomach", "Sitting up"), 1, "Placing someone on their side keeps the airway open and clear."),
            QuizQuestion("What should you do for a suspected fracture?", listOf("Move the bone back", "Immobilize the limb", "Rub the area", "Make them walk"), 1, "Keep the injured area still to prevent further damage."),
            QuizQuestion("What are the symptoms of Hypothermia?", listOf("Sweating", "Shivering and confusion", "High fever", "Hyperactivity"), 1, "Hypothermia occurs when the body temperature drops dangerously low."),
            QuizQuestion("If a chemical splashes in eyes, how long should you rinse?", listOf("2 mins", "20 mins", "5 mins", "1 min"), 1, "Rinse continuously with clean water for at least 20 minutes."),
            QuizQuestion("What is a tourniquet used for?", listOf("Headaches", "Life-threatening limb bleeding", "Sprains", "Acne"), 1, "A tourniquet is used only when direct pressure cannot stop life-threatening bleeding."),
            QuizQuestion("What is the first thing to do for a poisoned person?", listOf("Make them vomit", "Call poison control", "Give them milk", "Wait and see"), 1, "Call your local poison control center immediately for expert advice."),
            QuizQuestion("How do you check for breathing?", listOf("Ask them a question", "Look, listen, and feel", "Poke them", "Check their pulse only"), 1, "Check for no more than 10 seconds to see if the chest rises and falls."),
            QuizQuestion("What does FAST stand for in stroke recognition?", listOf("Face, Arm, Speech, Time", "Feet, Arms, Sight, Tongue", "Face, Airway, Skin, Temperature", "Fast, Alert, Safe, True"), 0, "F-Face dropping, A-Arm weakness, S-Speech difficulty, T-Time to call 911."),
            QuizQuestion("What should you NOT do to a frostbitten area?", listOf("Wrap it loosely", "Rub it", "Seek medical help", "Remove wet clothes"), 1, "Rubbing frostbitten skin can cause further tissue damage."),
            QuizQuestion("A person is pale, cold, and has a weak pulse. This is?", listOf("Fever", "Shock", "Flu", "Stroke"), 1, "Shock occurs when the circulatory system fails to provide enough blood to the body."),
            QuizQuestion("When do you stop performing CPR?", listOf("When you get tired", "When help arrives or victim recovers", "After 5 minutes", "After 10 compressions"), 1, "Continue CPR until professional help takes over or the person starts breathing."),
            QuizQuestion("What is 'Triage'?", listOf("A type of bandage", "Prioritizing patients by urgency", "A medical tool", "A surgical procedure"), 1, "Triage is the process of determining the priority of patients' treatments."),
            QuizQuestion("How to treat a sprained ankle?", listOf("Heat and exercise", "Rest, Ice, Compression, Elevation", "Walk it off", "Massage deeply"), 1, "Ice reduces swelling, and elevation helps fluid drain away from the injury."),
            QuizQuestion("Signs of a concussion include?", listOf("Hunger", "Confusion and dizziness", "Fast running", "Clear vision"), 1, "Concussions are brain injuries that can happen from any blow to the head."),
            QuizQuestion("What to do if someone faints?", listOf("Pour water on them", "Check airway and raise legs", "Slap their face", "Make them stand up"), 1, "Laying them down and raising their legs helps blood flow back to the brain."),
            QuizQuestion("What is 'Secondary Survey'?", listOf("The first check", "A deeper check for other injuries", "Calling the police", "Writing a report"), 1, "A secondary survey is a head-to-toe physical assessment once life-threats are managed."),
            QuizQuestion("A diabetic person is shaky and confused. Give them?", listOf("Insulin", "Sugar/Juice", "Water", "Nothing"), 1, "Hypoglycemia (low blood sugar) requires quick-acting sugar like juice or candy."),
            QuizQuestion("How do you treat a blister?", listOf("Pop it", "Cover it loosely and don't pop", "Peel the skin", "Rub with salt"), 1, "Popping a blister increases the risk of infection."),
            QuizQuestion("What is an 'Epipen' used for?", listOf("Diabetes", "Severe allergies", "Heart attack", "Asthma"), 1, "Epinephrine autoinjectors reverse the symptoms of severe allergic reactions."),
            QuizQuestion("What is the 'Good Samaritan Law'?", listOf("Law to pay rescuers", "Protects rescuers from liability", "Law about doctors", "Law about hospital fees"), 1, "These laws encourage people to help others in emergency situations."),
            QuizQuestion("What to do if a person is electrocuted?", listOf("Touch them immediately", "Turn off the power source", "Give them water", "Move them with metal"), 1, "Never touch the victim until the power is off to avoid being electrocuted yourself."),
            QuizQuestion("Venous bleeding (from a vein) is usually?", listOf("Spurting bright red", "Steady flow of dark red", "Oozing", "Invisible"), 1, "Venous blood is dark because it is returning to the heart and carries less oxygen."),
            QuizQuestion("Arterial bleeding is usually?", listOf("Steady flow", "Spurting bright red", "Very slow", "Dark blue"), 1, "Arterial blood is oxygen-rich and under pressure from the heart."),
            QuizQuestion("What to do for a snake bite?", listOf("Suck out venom", "Keep limb still and below heart", "Apply a tight tourniquet", "Cut the wound"), 1, "Keep the victim calm and still to slow the spread of venom."),
            QuizQuestion("How to treat a hyperventilating person?", listOf("Make them run", "Encourage slow breathing", "Give them oxygen mask", "Slap them"), 1, "Calmly guide them to take slow, regular breaths."),
            QuizQuestion("Signs of internal bleeding include?", listOf("Laughter", "Bruising and rigid abdomen", "Sneezing", "High energy"), 1, "Internal bleeding can cause shock and requires immediate medical attention."),
            QuizQuestion("What is 'Universal Precautions'?", listOf("Treating all blood as infectious", "Always calling 911", "Using a mask for everything", "Cleaning with water only"), 0, "Use barriers like gloves to protect yourself from bloodborne pathogens."),
            QuizQuestion("How to help someone with an asthma attack?", listOf("Tell them to hold breath", "Help them use their inhaler", "Give them hot tea", "Lay them flat"), 1, "Guide them to sit up and use their rescue inhaler (usually blue)."),
            QuizQuestion("What should you do with an embedded object in a wound?", listOf("Pull it out", "Stabilize it with padding", "Wash it", "Push it deeper"), 1, "Removing the object can cause more severe bleeding; stabilize it instead."),
            QuizQuestion("A 'Sucking Chest Wound' should be covered with?", listOf("Nothing", "Occlusive dressing (airtight)", "A porous cloth", "Paper towel"), 1, "An airtight dressing helps prevent lung collapse."),
            QuizQuestion("What is the rate of compressions per minute?", listOf("60-80", "100-120", "150-200", "40-50"), 1, "Compress to the beat of 'Stayin' Alive' for the correct tempo."),
            QuizQuestion("What is the 'Rule of Nines' used for?", listOf("Checking pulse", "Estimating burn surface area", "Calculating medicine", "Scoring reflexes"), 1, "It is a standardized way to quickly estimate the size of a burn."),
            QuizQuestion("What to do if a baby is choking?", listOf("Heimlich", "5 back blows and 5 chest thrusts", "Shake them", "Give them water"), 1, "Use your lap to support the baby and perform gentle back slaps and chest thrusts."),
            QuizQuestion("The most important thing in First Aid is?", listOf("Having a kit", "Staying calm and safety", "Being a doctor", "Running fast"), 1, "A calm rescuer can assess the situation better and provide better care.")
        )
    ),

    // 5. Nutrition Quiz (50 Questions)
    QuizCategory(
        name = "Nutrition Quiz",
        icon = "🍎",
        questions = listOf(
            QuizQuestion("Which vitamin is primarily obtained from sunlight?", listOf("Vitamin A", "Vitamin C", "Vitamin D", "Vitamin B12"), 2, "Your body makes vitamin D when your skin is exposed to the sun."),
            QuizQuestion("What is the main source of energy for the body?", listOf("Proteins", "Carbohydrates", "Fats", "Vitamins"), 1, "Carbs are broken down into glucose, the body's primary fuel."),
            QuizQuestion("Which mineral is essential for strong bones?", listOf("Iron", "Calcium", "Potassium", "Sodium"), 1, "Calcium and Vitamin D work together to build strong bones."),
            QuizQuestion("What are the building blocks of proteins?", listOf("Glucose", "Fatty acids", "Amino acids", "Nucleotides"), 2, "There are 20 different amino acids that combine to make proteins."),
            QuizQuestion("Which vitamin is found in high amounts in citrus fruits?", listOf("Vitamin K", "Vitamin C", "Vitamin D", "Vitamin E"), 1, "Vitamin C is also known as ascorbic acid and supports the immune system."),
            QuizQuestion("Which of these is a 'Good Fat'?", listOf("Trans fat", "Saturated fat", "Monounsaturated fat", "Artificial fat"), 2, "Healthy fats are found in olive oil, avocados, and nuts."),
            QuizQuestion("What is the primary function of Iron in the blood?", listOf("Energy", "Oxygen transport", "Digestion", "Vision"), 1, "Iron is a component of hemoglobin, which carries oxygen in the blood."),
            QuizQuestion("Which nutrient helps with bowel movements?", listOf("Sugar", "Fiber", "Protein", "Fat"), 1, "Fiber adds bulk to your stool and helps it pass more quickly."),
            QuizQuestion("What is the 'Master Mineral' for heart rhythm?", listOf("Iron", "Magnesium", "Potassium", "Zinc"), 2, "Potassium is an electrolyte that helps your heart beat regularly."),
            QuizQuestion("Scurvy is caused by a lack of which vitamin?", listOf("Vitamin C", "Vitamin A", "Vitamin B1", "Vitamin K"), 0, "Scurvy symptoms include fatigue, bleeding gums, and bruising."),
            QuizQuestion("Which food is a great source of Omega-3?", listOf("Chicken", "Salmon", "Beef", "Pork"), 1, "Omega-3 fatty acids are essential fats that support heart and brain health."),
            QuizQuestion("What is the main sugar found in milk?", listOf("Fructose", "Lactose", "Sucrose", "Maltose"), 1, "Lactose intolerance occurs when the body can't break down this sugar."),
            QuizQuestion("Which vitamin is vital for blood clotting?", listOf("Vitamin A", "Vitamin K", "Vitamin E", "Vitamin C"), 1, "Vitamin K is often given to newborns to prevent bleeding issues."),
            QuizQuestion("A deficiency in Vitamin A can cause?", listOf("Rickets", "Night blindness", "Scurvy", "Anemia"), 1, "Vitamin A is essential for maintaining clear vision and healthy skin."),
            QuizQuestion("Which of these is a macronutrient?", listOf("Vitamin C", "Protein", "Calcium", "Iron"), 1, "Macronutrients (Carbs, Proteins, Fats) are needed in large amounts."),
            QuizQuestion("What is the healthiest way to cook vegetables to keep nutrients?", listOf("Boiling", "Steaming", "Deep frying", "Microwaving"), 1, "Steaming preserves water-soluble vitamins better than boiling."),
            QuizQuestion("Which mineral is often added to salt to prevent goiter?", listOf("Zinc", "Iodine", "Copper", "Fluoride"), 1, "Iodine is essential for proper thyroid gland function."),
            QuizQuestion("What is the main source of Sodium in our diet?", listOf("Fruit", "Table salt", "Water", "Meat"), 1, "Most sodium comes from processed foods and added salt."),
            QuizQuestion("Which vitamin is also known as Ascorbic Acid?", listOf("Vitamin B6", "Vitamin C", "Vitamin D", "Vitamin A"), 1, "Ascorbic acid acts as a powerful antioxidant in the body."),
            QuizQuestion("What is the primary role of Vitamin E?", listOf("Bone health", "Antioxidant", "Energy", "Digestion"), 1, "Vitamin E helps protect cells from damage caused by free radicals."),
            QuizQuestion("Which food group should make up the base of a healthy diet?", listOf("Sweets", "Vegetables and Fruits", "Meat", "Oil"), 1, "Plants provide essential vitamins, minerals, and fiber."),
            QuizQuestion("What are 'Empty Calories'?", listOf("High fiber food", "Food with calories but no nutrients", "No calories", "Water"), 1, "Foods like soda and candy provide energy but no health benefits."),
            QuizQuestion("Which vitamin is mostly found in animal products?", listOf("Vitamin C", "Vitamin B12", "Folate", "Vitamin E"), 1, "Vegans often need to supplement Vitamin B12."),
            QuizQuestion("What is 'HDL' often called?", listOf("Bad Cholesterol", "Good Cholesterol", "Sugar", "Protein"), 1, "HDL (High-Density Lipoprotein) helps remove other forms of cholesterol from the blood."),
            QuizQuestion("Which mineral helps prevent tooth decay?", listOf("Iron", "Fluoride", "Zinc", "Calcium"), 1, "Fluoride strengthens tooth enamel and makes it more resistant to acid."),
            QuizQuestion("What is the Glycemic Index (GI) used for?", listOf("Fat content", "Sugar's effect on blood glucose", "Protein quality", "Vitamin levels"), 1, "Low GI foods cause a slower, smaller rise in blood sugar levels."),
            QuizQuestion("Which nutrient is essential for wound healing?", listOf("Sugar", "Zinc and Vitamin C", "Sodium", "Fat"), 1, "Zinc supports immune function and the inflammatory response."),
            QuizQuestion("What happens if you consume too much Vitamin A?", listOf("Better vision", "Toxicity/Liver damage", "Stronger bones", "No effect"), 1, "Fat-soluble vitamins like A can build up to toxic levels in the liver."),
            QuizQuestion("Which of these is a complex carbohydrate?", listOf("White sugar", "Oats", "Honey", "Candy"), 1, "Complex carbs provide long-lasting energy and fiber."),
            QuizQuestion("What is the primary function of Fats?", listOf("Fast energy", "Long-term energy storage", "Building muscle", "Oxygen transport"), 1, "Fats also help the body absorb certain vitamins and protect organs."),
            QuizQuestion("Anemia is linked to a deficiency of?", listOf("Calcium", "Iron", "Vitamin D", "Vitamin K"), 1, "Iron-deficiency anemia is the most common form of anemia."),
            QuizQuestion("Which vitamin helps the body absorb Calcium?", listOf("Vitamin A", "Vitamin D", "Vitamin C", "Vitamin B"), 1, "Without enough Vitamin D, your body can't absorb calcium effectively."),
            QuizQuestion("What are 'Antioxidants'?", listOf("Poisons", "Substances that protect cells", "Type of fat", "Muscle builders"), 1, "Antioxidants help prevent or delay some types of cell damage."),
            QuizQuestion("Which nutrient is lost when grains are refined (e.g., white flour)?", listOf("Sugar", "Fiber", "Starch", "Calories"), 1, "Whole grains contain the entire grain kernel, including the fiber-rich bran."),
            QuizQuestion("What is the recommended daily intake of water for an average adult?", listOf("1 liter", "2-3 liters", "10 liters", "0.5 liters"), 1, "Proper hydration is essential for every function in the body."),
            QuizQuestion("Which vitamin is essential for a healthy pregnancy (prevents neural tube defects)?", listOf("Vitamin E", "Folic Acid (B9)", "Vitamin C", "Vitamin K"), 1, "Folic acid is crucial for the development of the baby's brain and spinal cord."),
            QuizQuestion("Which of these is a lean protein?", listOf("Bacon", "Chicken breast", "Sausage", "Salami"), 1, "Lean proteins are lower in saturated fat and calories."),
            QuizQuestion("What is 'Ldl' often called?", listOf("Good Cholesterol", "Bad Cholesterol", "Sugar", "Fat"), 1, "LDL (Low-Density Lipoprotein) can lead to plaque buildup in your arteries."),
            QuizQuestion("Which mineral is necessary for muscle contraction?", listOf("Iron", "Potassium", "Iodine", "Copper"), 1, "Electrolytes like potassium and magnesium help muscles contract and relax."),
            QuizQuestion("What is the main component of human body weight?", listOf("Fat", "Water", "Bones", "Muscle"), 1, "The human body is about 60% water on average."),
            QuizQuestion("Which vitamin deficiency causes Beriberi?", listOf("Vitamin A", "Vitamin B1 (Thiamine)", "Vitamin C", "Vitamin D"), 1, "Vitamin B1 helps the body convert food into energy."),
            QuizQuestion("What is the healthiest type of bread?", listOf("White bread", "Whole grain bread", "Sweet bread", "French bread"), 1, "Whole grain bread contains more nutrients and fiber than white bread."),
            QuizQuestion("What are 'Trans Fats' usually found in?", listOf("Fresh fruit", "Processed/Fried foods", "Olive oil", "Raw nuts"), 1, "Trans fats are artificial fats that increase heart disease risk."),
            QuizQuestion("Which nutrient is the primary repair material for tissues?", listOf("Carbs", "Protein", "Vitamin A", "Fructose"), 1, "Protein is essential for building and repairing every cell in the body."),
            QuizQuestion("What is the main source of Fructose?", listOf("Milk", "Fruit", "Potatoes", "Eggs"), 1, "Fructose is a natural sugar found in fruits and honey."),
            QuizQuestion("Which vitamin B is also known as Riboflavin?", listOf("B1", "B2", "B6", "B12"), 1, "Vitamin B2 is important for growth and overall good health."),
            QuizQuestion("What is the main health risk of high salt intake?", listOf("Diabetes", "High blood pressure", "Scurvy", "Obesity"), 1, "Excess sodium holds excess fluid in the body, placing a burden on the heart."),
            QuizQuestion("Which of these is a fermented food (probiotic)?", listOf("Pizza", "Yogurt", "Pasta", "Steak"), 1, "Probiotics support a healthy balance of gut bacteria."),
            QuizQuestion("What does 'Metabolism' refer to?", listOf("Eating fast", "Chemical processes to maintain life", "Weight lifting", "Sleeping"), 1, "Metabolism is how your body converts food and drink into energy."),
            QuizQuestion("A balanced diet includes?", listOf("Only protein", "Variety of all food groups", "Only veggies", "No fats"), 1, "Balance ensures you get all the nutrients your body needs to thrive.")
        )
    ),

    QuizCategory(
        name = "Comprehensive Quiz",
        icon = "🏆",
        questions = listOf(
            QuizQuestion(
                question = "-",
                options = listOf("Increased focus", "Better sleep", "Irritability", "Lower heart rate"),
                correctAnswerIndex = 2
            )
        )
    ),

    QuizCategory(
        name = "Simulation",
        icon = "🧪",
        isSimulation = true,
        questions = listOf(
            QuizQuestion("How many hours of sleep do you average per night?", listOf("Less than 5 hours", "5-6 hours", "7-8 hours", "More than 9 hours"), tip = "Consistency is key; try to maintain the same sleep schedule even on weekends."),
            QuizQuestion("How often do you feel overwhelmed by your daily tasks?", listOf("Rarely", "Occasionally", "Frequently", "Almost always"), tip = "Breaking large tasks into smaller, manageable steps can reduce the feeling of being overwhelmed."),
            QuizQuestion("How many cups of water do you drink on a typical day?", listOf("1-2 cups", "3-5 cups", "6-8 cups", "More than 8 cups"), tip = "Drinking water before meals can aid digestion and help control appetite."),
            QuizQuestion("How much time do you spend sitting in front of a screen daily?", listOf("Less than 2 hours", "2-5 hours", "6-9 hours", "More than 10 hours"), tip = "Follow the 20-20-20 rule: every 20 minutes, look at something 20 feet away for 20 seconds."),
            QuizQuestion("How many servings of fruits and vegetables do you eat daily?", listOf("0-1 servings", "2-3 servings", "4-5 servings", "More than 5 servings"), tip = "Try to 'eat the rainbow' to ensure you get a variety of vitamins and minerals."),
            QuizQuestion("How often do you engage in vigorous physical activity (30+ mins)?", listOf("Never", "Once a week", "3 times a week", "Daily"), tip = "Vigorous activity increases heart rate and improves cardiovascular health."),
            QuizQuestion("How do you typically react to a high-stress situation?", listOf("Stay calm", "Feel anxious but cope", "Panic slightly", "Completely lose control"), tip = "Mindful breathing exercises can help lower your immediate physiological response to stress."),
            QuizQuestion("How often do you skip breakfast?", listOf("Never", "Sometimes", "Often", "Always"), tip = "A balanced breakfast provides the necessary fuel to start your metabolism for the day."),
            QuizQuestion("How would you rate your typical posture while working/studying?", listOf("Excellent", "Good", "Fair", "Poor (slouching)"), tip = "Ensure your monitor is at eye level and your feet are flat on the floor to maintain good posture."),
            QuizQuestion("How often do you take breaks during long working sessions?", listOf("Every hour", "Every 2 hours", "Only when finished", "Rarely"), tip = "Short breaks can actually improve focus and prevent mental fatigue."),
            QuizQuestion("How much fast food or processed snacks do you consume weekly?", listOf("None", "1-2 times", "3-4 times", "5+ times"), tip = "Processed foods are often high in sodium and unhealthy fats; try whole-food alternatives."),
            QuizQuestion("How often do you experience physical symptoms of stress (e.g., headaches)?", listOf("Never", "Rarely", "Monthly", "Weekly"), tip = "Physical symptoms are your body's way of telling you to slow down and de-stress."),
            QuizQuestion("How satisfied are you with your current social connections?", listOf("Very satisfied", "Satisfied", "Neutral", "Dissatisfied"), tip = "Social support is a crucial component of overall mental and emotional well-being."),
            QuizQuestion("How often do you practice self-reflection or journaling?", listOf("Daily", "Weekly", "Monthly", "Never"), tip = "Writing down your thoughts can help clarify your emotions and reduce stress."),
            QuizQuestion("How would you describe your relationship with caffeine?", listOf("Don't use it", "Moderate use", "Dependent on it", "Excessive use"), tip = "Avoid caffeine in the late afternoon to prevent it from interfering with your sleep cycle.")
        )
    )
    
)
// ========================================================

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int? = null,
    val tip: String? = null
)

data class QuizCategory(
    val name: String,
    val icon: String,
    val questions: List<QuizQuestion>,
    val isSimulation: Boolean = false
)

@Composable
fun QuizScreen() {
    var selectedCategory by remember { mutableStateOf<QuizCategory?>(null) }
    var randomizedQuestions by remember { mutableStateOf<List<QuizQuestion>>(emptyList()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (selectedCategory == null) {
            CategorySelectionUI(MyQuizzes) { category ->
                // Logic: Comprehensive Quiz pools 50 random questions from all non-simulation topics
                randomizedQuestions = if (category.name == "Comprehensive Quiz") {
                    MyQuizzes.filter { !it.isSimulation && it.name != "Comprehensive Quiz" }
                        .flatMap { it.questions }
                        .shuffled()
                        .take(50)
                } else {
                    // Regular quizzes take 15 random questions
                    category.questions.shuffled().take(15)
                }
                selectedCategory = category
            }
        } else {
            QuizPlayUI(
                category = selectedCategory!!,
                questions = randomizedQuestions,
                onBack = { 
                    selectedCategory = null
                    randomizedQuestions = emptyList()
                }
            )
        }
    }
}

@Composable
fun CategorySelectionUI(categories: List<QuizCategory>, onCategorySelected: (QuizCategory) -> Unit) {
    val headerGradient = getQuizHeaderGradient()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Card(
            shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerGradient)
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                Column {
                    Text(
                        text = "Quiz Challenge",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Test your health knowledge",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }

        if (categories.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No quizzes defined in code yet.", color = SubTextGray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(categories) { category ->
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onCategorySelected(category) }
                    ) {
                        Row(
                            modifier = Modifier.padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(ThemeGreenDark.copy(alpha = 0.1f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(category.icon, fontSize = 30.sp)
                            }
                            Spacer(modifier = Modifier.width(20.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(category.name, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MaterialTheme.colorScheme.onSurface)
                                val questionDisplayCount = if (category.name == "Comprehensive Quiz") 50 else 15
                                Text("$questionDisplayCount Questions", fontSize = 14.sp, color = SubTextGray)
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = SubTextGray.copy(alpha = 0.5f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuizPlayUI(category: QuizCategory, questions: List<QuizQuestion>, onBack: () -> Unit) {
    val quizGradient = getQuizGradient()
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedAnswerIndex by remember { mutableStateOf<Int?>(null) }
    var score by remember { mutableIntStateOf(0) }
    var isQuizFinished by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }
    var showTipDialog by remember { mutableStateOf(false) }

    val currentQuestion = questions.getOrNull(currentQuestionIndex)

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Quit Quiz?", fontWeight = FontWeight.Bold) },
            text = { Text("Your progress will be lost. Are you sure you want to go back?") },
            confirmButton = {
                TextButton(onClick = onBack) {
                    Text("Yes, Quit", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    if (showTipDialog && currentQuestion?.tip != null) {
        AlertDialog(
            onDismissRequest = { showTipDialog = false },
            title = { Row(verticalAlignment = Alignment.CenterVertically) {
                Text("💡 Health Tip", fontWeight = FontWeight.Bold)
            }},
            text = { Text(currentQuestion.tip) },
            confirmButton = {
                TextButton(onClick = { showTipDialog = false }) {
                    Text("Got it!", color = QuizPurple)
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Gradient Card containing Header, Progress and Question
        Card(
            shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(quizGradient)
                    .padding(top = 16.dp, bottom = 32.dp, start = 20.dp, end = 20.dp)
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = { showExitDialog = true }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                        Text(
                            text = category.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.weight(1f).padding(start = 8.dp)
                        )
                        
                        // Tip Button
                        if (!isQuizFinished && currentQuestion?.tip != null) {
                            IconButton(onClick = { showTipDialog = true }) {
                                Icon(
                                    imageVector = Icons.Default.Lightbulb,
                                    contentDescription = "Show Tip",
                                    tint = Color.White
                                )
                            }
                        }
                    }

                    if (!isQuizFinished && currentQuestion != null) {
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Progress Bar Section
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Question ${currentQuestionIndex + 1}/${questions.size}",
                                    fontSize = 14.sp,
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "${((currentQuestionIndex + 1).toFloat() / questions.size * 100).toInt()}%",
                                    fontSize = 14.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            LinearProgressIndicator(
                                progress = { (currentQuestionIndex + 1).toFloat() / questions.size },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(5.dp)),
                                color = Color.White,
                                trackColor = Color.White.copy(alpha = 0.3f),
                                strokeCap = StrokeCap.Round
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // Question Box
                        Text(
                            text = currentQuestion.question,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            lineHeight = 32.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        if (isQuizFinished || currentQuestion == null) {
            if (category.isSimulation) {
                SimulationResultUI(onBack)
            } else {
                ResultUI(score, questions.size, onBack)
            }
        } else {
            // Options List
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                currentQuestion.options.forEachIndexed { index, option ->
                    val isSelected = selectedAnswerIndex == index
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedAnswerIndex = index },
                        shape = RoundedCornerShape(18.dp),
                        color = if (isSelected) ThemeGreenDark else MaterialTheme.colorScheme.surface,
                        border = if (isSelected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                        shadowElevation = if (isSelected) 4.dp else 0.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .border(
                                        width = 2.dp,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .background(Color.White, CircleShape)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = option,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Action Button
            Box(modifier = Modifier.padding(24.dp)) {
                Button(
                    onClick = {
                        if (!category.isSimulation && selectedAnswerIndex == currentQuestion.correctAnswerIndex) {
                            score++
                        }
                        if (currentQuestionIndex < questions.size - 1) {
                            currentQuestionIndex++
                            selectedAnswerIndex = null
                        } else {
                            isQuizFinished = true
                        }
                    },
                    enabled = selectedAnswerIndex != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ThemeGreenDark,
                        disabledContainerColor = ThemeGreenDark.copy(alpha = 0.3f)
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = if (currentQuestionIndex < questions.size - 1) "Next Question" else "Finish",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun SimulationResultUI(onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(ThemeGreenDark.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("✅", fontSize = 60.sp)
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            "Simulation Completed!",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = ThemeGreenDark,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Thank you for completing the assessment.\nYour data has been recorded for detection.",
            color = SubTextGray,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp),
            lineHeight = 24.sp
        )
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Back to Quizzes", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ResultUI(score: Int, total: Int, onBack: () -> Unit) {
    val percentage = (score.toFloat() / total * 100).toInt()
    val feedback = when {
        percentage >= 80 -> "Excellent! 🌟"
        percentage >= 50 -> "Good Job! 👍"
        else -> "Keep Learning! 📚"
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp)) {
            CircularProgressIndicator(
                progress = { score.toFloat() / total },
                modifier = Modifier.fillMaxSize(),
                color = ThemeGreenDark,
                strokeWidth = 12.dp,
                trackColor = Color(0xFFE2E8F0),
                strokeCap = StrokeCap.Round
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$percentage%",
                    fontSize = 44.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "$score / $total",
                    fontSize = 18.sp,
                    color = SubTextGray,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        
        Spacer(modifier = Modifier.height(40.dp))
        
        Text(
            text = feedback,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = ThemeGreenDark
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Try Another Quiz", fontWeight = FontWeight.Bold)
        }
    }
}
