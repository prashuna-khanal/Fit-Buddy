package com.example.fit_buddy.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fit_buddy.R
import com.example.fit_buddy.viewmodel.FeedViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.ui.layout.ContentScale


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun OtherUserProfileScreen(
    userId: String, //friend user id
    viewModel: FeedViewModel,
    onBack:() -> Unit
){
    val userPosts by viewModel.getPostsByUser(userId).observeAsState(emptyList())

    val friendsCount by viewModel.getFriendCount(userId).observeAsState(0)
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(userId, fontWeight = FontWeight.Bold) //dynamic
                },
                navigationIcon = {
                    IconButton(onClick = onBack){
                        Icon(painterResource(R.drawable.outline_arrow_back_ios_24),contentDescription = null)
                    }
                }

            )
        }
    ) {
        padding ->
        Column (
            modifier = Modifier.fillMaxSize().padding(padding).background(Color.White)
        ){
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),verticalAlignment= Alignment.CenterVertically
            ){
                Box(
                    modifier = Modifier.size(80.dp).clip(CircleShape).background(Color.Gray)
                )
                Spacer(modifier = Modifier.width(24.dp))
                Row (
                    modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.SpaceEvenly
                ){
                    ProfileStat(userPosts.size.toString(), label = "Posts")
                    ProfileStat(friendsCount.toString(), label = "Friends")
                }


            }
            androidx.compose.material3.HorizontalDivider(
                modifier = Modifier.padding(top = 8.dp),
                thickness = 0.5.dp,
                color = Color.LightGray
            )

            androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize()
            ) {
                items(userPosts.size) { index ->
                    val post = userPosts[index]
                    coil.compose.AsyncImage(
                        model = post.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                      .aspectRatio(1f)
                            .padding(1.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }

}
@Composable
fun ProfileStat(count:String,label: String){
    Column (horizontalAlignment = Alignment.CenterHorizontally){
        Text(text=count, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(text=label, fontSize = 12.sp, color = Color.Gray)
    }
}