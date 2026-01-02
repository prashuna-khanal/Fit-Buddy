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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.example.fit_buddy.model.FriendRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendRequestsScreen(requests: List<com.example.fit_buddy.model.FriendRequest>,
                         onAccept:(String) -> Unit,

                         onDelete:(String) -> Unit,
                         onProfileClick:(String) -> Unit,
                         onBack:() -> Unit
){
    val confirmLavender = Color(0xFFD9C8F9)
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Friend Requests", fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(painterResource(R.drawable.outline_arrow_back_ios_24),null)
                    }
                }
            )
        }
    ) { padding ->
        if(requests.isEmpty()){
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center){
                Text("No new Friend Requests", color = Color.Gray)
            }
        }else{
            LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
                items(requests){
                    request ->
                    Row (
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
//                        viewing profile of sent request
                        AsyncImage(
                            model = request.profilePicUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .clickable { onProfileClick(request.userId) },
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = request.username,
                            modifier = Modifier.weight(1f).padding(start = 12.dp),
                            fontWeight = FontWeight.SemiBold
                        )
//                        buttons
                        Button(
                            onClick = { onAccept(request.userId) },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = confirmLavender)
                        ) {
                            Text("Confirm",color = Color.Black, fontSize = 12.sp)
                        }
                        Spacer(Modifier.width(8.dp))

                        TextButton(onClick = { onDelete(request.userId) }) {
                            Text("Delete", color = Color.Gray)
                        }

                    }
                }
            }
        }

    }

}