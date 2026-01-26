package com.example.fit_buddy.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.fit_buddy.R
import androidx.compose.foundation.lazy.items
import com.example.fit_buddy.viewmodel.FeedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendListScreen(
    viewModel: FeedViewModel,onBack: () -> Unit,onFriendClick:(String) -> Unit
){
//    accepted friends list
    val friends by viewModel.acceptedFriends.observeAsState(emptyList())
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Friends", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(painterResource((R.drawable.outline_arrow_back_ios_24)), null)
                    }
                }
            )
        }
    ){
        padding ->
        if(friends.isEmpty()){
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center){
                Text("Oops No friends yet !", color = Color.Gray)
            }
        }else{
            LazyColumn (modifier = Modifier.fillMaxSize().padding(padding)){
                items(friends) {friend ->
                    Row (modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onFriendClick(friend.userId) }
                        .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically){
                        AsyncImage(
                            model = friend.profilePicUrl,
                            contentDescription = null,
                            modifier = Modifier.size(50.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(R.drawable.baseline_person_24)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = friend.username,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Icon(
                            painter = painterResource(R.drawable.baseline_arrow_forward_ios_24),
                            contentDescription = null,
                            tint = Color.LightGray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }

}