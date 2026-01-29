# 💪 Fit-Buddy – AI Powered Fitness Companion

Fit-Buddy is a smart fitness application that combines **AI pose detection**, **workout tracking**, **social features**, and **health analytics** to help users stay consistent, motivated, and connected on their fitness journey.

## 🎯 Goals of Fit-Buddy
- Make workouts **interactive and intelligent**
- Encourage **daily consistency**
- Combine **AI + Social motivation**
- Provide real-time feedback like a personal trainer
  
## 🏆 Features

### AI Pose Detection & Feedback
- Real-time camera-based pose detection using **MediaPipe**
- Supports exercises like:
  - Squats
  - Push-ups
  - Lunges
  - Plank
  - Jumping Jacks
  - Mountain Climbers
- Live feedback:
  - *Good form*, *Stand tall*, *Go lower*, *Show full body*, etc.
- Accurate **rep counting**
- Voice feedback using **Text-to-Speech**
- Detects body presence — no false detection when user is not in frame

---

### 📈 Smart Dashboard
- Shows:
  - Total workout time completed **today**
  - Dynamic progress graph
- Effort visualization:
  - Bar slowly transitions to **purple** as effort increases
  - Turns fully purple when **15+ minutes** workout goal is achieved
- Personalized **Welcome message**

---

### 🥇 Achievements & Consistency Tracking
- Daily workout streak system (minimum **15 minutes/day**)
- Calendar **heatmap** showing:
  - Days worked out
  - Missed days
- Motivation through visual consistency tracking

---

### 🩺 Health Metrics
- BMI (Body Mass Index) calculation
- Body fat percentage estimation
- Goal setting:
  - Example: *Workout 30 minutes today*
- Progress awareness for healthier habits

---

### 👥 Social Media & Community
- Fitness-focused social feed
- Post workout updates
- Send, accept, or delete **friend requests**
- Stay motivated with friends’ activity

---

### 👤 Profile Management
- Edit profile picture
- Update:
  - Email
  - Password
  - Personal details
- Secure user data handling

---

## 🛠 Tech Stack

- **Kotlin**
- **Jetpack Compose**
- **MVVM Architecture**
- **CameraX**
- **MediaPipe Pose Landmarker**
- **Text-to-Speech (TTS)**
- **Material 3 UI**
- **Firebase** (Auth / Database / Storage – if applicable)

---

## 📸 Screens & UI
- Clean modern UI
- Dynamic graphs and smooth animations
- Camera overlay for AI workout screen
- Responsive layouts across devices


## 📂 Project Architecture
MVVM with Repository

I updates with state

View → ViewModel → Repository → Model

 ↑_________________________________________↓

