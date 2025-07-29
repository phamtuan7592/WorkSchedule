package com.example.workschedule.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.workschedule.R

@Composable
fun ButtonBar(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(Color(0xFFDFF3EA))
        )


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .padding(start = 20.dp, end = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.today),
                contentDescription = "Today",
                modifier = Modifier.size(30.dp)
            )

            Image(
                painter = painterResource(R.drawable.mountain),
                contentDescription = "Mountain",
                modifier = Modifier.size(30.dp)
            )

            Spacer(modifier = Modifier.size(50.dp))

            Image(
                painter = painterResource(R.drawable.statsbars),
                contentDescription = "Stats",
                modifier = Modifier.size(30.dp)
            )

            Image(
                painter = painterResource(R.drawable.book),
                contentDescription = "Book",
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        navController.navigate("search")
                    }
            )
        }


        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-30).dp)
                .background(Color(0xFF7CEC9F), CircleShape)
        ) {
            Image(
                painter = painterResource(R.drawable.cong),
                contentDescription = "Add",
                modifier = Modifier.size(30.dp)
            )
        }
    }
}
