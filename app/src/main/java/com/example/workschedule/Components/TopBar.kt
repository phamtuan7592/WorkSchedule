package com.example.workschedule.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.workschedule.R

@Composable
fun TopBar(navController: NavController){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
            .padding(top = 20.dp, start = 10.dp, end = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Image(
            painter = painterResource(R.drawable.today),
            contentDescription = "icon",
            modifier = Modifier
                .size(28.dp)
                .clickable { navController.navigate("calendar") }
        )
        Text(
            text = "Work Schedule",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
        )

        Image(
            painter = painterResource(R.drawable.settings),
            contentDescription = "settings",
            modifier = Modifier
                .size(28.dp)
        )

    }
}

