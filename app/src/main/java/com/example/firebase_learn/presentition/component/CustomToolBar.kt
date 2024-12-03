package com.example.firebase_learn.presentition.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomToolBar(text:String,imageVectorStart:ImageVector?=null,imageVectorEnd: ImageVector?=null,onIconClicked: () -> Unit,onBackClicked:(()->Unit)?=null   ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color.Blue),
        verticalAlignment = Alignment.CenterVertically
    ) {
        imageVectorStart?.let {
                IconButton(onClick = onBackClicked ?:{}) {
                    Icon(imageVector = imageVectorStart, contentDescription = "Back")
                }

        }


        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier
                .padding(16.dp)
                .weight(1f)
        )
        imageVectorEnd?.let {
            IconButton(onClick = onIconClicked) {
                Icon(imageVector=imageVectorEnd, contentDescription = "Delete")
            }
        }

    }
}