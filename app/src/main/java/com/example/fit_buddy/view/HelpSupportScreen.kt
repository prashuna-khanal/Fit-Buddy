package com.example.fit_buddy.view

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.tooling.preview.Preview
import com.example.fit_buddy.R
import com.example.fit_buddy.ui.theme.*

@Composable
fun HelpSupportScreenComposable(
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current

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
                    .clickable { onBackClick() } // back button
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

        // ------------------ FAQ Items ------------------
        FAQItemExpandableAesthetic(
            question = "How do I track my workouts?",
            answer = "Open Fit Buddy and tap 'Start Workout'. Start, and the app will track time, sets/reps and pace. After finishing, save your workout to view detailed stats."
        )

        FAQItemExpandableAesthetic(
            question = "How do I reset my password?",
            answer = "Go to Settings > Edit Profile > Password > Change Password. Enter your current password, then your new password twice. Passwords must have at least 8 characters, one uppercase letter, one number, and one special character."
        )
        FAQItemExpandableAesthetic(
            question = "How do I update my profile information?",
            answer = "Tap 'Edit Profile' in your Profile screen. You can update your name, profile picture, date of birth, and other personal information."
        )
        FAQItemExpandableAesthetic(
            question = "Does Fit Buddy offer guided workouts?",
            answer = "Absolutely! We provide video and audio-guided workouts for strength, cardio, yoga, and HIIT. You can filter by difficulty and duration."
        )
        FAQItemExpandableAesthetic(
            question = "Can I share my workouts with friends?",
            answer = "Yes! After finishing a workout, tap 'Share' to send your results via social media or Fit Buddy friend network."
        )
        FAQItemExpandableAesthetic(
            question = "How can I monitor my progress over time?",
            answer = "Check the 'Progress' tab for weekly, monthly, and yearly stats. Fit Buddy charts weight, strength, cardio, and consistency trends."
        )
        FAQItemExpandableAesthetic(
            question = "Is there a community forum inside Fit Buddy?",
            answer = "Yes! Join the 'Community' tab to ask questions, share workouts, and connect with other fitness enthusiasts."
        )
        
        Spacer(modifier = Modifier.height(30.dp))

        // ------------------ Contact Support ------------------
        Text(
            text = "Need more help?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = textPrimary
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Contact Support Button opens email app
        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:support@fitbuddy.com")
                    putExtra(Intent.EXTRA_SUBJECT, "Fit Buddy Support Request")
                    putExtra(Intent.EXTRA_TEXT, "Hi Fit Buddy team,\n\nI need help with...")
                }
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Contact Support")
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun FAQItemExpandableAesthetic(
    question: String,
    answer: String
) {
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
