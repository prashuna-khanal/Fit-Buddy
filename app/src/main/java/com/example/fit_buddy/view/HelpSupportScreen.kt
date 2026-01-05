package com.example.fit_buddy.view

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.tooling.preview.Preview
import com.example.fit_buddy.R
import com.example.fit_buddy.ui.theme.*

@Composable
fun HelpSupportScreenComposable() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLightLavender)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_arrow_back_ios_24),
                contentDescription = "Back",
                tint = lavender400,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { }
            )

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Help & Support",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
            }

            Spacer(modifier = Modifier.width(28.dp))
        }

        Spacer(modifier = Modifier.height(30.dp))

        // FAQ Section Title
        Text(
            text = "Frequently Asked Questions",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = textPrimary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Individual FAQ Items
        FAQItemExpandableAesthetic(
            question = "How do I track my workouts?",
            answer = "Open Fit Buddy and tap 'Start Workout'. Select the workout type, start, and the app will track time, calories, sets/reps, heart rate, and pace. After finishing, save your workout to view detailed stats."
        )

        FAQItemExpandableAesthetic(
            question = "Can I sync my data with other apps?",
            answer = "Yes! Fit Buddy supports syncing with Google Fit and Apple Health. Go to Settings > App Integration > Connect Apps, authorize Fit Buddy, and your data will sync automatically."
        )

        FAQItemExpandableAesthetic(
            question = "How do I reset my password?",
            answer = "Go to Settings > Edit Profile > Password > Change Password. Enter your current password, then your new password twice. Passwords must have at least 8 characters, one uppercase letter, one number, and one special character."
        )

        FAQItemExpandableAesthetic(
            question = "How can I upgrade to premium?",
            answer = "Fit Buddy Premium unlocks AI workout plans, nutrition tracking, and advanced analytics. Go to App Settings > Premium, choose your subscription, enter payment details, and start enjoying premium features immediately."
        )

        FAQItemExpandableAesthetic(
            question = "Is my data secure?",
            answer = "All data is encrypted on your device and in our cloud. Fit Buddy does not share your personal info with third-party advertisers. You have full control over privacy settings."
        )

        FAQItemExpandableAesthetic(
            question = "Can I use the app offline?",
            answer = "Offline, you can track workouts and log meals. Features like syncing, AI recommendations, and premium content require internet. Connect to Wi-Fi periodically to back up progress and get updates."
        )

        FAQItemExpandableAesthetic(
            question = "How do I track my nutrition?",
            answer = "Go to the 'Nutrition' tab, enter your meals, and Fit Buddy will calculate calories, protein, carbs, and fats. You can also scan barcodes for fast tracking."
        )

        FAQItemExpandableAesthetic(
            question = "Can I set fitness goals?",
            answer = "In the 'Goals' section, set daily or weekly targets for steps, calories burned, or workout frequency. Fit Buddy will track progress and give reminders."
        )

        FAQItemExpandableAesthetic(
            question = "How do I customize my workout plans?",
            answer = "Premium users can create personalized plans under 'Workout Plans'. Choose exercises, sets, reps, rest intervals, and Fit Buddy will generate a full schedule for you."
        )

        FAQItemExpandableAesthetic(
            question = "How do I contact support?",
            answer = "For further assistance, tap 'Help & Support' > 'Contact Us'. Submit questions, feedback, or report issues. Our support team usually responds within 24 hours."
        )

        FAQItemExpandableAesthetic(
            question = "Can I track sleep?",
            answer = "Fit Buddy allows you to track sleep manually or via compatible smart devices. Go to 'Sleep Tracker' and input sleep/wake times or connect your device for automatic tracking."
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun FAQItemExpandableAesthetic(question: String, answer: String) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(lavender100, RoundedCornerShape(16.dp))
            .clickable { expanded = !expanded }
            .padding(18.dp)
            .animateContentSize()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = question,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                color = textPrimary
            )

            Icon(
                painter = painterResource(R.drawable.baseline_arrow_drop_down_24),
                contentDescription = null,
                tint = lavender400,
                modifier = Modifier.rotate(if (expanded) 180f else 0f)
            )
        }

        if (expanded) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = answer,
                fontSize = 14.sp,
                color = textSecondary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HelpSupportScreenComposablePreview() {
    HelpSupportScreenComposable()
}
